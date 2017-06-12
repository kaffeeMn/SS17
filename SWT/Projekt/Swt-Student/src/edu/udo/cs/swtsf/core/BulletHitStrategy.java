package edu.udo.cs.swtsf.core;

import java.util.ConcurrentModificationException;

/**
 * <p>{@link BulletHitStrategy BulletHitStrategy's} are used to control 
 * what should happen when a {@link Bullet} hits a {@link Target}. A 
 * BulletHitStrategy can be added to a Bullet by calling the 
 * {@link Bullet#addHitStrategy(BulletHitStrategy)} method. If a strategy 
 * is added to a Bullet this Bullet will be called the host of the strategy. 
 * The host will then call the {@link BulletHitStrategy#onHit(Bullet, Target)} 
 * method every time it hits a Target. The first argument will always be the 
 * host Bullet while the second argument is the Target that was hit.</p>
 * 
 * <p>A BulletHitStrategy can be added to any number of Bullet objects at once. 
 * its {@link BulletHitStrategy#onHit(Bullet, Target)} method will be called 
 * by each host for every hit separately.<br>
 * BulletHitStrategys can be added to and removed from a host during an event 
 * invocation without throwing any {@link ConcurrentModificationException 
 * ConcurrentModificationExceptions}.</p>
 * 
 * <p>As the name suggests, this interface is an implementation of the strategy pattern.</p>
 * 
 * @see Bullet#addHitStrategy(BulletHitStrategy)
 * @see Bullet#removeHitStrategy(BulletHitStrategy)
 */
public interface BulletHitStrategy {
	
	/**
	 * <p>Called by the host {@link Bullet} every time it hits a {@link Target}. 
	 * The first argument will always be the host. The second argument will be 
	 * the target which was hit.</p>
	 * 
	 * @param host		the host Bullet. Guaranteed to not be null.
	 * @param target	the Target which was hit. Guaranteed to not be null.
	 */
	public void onHit(Bullet host, Target target);
	
}