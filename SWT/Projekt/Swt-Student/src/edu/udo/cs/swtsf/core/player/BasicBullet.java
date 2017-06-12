package edu.udo.cs.swtsf.core.player;

import edu.udo.cs.swtsf.core.Bullet;
import edu.udo.cs.swtsf.core.Entity;

/**
 * BasicBullets are Bullets which are automatically disposed after hitting a target.
 */
public class BasicBullet extends Bullet {
	
	/**
	 * @see Bullet#Bullet(Entity)
	 */
	public BasicBullet(Entity sourceEntity) {
		super(sourceEntity);
		addHitStrategy(BULLET_SELF_DESTRUCT_ON_HIT_STRAT);
	}
	
}