package edu.tis.phille97.Scenes.MenuItems;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

public class Popup {
	private double x, y;
	private int width, height;
	private Color innerColor, borderColor;
	private String text;
	private Dimension screenSize;
	
	private int ySpeed = 5;
	
	public Popup(String message, Color innerColor, Color borderColor, Dimension screenSize, int width, int height){
		this.text = message;
		this.screenSize = screenSize;
		this.innerColor = innerColor;
		this.borderColor = borderColor;
		this.x = (screenSize.width/2)-(width/2);
		this.y = -(height+20);
		this.width = width;
		this.height = height;
	}
	
	public void print(Graphics2D g2){
		g2.setFont(new Font("", Font.BOLD, 20));
		
		g2.setColor(innerColor);
		g2.fillRect((int)x, (int)y, width, height);
		g2.setColor(borderColor);
		g2.drawRect((int)x, (int)y, width, height);
		g2.setColor(Color.WHITE);
		g2.drawString(text, (int)x+15, (int)y+(height/2)+5);
	}
	
	public void tick(){
		if(y > (screenSize.height/2) && y < (screenSize.height/2)+100){
			y += 0.5;
			return;
		}
		y += ySpeed;
	}
	
	public int getX(){
		return (int)this.x;
	}
	public int getY(){
		return (int)this.y;
	}
	public int getWidth(){
		return this.width;
	}
	public int getHeight(){
		return this.height;
	}
}
