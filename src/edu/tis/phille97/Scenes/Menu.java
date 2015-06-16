package edu.tis.phille97.Scenes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import edu.tis.phille97.Core.Game;
import edu.tis.phille97.Entities.Entity;
import edu.tis.phille97.Scenes.MenuItems.Button;
import edu.tis.phille97.Scenes.MenuItems.Popup;
import edu.tis.phille97.Scenes.MenuItems.SlidingImage;
import edu.tis.phille97.main.MainFrame;

public class Menu extends Scene {
	
	private Image backCover;
	
	private String[] sponsorImagesList = {"info.png"};
	private ArrayList<SlidingImage> slidingimages = new ArrayList<SlidingImage>();
	private ArrayList<Button> buttons = new ArrayList<Button>();
	private ArrayList<Popup> popups = new ArrayList<Popup>();
	
	private Button start = new Button("START", Color.DARK_GRAY, Color.DARK_GRAY, 70, 275, 375, 50);
	private Button multiplayer = new Button("MULTIPLAYER", Color.DARK_GRAY, Color.DARK_GRAY, 70, 330, 375, 50);
	private Button help = new Button("HELP", Color.DARK_GRAY, Color.DARK_GRAY, 70, 385, 375, 50);
	private Button quit = new Button("QUIT", Color.DARK_GRAY, Color.DARK_GRAY, 70, 440, 375, 50);
	
	public Menu(Game game, Dimension screenSize, boolean hasPlayer, String message) {
		this(game,screenSize, hasPlayer);
		int length = message.length()*12;
		popups.add(new Popup(message, Color.LIGHT_GRAY, Color.BLUE, screenSize, length, 50));
	}
	
	public Menu(Game game, Dimension screenSize, boolean hasPlayer) {
		super(game,(new ArrayList<Entity>()), screenSize, hasPlayer, screenSize.height);
		
		buttons.add(start);
		buttons.add(multiplayer);
		buttons.add(help);
		buttons.add(quit);
		
		try {
			backCover = ImageIO.read(new File("images/" + "backcover.png"));
		} catch (IOException e) {
			System.out.println("Error loading image!q\n  >Could not find: images/" + "backcover.png");
		}
		
		for(String imageName : sponsorImagesList){
			Image image = null;
			try {
				image = ImageIO.read(new File("images/" + imageName));
			} catch (IOException e) {
				System.out.println("Error loading image!\n  >Could not find: images/" + imageName);
			}
			if(image != null){
				slidingimages.add(new SlidingImage(image, WIDTH-500, -1000, WIDTH-500, (HEIGHT-25)-(150+(slidingimages.size()*150))));
			}
		}
	}

	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	private int mouseX, mouseY;
	
	public void tick(int mouseX, int mouseY, boolean LeftButtonClicked){
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		for(Entity e : entities) {
			if(e != null) e.tick();
		}
		for(SlidingImage i : slidingimages){
			i.tick(mouseX, mouseY);
		}
		ArrayList<Popup> toDelePopup = new ArrayList<Popup>();
		for(Popup p : popups){
			p.tick();
			if(p.getY() > HEIGHT){
				toDelePopup.add(p);
			}
		}
		for(Popup p : toDelePopup){
			popups.remove(p);
		}
		for(Button b : buttons){
			b.tick(mouseX, mouseY, LeftButtonClicked);
		}
	}
	
	public void mousePressed(MouseEvent e){
		for(Button b : buttons){
			if(mouseX > b.getX() && mouseX < b.getX()+b.getWidth()){
				if(mouseY > b.getY() && mouseY < b.getY()+b.getHeight()){
					if(b.equals(start)){
						// START
						LobbyLevel goToLevel = new LobbyLevel(game, entities, (new Dimension(WIDTH, HEIGHT)), true, HEIGHT-50);
						this.game.changeScene(new ChooseUsernameScene(game, goToLevel, entities, (new Dimension(WIDTH, HEIGHT)), Color.DARK_GRAY, Color.RED));
					}else if(b.equals(multiplayer)){
						// MULTIPLAYER
						MultiplayerLevel goToLevelAftersreverSelection = new MultiplayerLevel(this.game, entities, (new Dimension(WIDTH, HEIGHT)), true, HEIGHT-50);
						ServerSelectionScene goToLevel = new ServerSelectionScene(game, goToLevelAftersreverSelection, entities, (new Dimension(WIDTH, HEIGHT)), Color.DARK_GRAY, Color.RED);
						this.game.changeScene(new ChooseUsernameScene(game, goToLevel, entities, (new Dimension(WIDTH, HEIGHT)), Color.DARK_GRAY, Color.RED));
					}else if(b.equals(help)){
						// HELP
						this.game.changeScene(new HelpScene(this.game, entities, (new Dimension(WIDTH, HEIGHT))));
					}else if(b.equals(quit)){
						// Quit
						this.game.stopEngine();
					}
				}
			}
		}
	}
	
	public void printBackground(Graphics2D g2){
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		Color previousColor = g2.getColor();
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, WIDTH, HEIGHT);
		
		
		for(SlidingImage i : slidingimages){
			i.print(g2);
		}
		
		g2.drawImage(backCover, 0, 0, 500, 500, null);
		
		for(Button b : buttons){
                    b.print(g2);
		}
		g2.setFont(new Font("", Font.BOLD, 30));
		g2.drawString(MainFrame.MENU_BOTTOM_TEXT, 20, HEIGHT-50);
		
                if(MainFrame.DEBUG) {
                    g2.setColor(Color.MAGENTA);
                    g2.drawString("DEBUG MODE", 10, 40);
                }
                
		for(Popup p : popups){
			p.print(g2);
		}
		g2.setColor(previousColor);
	}
}
