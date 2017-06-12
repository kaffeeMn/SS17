package edu.udo.cs.swtsf.core.player;

import edu.udo.cs.swtsf.core.Bullet;
import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.view.GraphicalElement;
import edu.udo.cs.swtsf.view.GraphicalElementFactory;
import edu.udo.cs.swtsf.view.Sprite;
import edu.udo.cs.swtsf.view.SpriteSet;
import edu.udo.cs.swtsf.view.ViewManager;

/**
 * <p>This {@link GraphicalElementFactory} can be used to display arbitrary 
 * {@link Bullet Bullets}. This sprite factory can be used for any kind of 
 * Bullet.</p>
 * 
 * <p>{@link GraphicalElement GraphicalElements} produced by this factory 
 * are displayed as many red triangles orbiting around a shared center. 
 * The number of red triangles is equal to the {@link Bullet#getDamage() 
 * damage} of the Bullet.</p>
 */
public class PlayerLaserSpriteFactory implements GraphicalElementFactory {
	
	public GraphicalElement createForEntity(ViewManager view, Entity entity) {
		Bullet bullet = (Bullet) entity;
		int damage = bullet.getDamage();
		
		// This spriteset is used for the Bullet itself.
		SpriteSet spriteSet = view.newEntitySpriteSet(bullet);
		spriteSet.setScale(bullet.getSize() / 8.0);
		
		// In this loop we create one Sprite for each point of damage
		for (int i = 0; i < damage; i++) {
			// angle to and offset from spriteset calculated by damage point index
			double angle = (360 / damage) * i;
			double offsetX = Entity.getOffsetX(angle, 7);
			double offsetY = Entity.getOffsetY(angle, 7);
			
			Sprite sprite = view.newSprite();
			sprite.setImagePath("BulletRed");
			sprite.setImageCutoutWidth(32);
			sprite.setImageCutoutHeight(32);
			sprite.setTranslation(offsetX, offsetY);
			sprite.setScale(0.25);
			sprite.setRotation(angle);
			sprite.setParent(spriteSet);
		}
		
		// Animation makes spriteset rotate over time
		spriteSet.setAnimator((ele, millis) -> {
			double rotationIncrement = (360 * millis) / 1000.0;
			spriteSet.setRotation(spriteSet.getRotation() + rotationIncrement);
		});
		
		return spriteSet;
	}
	
}