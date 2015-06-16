package edu.tis.phille97.main;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import edu.tis.phille97.Core.Game;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private static int HEIGHT, WIDTH;
	
	public static final boolean DEBUG = false;
	
	public static final boolean STOP_DOWNLOADS = (true || DEBUG);
	public static final boolean FULLSCREEN = true;
	public static final String MENU_BOTTOM_TEXT = "preAlpha";
	public static final String COMPU_USERNAME = System.getProperty("user.name");
	public static final String OSNAME = System.getProperty("os.name");
	
	public MainFrame(String username) {
		System.out.println("[Game] Starting RainJuper preAlpha...");
		System.out.println("[Game] Operating system: " + OSNAME);
		System.out.println("[Game] Login name: " + COMPU_USERNAME);
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		this.getContentPane().setCursor(blankCursor);
		
		if(FULLSCREEN){
			this.setExtendedState(Frame.MAXIMIZED_BOTH);
			this.setUndecorated(true);
		}else{
			this.setSize(1000, 700);
		}
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		String some = DEBUG ? " debug mode" : " Made by: Philip Johansson";
		this.setTitle("RainJumper - " + some);
		Game da = new Game();
		this.add(da);
		this.setVisible(true);
		
		HEIGHT = this.getSize().height;
		WIDTH = this.getSize().width;
		da.setStuffs(HEIGHT, WIDTH, username);
		
		
		System.out.println("[Game] Window is open with Width: " + WIDTH + ", Height: " + HEIGHT);
	}
	
	public int getHeight(){
		return HEIGHT;
	}
	public int getWidth(){
		return WIDTH;
	}
	
        @Override
	public void finalize() throws Throwable {
		System.out.println("Stopping!");
                super.finalize();
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		for(int i = 0; i < args.length; ++i) {
			System.out.println(args[i]);
		}
		/*
		 * Starta launchern!
		 */
		Launcher launcher = new Launcher("Rain launcher");
	}

}
