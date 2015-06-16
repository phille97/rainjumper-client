package edu.tis.phille97.Mobs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class OtherPlayer extends Mob{
	private BufferedImage image = null;
	private double x, y;
	private int width = 70, height = 120;
	private boolean goingRight = true;
	private int ID;
	
	private int update_counter;
	
	private String name = "OtherPlayer";
	
	public OtherPlayer(int x, int y, String name, int ID){
		this.name = name;
		this.x = (double)x;
		this.y = (double)y;
		this.ID = ID;
		try {
		    image = ImageIO.read(new File("images/player.png"));
		} catch (IOException e) {
			System.out.println("Error loading otherplayer image!\n  >Could not find: images/player.png");
		}
	}
	
	public void setGoingRight(boolean goingRight){
		this.goingRight = goingRight;
	}
	
	public int getX(){
		return (int) this.x;
	}
	
	public int getID(){
		return this.ID;
	}
	public String getName(){
		return name;
	}
	
	public void setX(int x){
		this.x = (int) x;
	}
	public void setY(int y){
		this.y = (int) y;
	}
	
	public void resetUpdatingCounter(){
		this.update_counter = 0;
	}

	public boolean netUpdatinglatestover(int thisNumber){
		if(this.update_counter > thisNumber) return true;
		return false;
	}
	
	public void tick(){
		this.update_counter++;
	}
	
	public void print(Graphics2D g){
		Color prev = g.getColor();
		
		g.setColor(Color.WHITE);
		g.drawString("#" + this.ID + ", " + name + " HP: "+ this.getHP() , (int)x-40, (int)y-(height+10));
		
		if(image != null) {
			if(this.goingRight){
				g.drawImage(image, (int)x-width, (int)y-height, width, height, null);
			}else{
				g.drawImage(image, (int)x, (int)y-height, -width, height, null);
			}
		}else{
			g.setColor(Color.RED);
			g.drawRect((int)x-width, (int)y-height, width, height);
			
		}
		
		g.setColor(prev);
	}
}
