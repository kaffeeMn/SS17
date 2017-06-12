package edu.udo.cs.swtsf.swing.game;

import java.awt.Graphics2D;

import edu.udo.cs.swtsf.view.SpriteSet;

public interface SwingGraphicalElement {
	
	public void setParent(SpriteSet spriteSet);
	
	public void setModelObject(Object object);
	
	public Object getModelObject();
	
	public void invalidate();
	
	public void paint(Graphics2D g);
	
}