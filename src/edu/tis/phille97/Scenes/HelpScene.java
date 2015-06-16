package edu.tis.phille97.Scenes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import edu.tis.phille97.Core.Game;
import edu.tis.phille97.Entities.Entity;
import edu.tis.phille97.Scenes.MenuItems.Button;

public class HelpScene extends Scene{
	
	private Button back = new Button("Back", Color.DARK_GRAY, Color.RED, 20, HEIGHT - 100, 100, 30);
	
	
	public HelpScene(Game game, ArrayList<Entity> entities, Dimension screenSize) {
		super(game, entities, screenSize, false, screenSize.height);
	}
	
	public void mousePressed(MouseEvent e){
		if(mouseX > back.getX() && mouseX < back.getX()+back.getWidth()){
			if(mouseY > back.getY() && mouseY < back.getY()+back.getHeight()){
				this.game.changeScene(new Menu(this.game, (new Dimension(WIDTH, HEIGHT)), false));
			}
		}
	}
	
	int mouseX=0, mouseY=0;
	public void tick(int mouseX, int mouseY, boolean LeftButtonClicked){
		back.tick(mouseX, mouseY, LeftButtonClicked);
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}
	
	public void printBackground(Graphics2D g2){
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		Color previousColor = g2.getColor();
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, WIDTH, HEIGHT);
		
		g2.setFont(new Font("", Font.BOLD, 20));
		g2.setColor(Color.WHITE);
		
		String[] infotext = {
				"Keys:",
				"",
				"Move with A and D",
				"E  - Switch tool",
				"S  - Jump down",
				"SPACE  - Jump",
				"T  - Open the chat input",
				"R  - Toggle rain and clouds",
				"Q  - Exit to the menu",
				"Z  - Remove the latest placed block",
				"ESC  - Remove all blocks",
				"",
				"",
				"Click on the screen to place the current block!",
				"Type '/help' in the chat input to see available commands",
				"",
				"",
				"",
				"",
				};
		
		int i = 0;
		for(int y = 0; y < infotext.length; y++){
			try{
				g2.drawString(infotext[i], 100, 100+(y*20));
			}catch (Exception e){
			}
				i++;
		}
		
		back.print(g2);
		
		g2.setColor(previousColor);
	}

}
