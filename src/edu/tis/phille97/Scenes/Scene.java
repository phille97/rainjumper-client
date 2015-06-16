package edu.tis.phille97.Scenes;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import edu.tis.phille97.Core.Game;
import edu.tis.phille97.Entities.Cloud;
import edu.tis.phille97.Entities.Entity;
import edu.tis.phille97.Entities.Rain;

public class Scene {
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	private final boolean hasplayer;
	public final int WIDTH, HEIGHT;
	public final Game game;

	private int FLOORY;
	public int getFloorY(){
		return this.FLOORY;
	}
	
	public boolean havePlayer(){
		return this.hasplayer;
	}
	private boolean[] keys = new boolean[420];
	public boolean isKeyPressed(int key){
		return keys[key];
	}
	public void KeyPressed(int key){
		keys[key] = true;
	}
	public void KeyReleased(int key){
		keys[key] = false;
	}
	public void keyTyped(KeyEvent e){
		
	}
	public void mousePressed(MouseEvent e){
		
	}
	
	public Scene(Game game, ArrayList<Entity> entities, Dimension screenSize, boolean hasPlayer, int floorY) {
		this.game = game;
		this.entities = entities;
		this.HEIGHT = screenSize.height;
		this.WIDTH = screenSize.width;
		this.FLOORY = floorY;
		this.hasplayer = hasPlayer;
	}
	
	@Deprecated
	public void printBackground(Graphics g){
		printBackground((Graphics2D) g);
	}
	
	public void printBackground(Graphics2D g2){
		
	}
	
	public void tick(int mouseX, int mouseY, boolean LeftButtonClicked){
		for(int i = 0; i < entities.size(); i++){ // Update entities
			Entity r1 = entities.get(i);
			if(r1 != null) {
				try {
					r1.tick();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
			
			if(r1 instanceof Rain){
				if(((Rain)r1).getY() >= FLOORY+10){
					entities.remove(i);
					continue;
				}
			} else if( r1 instanceof Cloud){
				if(((Cloud)r1).getX() >= WIDTH){
					entities.remove(i);
					continue;
				}
			}
		}
	}
	
	public ArrayList<Entity> getEntities(){
		return this.entities;
	}
}
