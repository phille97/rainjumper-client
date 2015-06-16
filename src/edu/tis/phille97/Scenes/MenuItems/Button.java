package edu.tis.phille97.Scenes.MenuItems;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Button {
	private int x, y, width, height;
	private Color innerColor, borderColor;
	private String text;
	private boolean hovering = false;
	private boolean clicked = false;
	
	public Button(String text, Color innerColor, Color borderColor, int x, int y, int width, int height){
		this.text = text;
		this.innerColor = innerColor;
		this.borderColor = borderColor;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}
	public int getWidth(){
		return this.width;
	}
	public int getHeight(){
		return this.height;
	}
	public void print(Graphics2D g2){
		g2.setFont(new Font("", Font.BOLD, 20));
		if(this.clicked){
			g2.setColor(innerColor.brighter());
			g2.fillRect(x, y, width, height);
			g2.setColor(borderColor.brighter());
			g2.drawRect(x, y, width, height);
			g2.setColor(Color.WHITE);
			g2.drawString(text, x+19, y+(height/2)+5);
		}else if(this.hovering){
			g2.setColor(innerColor.darker());
			g2.fillRect(x - 2, y, width, height);
			g2.setColor(borderColor.darker());
			g2.drawRect(x - 2, y, width, height);
			g2.setColor(Color.WHITE);
			g2.drawString(text, x+17, y+(height/2)+5);
		}else{
			g2.setColor(innerColor);
			g2.fillRect(x, y, width, height);
			g2.setColor(borderColor);
			g2.drawRect(x, y, width, height);
			g2.setColor(Color.WHITE);
			g2.drawString(text, x+15, y+(height/2)+5);
		}
	}
	public void tick(int mouseX, int mouseY, boolean LeftButtonClicked){
		if(mouseX > x && mouseX < x+width){
			if(mouseY > y && mouseY < y+height){
				if(LeftButtonClicked) this.clicked = true;
				else {
					this.clicked = false;
					this.hovering = true;
				}
				
			}else{
				this.hovering = false;
				
			}
		}else{
			this.hovering = false;
			this.clicked = false;
		}
	}
}
