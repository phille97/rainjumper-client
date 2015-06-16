package edu.tis.phille97.Entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Cloud extends Entity {
	private int x, y, size, ySpeed;
	private double xSpeed;
	private Image image = null;
	
	public Cloud(int startX, int startY, int size, int xSpeed, int screenWidth, int ySpeed, String picture) {
		this.x = startX;
		this.y = startY;
		this.size = size;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		try {
		    image = ImageIO.read(new File("images/" + picture));
		} catch (IOException e) {
			System.out.println("Error loading cloud image!\n  >Could not find: images/" + picture);
		}
	}
	
	public void print(Graphics2D g) {
		//g.fillOval(x, y, length+2, length+3);
		g.setColor(Color.CYAN);
		if(image != null) g.drawImage(image, x, y, size, (int) (size/2.5), null);
		else g.drawString("A cloud!", x, y);
	}
	int yOrigin = y;
	public void tick(){
		x += xSpeed;
		if(y > yOrigin) y -= ySpeed;
		else if(y < yOrigin) y += ySpeed;
		else y += ySpeed;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
}
