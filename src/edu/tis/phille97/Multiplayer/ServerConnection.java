package edu.tis.phille97.Multiplayer;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import edu.tis.phille97.Core.Game;
import edu.tis.phille97.Entities.Entity;
import edu.tis.phille97.Entities.Obstacle;
import edu.tis.phille97.Mobs.Mob;
import edu.tis.phille97.Mobs.OtherPlayer;
import edu.tis.phille97.Mobs.Player;
import edu.tis.phille97.Multiplayer.Objects.PlayerUpdateRequest;
import edu.tis.phille97.Multiplayer.Objects.PlayerUpdateResponse;
import edu.tis.phille97.Multiplayer.Objects.SomeRequest;
import edu.tis.phille97.Multiplayer.Objects.SomeResponse;

/**
 * ServerConnection from the game client
 * 
 * @author Philip Johansson
 *
 */
public class ServerConnection {
	
	private HejConnection hejConnection;
	private ArrayList<String> chat;
	private ArrayList<Entity> entities;
	private ArrayList<Mob> mobs;
	private Player player = null;
	
	private Client client;
	
	/**
	 * Displays a line of text in the chat box
	 * @param line text to display
	 */
	private synchronized void displayMessage(String line){
		if(chat != null) chat.add(line);
		System.out.println(line);
	}
	/**
	 * This ServerConnection is intended to be used in the game RainJumper!
	 * 
	 * @param game The instance of the Game class
	 * @param chat The chat list
	 * @param entities An arraylist that new entities from the server will be written to, and removed
	 * @param mobs Where the new players will be created and destroyed
	 */
	public ServerConnection(Game game, ArrayList<String> chat, ArrayList<Entity> entities, ArrayList<Mob> mobs){
		this.chat = chat;
		this.entities = entities;
		this.mobs = mobs;
		Log.set(Log.LEVEL_INFO);
		this.client = new Client();
	}
	
	/**
	 * This will connect to the server specified
	 * @param player The player to be used as a controlled player
	 * @param HOST_NAME
	 * @param TCP_PORT TCP port number.(Requests and responses)
	 * @param UDP_PORT UDP port number.(Player updates and alot of fast traffic)
	 * @throws IOException If the client is unable to connect to the server.
	 */
	public void connectToServer(Player player, String HOST_NAME, int TCP_PORT, int UDP_PORT) throws IOException {
		this.player = player;
		Kryo kryo = client.getKryo();
	    kryo.register(SomeRequest.class);
	    kryo.register(SomeResponse.class);
	    kryo.register(PlayerUpdateRequest.class);
	    kryo.register(PlayerUpdateResponse.class);
	    
	    client.start();
	    client.connect(5000, HOST_NAME, TCP_PORT, UDP_PORT);
	    
	    
	    
	    this.hejConnection = new HejConnection(player.getName());
	    this.hejConnection.start();
	}
	public void disconnect(){
		if(this.client != null){
			client.close();
		}
	}
	/**
	 * Sends a chat message to the server if connected.
	 * @param line text to send to the server as a chat message
	 */
	public void sendMessagetoServer(String line){
		if(hejConnection != null && client != null) hejConnection.sendChatmessage(line);
	}
	/**
	 * Send a request to the server
	 * @param line Request
	 */
	public void sendtoServer(String line){
		if(hejConnection != null && client != null) hejConnection.send(line);
	}
	/**
	 * 
	 * @return Returns true if connected to the server
	 */
	public boolean isConnected(){
		if(client != null) return client.isConnected();
		return false;
	}
	
	/**
	 * Returns the hostname of the server that it is connected to
	 * @return Return the hostname as a String if connected and null if not connected
	 */
	public String getServerInfo(){
		if(this.client != null) return client.getRemoteAddressTCP().getHostName();
		return "";
	}
	
	
	
	private class HejConnection extends Thread {
		private boolean running = false;
		
		public HejConnection(String username) {
			client.addListener(new Listener() {
				public void received (Connection connection, Object object) {
					if (object instanceof SomeResponse) {
						SomeResponse response = (SomeResponse)object;
						HejConnection.this.handleTextResponse(response.text);
						if(!response.text.startsWith("#reqPOS#"))System.out.println("Response: " + response.text);
						return;
					}else if (object instanceof PlayerUpdateResponse){
						PlayerUpdateResponse response = (PlayerUpdateResponse)object;
						
						int id = response.ID;
						int x = response.X;
						int y = response.Y;
						
						boolean found = false;
						ArrayList<Integer> updated = new ArrayList<Integer>();
						for(Mob m : mobs){
							if(m instanceof OtherPlayer){
								OtherPlayer player = (OtherPlayer) m;
								if(player.getID() == ServerConnection.this.player.getID()){
									mobs.remove(m);
									break;
								}
								
								if(player.getID() == id){
									if(x > player.getX()) player.setGoingRight(true);
									else if(x < player.getX()) player.setGoingRight(false);
									player.setX(x);
									player.setY(y);
									found = true;
									player.resetUpdatingCounter();
									updated.add(player.getID());
								}
							}
						}
						for(Mob m : mobs){
							if(m instanceof OtherPlayer){
								OtherPlayer player = (OtherPlayer) m;
								boolean missingFound = false;
								for(Integer i : updated){
									if(player.getID() == i){
										if(player.netUpdatinglatestover(1000)) missingFound = true;
									}
								}
								if(!missingFound){// #whatAbout#ID
									send("#whatAbout#" + player.getID());
								}
							}
						}
						if(!found && player.getID() != id){
							String username = response.USERNAME;
							OtherPlayer e = new OtherPlayer(x, y, username, id);
							mobs.add(e);
						}
						return;
					}
					
					System.err.println("ERROR: [SERVER] Unhandled response @ connection.setupListener() : " + object);
				}
			});
			SomeRequest request = new SomeRequest();
		    request.text = "#username#" + username;
		    client.sendTCP(request);
		}
		
		@SuppressWarnings("unused")
		public boolean isRunning() {
			return this.running ;
		}
		
		public synchronized void send(String line){
			SomeRequest request = new SomeRequest();
		    request.text = line;
		    client.sendTCP(request);
		}
		
		public synchronized void sendChatmessage(String line){
			send("#msg#" + line);
		}
		private void updatePlayerCoord(){
			PlayerUpdateRequest request = new PlayerUpdateRequest();
			request.X = (int) player.getX();
			request.Y = (int) player.getY();
			client.sendUDP(request);
		}
		
		private void handleTextResponse(String line){
			if(line.startsWith("#reqPOS#")){
				updatePlayerCoord();
			}else if(line.startsWith("#msg#")){
				line = line.replaceFirst("#msg#", "");
				ServerConnection.this.displayMessage(line);
			}else if(line.startsWith("#close#")){
				line = line.replaceFirst("#close#", "");
				ServerConnection.this.displayMessage("[Server]" + line);
				running = false;
				return;
			}else if(line.startsWith("#newPlayer#")){ //#newPlayer#ID#USERNAME#X#Y
				line = line.replaceFirst("#newPlayer#", "");
				int i1 = line.indexOf("#");
				int i2 = line.indexOf("#", i1 + 1);
				int i3 = line.indexOf("#", i2 + 1);
				String ID = line.substring(0, i1);
				String username = line.substring(i1+1, i2);
				String X = line.substring(i2+1, i3);
				String Y = line.substring(i3+1);
				int x = Integer.parseInt(X);
				int y = Integer.parseInt(Y);
				int id = Integer.parseInt(ID);
				
				if(id != player.getID()){
					OtherPlayer e = new OtherPlayer(x, y, username, id);
					mobs.add(e);
				}
			}else if(line.startsWith("#removePlayer#")){ // #removePlayer#ID
				line = line.replaceFirst("#removePlayer#", "");
				OtherPlayer playertoremove = null;
				for(Mob m : mobs){
					if(m instanceof OtherPlayer){
						OtherPlayer test = (OtherPlayer) m;
						if(test.getID() == Integer.parseInt(line)){
							playertoremove = test;
						}
					}
				}
				mobs.remove(playertoremove);
			
			}else if(line.startsWith("#addBlock#")){     // #addBlock#blockID#colour#x#y#width#height#ability(jump|deco|speed|platform)
				line = line.replaceFirst("#addBlock#", "");
				
				int i1 = line.indexOf("#");
				int i2 = line.indexOf("#", i1+1);
				int i3 = line.indexOf("#", i2+1);
				int i4 = line.indexOf("#", i3+1);
				int i5 = line.indexOf("#", i4+1);
				int i6 = line.indexOf("#", i5+1);
				int blockID = Integer.parseInt(line.substring(0, i1));
				String colour = line.substring(i1+1, i2);
				int x = Integer.parseInt(line.substring(i2+1, i3));
				int y = Integer.parseInt(line.substring(i3+1, i4));
				int width = Integer.parseInt(line.substring(i4+1, i5));
				int height = Integer.parseInt(line.substring(i5+1, i6));
				String ability = line.substring(i6+1);
				
				// From server  #addBlock#blockID#colour(GRAY|CYAN|ORANGE|GREEN)#x#y#width#height#ability(jump|deco|speed|platform)
				// From client  #addBlock#colour(GRAY|CYAN|ORANGE|GREEN)#x#y#width#height#ability(jump|deco|speed|platform)
				Color color = Color.white;
				
				if(colour.equals("GRAY"))color = Color.GRAY;
				else if(colour.equals("GREEN")) color = Color.GREEN;
				else if(colour.equals("ORANGE")) color = Color.ORANGE;
				else if(colour.equals("CYAN")) color = Color.CYAN;
				
				if (ability.equalsIgnoreCase("platform")) {
					entities.add(new Obstacle(blockID, x, y, height, width, color, true, false));// Long Platform block
				}else if(ability.equalsIgnoreCase("deco")){
					entities.add(new Obstacle(blockID, x, y, height, width, color, false, false));// Short decoration block
				}else if(ability.equalsIgnoreCase("jump")){
					entities.add(new Obstacle(blockID, x, y, height, width, color, true, true));// Long Jump block
				}else if(ability.equalsIgnoreCase("speed")){
					entities.add(new Obstacle(blockID, x, y, height, width, color, 6));// Long Speed block
				}
				
				// ##################
			}else if(line.startsWith("#removeBlock#")){  // #removeBlock#blockID
				line = line.replaceFirst("#removeBlock#", "");
				ArrayList<Entity> toRemove = new ArrayList<Entity>();
				
				for(Entity e : entities){
					if(e.getID() == Integer.parseInt(line)){
						toRemove.add(e);
					}
				}
				for(Entity e : toRemove){
					entities.remove(e);
				}
			}else if(line.startsWith("#yourID#")){
				line = line.replaceFirst("#yourID#", "");
				player.setID(Integer.parseInt(line));
				System.out.println("New Player ID: " + line);
			}
		}
		
		@Override
		public void run(){
			running = true;
			
			while(running){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {}
			}
			
			client.close();
			mobs.clear();
		}
	}
}
