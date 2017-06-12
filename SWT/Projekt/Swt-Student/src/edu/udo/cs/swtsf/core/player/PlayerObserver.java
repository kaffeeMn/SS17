package edu.udo.cs.swtsf.core.player;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;

import edu.udo.cs.swtsf.core.Bullet;
import edu.udo.cs.swtsf.core.EntityObserver;
import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.GameObserver;
import edu.udo.cs.swtsf.core.PickUp;
import edu.udo.cs.swtsf.core.Target;
import edu.udo.cs.swtsf.core.TargetObserver;

/**
 * <p>A PlayerObserver can be registered at an instance of {@link Player}. 
 * Once registered the PlayerObserver will be notified of various Player specific 
 * events like a {@link Player#getScore() score} change, changes to 
 * {@link LaserUpgrade laser upgrades}, changes to {@link PickUp} 
 * {@link Player#getPickUpCount(Class) counts} and others.<br>
 * The Player can have more than one PlayerObservers registered at the same time. 
 * No assumptions should be made about the order in which PlayerObservers will 
 * be notified of events.</p>
 * 
 * <p>PlayerObservers can be added to and removed from the Player during a notification 
 * without throwing any {@link ConcurrentModificationException 
 * ConcurrentModificationExceptions}.</p>
 * 
 * <p>As the name suggests, this interface is an implementation of the observer pattern.</p>
 * 
 * @see Player#addPlayerObserver(PlayerObserver)
 * @see Player#removePlayerObserver(PlayerObserver)
 * @see EntityObserver
 * @see TargetObserver
 * @see GameObserver
 */
public interface PlayerObserver {
	
	/**
	 * <p>Called immediately by the {@link Player} after a {@link LaserUpgrade} 
	 * was added to the Player. The upgrade which was added is passed as the 
	 * second argument to this method.</p>
	 * 
	 * <p>This method is a default method with an empty body. An implementing 
	 * class does not have to implement this method if it is not interested in 
	 * this specific event. An implementation of this method does not need to 
	 * call super.</p>
	 * 
	 * @param player	the Player of the {@link Game}. Guaranteed to not be null.
	 * @param upgrade	the added upgrade. Guaranteed to not be null.
	 * @see Laser
	 * @see LaserUpgrade
	 * @see Player#addLaserUpgrade(LaserUpgrade)
	 * @see Player#getLaser()
	 */
	public default void onLaserUpgradeAdded(Player player, LaserUpgrade upgrade) {}
	
	/**
	 * <p>Called immediately by the {@link Player} after a {@link LaserUpgrade} 
	 * was removed from the Player. The upgrade which was removed is passed as the 
	 * second argument to this method.</p>
	 * 
	 * <p>This method is a default method with an empty body. An implementing 
	 * class does not have to implement this method if it is not interested in 
	 * this specific event. An implementation of this method does not need to 
	 * call super.</p>
	 * 
	 * @param player	the Player of the {@link Game}. Guaranteed to not be null.
	 * @param upgrade	the removed upgrade. Guaranteed to not be null.
	 * @see Laser
	 * @see LaserUpgrade
	 * @see Player#removeLaserUpgrade(LaserUpgrade)
	 * @see Player#getLaser()
	 */
	public default void onLaserUpgradeRemoved(Player player, LaserUpgrade upgrade) {}
	
	/**
	 * <p>Called immediately by the {@link Player} after a {@link PickUp} was either  
	 * added to or removed from the Player. The {@link Class type} of PickUp for 
	 * which the count was changed is passed as the second argument to this method. 
	 * The amount the count was changed by is passed as the third argument.</p>
	 * 
	 * <p>This method is a default method with an empty body. An implementing 
	 * class does not have to implement this method if it is not interested in 
	 * this specific event. An implementation of this method does not need to 
	 * call super.</p>
	 * 
	 * @param player		the Player of the {@link Game}. Guaranteed to not be null.
	 * @param pickUpClass	the type of {@link PickUp} for the count change. Guaranteed to not be null.
	 * @param value			by how much the count has changed. Positive if count was increased, negative if count was decreased.
	 * @see PickUp
	 * @see Class
	 * @see Player#addPickUp(Class)
	 * @see Player#removePickUp(Class)
	 * @see Player#getPickUpCount(Class)
	 */
	public default void onPickUpCountChanged(Player player, 
			Class<? extends PickUp> pickUpClass, int value) {}
	
	/**
	 * <p>Called immediately by the {@link Player} after the score of the player 
	 * was changed.</p>
	 * 
	 * <p>This method is a default method with an empty body. An implementing 
	 * class does not have to implement this method if it is not interested in 
	 * this specific event. An implementation of this method does not need to 
	 * call super.</p>
	 * 
	 * @param player		the Player of the {@link Game}. Guaranteed to not be null.
	 * @param value			by how much the score has changed. Positive if the score increased, negative if the score decreased.
	 * @see Player#addScore(int)
	 * @see Player#getScore()
	 */
	public default void onScoreChanged(Player player, int value) {}
	
	/**
	 * <p>Called immediately by the {@link Player} after a {@link Bullet} 
	 * {@link ShootLaserAction fired by} the Player has hit a {@link Target}. 
	 * The Target which was hit is passed as the second argument to this method.</p>
	 * <p>This method is called regardless of whether the hit Target was 
	 * {@link Target#getHitpoints() damaged} by the Player.</p>
	 * 
	 * <p>This method is a default method with an empty body. An implementing 
	 * class does not have to implement this method if it is not interested in 
	 * this specific event. An implementation of this method does not need to 
	 * call super.</p>
	 * 
	 * @param player		the Player of the {@link Game}. Guaranteed to not be null.
	 * @param target		the Target hit by the Player. Guaranteed to not be null.
	 * @see ShootLaserAction
	 * @see Target
	 */
	public default void onPlayerHitTarget(Player player, Target target) {}
	
	/**
	 * <p>Called immediately by the {@link Player} after a {@link Target} was 
	 * {@link Target#isDead() destroyed} by a {@link Bullet} 
	 * {@link ShootLaserAction fired by} the Player. The Target which was 
	 * destroyed is passed as the second argument.</p>
	 * 
	 * <p>This method is a default method with an empty body. An implementing 
	 * class does not have to implement this method if it is not interested in 
	 * this specific event. An implementation of this method does not need to 
	 * call super.</p>
	 * 
	 * @param player		the Player of the {@link Game}. Guaranteed to not be null.
	 * @param target		the destroyed Target. Guaranteed to not be null.
	 * @see ShootLaserAction
	 * @see Target
	 * @see Target#addHitpoints(int)
	 * @see Target#getHitpoints()
	 * @see Target#isAlive()
	 * @see Target#isDead()
	 * @see TargetObserver#onDeath(Target)
	 */
	public default void onPlayerKilledTarget(Player player, Target target) {}
	
	/**
	 * <p>Called immediately by the {@link Player} after the Player has fired 
	 * any {@link Bullet Bullets}. This method is passed a {@link List} of all 
	 * fired Bullets as second argument. The List should not be 
	 * {@link List#isEmpty() empty}.</p>
	 * 
	 * <p>This method is a default method with an empty body. An implementing 
	 * class does not have to implement this method if it is not interested in 
	 * this specific event. An implementation of this method does not need to 
	 * call super.</p>
	 * 
	 * @param player		the Player of the {@link Game}. Guaranteed to not be null.
	 * @param bullets		a {@link List} of all fired {@link Bullet Bullets}. Guaranteed to not be null.
	 * @see ShootLaserAction
	 * @see Bullet
	 */
	public default void onLaserFired(Player player, Collection<Bullet> bullets) {}
	
}