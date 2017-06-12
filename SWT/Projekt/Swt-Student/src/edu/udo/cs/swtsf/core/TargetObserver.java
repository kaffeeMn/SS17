package edu.udo.cs.swtsf.core;

import java.util.ConcurrentModificationException;

import edu.udo.cs.swtsf.core.player.PlayerObserver;

/**
 * <p>TargetObserver's can be registered at individual or multiple {@link Target Targets}. 
 * Once registered the TargetObserver will be notified every time the 
 * {@link Target#getHitpoints() hitpoints} of the target are changed, or when the target 
 * is {@link Target#isDead() destroyed}.</p>
 * 
 * <p>A Target can have any number of TargetObserver's registered at the same time. 
 * No assumptions should be made about the order in which observers will be notified of 
 * changes.</p>
 * 
 * <p>Observers can be added to and removed from a Target during a notification 
 * without throwing any {@link ConcurrentModificationException 
 * ConcurrentModificationExceptions}.</p>
 * 
 * <p>As the name suggests, this interface is an implementation of the observer pattern.</p>
 * 
 * @see Target#addTargetObserver(TargetObserver)
 * @see Target#removeTargetObserver(TargetObserver)
 * @see EntityObserver
 * @see PlayerObserver
 */
public interface TargetObserver {
	
	/**
	 * <p>This method is called immediately before the host {@link Target} is about to 
	 * have its {@link Target#getHitpoints() hitpoints} changed.</p>
	 * <p>It may not be, that the Targets hitpoints are changed. If the Target has the 
	 * {@link Target#isHitpointsReadOnly()} flag set the change will not be performed. 
	 * The same is true if the Target was already {@link Target#isDead() destroyed}.</p>
	 * @param target		the Target for which the hitpoints are about to be changed. This is never null.
	 * @param delta			the amount by which the hitpoints will be changed. May be either positive or negative.
	 * @see Target#setHitpoints(int)
	 * @see Target#getHitpoints()
	 * @see Target#setHitpointsReadOnly(boolean)
	 * @see Target#isHitpointsReadOnly()
	 * @see Target#isAlive()
	 * @see Target#isDead()
	 * @see Target#destroy()
	 */
	public default void onBeforeHitpointsChanged(Target target, int delta) {}
	
	/**
	 * <p>This method is called immediately after the host {@link Target} had its 
	 * {@link Target#getHitpoints() hitpoints} changed. The change has already been performed.</p>
	 * @param target		the Target for which the hitpoints have changed. This is never null.
	 * @param delta			the amount by which the hitpoints were changed. May be either positive or negative.
	 * @see Target#setHitpoints(int)
	 * @see Target#getHitpoints()
	 * @see Target#setHitpointsReadOnly(boolean)
	 * @see Target#isHitpointsReadOnly()
	 * @see Target#isAlive()
	 * @see Target#isDead()
	 * @see Target#destroy()
	 */
	public default void onAfterHitpointsChanged(Target target, int delta) {}
	
	/**
	 * <p>This method is called immediately after the host {@link Target} was {@link Target#isDead() 
	 * destroyed}.</p>
	 * <p>A Target is destroyed when its {@link Target#getHitpoints() hitpoints} are set to 0.</p>
	 * @param target		the Target which was destroyed. This is never null.
	 * @see Target#setHitpoints(int)
	 * @see Target#getHitpoints()
	 * @see Target#setHitpointsReadOnly(boolean)
	 * @see Target#isHitpointsReadOnly()
	 * @see Target#isAlive()
	 * @see Target#isDead()
	 * @see Target#destroy()
	 */
	public default void onDeath(Target target) {}
	
}