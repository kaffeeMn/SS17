package edu.udo.cs.swtsf.swing.game;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import edu.udo.cs.swtsf.view.Animator;
import edu.udo.cs.swtsf.view.GraphicalElement;
import edu.udo.cs.swtsf.view.SpriteSet;
import edu.udo.cs.swtsf.view.ViewManager;

public class SwingSpriteSet extends SpriteSet implements SwingGraphicalElement {
	
	private final AffineTransform localMat = new AffineTransform();
	private final AffineTransform globalMat = new AffineTransform();
	private boolean localMatValid = false;
	private boolean globalMatValid = false;
	private Object modelObj;
	
	protected SwingSpriteSet() {
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
		for (GraphicalElement node : children) {
			SwingGraphicalElement child = (SwingGraphicalElement) node;
			child.paint(g);
		}
	}
	
	public void invalidate() {
		localMatValid = false;
		globalMatValid = false;
		for (GraphicalElement node : children) {
			SwingGraphicalElement child = (SwingGraphicalElement) node;
			child.invalidate();
		}
	}
	
	private void buildLocalMatrix() {
		if (localMatValid) {
			return;
		}
		localMat.setToIdentity();
		localMat.translate(translationX, translationY);
		localMat.rotate(Math.toRadians(rotation));
		localMat.scale(scale, scale);
		localMatValid = true;
	}
	
	void buildGlobalMatrix() {
		if (globalMatValid) {
			return;
		}
		buildLocalMatrix();
		globalMat.setTransform(localMat);
		if (getParent() != null) {
			SwingSpriteSet parent = (SwingSpriteSet) getParent();
			parent.buildGlobalMatrix();
			globalMat.preConcatenate(parent.globalMat);
		}
		globalMatValid = true;
	}
	
	AffineTransform getGlobalMatrix() {
		return globalMat;
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