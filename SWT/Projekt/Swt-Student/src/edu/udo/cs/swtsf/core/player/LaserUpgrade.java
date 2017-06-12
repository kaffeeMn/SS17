package edu.udo.cs.swtsf.core.player;

import java.util.List;

import edu.udo.cs.swtsf.core.Bullet;
import edu.udo.cs.swtsf.core.Entity;

/**
 * <p>A {@link LaserUpgrade} is part of the {@link Laser} system and is used 
 * to extend an existing Laser with additional functionality or to change the 
 * functionality of an existing Laser in some way. LaserUpgrades can be added 
 * to a {@link Player} with the {@link Player#addLaserUpgrade(LaserUpgrade)} 
 * method. In addition to the methods of the {@link Laser} interface, the 
 * LaserUpgrade has methods to set up the Decorator-Pattern structure.</p>
 */
public interface LaserUpgrade extends Laser {
	
	/**
	 * <p>This method is called by the {@link Player} to build the link between 
	 * {@link Laser} instances in the Decorator-Pattern. This method should not 
	 * be called from any other method.</p>
	 * @param laser
	 */
	public void setDecorated(Laser laser);
	
	/**
	 * <p>Removes the given {@link LaserUpgrade} from the {@link Laser} chain. 
	 * This method is called by the {@link Player} and does not need to be called 
	 * from anywhere else.</p>
	 * @param toBeRemoved		the LaserUpgrade to be removed
	 * @return					the removed LaserUpgrade or null if no upgrade was removed
	 */
	public default LaserUpgrade remove(LaserUpgrade toBeRemoved) {
		Laser next = getDecorated();
		if (next instanceof LaserUpgrade) {
			if (toBeRemoved.equals(next)) {
				LaserUpgrade removed = (LaserUpgrade) getDecorated();
				setDecorated(removed.getDecorated());
				return removed;
			}
			return ((LaserUpgrade) next).remove(toBeRemoved);
		}
		return null;
	}
	
	/**
	 * <p>Passes the call through to the {@link #getDecorated() decorated} {@link Laser}.</p>
	 */
	public default int getDamage() {
		if (getDecorated() == null) {
			throw new IllegalStateException("getDecorated() == null");
		}
		return getDecorated().getDamage();
	}
	
	/**
	 * <p>Passes the call through to the {@link #getDecorated() decorated} {@link Laser}.</p>
	 */
	public default int getBulletLifeTime() {
		if (getDecorated() == null) {
			throw new IllegalStateException("getDecorated() == null");
		}
		return getDecorated().getBulletLifeTime();
	}
	
	/**
	 * <p>Passes the call through to the {@link #getDecorated() decorated} {@link Laser}.</p>
	 */
	public default int getBulletSize() {
		if (getDecorated() == null) {
			throw new IllegalStateException("getDecorated() == null");
		}
		return getDecorated().getBulletSize();
	}
	
	/**
	 * <p>Passes the call through to the {@link #getDecorated() decorated} {@link Laser}.</p>
	 */
	public default double getBulletSpeed() {
		if (getDecorated() == null) {
			throw new IllegalStateException("getDecorated() == null");
		}
		return getDecorated().getBulletSpeed();
	}
	
	/**
	 * <p>Passes the call through to the {@link #getDecorated() decorated} {@link Laser}.</p>
	 */
	public default int getCooldownTime() {
		if (getDecorated() == null) {
			throw new IllegalStateException("getDecorated() == null");
		}
		return getDecorated().getCooldownTime();
	}
	
	/**
	 * <p>Passes the call through to the {@link #getDecorated() decorated} {@link Laser}.</p>
	 */
	public default void initializeBullets(List<Bullet> bullets) {
		if (getDecorated() == null) {
			throw new IllegalStateException("getDecorated() == null");
		}
		getDecorated().initializeBullets(bullets);
	}
	
	/**
	 * <p>Passes the call through to the {@link #getDecorated() decorated} {@link Laser}.</p>
	 */
	public default List<Bullet> createBullets(Entity src) {
		if (getDecorated() == null) {
			throw new IllegalStateException("getDecorated() == null");
		}
		return getDecorated().createBullets(src);
	}
	
}