package edu.tis.phille97.Scenes.MenuItems;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class InputField {
	private int x, y, width, height;
	private Color innerColor, borderColor;
	private String containingText;
	private boolean marked = false;
	
	public InputField(String text, Color innerColor, Color borderColor, int x, int y, int width, int height){
		this.containingText = text;
		this.innerColor = innerColor;
		this.borderColor = borderColor;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void backspace(){
		if(this.containingText.length() == 0) return;
		this.containingText = this.containingText.substring(0, this.containingText.length()-1);
	}
	public void appendText(String text){
		this.containingText += text;
	}
	
	public String getContainingText(){
		return this.containingText;
	}
	
	public boolean isMarked(){
		return this.marked;
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
		String curseror = marked ? "|" : "";
		
		g2.setFont(new Font("", Font.BOLD, 20));
		if(this.marked){
			g2.setColor(innerColor.brighter());
			g2.fillRect(x, y, width, height);
			g2.setColor(borderColor.brighter());
			g2.drawRect(x, y, width, height);
			g2.setColor(innerColor.darker());
			g2.drawString(containingText + curseror, x+15, y+(height/2)+5);
		}else{
			g2.setColor(innerColor);
			g2.fillRect(x, y, width, height);
			g2.setColor(borderColor);
			g2.drawRect(x, y, width, height);
			g2.setColor(innerColor.darker());
			g2.drawString(containingText + curseror, x+15, y+(height/2)+5);
		}
	}
	public void tick(int mouseX, int mouseY, boolean LeftButtonClicked){
		if(mouseX > x && mouseX < x+width) {
			if(mouseY > y && mouseY < y+height) {
				if(LeftButtonClicked) this.marked = true;
				
			} else {
				this.marked = false;
			}
		} else {
			this.marked = false;
		}
	}
}
