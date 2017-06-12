package edu.udo.cs.swtsf.core.player;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import edu.udo.cs.swtsf.core.Bullet;
import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.core.EntityBehaviorStrategy;
import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.PickUp;
import edu.udo.cs.swtsf.core.Target;
import edu.udo.cs.swtsf.util.Group;
import edu.udo.cs.swtsf.util.GroupFactory;

/**
 * <p>A Player is a special kind of {@link Entity} which has a unique function 
 * within a {@link Game}. Each game will always contain at least one Player 
 * instance. Although it is possible for a Game to contain more than one Player 
 * it is not recommended to do so. A Game is supposed to end the moment the 
 * initial Player of that game is {@link Entity#isDisposed() disposed}.</p>
 * 
 * <p>A Player always has a {@link Laser} which is a {@link BasicLaser} by 
 * default but can be {@link #addLaserUpgrade(LaserUpgrade) upgraded} with all 
 * kinds of {@link LaserUpgrade LaserUpgrades}. The laser is used by the 
 * {@link ShootLaserAction}, an {@link EntityBehaviorStrategy} added to a player 
 * by default.</p>
 * 
 * <p>In addition to the {@link ShootLaserAction} a player also has the 
 * {@link MovePlayerAction} by default. Both of these are {@link EntityBehaviorStrategy 
 * EntityBehaviorStrategys} which define the controls of the player.</p>
 * 
 * <p>A Player can also keep track of how many {@link PickUp PickUps} it has 
 * collected at the moment. A player can never have {@link #getPickUpCount(Class) 
 * less than 0} PickUps of a certain {@link Class type}.</p>
 * 
 * <p>A Player has a {@link #getScore() score} which is the amount of points the 
 * player has been awarded so far during the Game.</p>
 * 
 * @see Laser
 * @see PickUp
 * @see MovePlayerAction
 * @see ShootLaserAction
 * @see PlayerSpriteFactory
 * @see Game#getPlayer()
 */
public class Player extends Target {
	
	/**
	 * Maps each type of {@link PickUp} to a number.
	 * @see #addPickUp(Class)
	 * @see #removePickUp(Class)
	 * @see #getPickUpCount(Class)
	 */
	private final Map<Class<? extends PickUp>, Integer> pickUpMap = new HashMap<>();
	/**
	 * Contains all {@link PlayerObserver PlayerObservers} observing this player.
	 * @see #addObserver(edu.udo.cs.swtsf.core.EntityObserver)
	 * @see #removeObserver(edu.udo.cs.swtsf.core.EntityObserver)
	 * @see #fireScoreChanged(int)
	 */
	private final Group<PlayerObserver> observers = GroupFactory.get().createNewGroup();
	/**
	 * A reference to the most recent {@link LaserUpgrade} or the {@link BasicLaser} 
	 * if no upgrade has been added yet.
	 * @see #getLaser()
	 * @see #addLaserUpgrade(LaserUpgrade)
	 * @see #removeLaserUpgrade(LaserUpgrade)
	 */
	private Laser curLaser = new BasicLaser();
	/**
	 * The score of the Player.
	 * @see #addScore(int)
	 * @see #getScore()
	 */
	private int score = 0;
	/**
	 * Used by the {@link PlayerSpriteFactory} to animate the movement.
	 * @see #setHasMoveInput(boolean)
	 * @see #hasMoveInput()
	 */
	private boolean hasMoveInput = false;
	
	/**
	 * <p>Constructs a Player with a {@link #getSize() size} of 32, a position of 
	 * ({@link #getX() 200}, {@link #getY() 200}), a {@link #getMaxHitpoints() maximum 
	 * number of hitpoints} of 100 and 5 {@link #getHitpoints() current hitpoints}.</p>
	 * 
	 * <p>The player also has the {@link ShootLaserAction} and {@link MovePlayerAction} 
	 * added by default.</p> 
	 */
	public Player() {
		super();
		setSize(32);
		setPosition(200, 200);
		setMaxHitpoints(100);
		setHitpoints(5);
		
		addBehaviorStrategy(new ShootLaserAction());
		addBehaviorStrategy(new MovePlayerAction());
	}
	
	/**
	 * <p>Adds one {@link PickUp} of the given {@link #getClass() type} to this {@link Player}.</p>
	 * @param pickUp						a non-null instance of PickUp
	 * @throws IllegalArgumentException		if pickUp == null
	 * @see #addPickUp(Class)
	 * @see #addPickUp(Class, int)
	 * @see #removePickUp(Class)
	 * @see #removePickUp(Class, int)
	 * @see #removePickUp(PickUp)
	 * @see #getPickUpCount(Class)
	 * @see #getPickUpCount(PickUp)
	 */
	public void addPickUp(PickUp pickUp) {
		if (pickUp == null) {
			throw new IllegalArgumentException("pickUp == null");
		}
		Class<? extends PickUp> pickUpCls = pickUp.getClass();
		addPickUp(pickUpCls);
	}
	
	/**
	 * <p>Adds one {@link PickUp} of the given {@link Class type} to this {@link Player}.</p>
	 * @param pickUpCls						the type of PickUp to add
	 * @throws IllegalArgumentException		if pickUpCls == null
	 * @see #addPickUp(Class, int)
	 * @see #addPickUp(PickUp)
	 * @see #removePickUp(Class)
	 * @see #removePickUp(Class, int)
	 * @see #removePickUp(PickUp)
	 * @see #getPickUpCount(Class)
	 * @see #getPickUpCount(PickUp)
	 */
	public void addPickUp(Class<? extends PickUp> pickUpCls) {
		addPickUp(pickUpCls, 1);
	}
	
	/**
	 * <p>Adds one or more {@link PickUp PickUps} of the given {@link Class type} 
	 * to this {@link Player}.</p>
	 * @param pickUpCls						the type of PickUp to be added
	 * @param times							how often the PickUp is added
	 * @throws IllegalArgumentException		if pickUpCls == null or times < 1
	 * @see #addPickUp(Class)
	 * @see #addPickUp(PickUp)
	 * @see #removePickUp(Class)
	 * @see #removePickUp(Class, int)
	 * @see #removePickUp(PickUp)
	 * @see #getPickUpCount(Class)
	 * @see #getPickUpCount(PickUp)
	 */
	public void addPickUp(Class<? extends PickUp> pickUpCls, int times) {
		if (pickUpCls == null) {
			throw new IllegalArgumentException("pickUpCls == null");
		}
		if (times < 1) {
			throw new IllegalArgumentException("times < 1");
		}
		Integer previousCount = pickUpMap.get(pickUpCls);
		Integer newCount;
		// previousCount is null if we did not have a PickUp of that type stored before
		if (previousCount == null) {
			newCount = Integer.valueOf(times);
		} else {
			newCount = previousCount + times;
		}
		pickUpMap.put(pickUpCls, newCount);
		firePickUpCountChanged(pickUpCls, times);
	}
	
	/**
	 * <p>Removes one {@link PickUp} of the given {@link #getClass() type} from this {@link Player}.</p>
	 * @param pickUp						a non-null instance of PickUp
	 * @throws IllegalArgumentException		if pickUp == null
	 * @see #addPickUp(Class)
	 * @see #addPickUp(Class, int)
	 * @see #addPickUp(PickUp)
	 * @see #removePickUp(Class)
	 * @see #removePickUp(Class, int)
	 * @see #getPickUpCount(Class)
	 * @see #getPickUpCount(PickUp)
	 */
	public void removePickUp(PickUp pickUp) {
		if (pickUp == null) {
			throw new IllegalArgumentException("pickUp == null");
		}
		Class<? extends PickUp> pickUpCls = pickUp.getClass();
		removePickUp(pickUpCls);
	}
	
	/**
	 * <p>Removes one {@link PickUp} of the given {@link Class type} from this {@link Player}.</p>
	 * @param pickUpCls						the type of PickUp to remove
	 * @throws IllegalArgumentException		if pickUpCls == null
	 * @see #addPickUp(Class)
	 * @see #addPickUp(Class, int)
	 * @see #addPickUp(PickUp)
	 * @see #removePickUp(Class, int)
	 * @see #removePickUp(PickUp)
	 * @see #getPickUpCount(Class)
	 * @see #getPickUpCount(PickUp)
	 */
	public void removePickUp(Class<? extends PickUp> pickUpCls) {
		removePickUp(pickUpCls, 1);
	}
	
	/**
	 * <p>Removes one or more {@link PickUp PickUps} of the given {@link Class type} 
	 * from this {@link Player}.</p>
	 * @param pickUpCls						the type of PickUp to be removed
	 * @param times							how often the PickUp is removed
	 * @throws IllegalArgumentException		if pickUpCls == null or times < 1
	 * @see #addPickUp(Class)
	 * @see #addPickUp(Class, int)
	 * @see #addPickUp(PickUp)
	 * @see #removePickUp(Class)
	 * @see #removePickUp(PickUp)
	 * @see #getPickUpCount(Class)
	 * @see #getPickUpCount(PickUp)
	 */
	public void removePickUp(Class<? extends PickUp> pickUpCls, int times) {
		if (pickUpCls == null) {
			throw new IllegalArgumentException("pickUpCls == null");
		}
		if (times < 1) {
			throw new IllegalArgumentException("times < 1");
		}
		Integer previousCount = pickUpMap.get(pickUpCls);
		if (previousCount == null || previousCount < times) {
			throw new IllegalArgumentException("getPickUpCount("
					+pickUpCls.getSimpleName()+") < times="+times);
		}
		Integer newCount = previousCount - times;
		pickUpMap.put(pickUpCls, newCount);
		firePickUpCountChanged(pickUpCls, -times);
	}
	
	/**
	 * <p>Returns the number of {@link PickUp PickUps} of the given {@link #getClass() type}. 
	 * The returned value is never negative.</p>
	 * @param pickUp						a non-null instance of PickUp
	 * @return								the number of times a PickUp of the given type is owned by this Player
	 * @throws IllegalArgumentException		if pickUp == null
	 * @see #getPickUpCount(Class)
	 * @see PickUp#getClass()
	 */
	public int getPickUpCount(PickUp pickUp) {
		if (pickUp == null) {
			throw new IllegalArgumentException("pickUp == null");
		}
		return getPickUpCount(pickUp.getClass());
	}
	
	/**
	 * <p>Returns the number of {@link PickUp PickUps} of the given {@link Class type}. 
	 * The returned value is never negative.</p>
	 * @param pickUpCls						the type of PickUps to count
	 * @return								the number of times a PickUp of the given type is owned by this Player
	 * @throws IllegalArgumentException		if pickUpCls == null
	 * @see #getPickUpCount(PickUp)
	 */
	public int getPickUpCount(Class<? extends PickUp> pickUpCls) {
		if (pickUpCls == null) {
			throw new IllegalArgumentException("pickUpCls == null");
		}
		Integer count = pickUpMap.get(pickUpCls);
		if (count == null) {
			return 0;
		}
		return count.intValue();
	}
	
	/**
	 * <p>Returns whether the given {@link LaserUpgrade} was {@link #addLaserUpgrade(LaserUpgrade) 
	 * added} to this {@link Player} before and has not been {@link #removeLaserUpgrade(LaserUpgrade) 
	 * removed} since.</p> 
	 * @param upgrade						a non-null instance of LaserUpgrade
	 * @return								true if upgrade is currently used by this Player; else false
	 * @throws IllegalArgumentException		if upgrade == null
	 * @see #hasLaserUpgradeOfType(Class)
	 * @see #addLaserUpgrade(LaserUpgrade)
	 * @see #removeLaserUpgrade(LaserUpgrade)
	 */
	public boolean hasLaserUpgrade(LaserUpgrade upgrade) {
		if (upgrade == null) {
			throw new IllegalArgumentException("upgrade == null");
		}
		return curLaser.contains(upgrade);
	}
	
	/**
	 * <p>Returns whether this {@link Player} currently has at least one instance of 
	 * {@link LaserUpgrade} of the given type. A Player has a LaserUpgrade if it was 
	 * {@link #addLaserUpgrade(LaserUpgrade) added} to the Player before and has not been 
	 * {@link #removeLaserUpgrade(LaserUpgrade) removed} since.</p>
	 * @param upgradeCls					the type of LaserUpgrade to check for
	 * @return								true if at least one upgrade of the given type is used by this player; else false
	 * @throws IllegalArgumentException		if upgradeCls == null
	 * @see #hasLaserUpgrade(LaserUpgrade)
	 * @see #addLaserUpgrade(LaserUpgrade)
	 * @see #removeLaserUpgrade(LaserUpgrade)
	 */
	public boolean hasLaserUpgradeOfType(Class<? extends LaserUpgrade> upgradeCls) {
		if (upgradeCls == null) {
			throw new IllegalArgumentException("upgradeCls == null");
		}
		return curLaser.contains(upgradeCls);
	}
	
	/**
	 * <p>Adds the given {@link LaserUpgrade} to this {@link Player}.</p>
	 * <p>This method causes the {@link PlayerObserver#onLaserUpgradeAdded(Player, LaserUpgrade)} 
	 * method to be called for each {@link PlayerObserver} observing this Player at the moment.</p>
	 * @param upgrade			a non-null LaserUpgrade
	 * @throws IllegalArgumentException		if upgrade == null
	 * @throws IllegalStateException		if upgrade is already in use by a Player
	 * @see #hasLaserUpgrade(LaserUpgrade)
	 * @see #removeLaserUpgrade(LaserUpgrade)
	 * @see #getLaser()
	 * @see ShootLaserAction
	 */
	public void addLaserUpgrade(LaserUpgrade upgrade) {
		if (upgrade == null) {
			throw new IllegalArgumentException("upgrade == null");
		}
		if (upgrade.getDecorated() != null) {
			throw new IllegalStateException("upgrade.getDecorated() != null");
		}
		upgrade.setDecorated(curLaser);
		curLaser = upgrade;
		fireLaserUpgradeAdded(upgrade);
	}
	
	/**
	 * 
	 * @param upgrade
	 */
	public void removeLaserUpgrade(LaserUpgrade upgrade) {
		if (upgrade == null) {
			throw new IllegalArgumentException("upgrade == null");
		}
		Laser current = getLaser();
		if (current instanceof BasicLaser) {
			throw new IllegalStateException("hasLaserUpgrade(upgrade) == false");
		}
		LaserUpgrade currentUpgrade = ((LaserUpgrade) current);
		if (currentUpgrade.equals(upgrade)) {
			curLaser = curLaser.getDecorated();
			fireLaserUpgradeRemoved(currentUpgrade);
			return;
		}
		LaserUpgrade removedUpgrade = currentUpgrade.remove(upgrade);
		if (removedUpgrade == null) {
			throw new IllegalStateException("hasLaserUpgrade(upgrade) == false");
		}
		fireLaserUpgradeRemoved(removedUpgrade);
	}
	
	/**
	 * <p>Returns the {@link Laser} of this {@link Player}. The Laser is used by the 
	 * {@link ShootLaserAction} and controls the number, strength and kind of {@link Bullet Bullets} 
	 * fired by the Player. The Laser can be decorated with {@link LaserUpgrade LaserUpgrades}.</p> 
	 * <p>The Laser is never null. No assumptions should be made about the runtime {@link Class} of 
	 * the returned Laser.</p>
	 * @return	a non-null instance of {@link Laser}
	 * @see Laser
	 * @see LaserUpgrade
	 * @see #addLaserUpgrade(LaserUpgrade)
	 * @see #removeLaserUpgrade(LaserUpgrade)
	 */
	public Laser getLaser() {
		return curLaser;
	}
	
	/**
	 * <p>Sets the {@link #hasMoveInput()} property. This property is used by the 
	 * {@link PlayerSpriteFactory} to animate the movement of the Player. It has no other 
	 * purpose at the moment.</p>
	 * <p>By default this method is only called by the {@link MovePlayerAction}.</p>
	 * @param value		true if a move input has been made; else false
	 * @see #hasMoveInput()
	 * @see MovePlayerAction
	 * @see PlayerSpriteFactory
	 */
	public void setHasMoveInput(boolean value) {
		hasMoveInput = value;
	}
	
	/**
	 * <p>Returns true if this {@link Player} is moving because of a human player input. This property 
	 * is used by the {@link PlayerSpriteFactory} to animate the movement of the Player. It has no other 
	 * purpose at the moment.</p>
	 * @return		true if the {@link Player} is currently moving because of a key input; else false.
	 * @see #setHasMoveInput(boolean)
	 * @see MovePlayerAction
	 * @see PlayerSpriteFactory
	 */
	public boolean hasMoveInput() {
		return hasMoveInput;
	}
	
	/**
	 * <p>Adds the given value to the current {@link #getScore() score} of this 
	 * {@link Player}. The value may be negative in which case the score is 
	 * decreased rather than increased.</p>
	 * <p>All currently registered {@link PlayerObserver observers} will be 
	 * notified of a score change by having their 
	 * {@link PlayerObserver#onScoreChanged(Player, int)} method called.</p>
	 * @param value			the value by which the score changed. May be negative.
	 * @see #getScore()
	 * @see PlayerObserver
	 * @see PlayerObserver#onScoreChanged(Player, int)
	 */
	public void addScore(int value) {
		if (value != 0) {
			score += value;
			fireScoreChanged(value);
		}
	}
	
	/**
	 * <p>Returns the current score of this {@link Player}. The score is an 
	 * arbitrary value representing how 'good' a player is performing. A 
	 * higher score value indicates a better performance in the game. The game 
	 * may reward a player for a high score in some way.</p>
	 * @return		an integer value representing the score
	 * @see #addScore(int)
	 * @see PlayerObserver
	 * @see PlayerObserver#onScoreChanged(Player, int)
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * <p>Adds the given {@link PlayerObserver} to the observers of this {@link Player}.</p>
	 * <p>Once added, an PlayerObserver will be notified of many changes and events happening 
	 * at this Player.</p>
	 * <p>Observers can be added and removed at any time without a 
	 * {@link ConcurrentModificationException} being thrown.</p>
	 * @param observer					a non-null PlayerObserver
	 * @see #removePlayerObserver(PlayerObserver)
	 * @see PlayerObserver
	 */
	public void addPlayerObserver(PlayerObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException("observer == null");
		}
		observers.add(observer);
	}
	
	/**
	 * <p>Removes the given {@link PlayerObserver} from the observers of this {@link Player}.</p>
	 * <p>After it has been removed, the PlayerObserver will no longer be notified of any 
	 * changes to this Player.</p>
	 * <p>Observers can be added and removed at any time without a 
	 * {@link ConcurrentModificationException} being thrown.</p>
	 * @param observer					a non-null PlayerObserver
	 * @see #addPlayerObserver(PlayerObserver)
	 * @see PlayerObserver
	 */
	public void removePlayerObserver(PlayerObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException("observer == null");
		}
		observers.remove(observer);
	}
	
	/**
	 * Calls {@link PlayerObserver#onLaserUpgradeAdded(Player, LaserUpgrade)} 
	 * on all registered observers.
	 * @param upgrade		the added upgrade
	 */
	protected void fireLaserUpgradeAdded(LaserUpgrade upgrade) {
		observers.forEach(observer 
				-> observer.onLaserUpgradeAdded(this, upgrade));
	}
	
	/**
	 * Calls {@link PlayerObserver#onLaserUpgradeRemoved(Player, LaserUpgrade)} 
	 * on all registered observers.
	 * @param upgrade		the removed upgrade
	 */
	protected void fireLaserUpgradeRemoved(LaserUpgrade upgrade) {
		observers.forEach(observer 
				-> observer.onLaserUpgradeRemoved(this, upgrade));
	}
	
	/**
	 * Calls {@link PlayerObserver#onPickUpCountChanged(Player, Class, int)} 
	 * on all registered observers.
	 * @param pickUpClass	for which PickUp the count changed
	 * @param value			how much the count changed
	 */
	protected void firePickUpCountChanged(Class<? extends PickUp> pickUpClass, int value) {
		observers.forEach(observer 
				-> observer.onPickUpCountChanged(this, pickUpClass, value));
	}
	
	/**
	 * Calls {@link PlayerObserver#onScoreChanged(Player, int)} 
	 * on all registered observers.
	 * @param value			how much the score changed
	 */
	protected void fireScoreChanged(int value) {
		observers.forEach(observer 
				-> observer.onScoreChanged(this, value));
	}
	
	/**
	 * Calls {@link PlayerObserver#onPlayerHitTarget(Player, Target)} 
	 * on all registered observers.
	 * @param target		the hit Target
	 */
	protected void fireHitEntity(Target target) {
		observers.forEach(observer 
				-> observer.onPlayerHitTarget(this, target));
	}
	
	/**
	 * Calls {@link PlayerObserver#onPlayerKilledTarget(Player, Target)} 
	 * on all registered observers.
	 * @param target		the killed Target
	 */
	protected void fireKilledEntity(Target target) {
		observers.forEach(observer 
				-> observer.onPlayerKilledTarget(this, target));
	}
	
	/**
	 * Calls {@link PlayerObserver#onLaserFired(Player, Collection)} 
	 * on all registered observers.
	 * @param bullets		the fired bullets
	 */
	protected void fireLaserFired(Collection<Bullet> bullets) {
		observers.forEach(observer 
				-> observer.onLaserFired(this, bullets));
	}
	
} 