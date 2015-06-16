package edu.tis.phille97.Scenes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;

import edu.tis.phille97.Core.Game;
import edu.tis.phille97.Entities.Entity;
import edu.tis.phille97.main.MainFrame;

public class LobbyLevel extends Scene {
	public LobbyLevel(Game game, ArrayList<Entity> entities, Dimension screenSize, boolean hasPlayer, int floorY){
		super(game, entities, screenSize, hasPlayer, floorY);
	}
	
	public void tick(int mouseX, int mouseY, boolean LeftButtonClicked){
		for(int i = 0; i < this.getEntities().size(); i++){
			Entity e = this.getEntities().get(i);
			if(e != null) e.tick();
		}
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
                        
                        g2.setColor(Color.WHITE);
			for(String text : infotext){
				g2.drawString(text, 20, placementY);
				placementY += 15;
			}
		}
                g2.setColor(Color.RED);
                g2.fillRect(1, 1, 300, 30);
		for(int i = 0; i < this.getEntities().size(); i++){
			Entity e = this.getEntities().get(i);
			if(e != null) e.print(g2);
		}
	}
}
