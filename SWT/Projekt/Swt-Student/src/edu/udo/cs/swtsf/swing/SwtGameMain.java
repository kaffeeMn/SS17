package edu.udo.cs.swtsf.swing;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class SwtGameMain {
	
	public static final String TITLE_TEXT = "SWT - Starfighter";
	public static final int DEFAULT_WIDTH = 640;
	public static final int DEFAULT_HEIGHT = (int) (DEFAULT_WIDTH * 0.75);
	
	public static void main(String[] args) {
		/*
		 * Swing Applications must be started this way.
		 * This makes sure that all Swing actions are run on the 
		 * Event-Dispatch-Thread EDT) instead of the main thread.
		 */
		EventQueue.invokeLater(() -> new SwtGameMain());
	}
	
	private final ImageCache imgCache = new ImageCache();
	private final JFrame frame;
	// The title-screen, highscore-screen, game-screen, etc
	private SwingGameScreen currentScreen;
	
	public SwtGameMain() {
		frame = new JFrame();
		
		frame.setTitle(TITLE_TEXT);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		// centers the frame on screen
		frame.setLocationRelativeTo(null);
		
		// to pause / unpause the game
		frame.addWindowListener(new WindowAdapter() {
			public void windowGainedFocus(WindowEvent e) {
				SwtGameMain.this.onWindowActivated();
			}
			public void windowLostFocus(WindowEvent e) {
				SwtGameMain.this.onWindowDeactivated();
			}
			public void windowClosed(WindowEvent e) {
				SwtGameMain.this.onWindowClosed();
			}
		});
		
		SwingKeyBindingsScreen.loadKeyBindingMap(frame.getContentPane());
		setScreen(new SwingTitleScreen());
		frame.setVisible(true);
	}
	
	public ImageCache getImageCache() {
		return imgCache;
	}
	
	public void setScreen(SwingGameScreen screen) {
		// clean up the old screen
		if (currentScreen != null) {
			currentScreen.terminate();
			currentScreen.setSwtGameMain(null);
		}
		currentScreen = screen;
		// if screen is null we quit the application
		if (currentScreen == null) {
			frame.dispose();
		} else {
			if (currentScreen.asJComponent() == null) {
				throw new IllegalArgumentException("screen.asJComponent() == null");
			}
			// setup the new screen
			currentScreen.setSwtGameMain(this);
			currentScreen.initialize();
			frame.setContentPane(currentScreen.asJComponent());
			frame.revalidate();
			frame.repaint();
		}
	}
	
	public SwingGameScreen getCurrentScreen() {
		return currentScreen;
	}
	
	private void onWindowActivated() {
		if (getCurrentScreen() != null) {
			getCurrentScreen().unpause();
		}
	}
	
	private void onWindowDeactivated() {
		if (getCurrentScreen() != null) {
			getCurrentScreen().pause();
		}
	}
	
	private void onWindowClosed() {
		if (getCurrentScreen() != null) {
			getCurrentScreen().terminate();
		}
	}
	
}