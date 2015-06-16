package edu.tis.phille97.Scenes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import edu.tis.phille97.Core.Game;
import edu.tis.phille97.Entities.Entity;
import edu.tis.phille97.main.MainFrame;

public class MultiplayerLevel extends Scene {
	
	private boolean connecting = false;
	@SuppressWarnings("unused")
	private boolean connectionFailed = false;
	private int connectionCounter = 0;
	
	private String failmessage = "";
	
	public MultiplayerLevel(Game game, ArrayList<Entity> entities, Dimension screenSize, boolean hasPlayer, int floorY){
		super(game, entities, screenSize, hasPlayer, floorY);
	}
	
	public void tick(int mouseX, int mouseY, boolean LeftButtonClicked){
		if(!this.game.isConnectedToMultiplayer()){
			if(!this.connecting && connectionCounter != 0){
				this.game.changeScene(new Menu(this.game, (new Dimension(WIDTH, HEIGHT)), false, "Lost connection to server!"));
			}else{
				connect();
			}
		}
		
		for(int i = 0; i < this.getEntities().size(); i++){
			Entity e = this.getEntities().get(i);
			if(e != null) e.tick();
		}
	}
	
	private void connect(){
		this.connecting = true;
		try{
			this.game.connectMultiplayer();
		} catch (IllegalStateException e) {
			this.game.changeScene(new Menu(this.game, (new Dimension(WIDTH, HEIGHT)), false, "Error in code! Contact the developer!"));
			return;
		} catch (UnknownHostException e) {
			this.failmessage = (e.getMessage());
			this.connectionFailed = true;
			this.game.changeScene(new Menu(this.game, (new Dimension(WIDTH, HEIGHT)), false, failmessage));
			return;
		} catch (IOException e){
			this.failmessage = (e.getMessage());
			this.connectionFailed = true;
			this.game.changeScene(new Menu(this.game, (new Dimension(WIDTH, HEIGHT)), false, failmessage));
			e.printStackTrace();
			return;
		} catch (Exception e){
			// Denied access to the server!
			this.failmessage = (e.getMessage());
			this.connectionFailed = true;
			this.game.changeScene(new Menu(this.game, (new Dimension(WIDTH, HEIGHT)), false, failmessage));
			e.printStackTrace();
			return;
		}
		this.connecting = false;
		connectionCounter++;
	}
	
	public void printBackground(Graphics2D g2){
		g2.setColor(new Color(0, 90, 255));
		g2.fillRect(0, 0, WIDTH, HEIGHT);
		
		g2.setColor(Color.DARK_GRAY);
		g2.fillRect(0, getFloorY(), WIDTH, HEIGHT-getFloorY());
		
		if(MainFrame.DEBUG){			// DEBUG WINDOW
			g2.setColor(Color.WHITE);
			String keys_pressed = "";
			for(int i = 0; i < 300; i++){
				if(isKeyPressed(i)) keys_pressed += i + ", ";
			}
			String[] infotext = {
					"--- DEBUG MODE ---",
					"Entities: " + getEntities().size(),
					"Keys pressed: " + keys_pressed};
			
			g2.setColor(Color.WHITE);
			int placementY = 50;
			for(String text : infotext){
				g2.drawString(text, 20, placementY);
				placementY += 15;
			}
		}
		for(int i = 0; i < this.getEntities().size(); i++){
			Entity e = this.getEntities().get(i);
			if(e != null) e.print(g2);
		}
	}
}
