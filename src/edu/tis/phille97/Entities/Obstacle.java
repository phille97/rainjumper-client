package edu.tis.phille97.Entities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edu.tis.phille97.main.MainFrame;


public class Obstacle extends Entity{
	private boolean solid = false;
	
	private int x, y, height, width, conveyor_speed = 0;
	@SuppressWarnings("unused")
	private String picture;
	private BufferedImage image = null;
	private Color color;
	private int ID = -1;
	
	public int getID(){
		return this.ID;
	}
	
	private boolean trampoline = false, conveyor = false;
	
	public Obstacle(int ID, int x, int y, int height, int width, String picture){
		this.ID = ID;
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.picture = picture;
		this.color = Color.PINK;
		this.solid = true;
		try {
		    image = ImageIO.read(new File("images/" + picture));
		} catch (IOException e) {
			System.out.println("Error loading player image!\n  >Could not find: images/" + picture);
		}
	}
	
	public Obstacle(int ID, int x, int y, int height, int width, Color color, int conveyor_speed){
		this.ID = ID;
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.color = color;
		this.solid = true;
		this.conveyor = true;
		this.conveyor_speed = conveyor_speed;
	}
	
	public Obstacle(int ID, int x, int y, int height, int width, Color color, boolean solid, boolean trampoline){
		this.ID = ID;
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.color = color;
		this.solid = solid;
		this.trampoline = trampoline;
	}
	
	public boolean isConveyor(){
		return this.conveyor;
	}
	
	public int getConveyorSpeed(){
		return this.conveyor_speed;
	}
	
	public boolean isSolid(){
		return this.solid;
	}
	
	public boolean isTrampoline(){
		return this.trampoline;
	}
	
	public void print(Graphics2D g2) {
		Color prev = g2.getColor();
		if(MainFrame.DEBUG) {
			g2.setColor(Color.WHITE);
			g2.drawString("ID: " + this.ID + ", X:" + this.x + ", Y:" + this.y, x, y-10);
		}
		if(this.image != null){
			g2.drawImage(image, x, y, width, height, null);
			return;
		}
		g2.setColor(this.color);
		g2.fillRect(x, y, width, height);
		g2.setColor(prev);
	}
	
	public void tick(){
		
	}

	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
}
