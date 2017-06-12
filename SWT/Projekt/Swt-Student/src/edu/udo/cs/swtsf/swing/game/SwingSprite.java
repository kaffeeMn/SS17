package edu.udo.cs.swtsf.swing.game;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import edu.udo.cs.swtsf.view.Animator;
import edu.udo.cs.swtsf.view.Sprite;
import edu.udo.cs.swtsf.view.ViewManager;

public class SwingSprite extends Sprite implements SwingGraphicalElement {
	
	private final AffineTransform localMat = new AffineTransform();
	private final AffineTransform globalMat = new AffineTransform();
	private BufferedImage cachedSrcImg;
	private BufferedImage cachedSubImg;
	private boolean localMatValid = false;
	private boolean globalMatValid = false;
	private boolean hasErrors = false;
	private Object modelObj;
	
	protected SwingSprite() {
	}
	
	protected void setViewManager(ViewManager viewManager) {
		if (getAnimator() != null && getPainter() != null) {
			getPainter().removeFromAnimatedSpritesList(this);
		}
		super.setViewManager(viewManager);
		if (getAnimator() != null && getPainter() != null) {
			getPainter().addToAnimatedSpritesList(this);
		}
	}
	
	public SwingPainter getPainter() {
		return (SwingPainter) getViewManager();
	}
	
	public void setAnimator(Animator value) {
		if (getPainter() == null) {
			super.setAnimator(value);
			return;
		}
		boolean wasAdded = getAnimator() != null;
		boolean isAdded = value != null;
		super.setAnimator(value);
		
		if (!wasAdded && isAdded) {
			getPainter().addToAnimatedSpritesList(this);
		} else if (wasAdded && !isAdded) {
			getPainter().removeFromAnimatedSpritesList(this);
		}
	}
	
	public void setModelObject(Object object) {
		modelObj = object;
	}
	
	public Object getModelObject() {
		return modelObj;
	}
	
	public void paint(Graphics2D g) {
		if (hasErrors || getImagePath().isEmpty()) {
			return;
		}
		buildGlobalMatrix();
		buildCachedImage();
		
		if (hasErrors) {
			return;
		}
		
		BufferedImage bufImg = cachedSubImg;
		int x = -bufImg.getWidth() / 2;
		int y = -bufImg.getHeight() / 2;
		
		g.setTransform(globalMat);
		g.drawImage(bufImg, x, y, null);
	}
	
	public void invalidate() {
		localMatValid = false;
		globalMatValid = false;
	}
	
	private void buildLocalMatrix() {
		if (localMatValid) {
			return;
		}
		localMat.setToIdentity();
		localMat.translate(translationX, translationY);
		localMat.quadrantRotate(1);
		localMat.rotate(Math.toRadians(rotation));
		localMat.scale(scale, scale);
		localMatValid = true;
	}
	
	private void buildGlobalMatrix() {
		if (globalMatValid) {
			return;
		}
		buildLocalMatrix();
		globalMat.setTransform(localMat);
		if (getParent() != null) {
			SwingSpriteSet parent = (SwingSpriteSet) getParent();
			parent.buildGlobalMatrix();
			globalMat.preConcatenate(parent.getGlobalMatrix());
		}
		globalMatValid = true;
	}
	
	private void invalidateImageCache() {
		cachedSubImg = null;
		hasErrors = false;
	}
	
	private void buildCachedImage() {
		if (cachedSubImg != null) {
			return;
		}
		try {
			cachedSrcImg = getPainter().fetchImage(getImagePath());
			
			cachedSubImg = cachedSrcImg.getSubimage(
					getImageCutoutX(), getImageCutoutY(), 
					getImageCutoutWidth(), getImageCutoutHeight());
		} catch (Exception e) {
			System.err.println("getImagePath()="+getImagePath());
			e.printStackTrace();
			hasErrors = true;
		}
	}
	
	public void setImagePath(String value) {
		super.setImagePath(value);
		cachedSrcImg = null;
		invalidateImageCache();
	}
	
	public void setImageCutoutX(int value) {
		super.setImageCutoutX(value);
		invalidateImageCache();
	}
	
	public void setImageCutoutY(int value) {
		super.setImageCutoutY(value);
		invalidateImageCache();
	}
	
	public void setImageCutoutWidth(int value) {
		super.setImageCutoutWidth(value);
		invalidateImageCache();
	}
	
	public void setImageCutoutHeight(int value) {
		super.setImageCutoutHeight(value);
		invalidateImageCache();
	}
	
	public void setTranslation(double x, double y) {
		super.setTranslation(x, y);
		invalidate();
	}
	
	public void setScale(double value) {
		super.setScale(value);
		invalidate();
	}
	
	public void setRotation(double value) {
		super.setRotation(value);
		invalidate();
	}
	
}