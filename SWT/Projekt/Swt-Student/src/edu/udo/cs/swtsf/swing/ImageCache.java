package edu.udo.cs.swtsf.swing;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageCache {
	
	public static final String IMAGE_PATH_PREFIX = "images/";
	public static final String IMAGE_PATH_SUFFIX = ".png";
	
	/*
	 * This will keep a reference to any loaded image for as long as possible.
	 * If the application is running out of memory images that are no longer used 
	 * will be freed. Any images that are still in use will stay cached.
	 */
	private final Map<String, SoftReference<BufferedImage>> imgMap = new HashMap<>();
	
	public BufferedImage fetchImage(String imagePath) throws IOException {
		if (imagePath == null) {
			throw new IllegalArgumentException("imagePath == null");
		}
		SoftReference<BufferedImage> imgRef = imgMap.get(imagePath);
		if (imgRef == null || imgRef.get() == null) {
			File imgFile;
			if (imagePath.lastIndexOf('.') == -1) {
				imgFile = new File(IMAGE_PATH_PREFIX 
						+ imagePath + IMAGE_PATH_SUFFIX);
			} else {
				imgFile = new File(IMAGE_PATH_PREFIX + imagePath);
			}
			
			try {
				BufferedImage img = createAcceleratedImgCopy(ImageIO.read(imgFile));
				imgRef = new SoftReference<>(img);
				imgMap.put(imagePath, imgRef);
			} catch (Exception e) {
				System.err.println("File: "+imgFile);
				throw e;
			}
		}
		return imgRef.get();
	}
	
	/**
	 * <p>Creates an instance of {@link BufferedImage} that is hardware 
	 * accelerated to improve performance.</p>
	 * @param w					the width of the image
	 * @param h					the height of the image
	 * @param transparency		the bit depth for transparency
	 * @return					a new BufferedImage (never null)
	 */
	public static BufferedImage createAcceleratedImg(int w, int h, int transparency) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		BufferedImage compImg = gc.createCompatibleImage(w, h, transparency);
		return compImg;
	}
	
	/**
	 * <p>Creates a copy of the given {@link BufferedImage}. The copy is 
	 * hardware accelerated and might have better performance.</p>
	 * @param img				a non-null BufferedImage 
	 * @return					a new BufferedImage (never null)
	 * @see #createAcceleratedImg(int, int, int)
	 */
	public static BufferedImage createAcceleratedImgCopy(BufferedImage img) {
		BufferedImage copyImg = createAcceleratedImg(img.getWidth(), 
				img.getHeight(), img.getTransparency());
		Graphics compGraphics = copyImg.createGraphics();
		compGraphics.drawImage(img, 0, 0, null);
		compGraphics.dispose();
		return copyImg;
	}
	
}