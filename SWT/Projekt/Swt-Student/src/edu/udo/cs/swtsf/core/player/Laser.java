package edu.udo.cs.swtsf.core.player;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import edu.udo.cs.swtsf.core.Bullet;
import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.core.EntityBehaviorStrategy;
import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.Target;

/**
 * <p>The {@link Player} is able to use a Laser as its main form of 
 * attack in the SWT-Starfighter. The Laser uses a Decorator-Pattern to 
 * allow many different {@link LaserUpgrade upgrades} to the laser to be 
 * implemented.</p>
 * 
 * <p>A Laser has a number of attributes which are extended or overwritten 
 * by individual upgrades and are used by the {@link ShootLaserAction} of 
 * the {@link Player}. The Laser interface also offers a few convenience 
 * methods to count upgrades or check for their existence.</p>
 * 
 * @see #getDecorated()
 * @see LaserUpgrade
 * @see BasicLaser
 */
public interface Laser extends Iterable<Laser> {
	
	/**
	 * <p>The decorated laser is the {@link Laser} or {@link LaserUpgrade} 
	 * which is next in line within the Decorator-Pattern. The decorated is 
	 * considered when calculating the {@link #getDamage() damage}, 
	 * {@link #getCooldownTime() cooldown time} as well as the bullet 
	 * {@link #getBulletLifeTime() life time}, {@link #getBulletSize() size} 
	 * and {@link #getBulletSpeed() speed}.</p>
	 * <p>The {@link BasicLaser} does never have a decorated. 
	 * {@link LaserUpgrade Upgrades} which are being used always have a decorated.</p>
	 * 
	 * @return		either {@code null} or an instance of Laser
	 */
	public Laser getDecorated();
	
	/**
	 * <p>Returns the number of {@link Target#getHitpoints() hitpoints} taken away 
	 * from a {@link Target} hit by this laser.</p>
	 * 
	 * <p>This value is used by the {@link ShootLaserAction} for the 
	 * {@link Bullet#getDamage() damage} of fired {@link Bullet Bullets}.</p>
	 * @return		a positive integer value or zero
	 * @see ShootLaserAction
	 * @see Bullet#setDamage(int)
	 * @see Bullet#getDamage()
	 * @see Target#addHitpoints(int)
	 * @see Target#getHitpoints()
	 */
	public int getDamage();
	
	/**
	 * <p>Returns the time a {@link Bullet} created by this Laser will exist before 
	 * it is automatically {@link Entity#isDisposed() disposed}.</p>
	 * 
	 * <p>This value is used by the {@link ShootLaserAction} for the 
	 * {@link Bullet#getLifeTimer() life timer} of fired {@link Bullet Bullets}. 
	 * This value is ignored if the Bullets created by the {@link #createBullets(Entity)} 
	 * method do not have the {@link Bullet#BULLET_LIFE_TIMER_STRAT} strategy.</p>
	 * @return		a positive integer value
	 * @see ShootLaserAction
	 * @see Bullet#setLifeTimer(int)
	 * @see Bullet#getLifeTimer()
	 * @see Bullet#BULLET_LIFE_TIMER_STRAT
	 */
	public int getBulletLifeTime();
	
	/**
	 * <p>Returns the {@link Entity#getSize() size} of {@link Bullet Bullets} created by 
	 * this Laser.</p>
	 * @return		a positive integer value
	 * @see ShootLaserAction
	 * @see Bullet#setSize(Entity)
	 * @see Bullet#getSize()
	 */
	public int getBulletSize();
	
	/**
	 * <p>Returns the {@link Entity#getSpeed() speed} at which {@link Bullet Bullets} created 
	 * by this Laser will be traveling.</p>
	 * @return		a positive double value or zero
	 * @see ShootLaserAction
	 * @see Bullet#setVelocity(double, double)
	 * @see Bullet#getSpeed()
	 * @see Bullet#getVelocityX()
	 * @see Bullet#getVelocityY()
	 */
	public double getBulletSpeed();
	
	/**
	 * <p>Returns the minimum time in frames in between two shots fired by this Laser.</p>
	 * @return		a positive integer value
	 * @see ShootLaserAction
	 */
	public int getCooldownTime();
	
	/**
	 * <p>Initializes {@link Bullet Bullets} of this Laser with additional properties like 
	 * {@link EntityBehaviorStrategy behaviors} or similar. The argument is a {@link List} 
	 * of all Bullets fired by this Laser.</p>
	 * @param bullets		a {@link List} of all Bullets fired by this Laser
	 */
	public void initializeBullets(List<Bullet> bullets);
	
	/**
	 * <p>Creates the {@link Bullet Bullets} for this Laser. The Bullets within the 
	 * returned {@link List} will be {@link Game#addEntity(Entity) added} to the {@link Game}. 
	 * The {@link ShootLaserAction} makes sure to set the {@link Bullet#getDamage() damage}, 
	 * {@link Entity#getSize() size}, {@link Entity#getSpeed() speed}, etc of all Bullets as 
	 * defined the corresponding get-methods of this Laser.</p>
	 * @param src			the {@link Entity} which is firing the Bullets
	 * @return				a non-null {@link List} of Bullets
	 */
	public List<Bullet> createBullets(Entity src);
	
	public default Iterator<Laser> iterator() {
		return new LaserIterator(this);
	}
	
	/**
	 * <p>Returns true if the given {@link LaserUpgrade} is part of this {@link Laser}.</p>
	 * @param upgrade		the upgrade to check
	 * @return				true if upgrade is part of this Laser, else false
	 * @see #contains(Class)
	 * @see #count(LaserUpgrade)
	 */
	public default boolean contains(LaserUpgrade upgrade) {
		return this == upgrade || equals(upgrade) || (getDecorated() != null 
				&& getDecorated().contains(upgrade));
	}
	
	/**
	 * <p>Counts how often the given {@link LaserUpgrade} is part of this {@link Laser} 
	 * and returns the result. The result may be 0 if upgrade is not part of this Laser.</p>
	 * @param upgrade		the upgrade to count
	 * @return				a non-negative integer value
	 * @see #contains(LaserUpgrade)
	 * @see #count(Class)
	 */
	public default int count(LaserUpgrade upgrade) {
		return count(upgrade.getClass());
	}
	
	/**
	 * <p>Returns true if at least one {@link LaserUpgrade} of the given {@link Class type} 
	 * is part of this {@link Laser}.</p>
	 * @param upgradeClass	the type of upgrade to check
	 * @return				true if at least one upgrade of that type is part of this Laser, else false
	 * @see #contains(LaserUpgrade)
	 * @see #count(Class)
	 */
	public default boolean contains(Class<? extends LaserUpgrade> upgradeClass) {
		return upgradeClass.isInstance(this) || (getDecorated() != null 
				&& getDecorated().contains(upgradeClass));
	}
	
	/**
	 * <p>Counts how often {@link LaserUpgrade LaserUpgrades} of the given {@link Class type} 
	 * are part of this {@link Laser} and returns the result. The result may be 0 if no 
	 * upgrades of that type are part of this Laser.</p>
	 * @param upgradeClass	the type of upgrade to count
	 * @return				a non-negative integer value
	 * @see #contains(Class)
	 * @see #count(LaserUpgrade)
	 */
	public default int count(Class<? extends LaserUpgrade> upgradeClass) {
		int ownCount = upgradeClass.isInstance(this) ? 1 : 0;
		int decoratedCount = getDecorated() == null ? 
				0 : getDecorated().count(upgradeClass);
		return ownCount + decoratedCount;
	}
	
	/**
	 * <p>A simple {@link Iterator} implementation for the {@link Laser} interface.</p>
	 */
	public static class LaserIterator implements Iterator<Laser> {
		
		private Laser current;
		
		public LaserIterator(Laser start) {
			current = start;
		}
		
		public boolean hasNext() {
			return current != null;
		}
		
		public Laser next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			Laser result = current;
			current = current.getDecorated();
			return result;
		}
		
	}
	
}