package edu.udo.cs.swtsf.core;

import java.util.ConcurrentModificationException;

import edu.udo.cs.swtsf.core.player.Player;
import edu.udo.cs.swtsf.util.Group;
import edu.udo.cs.swtsf.util.GroupFactory;

/**
 * <p>This class is the super class for all game objects that can be hit and destroyed. 
 * Be it Monsters, Asteroids, Space Mines or the {@link Player}.</p>
 * <p>All {@link Target Target's} have a concept of {@link #getHitpoints() hitpoints}, 
 * an arbitrary number representing the 'life' of a Target. If the hitpoints reach 0 a 
 * Target is considered {@link #isDead() destroyed} and is automatically 
 * {@link #isDisposed() disposed} and removed from the {@link Game}.</p>
 */
public abstract class Target extends Entity {
	
	/**
	 * <p>This {@link EntityBehaviorStrategy} will dispose a {@link Target} when its 
	 * {@link #getHitpoints() hitpoints} have reached 0.</p>
	 * <p>This strategy assumes that the host is always an instance of Target.</p>
	 * <p>This strategy is automatically added to all Target's when they are created.</p>
	 */
	public static final EntityBehaviorStrategy TARGET_DISPOSE_ON_DEATH = 
			(self) -> 
	{
		Target target = (Target) self;
		if (target.isDead() && !target.isDisposed()) {
			target.dispose();
		}
	};
	
	private final Group<TargetObserver> observers 
						= GroupFactory.get().createNewGroup();
	/**
	 * <p>The maximum number of hitpoints for this Target. The minimum is always 0.</p>
	 * <p>The default value for this is {@link Integer#MAX_VALUE}.</p>
	 * @see #setHitpoints(int)
	 * @see #getMaxHitpoints()
	 */
	private int maxHitpoints = Integer.MAX_VALUE;
	/**
	 * <p>The current hitpoints of this Target. When this reaches 0 this Target is considered 
	 * {@link #isDead() dead} and automatically {@link #isDisposed() disposed}.</p>
	 * @see #setHitpoints(int)
	 * @see #getHitpoints()
	 * @see #isAlive()
	 * @see #isDead()
	 * @see #TARGET_DISPOSE_ON_DEATH
	 */
	private int hitpoints = 1;
	/**
	 * <p>If {@code true} the {@link #getHitpoints() hitpoints} of this Target will become read-only. 
	 * Neither the {@link #setHitpoints(int)} nor {@link #addHitpoints(int)} methods will 
	 * be able to change the number of {@link #getHitpoints() hitpoints} this target has.</p>
	 * @see #setHitpointsReadOnly(boolean)
	 * @see #isHitpointsReadOnly()
	 * @see #setHitpoints(int)
	 * @see #addHitpoints(int)
	 */
	private boolean hitpointsReadOnly;
	
	{
		addBehaviorStrategy(TARGET_DISPOSE_ON_DEATH);
	}
	
	/**
	 * <p>Adds {@code value} many hitpoints to this Target iff it is not {@link #isDead() dead}.
	 * If this Target is dead nothing will happen.</p>
	 * <p>The hitpoints of a Target will not go above the {@link #getMaxHitpoints() 
	 * maximum hitpoints} or below 0.</p>
	 * @param value		the amount of hitpoints added
	 * @see #setHitpoints(int)
	 * @see #getHitpoints()
	 * @see #isAlive()
	 * @see #isDead()
	 */
	public void addHitpoints(int value) {
		if (isDead()) {
			return;
		}
		setHitpoints(getHitpoints() + value);
	}
	
	/**
	 * <p>Sets the current {@link #getHitpoints() hitpoints} of this Target to {@code value} unless 
	 * the hitpoints are currently {@link #isHitpointsReadOnly() read-only}. If the hitpoints are 
	 * read-only this operation does nothing..</p>
	 * <p>The hitpoints of a Target will not go above the {@link #getMaxHitpoints() 
	 * maximum hitpoints} or below 0.</p>
	 * @param value		the new hitpoints of this Target iff value <= {@link #getMaxHitpoints()} && value >= 0
	 * @see #addHitpoints(int)
	 * @see #getHitpoints()
	 * @see #isAlive()
	 * @see #isDead()
	 * @see #destroy()
	 * @see #setHitpointsReadOnly(boolean)
	 * @see #isHitpointsReadOnly()
	 */
	public void setHitpoints(int value) {
		int delta = value - getHitpoints();
		fireBeforeHitpointsChangedEvent(delta);
		if (isHitpointsReadOnly()) {
			return;
		}
		if (value < 0) {
			value = 0;
		} else if (value > getMaxHitpoints()) {
			value = getMaxHitpoints();
		}
		boolean wasAlive = isAlive();
		hitpoints = value;
		fireAfterHitpointsChangedEvent(delta);
		if (wasAlive && isDead()) {
			fireDeathEvent();
		}
	}
	
	/**
	 * <p>Immediately destroys this Target.</p>
	 * <p>The {@link #getHitpoints() hitpoints} of the Target will be set to 0 regardless 
	 * of whether the targets hitpoints are {@link #isHitpointsReadOnly() read-only} or not.</p>
	 * <p>This method does not change whether the hitpoints are read-only or not. If the 
	 * hitpoints were read-only before the call to this method they will still be read-only 
	 * after the method has returned.</p>
	 * @see #setHitpoints(int)
	 * @see #getHitpoints()
	 * @see #setHitpointsReadOnly(boolean)
	 * @see #isHitpointsReadOnly()
	 * @see #isAlive()
	 * @see #isDead()
	 */
	public void destroy() {
		boolean readOnly = isHitpointsReadOnly();
		setHitpointsReadOnly(false);
		setHitpoints(0);
		setHitpointsReadOnly(readOnly);
	}
	
	/**
	 * <p>Returns the current hitpoints of this Target. The hitpoints define whether this Target 
	 * is currently {@link #isAlive() alive} or {@link #isDead() dead}. The hitpoints are always 
	 * between 0 (inclusive) and {@link #getMaxHitpoints()} (inclusive).</p>
	 * <p>If the hitpoints of this Target reach 0 it will automatically be {@link #isDisposed() 
	 * disposed} iff the {@link #TARGET_DISPOSE_ON_DEATH} strategy is added to this Target.</p>
	 * @return		the current hitpoints of this Target. Always between 0 and {@link #getMaxHitpoints()}
	 */
	public int getHitpoints() {
		return hitpoints;
	}
	
	/**
	 * <p>Sets whether the {@link #getHitpoints() hitpoints} of this Target are read-only or not.</p>
	 * <p>If the hitpoints are read-only, the {@link #setHitpoints(int)} method can not be used to 
	 * modify the hitpoints. No exception will be thrown when attempted, but the hitpoints will be 
	 * unchanged.</p>
	 * <p>The {@link #destroy()} method ignores the {@link #isHitpointsReadOnly()} flag and sets the 
	 * hitpoints to 0 regardless of whether the hitpoints are read-only or not.</p>
	 * @param value		if {@code true} hitpoints will become read-only
	 * @see #isHitpointsReadOnly()
	 * @see #setHitpoints(int)
	 * @see #getHitpoints()
	 * @see #destroy()
	 */
	public void setHitpointsReadOnly(boolean value) {
		hitpointsReadOnly = value;
	}
	
	/**
	 * <p>Returns whether the {@link #getHitpoints() hitpoints} of this Target are read-only or not. 
	 * If the hitpoints are read-only, the {@link #setHitpoints(int)} method can not be used to 
	 * modify the hitpoints. No exception will be thrown when attempted, but the hitpoints will be 
	 * unchanged.</p>
	 * <p>The {@link #destroy()} method ignores the {@link #isHitpointsReadOnly()} flag and sets the 
	 * hitpoints to 0 regardless of whether the hitpoints are read-only or not.</p>
	 * @return	true if the hitpoints of this Target can not be modified by the {@link #setHitpoints(int)} method
	 * @see #setHitpointsReadOnly(boolean)
	 * @see #setHitpoints(int)
	 * @see #getHitpoints()
	 * @see #destroy()
	 */
	public boolean isHitpointsReadOnly() {
		return hitpointsReadOnly;
	}
	
	/**
	 * <p>Sets the maximum {@link #getHitpoints() hitpoints} for this Target to {@code value}.</p>
	 * <p>If the current hitpoints of this Target are greater then the new maximum hitpoints 
	 * the current hitpoints will be set to be equal to the new maximum hitpoints.</p>
	 * @param value							the new maximum hitpoints (>= 1)
	 * @throws IllegalArgumentException		if value is less then 1
	 * @see #setHitpoints(int)
	 * @see #getMaxHitpoints()
	 */
	public void setMaxHitpoints(int value) {
		if (value < 1) {
			throw new IllegalArgumentException("value="+value);
		}
		maxHitpoints = value;
		if (getHitpoints() > getMaxHitpoints()) {
			setHitpoints(getMaxHitpoints());
		}
	}
	
	/**
	 * <p>Returns the maximum number of {@link #getHitpoints() hitpoints} this Target can have.</p>
	 * @return	a value equal to or greater then 1
	 * @see #setMaxHitpoints(int)
	 * @see #setHitpoints(int)
	 */
	public int getMaxHitpoints() {
		return maxHitpoints;
	}
	
	/**
	 * Returns true if the current {@link #getHitpoints() hitpoints} of this Target are at 0.
	 * @return	true if {@link #getHitpoints()} == 0
	 * @see #addHitpoints(int)
	 * @see #setHitpoints(int)
	 * @see #getHitpoints()
	 * @see #isAlive()
	 */
	public boolean isDead() {
		return getHitpoints() <= 0;
	}
	
	/**
	 * Returns true if the current {@link #getHitpoints() hitpoints} of this Target are above 0.
	 * @return	true if {@link #getHitpoints()} > 0
	 * @see #addHitpoints(int)
	 * @see #setHitpoints(int)
	 * @see #getHitpoints()
	 * @see #isDead()
	 */
	public boolean isAlive() {
		return !isDead();
	}
	
	/**
	 * <p>Adds the given observer to this Target. The observer will be notified of changes to the 
	 * hitpoints of this Target and when the Target is destroyed.</p>
	 * <p>Observers can be added and removed at any time without a 
	 * {@link ConcurrentModificationException} being thrown. Observers can also be added to 
	 * multiple {@link Entity entities} at once but not more than once to the same Entity.</p>
	 * @param observer		a non-null TargetObserver
	 */
	public void addTargetObserver(TargetObserver observer) {
		observers.add(observer);
	}
	
	/**
	 * <p>Removes the given observer from this Target. The observer will no longer be notified of 
	 * any events by this Target.</p>
	 * <p>Observers can be added and removed at any time without a 
	 * {@link ConcurrentModificationException} being thrown. Observers can also be added to 
	 * multiple {@link Entity entities} at once but not more than once to the same Entity.</p>
	 * @param observer		a non-null TargetObserver
	 */
	public void removeTargetObserver(TargetObserver observer) {
		observers.remove(observer);
	}
	
	/**
	 * Called by {@link #setHitpoints(int)}. Only used internally.
	 * @param delta				how much the hitpoints are about to change
	 */
	private void fireBeforeHitpointsChangedEvent(int delta) {
		observers.forEach((obs) -> obs.onBeforeHitpointsChanged(this, delta));
	}
	
	/**
	 * Called by {@link #setHitpoints(int)}. Only used internally.
	 * @param delta				how much the hitpoints have changed
	 */
	private void fireAfterHitpointsChangedEvent(int delta) {
		observers.forEach((obs) -> obs.onAfterHitpointsChanged(this, delta));
	}
	
	/**
	 * Called by {@link #setHitpoints(int)}. Only used internally.
	 */
	private void fireDeathEvent() {
		observers.forEach((obs) -> obs.onDeath(this));
	}
	
}