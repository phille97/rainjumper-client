package edu.tis.phille97.Core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.esotericsoftware.kryonet.Client;

import edu.tis.phille97.Entities.Cloud;
import edu.tis.phille97.Entities.Entity;
import edu.tis.phille97.Entities.Obstacle;
import edu.tis.phille97.Entities.Rain;
import edu.tis.phille97.Mobs.Mob;
import edu.tis.phille97.Mobs.Player;
import edu.tis.phille97.Multiplayer.ServerConnection;
import edu.tis.phille97.Scenes.Menu;
import edu.tis.phille97.Scenes.Scene;

@SuppressWarnings("serial")
public class Game extends JPanel {
	private int x;
	private int y;
	private boolean LeftButtonClicked = false;
	private boolean[] keys = new boolean[420];
	private final String[] tool = {"Long block", "Short decoration block", "Long trampoline", "Long powerup", "Test block"};
	private int toolnr = 0;
	private int HEIGHT, WIDTH, floorY;
	private boolean raining = false;
	private final ArrayList<Mob> mobs = new ArrayList<Mob>();
	private final ArrayList<String> chat = new ArrayList<String>();
	private ServerConnection connection = null;
	
	private Player player = null;
	
	private Scene currentScene;
	private String username;
	private String multiplayerServer = "localhost";
	
	// private int cameraX, cameraY, camera_width, camera_height;
	
	public int getWidth(){
		return WIDTH;
	}
	
	public int getHeight(){
		return HEIGHT;
	}
	
	public void changeUsername(String newName){
		if(player != null) player.changeName(newName);
		this.username = newName;
	}
	
	public void stopEngine(){
		if(connection != null)if(connection.isConnected()){
			connection.sendMessagetoServer("[GAME CLOSED]");
			connection.disconnect();
		}
		System.out.println("Bye!");
		System.exit(-1);
	}
	
	public void changeScene(Scene scene){
		System.out.println("[Scene] Changed to: " + scene.getClass().getSimpleName() + " from: " + currentScene.getClass().getSimpleName());
		chat.clear();
		mobs.clear();
		
		if(currentScene != null){
			if(currentScene.getEntities() != null){
				currentScene.getEntities().clear();
			}
		}
		
		if(scene.havePlayer()) {
			player = new Player(100, scene.getFloorY()-2, scene.getFloorY(), this.username, 100, scene.getEntities(), WIDTH);
			mobs.add(player);
		}
		
		if(this.connection != null){
			if(this.connection.isConnected()){
				this.connection.disconnect();
			}
		}
		
		currentScene = scene;
	}
	public boolean isConnectedToMultiplayer(){
		if(this.connection == null) return false;
		return this.connection.isConnected();
	}
	
	public String getMultiplayerServer(){
		return this.multiplayerServer;
	}
	
	public void setMultiplayerServer(String hostname){
		this.multiplayerServer = hostname;
	}
	
	public void connectMultiplayer() throws Exception{
		if(this.connection != null){
			if(this.connection.isConnected()) throw new IllegalStateException("Already connected to a server!");
		}
		
		this.raining = false;
		currentScene.getEntities().clear();
		
		chat.add("Attemting to connect to a multiplayer server");
		
		this.connection = new ServerConnection(this, chat, currentScene.getEntities(), mobs);
		
		// Developers server: 80.240.132.129
		this.connection.connectToServer(player, multiplayerServer, 25560, 25560); // Tries to connect to the server
		
		chat.add("Connected succesfully!");
	}
	
	public Game() {
		this.setBackground(Color.BLACK);
		this.addKeyListener(new KeyHandler());
		this.addMouseMotionListener(new MouseHandler());
		this.addMouseListener(new MousePressHandler());
		
		this.setFocusable(true);
		this.requestFocus();
		
		Timer t = new Timer();
		TimerTask ta = new Updater();
		t.schedule(ta, 0, 10);
	}
	
	public void setStuffs(int height, int width, String name){
		this.HEIGHT = height;
		this.WIDTH = width;
		this.username = name;
		
		this.floorY = height - 50;
		currentScene = new Menu(this, new Dimension(WIDTH, HEIGHT), false);
		if(currentScene.havePlayer()) player = new Player(100, floorY-2, floorY, name, 100, currentScene.getEntities(), WIDTH);
		
		mobs.add(player);
	}
	
	public void showMessageInChat(String line){
		chat.add(line);
	}
	
	private void doChat(String line, Mob mob){
		if(mob == null) mob = new Player(0, 0, 0, "", 100, null, 0);
		if(connection != null) {
			if(connection.isConnected()) {
				connection.sendMessagetoServer(line);
			}else{
				String texttowrite = mob.getName() + ": " + line;
				chat.add(texttowrite);
				System.out.println(texttowrite);
			}
		}else{
			String texttowrite = mob.getName() + ": " + line;
			chat.add(texttowrite);
			System.out.println(texttowrite);
		}
	}
	
	private void doCommand(String line, Mob mob) {
		String texttowrite = "";
		if(line.equalsIgnoreCase("/quit")) {
			texttowrite = "--- Force quit through chat command ---";
			System.out.println(texttowrite);
			chat.add(texttowrite);
			stopEngine();
			
		}else if(line.equalsIgnoreCase("/help")){
			System.out.println("Printing info in the chat");
			chat.add("--- Help ---");
			chat.add("/help         -  Show this.");
			chat.add("/quit         -  Force quits the game.");
			chat.add("/clear        -  Clear the chat.");
			chat.add("/multiplayer  -  Tries to connect to the multiplayer server.");
			return;
		}else if(line.equalsIgnoreCase("/clear")){
			chat.clear();
		}else if(line.startsWith("/lan")){
			chat.add("Searching for LAN server!");
			InetAddress address = (new Client()).discoverHost(25560, 2000);
		    if(address != null)chat.add("Server found at: " + address.getHostAddress());
		    else chat.add("No server was found on the network!");
		}else if(line.startsWith("/getop")){ // #gainOP#password
			line = line.replaceFirst("/getop", "");
			line = line.trim();
			if(this.connection != null){
				if(connection.isConnected()){
					chat.add("Sending request to gain OP...");
					connection.sendtoServer("#gainOP#" + line);
					return;
				}
			}
			chat.add("You are not connected to a server!");
			
			// #clearallentities#
		}else{
			// Command not found 
			texttowrite = mob.getName() + " command: " + line + " was not found!";
		}
		
		System.out.println(texttowrite);
		chat.add(texttowrite);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.WHITE);
		if(currentScene != null)currentScene.printBackground(g2);
		
		for(int i = 0; i < mobs.size(); i++){
			Mob pmob = mobs.get(i);
			if(pmob != null){
				pmob.print(g2);
			}
		}
		
		g2.setColor(Color.WHITE);
		g2.drawLine(x, y, x+20, y);
		g2.drawLine(x, y, x, y+20);
		
		if(currentScene != null) if(currentScene.havePlayer()){
			for(int i = 0; i < chat.size(); i++){
				g2.drawString(chat.get(i), 50, HEIGHT - (300-(i*15)));
				if(i > 10){
					chat.remove(0);
				}
			}

			String ifConnected = "Singleplayer mode!";
			if(Game.this.connection != null){
				if(Game.this.connection.isConnected()){
					ifConnected = "Connected to server at: " + connection.getServerInfo();
					g2.setColor(Color.GREEN);
				} else g2.setColor(Color.RED);
			}else g2.setColor(Color.RED);
			
			g2.setFont(new Font("", Font.BOLD, 20));
			g2.drawString(ifConnected, (Game.this.WIDTH/2)-100, 20);
			
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("", Font.PLAIN, 20));
			g2.drawString("Current block:" + this.tool[this.toolnr], 10, 20);
		}
	}

	private class MouseHandler extends MouseMotionAdapter {
		@Override
		public void mouseMoved(MouseEvent e) {
			x = e.getX();
			y = e.getY();
		}
	}
	
	private class MousePressHandler extends MouseAdapter {
		
		@Override
		public void mouseReleased(MouseEvent e){
			LeftButtonClicked = false;
			Game.this.currentScene.mousePressed(e);
		}
		@Override
		public void mousePressed(MouseEvent e) {
			LeftButtonClicked = true;
			// From server  #addBlock#blockID#colour#x#y#width#height#ability(jump|deco|speed|platform)
			// From client  #addBlock#colour(GRAY|CYAN|ORANGE|GREEN)#x#y#width#height#ability(jump|deco|speed|platform)
			if(connection != null) if(connection.isConnected()){
				if (toolnr == 0) {
					// Game.this.entities.add(new Obstacle(x, y, 20, 100, Color.GRAY, true, false));// Long Platform block
					connection.sendtoServer("#addBlock#GRAY#" + x + "#" + y + "#100#20#platform");
				}else if(toolnr == 1){
					// Game.this.entities.add(new Obstacle(x, y, 20, 20, Color.CYAN, false, false));// Short decoration block
					connection.sendtoServer("#addBlock#CYAN#" + x + "#" + y + "#20#20#deco");
				}else if(toolnr == 2){
					// Game.this.entities.add(new Obstacle(x, y, 20, 100, Color.GREEN, true, true));// Long Jump block
					connection.sendtoServer("#addBlock#GREEN#" + x + "#" + y + "#100#20#jump");
				}else if(toolnr == 3){
					// Game.this.entities.add(new Obstacle(x, y, 20, 100, Color.ORANGE, 6));// Long Speed block
					connection.sendtoServer("#addBlock#ORANGE#" + x + "#" + y + "#100#20#speed");
				}else if(toolnr == 4){
					// Game.this.entities.add(new Obstacle(x, y, 500, 500, "player.png"));// Test block
					connection.sendtoServer("#addBlock#GRAY#" + x + "#" + y + "#10#10#platform"); 
				}
				return;
			}
			if (toolnr == 0) {
				Game.this.currentScene.getEntities().add(new Obstacle(-1, x, y, 20, 100, Color.GRAY, true, false));// Long Platform block
			}else if(toolnr == 1){
				Game.this.currentScene.getEntities().add(new Obstacle(-1, x, y, 20, 20, Color.CYAN, false, false));// Short decoration block
			}else if(toolnr == 2){
				Game.this.currentScene.getEntities().add(new Obstacle(-1, x, y, 20, 100, Color.GREEN, true, true));// Long Jump block
			}else if(toolnr == 3){
				Game.this.currentScene.getEntities().add(new Obstacle(-1, x, y, 20, 100, Color.ORANGE, 6));// Long Speed block
			}else if(toolnr == 4){
				Game.this.currentScene.getEntities().add(new Obstacle(-1, x, y, 500, 500, "player.png"));// Test block
			}
		}
	}
	
	private int cloud_counter = 0;
	private void updateCloudsAndRain(){
		Random r = new Random();
		if(Game.this.raining){
			for(int i = 0; i < 10; i++){
				currentScene.getEntities().add(new Rain(r.nextInt(4000)-currentScene.WIDTH, r.nextInt(50)-50, 3, 5, currentScene.getFloorY(), r.nextInt(3)));
			}
			
			if(cloud_counter == 0) {
				currentScene.getEntities().add(new Cloud(-200, 10+r.nextInt(200), 200, 1, WIDTH, 0, "cloud_1.png"));
				cloud_counter++;
			}else if(cloud_counter < 200){
				cloud_counter++;
			}else{
				cloud_counter = 0;
			}
		}
		for(int i = 0; i < currentScene.getEntities().size(); i++){
			Entity e = currentScene.getEntities().get(i);
			if(e instanceof Cloud){
				Cloud c = (Cloud)e;
				if(c.getX() > currentScene.WIDTH){
					currentScene.getEntities().remove(e);
				}
			}else if(e instanceof Rain){
				Rain r1 = (Rain)e;
				if(r1.getY() > currentScene.getFloorY()){
					currentScene.getEntities().remove(e);
				}
			}
		}
	}
	
	private class Updater extends TimerTask {
		
		@Override
		public void run() {
			if(currentScene != null){
				if(currentScene.getEntities() != null) updateCloudsAndRain();
				
				currentScene.tick(Game.this.x, Game.this.y, LeftButtonClicked);
			}
			
			for(int i = 0; i < mobs.size(); i++){
				Mob mob = mobs.get(i);
				if(mob != null) {
					try {
						mob.tick();
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			}
			if(player != null) {
				if(keys[KeyEvent.VK_D]) player.goRigth();
				if(keys[KeyEvent.VK_A]) player.goLeft();
				if(keys[KeyEvent.VK_SPACE] || keys[KeyEvent.VK_W]) player.jump();
				if(keys[KeyEvent.VK_S]) player.goDown();
			}
			repaint();
		}
	}

	private class KeyHandler extends KeyAdapter {
		
		@Override
		public void keyTyped(KeyEvent e){
			if(currentScene != null) currentScene.keyTyped(e);
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			if(keyCode == KeyEvent.VK_S){
				// TODO Button to start save-to-file-thread
			}else if(keyCode == KeyEvent.VK_L){
				// TODO Button to start load-from-file-thread
			}else if(keyCode == KeyEvent.VK_Q){
				Game.this.changeScene(new Menu(Game.this, new Dimension(WIDTH, HEIGHT), false));
			}else if(keyCode == KeyEvent.VK_E){
				toolnr++;
				if((toolnr >= tool.length)) toolnr = 0;
			}else if(keyCode == KeyEvent.VK_ESCAPE){
				for(int i = currentScene.getEntities().size()-1; i > -1; i--) {
					if(connection != null){
						if(connection.isConnected()){
							connection.sendtoServer("#clearallentities#");
							return;
						}
					}
					if(currentScene.getEntities().get(i) instanceof Obstacle){
						currentScene.getEntities().remove(i);
					}
				}
			}else if(keyCode == KeyEvent.VK_Z){
				for(int i = currentScene.getEntities().size()-1; i > -1; i--) {
					if(currentScene.getEntities().get(i) instanceof Obstacle){
						if(connection != null){
							if(connection.isConnected()){
								connection.sendtoServer("#removeBlock#" + currentScene.getEntities().get(i).getID());
								return;
							}
						}
						currentScene.getEntities().remove(i);
					}
				}
			}else if(keyCode == KeyEvent.VK_R){
				boolean rain = true;
				if(Game.this.connection != null){
					if(Game.this.connection.isConnected()) rain = false;
				}
				if(Game.this.raining && rain) Game.this.raining = false;
				else if(!Game.this.raining && rain) Game.this.raining = true;
			}else if(keyCode == KeyEvent.VK_T){
				if(currentScene != null) if(currentScene.havePlayer()){
					String input = JOptionPane.showInputDialog("");
					if(input.startsWith("/")) Game.this.doCommand(input, player);
					else Game.this.doChat(input, player);
				}
			}
			
			Game.this.currentScene.KeyPressed(keyCode);
			keys[keyCode] = true;
		}
		
		@Override
		public void keyReleased(KeyEvent e){
			int keyCode = e.getKeyCode();
			Game.this.currentScene.KeyReleased(keyCode);
			keys[keyCode] = false;
		}
	}
}
