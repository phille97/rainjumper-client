package edu.tis.phille97.main;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.net.URL;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Launcher extends JFrame{
	private static final long serialVersionUID = 1L;
	private static final String URL = "http://dl.suke.se/javares/", PATH = "images/";
	private final String[] dl_images = {"player.png", "cloud_1.png", "info.png", "backcover.png"};
	private String username = "Player 1";
	private JLabel loading = new JLabel("Downloading recources...");
	
	public Launcher(String title) {
		if(MainFrame.STOP_DOWNLOADS){
			this.startGame();
			return;
		}
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(300, 200);
		this.setResizable(false);
		this.setTitle(title);
		this.add(loading);
		
		this.setLocationByPlatform(true);
		this.setVisible(true);
		Downloader thread = new Downloader();
		thread.start();
		while(thread.isRunning()){
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.setVisible(false);
		this.startGame();
		
	}
	
	
	private void startGame(){
		@SuppressWarnings("unused")
		MainFrame frame = new MainFrame(username);
	}
	private class Downloader extends Thread {
		private boolean running = true;
		public boolean isRunning(){
			return running;
		}
		@Override
		public void run(){
			String directoryName = "images/";
			File theDir = new File(directoryName);
			
			if (!theDir.exists()) {
				System.out.println("creating directory: " + directoryName);
				boolean result = theDir.mkdir();
				if(result) {
					System.out.println("DIR created");
				}
			}
			System.out.println("Downloading...");
			try {
				for(int i = 0; i < dl_images.length; i++){
					loading.setText("Downloading recources: " + (i+1 + "/" + (dl_images.length+1)));
					System.out.println((i+1 + "/" + (dl_images.length+1)));
					String getUrl = URL + dl_images[i];
					saveImage(getUrl, PATH + dl_images[i]);
				}
			} catch (IOException e) {
				loading.setText("Check your internet connection!");
				e.printStackTrace();
				int answer = JOptionPane.showConfirmDialog(null, "The download failed!\n  Do you want to start the game anyway?", "Rain - Error dialog", JOptionPane.YES_NO_OPTION);
				if(answer == JOptionPane.NO_OPTION){
					System.exit(-1);
				}
			}
			
			loading.setText("Downloading recources: 100%");
			running = false;
		}
		
		public void saveImage(String imageUrl, String destinationFile) throws IOException {
			URL url = new URL(imageUrl);
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(destinationFile);

			byte[] b = new byte[2048];
			int length;

			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}

			is.close();
			os.close();
		}
	}
}
