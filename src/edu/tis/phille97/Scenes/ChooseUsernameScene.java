package edu.tis.phille97.Scenes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import edu.tis.phille97.Core.Game;
import edu.tis.phille97.Entities.Entity;
import edu.tis.phille97.Scenes.MenuItems.Button;
import edu.tis.phille97.Scenes.MenuItems.InputField;
import edu.tis.phille97.main.MainFrame;

public class ChooseUsernameScene extends Scene {
	
	private ArrayList<Button> buttons = new ArrayList<Button>();
	private Color backgroundColor, buttonColor = null;
	private Button okButton, enterUsername;
	private InputField nameIF;
	
	private Scene goTo;

	public ChooseUsernameScene(Game game, Scene goTo, ArrayList<Entity> entities, Dimension screenSize, Color bgColor, Color buttonColor) {
		super(game, entities, screenSize, false, screenSize.height);
		this.backgroundColor = bgColor;
		this.buttonColor = buttonColor;
		
		this.goTo = goTo;
		
		enterUsername = new Button("Enter a username:", this.buttonColor, this.buttonColor.darker(), (WIDTH/2)-150, (HEIGHT/2)-77, 220, 30);
		nameIF = new InputField(MainFrame.COMPU_USERNAME, Color.WHITE, Color.DARK_GRAY, (WIDTH/2)-150, (HEIGHT/2)-50, 300, 30);
		okButton = new Button("OK", this.buttonColor, this.buttonColor.darker(), (WIDTH/2)-150, (HEIGHT/2), 300, 30);
		buttons.add(okButton);
		
	}
	
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	private int mouseX, mouseY;
	
	public void tick(int mouseX, int mouseY, boolean LeftButtonClicked){
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		for(Entity e : entities) {
			if(e != null) e.tick();
		}
		for(Button b : buttons){
			b.tick(mouseX, mouseY, LeftButtonClicked);
		}
		nameIF.tick(mouseX, mouseY, LeftButtonClicked);
	}
	public void keyTyped(KeyEvent e){
		if(nameIF.isMarked()) {
			if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE){
				String text = "";
				text = "" + e.getKeyChar();
				if(Character.isDigit(e.getKeyChar()) || Character.isLetter(e.getKeyChar())){
					nameIF.appendText(text);
				}
			}
		}
	}
	public void KeyPressed(int e){
		if(e == KeyEvent.VK_BACK_SPACE) {
			nameIF.backspace();
		}else if(e == KeyEvent.VK_ENTER){
			this.game.changeUsername(this.nameIF.getContainingText());
			this.game.changeScene(goTo);
		}
	}
	public void mousePressed(MouseEvent e){
		for(Button b : buttons){
			if(mouseX > b.getX() && mouseX < b.getX()+b.getWidth()){
				if(mouseY > b.getY() && mouseY < b.getY()+b.getHeight()){
					if(b.equals(okButton)){ // OK
						this.game.changeUsername(this.nameIF.getContainingText());
						this.game.changeScene(goTo);
					}
				}
			}
		}
	}
	public void printBackground(Graphics2D g2){
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		Color previousColor = g2.getColor();
		
		g2.setColor(this.backgroundColor);
		g2.fillRect(0, 0, WIDTH, HEIGHT);
		
		enterUsername.print(g2);
		for(Button b : buttons){
			b.print(g2);
		}
		nameIF.print(g2);
		
		g2.setColor(previousColor);
	}

}
