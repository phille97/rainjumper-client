package edu.tis.phille97.Scenes.MenuItems;

import java.awt.Graphics2D;
import java.awt.Image;

public class SlidingImage {
	private Image image;
	private int x, y;
	private int goToY, goToX;
	public final int HEIGHT = 150, WIDTH = 500;
	
	public SlidingImage(Image image, int xSpawn, int ySpawn, int xGoal, int yGoal) {
		this.image = image;
		this.x = xSpawn;
		this.y = ySpawn;
		this.goToY = yGoal;
		this.goToX = xGoal;
	}
	
	public void tick(int mouseX, int mouseY){
		if(x > goToX){
			this.x -= 10;
		}
		if(x < goToX){
			this.x += 10;
		}
		if(y > goToY){
			this.y -= 10;
		}
		if(y < goToY){
			this.y += 10;
		}
	}
	
	public void print(Graphics2D g2){
		g2.drawImage(image, this.x, this.y, this.WIDTH, this.HEIGHT, null);
	}
}
