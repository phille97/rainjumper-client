package edu.tis.phille97.Mobs;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import edu.tis.phille97.Entities.Entity;
import edu.tis.phille97.Entities.Obstacle;
import edu.tis.phille97.main.MainFrame;


public class Player extends Mob{
	private BufferedImage image = null;
	private double x, y;
	private int width = 70, height = 120;
	private int floorY;
	private int ID = -1;
	private int maxLeft;
	
	@SuppressWarnings("unused")
	private Random rand = new Random();
	private String name = "Player 1";
	public String getName(){
		return name;
	}
	public void changeName(String newName){
		this.name = newName;
	}
	
	@SuppressWarnings("unused")
	private boolean solid = true;
	private static final boolean NOCLIP = false;
	
	private ArrayList<Entity> obs;
	
	private boolean goingRight = true;
	
	public Player(int x, int y, int floorY, String name, double HP, ArrayList<Entity> obs, int maxwalkingLeft){
		this.name = name;
                this.HP = HP;
		this.floorY = floorY;
		this.x = (double)x;
		this.y = (double)y;
		this.obs = obs;
		this.maxLeft = maxwalkingLeft;
		try {
		    image = ImageIO.read(new File("images/player.png"));
		} catch (IOException e) {
			System.out.println("Error loading player image!\n  >Could not find: images/player.png");
		}
	}
	
	private double xSpeed, ySpeed = 0;
	private int jumpCount = 0;
	private boolean collidedY = false;
	
	public void tick(){
		
		if(NOCLIP){
			// NOCLIP / FLYING
			if((ySpeed != 0 || y < floorY)){
				y += ySpeed;
				
				if(y < floorY && ySpeed > 0){
					ySpeed = 0;
				}else if(y >= floorY){ 
					y = floorY-1;
					ySpeed = 0;
				}else if(ySpeed < -0.5){
					ySpeed = 0;
				}else if(ySpeed < 0 && ySpeed > -0.5){ 
					ySpeed = 0;
				}
				if(ySpeed == 0 && y < floorY){
					ySpeed = 0;
				}
			}
		}else{
			// NORMAL COLLISION
			if((ySpeed != 0 || y < floorY)){
				y += ySpeed;
				
				if(y < floorY && ySpeed > 0){
					ySpeed += 0.2;
				}else if(y >= floorY){ 
					y = floorY-1;
					ySpeed = 0;
				}else if(ySpeed < -0.5){
					ySpeed += 0.4;
				}else if(ySpeed < 0 && ySpeed > -0.5){ 
					ySpeed = 0.5;
				}
				if(ySpeed == 0 && y < floorY){
					ySpeed = 1;
				}
			}
				
			if(xSpeed != 0){
				
				if(xSpeed > 0){
					xSpeed -= 0.25;
				} else {
					xSpeed += 0.25;
				}
				if(xSpeed < 0.5 && xSpeed > -0.5){
					xSpeed = 0;
				}
				
				if(x < width) x = width;
				else if(x > maxLeft) x = maxLeft;
				x += xSpeed;
			}
			
			boolean testCollideY = false;
			for(Entity o : this.obs){
				if(o.getClass().equals(Obstacle.class)){
					Obstacle ob = (Obstacle)o;
					if(!(ob.isSolid())) continue;
					
					// Check for collisions up and down!!!
					if(x > ob.getX() && x-width < ob.getX() + ob.getWidth()){
						if((y > ob.getY()) && (y < ob.getY() + ob.getHeight())){
							if((y-1 > ob.getY()) && (y-1 < ob.getY() + ob.getHeight())) {
								if(ob.isTrampoline()) ySpeed = -15;
								else ySpeed = -3;
								if(ob.isConveyor()){
									if(xSpeed > 0) xSpeed += ob.getConveyorSpeed();
									else if(xSpeed < 0) xSpeed -= ob.getConveyorSpeed();
								}
							}else if(ob.isTrampoline()){
								ySpeed = -15;
							}
							else ySpeed = 0;
							testCollideY = true;
						}
					}
				}
			}
			collidedY = testCollideY;
		}
		
	}
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	
	public void goRigth(){
		if(NOCLIP){
			x += 5;
			return;
		}
		this.goingRight = true;
		if(xSpeed < 4)xSpeed = 4;
	}
	
	public void goLeft(){
		if(NOCLIP){
			x -= 5;
			return;
		}
		this.goingRight = false;
		if(xSpeed > -4)xSpeed = -4;
	}
	
	public void goDown() {
		if(NOCLIP){
			ySpeed = 5;
		}else{
			ySpeed = 10;
		}
	}
	
	private long firstJump = 0;
	
	public void jump(){
		
		if(NOCLIP) {
			ySpeed = -5;
			return;
		}
		
		if(y >= floorY-10 || collidedY) {
			jumpCount = 0;
			firstJump = System.currentTimeMillis();
		}
		
		if(y >= floorY-10 || (jumpCount < 2 && firstJump+300 <= System.currentTimeMillis()) || collidedY){
			firstJump = System.currentTimeMillis();
			jumpCount += 1;
			ySpeed = -10;
		}
	}
	
	public void print(Graphics2D g){
		Color prev = g.getColor();
		
		g.setColor(Color.WHITE);
		g.drawString("#" + this.ID + ", " + name + " HP: "+ this.getHP() , (int)x-40, (int)y-(height+10));
		if(MainFrame.DEBUG){
			g.drawString("Collision with obstacle: " + collidedY , (int)x + width, (int)y - height);
			int ySpeedAvrundat = (int)(ySpeed);
			int xSpeedAvrundat = (int)(xSpeed);
			g.drawString("ySpeed: " + ySpeedAvrundat , (int)x + width, (int)y - height+15);
			g.drawString("xSpeed: " + xSpeedAvrundat , (int)x + width, (int)y - height+30);
			int xAvrundat = (int)(x);
			int yAvrundat = (int)(y);
			g.drawString("X: " + xAvrundat + ", Y: " + yAvrundat , (int)x + width, (int)y - height+45);
			
			g.setColor(Color.RED);
			g.drawRect((int)x-width, (int)y-height, width, height);
			g.setColor(Color.GREEN);
			g.drawLine((int)x-width, (int)y, (int)x, (int)y);
		}
		
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
	public int getID() {
		return this.ID;
	}
	public void setID(int id){
		this.ID = id;
	}
}
