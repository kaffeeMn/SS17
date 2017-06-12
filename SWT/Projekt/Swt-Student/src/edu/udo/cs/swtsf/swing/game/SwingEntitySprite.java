package edu.udo.cs.swtsf.swing.game;

import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.core.EntityObserver;
import edu.udo.cs.swtsf.view.ViewManager;

public class SwingEntitySprite extends SwingSprite {
	
	public static final int CUTOUT_SIZE = 32;
	
	private final EntityObserver observer = new EntityObserver() {
		public void onPositionChanged(Entity entity) {
			SwingEntitySprite.this.setTranslation(entity.getX(), entity.getY());
		}
		public void onSizeChanged(Entity entity) {
			SwingEntitySprite.this.setScale(calculateScale(entity));
		}
		public void onRotationChanged(Entity entity) {
			SwingEntitySprite.this.setRotation(entity.getRotation());
		}
	};
	private final Entity entity;
	
	protected SwingEntitySprite(Entity entity) {
		this.entity = entity;
		
		setImageCutoutWidth(CUTOUT_SIZE);
		setImageCutoutHeight(CUTOUT_SIZE);
		
		setImagePath(entity.getClass().getSimpleName());
		setTranslation(entity.getX(), entity.getY());
		setScale(calculateScale(entity));
		setRotation(entity.getRotation());
	}
	
	protected void setViewManager(ViewManager viewManager) {
		if (getViewManager() != null) {
			entity.removeObserver(observer);
		}
		super.setViewManager(viewManager);
		if (getViewManager() != null) {
			entity.addObserver(observer);
		}
	}
	
	private static double calculateScale(Entity entity) {
		return entity.getSize() / (double) CUTOUT_SIZE;
	}
	
}