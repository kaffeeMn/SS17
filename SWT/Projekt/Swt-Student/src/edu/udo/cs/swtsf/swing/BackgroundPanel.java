package edu.udo.cs.swtsf.swing;

import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	/*
	 * This JPanel shows an image as its background instead of a solid color.
	 */
	
	public static final String BACKGROUND_FILE_NAME = "TitleImage";
	
	private SwtGameMain main;
	private BufferedImage bckgrImg;
	
	public BackgroundPanel() {
		super();
	}
	
	public BackgroundPanel(LayoutManager layout) {
		super(layout);
	}
	
	public void setSwtGameMain(SwtGameMain swtGameMain) {
		main = swtGameMain;
		if (main == null) {
			bckgrImg = null;
		} else {
			try {
				bckgrImg = main.getImageCache().fetchImage(BACKGROUND_FILE_NAME);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		if (bckgrImg == null) {
			super.paintComponent(g);
		} else {
			g.drawImage(bckgrImg, 0, 0, getWidth(), getHeight(), null);
		}
	}
	
}