package edu.tis.phille97.Entities;
import java.awt.Graphics2D;


public class Entity {
	private boolean solid = false;
	
	public boolean isObstacle(){
		return this.solid;
	}
	
	private int ID = -1;
	
	public int getID(){
		return this.ID;
	}
	
	public void tick(){
		
	}
	
	public void print(Graphics2D g){
		
	}
}
