package edu.tis.phille97.Entities;
import java.awt.Color;
import java.awt.Graphics2D;


public class Rain extends Entity {
	private int x, y, length, floorY, offset_dir;
	private double speed;
	
	public Rain(int startX, int startY, int length, int speed, int floorY, int offset_dir) {
		this.x = startX;
		this.y = startY;
		this.length = length;
		this.speed = speed;
		this.floorY = floorY;
		this.offset_dir = offset_dir;
	}
	
	public void print(Graphics2D g) {
		//g.fillOval(x, y, length+2, length+3);
		g.setColor(Color.WHITE);
		g.drawString("Hej", x, y);
	}
	
	public void tick(){
		this.y += this.speed;
		this.x += (this.speed/2)+this.offset_dir;
		if(y + length >= floorY-5){
			this.speed = 1;
		}
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
}
