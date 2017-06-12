package edu.udo.cs.swtsf.core;

import java.util.ConcurrentModificationException;
import java.util.function.Predicate;

import edu.udo.cs.swtsf.util.Group;
import edu.udo.cs.swtsf.util.GroupFactory;

/**
 * <p>A {@link Bullet} is a special kind of {@link Entity} which is usually as some form of 
 * hit detection. Bullet's always have a source Entity which is assumed to have created 
 * the Bullet. A Bullet will, by default, not check for hits with its source Entity. It 
 * will also only hit instances of {@link Target} and not other kinds of Entities.</p>
 * 
 * <p>Bullet's can have {@link BulletHitStrategy BulletHitStrategy's} added to them. These 
 * strategies will be called whenever the Bullet has hit a Target. This can be used to 
 * create custom effects for the Bullet.</p>
 * 
 * <p>A Bullet has a number of strategies added to it by default, these are the 
 * {@link #BULLET_HIT_ON_COLLISION_STRAT}, the {@link #BULLET_DAMAGE_ON_HIT_STRAT} and 
 * the {@link #BULLET_LIFE_TIMER_STRAT}. A custom Bullet implementation is free to 
 * remove these strategies from itself within its constructor.</p>
 * 
 * @see Target
 * @see BulletHitStrategy
 */
public abstract class Bullet extends Entity {
	
	/**
	 * <p>This is an {@link EntityCollisionStrategy} which will call the {@link #fireOnHit(Target)} 
	 * method of the host {@link Bullet} every time the host Bullet hits a {@link Target} which 
	 * is not disposed and <b>not</b> the {@link #getSourceEntity() source entity} of the Bullet.</p>
	 * 
	 * <p>This strategy assumes that its host is always an instance of Bullet. If it is added to an 
	 * Entity which is not a Bullet it will generate an exception when triggered.</p>
	 * 
	 * <p>This is a default strategy for all Bullet's. It will automatically be added when the 
	 * Bullet's constructor is called.</p>
	 * 
	 * @see #dispose()
	 * @see #isDisposed()
	 * @see #addCollisionStrategy(EntityCollisionStrategy)
	 * @see #removeCollisionStrategy(EntityCollisionStrategy)
	 */
	public static final EntityCollisionStrategy BULLET_HIT_ON_COLLISION_STRAT = 
			(self, maybeTarget) -> 
	{
		Bullet bullet = (Bullet) self;
		// If a bullet is not armed it can not hit anything
		if (!bullet.isArmed()) {
			return;
		}
		// A bullet can not hit its own source entity
		// Only an entity of type Target can be hit
		if (maybeTarget != bullet.getSourceEntity() && maybeTarget instanceof Target) {
			Target target = (Target) maybeTarget;
			// The bullet may be set to only hit targets that are alive
			if (bullet.isOnlyHitAlive() && target.isDead()) {
				return;
			}
			// The target may already be disposed in which case it can not be hit
			if (!target.isDisposed()) {
				// There may be a custom condition to filter possible targets
				Predicate<Target> filter = bullet.getTargetFilter();
				if (filter == null || filter.test(target)) {
					bullet.fireOnHit(target);
				}
			}
		}
	};
	/**
	 * <p>This is a {@link BulletHitStrategy} which will {@link #dispose() dispose} the 
	 * {@link Bullet} whenever it hits any {@link Target}.</p>
	 * 
	 * <p>This is <b>NOT</b> a default strategy for Bullet's. To use it in a custom Bullet 
	 * implementation you have to add it to a {@link Bullet} via the 
	 * {@link #addHitStrategy(BulletHitStrategy)} method.</p>
	 * 
	 * @see #dispose()
	 * @see #isDisposed()
	 * @see #addHitStrategy(BulletHitStrategy)
	 * @see #removeHitStrategy(BulletHitStrategy)
	 */
	public static final BulletHitStrategy BULLET_SELF_DESTRUCT_ON_HIT_STRAT = 
			(bullet, target) -> 
	{
		bullet.dispose();
	};
	/**
	 * <p>This is a {@link BulletHitStrategy} which will subtract the {@link #damage} of the 
	 * {@link Bullet} from the {@link Target#getHitpoints() hitpoints} of the {@link Target}.<br>
	 * If the damage of the Bullet is negative the Target will thus be healed instead of 
	 * being hurt.</p>
	 * 
	 * <p>This is a default strategy for all Bullet's. It will automatically be added when the 
	 * Bullet's constructor is called.</p>
	 * 
	 * @see Target#setHitpoints(int)
	 * @see Target#getHitpoints()
	 * @see #setDamage(int)
	 * @see #getDamage()
	 * @see #addHitStrategy(BulletHitStrategy)
	 * @see #removeHitStrategy(BulletHitStrategy)
	 */
	public static final BulletHitStrategy BULLET_DAMAGE_ON_HIT_STRAT = 
			(bullet, target) -> 
	{
		target.addHitpoints(-bullet.getDamage());
	};
	/**
	 * <p>This {@link EntityBehaviorStrategy} will count down the {@link #lifeTimer} of a {@link Bullet} 
	 * until it reaches zero. When the lifeTimer has reached zero the Bullet will be disposed.<br>
	 * If the lifeTimer has a negative value nothing will happen.</p>
	 * 
	 * <p>This is a default strategy for all Bullet's. It will automatically be added when the 
	 * Bullet's constructor is called.</p>
	 * 
	 * @see #setLifeTimer(int)
	 * @see #getLifeTimer()
	 * @see #addBehaviorStrategy(EntityBehaviorStrategy)
	 * @see #removeBehaviorStrategy(EntityBehaviorStrategy)
	 */
	public static final EntityBehaviorStrategy BULLET_LIFE_TIMER_STRAT = 
			(self) -> 
	{
		Bullet bullet = (Bullet) self;
		int lifeTimer = bullet.getLifeTimer();
		if (lifeTimer > 0) {
			bullet.setLifeTimer(lifeTimer - 1);
		} else if (lifeTimer == 0) {
			bullet.dispose();
		}
	};
	
	/**
	 * Contains all {@link BulletHitStrategy BulletHitStrategy's} that have been added to this Bullet.
	 * @see #addHitStrategy(BulletHitStrategy)
	 * @see #removeHitStrategy(BulletHitStrategy)
	 */
	private final Group<BulletHitStrategy> hitStrats 
					= GroupFactory.get().createNewGroup();
	/**
	 * The {@link Entity} from which this Bullet originated. This can be null.<br>
	 * The {@link #BULLET_HIT_ON_COLLISION_STRAT} will generally not hit the source of the Bullet.<br>
	 * @see #Bullet(Entity)
	 */
	private final Entity source;
	private Predicate<Target> targetFilter;
	/**
	 * The damage dealt by the Bullet on impact. This is only meaningful if the Bullet has the 
	 * {@link #BULLET_DAMAGE_ON_HIT_STRAT} added to it.
	 */
	private int damage = 1;
	/**
	 * The Bullet's time to live. The {@link #BULLET_LIFE_TIMER_STRAT} will subtract 1 from this 
	 * value until it reaches zero. When it reaches zero the Bullet will be disposed by the 
	 * {@link #BULLET_LIFE_TIMER_STRAT}.
	 */
	private int lifeTimer = -1;
	/**
	 * Whether this Bullet is currently armed or not. If a Bullet is not armed it can not hit anything.<br>
	 * A Bullet is armed by default.
	 */
	private boolean armed = true;
	/**
	 * When {@link #onlyHitAlive} is {@code true} this Bullet will only hit {@link Target Target's} 
	 * which are {@link Target#isAlive() alive}. If {@link #onlyHitAlive} is {@code false} this Bullet 
	 * will also hit Target's hich are {@link Target#isDead() dead}.
	 */
	private boolean onlyHitAlive = true;
	
	/**
	 * <p>Creates a new Bullet with the given source Entity and all default strategies.<br>
	 * The Bullet will be placed at the position of the source Entity offset by the size of the 
	 * source. The Bullet will start with the same rotation and velocity as the source.</p>
	 * @param sourceEntity					a non-null Entity which will be the source of the Bullet
	 * @throws IllegalArgumentException		if sourceEntity is null
	 * @see #BULLET_HIT_ON_COLLISION_STRAT
	 * @see #BULLET_LIFE_TIMER_STRAT
	 * @see #BULLET_DAMAGE_ON_HIT_STRAT
	 */
	public Bullet(Entity sourceEntity) {
		if (sourceEntity == null) {
			throw new IllegalArgumentException("sourceEntity == null");
		}
		source = sourceEntity;
		// Add the default Strategies
		addCollisionStrategy(BULLET_HIT_ON_COLLISION_STRAT);
		addBehaviorStrategy(BULLET_LIFE_TIMER_STRAT);
		addHitStrategy(BULLET_DAMAGE_ON_HIT_STRAT);
		
		/* 
		 * Calculate the position of the Bullet based on the position, 
		 * rotation and size of the source.
		 */
		double posX = getSourceEntity().getX();
		double posY = getSourceEntity().getY();
		double rotation = getSourceEntity().getRotation();
		double srcSize = getSourceEntity().getSize();
		posX += Entity.getOffsetX(rotation, srcSize);
		posY += Entity.getOffsetY(rotation, srcSize);
		
		setPosition(posX, posY);
		// Copy values of velocity and rotation from source
		setVelocity(source);
		setRotation(source);
	}
	
	/**
	 * <p>Returns the {@link Entity} which acts as the source of this {@link Bullet}.
	 * The source will generally not be hit by the Bullet.</p>
	 * <p>The returned value is never null.</p>
	 * @return		a non-null Entity
	 * @see #Bullet(Entity)
	 */
	public Entity getSourceEntity() {
		return source;
	}
	
	/**
	 * <p>Changes how much damage this Bullet will deal with the {@link #BULLET_DAMAGE_ON_HIT_STRAT}.
	 * If the {@link #BULLET_DAMAGE_ON_HIT_STRAT} was not added to the Bullet this value will be 
	 * meaningless.<br>
	 * A negative value for damage will heal targets instead of hurting them.</p>
	 * 
	 * <p>Please note that the {@link #BULLET_DAMAGE_ON_HIT_STRAT} is added to all Bullet's by default. 
	 * To deactivate this feature for a custom Bullet implementation you have to remove the 
	 * {@link #BULLET_DAMAGE_ON_HIT_STRAT} from the Bullet.</p>
	 * 
	 * @param value	any integer value
	 * @see #getDamage()
	 * @see #BULLET_DAMAGE_ON_HIT_STRAT
	 */
	public void setDamage(int value) {
		damage = value;
	}
	
	/**
	 * <p>Returns the amount of life taken from a {@link Target} when this {@link Bullet} hits it 
	 * with the {@link #BULLET_DAMAGE_ON_HIT_STRAT} added to it. If the 
	 * {@link #BULLET_DAMAGE_ON_HIT_STRAT} was not added to this Bullet this value is meaningless.<br>
	 * The returned value can be negative in which case a Target will be healed instead of hurt 
	 * by the Bullet. If this value is 0 a Target will not be damaged by the Bullet.</p>
	 * 
	 * <p>Please note that the {@link #BULLET_DAMAGE_ON_HIT_STRAT} is added to all Bullet's by default. 
	 * To deactivate this feature for a custom Bullet implementation you have to remove the 
	 * {@link #BULLET_DAMAGE_ON_HIT_STRAT} from the Bullet.</p>
	 * 
	 * @return any integer value. A positive value will hurt the Target. A negative value will heal the Target.
	 * @see #setDamage(int)
	 * @see #BULLET_DAMAGE_ON_HIT_STRAT
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * <p>Sets the current {@link #lifeTimer} of this {@link Bullet}. The lifeTimer will be counted 
	 * down by 1 in each update cycle until it reaches 0. When the lifeTimer reaches 0 the Bullet 
	 * will be disposed. This will only happen if the {@link #BULLET_LIFE_TIMER_STRAT} was added 
	 * to this Bullet. A negative value for the lifeTimer will deactivate the lifeTimer feature.</p>
	 * 
	 * <p>Please note that the {@link #BULLET_LIFE_TIMER_STRAT} is added to all Bullet's by default. 
	 * To deactivate this feature for a custom Bullet implementation you have to remove the 
	 * {@link #BULLET_LIFE_TIMER_STRAT} from the Bullet.</p>
	 * 
	 * @param value		any integer value. A positive value will set the time to live for this Bullet. 
	 * 					A value of zero will dispose this Bullet on the next update cycle. 
	 * 					Negative values will deactivate the time to live.
	 * @see #getLifeTimer()
	 * @see #BULLET_LIFE_TIMER_STRAT
	 */
	public void setLifeTimer(int value) {
		lifeTimer = value;
	}
	
	/**
	 * <p>Returns the current {@link #lifeTimer} of this {@link Bullet}. The lifeTimer will be counted 
	 * down by 1 in each update cycle until it reaches 0. When the lifeTimer reaches 0 the Bullet 
	 * will be disposed. This will only happen if the {@link #BULLET_LIFE_TIMER_STRAT} was added 
	 * to this Bullet. A negative value for the lifeTimer will deactivate the lifeTimer feature.</p>
	 * 
	 * <p>Please note that the {@link #BULLET_LIFE_TIMER_STRAT} is added to all Bullet's by default. 
	 * To deactivate this feature for a custom Bullet implementation you have to remove the 
	 * {@link #BULLET_LIFE_TIMER_STRAT} from the Bullet.</p>
	 * 
	 * @return		any integer value. A positive value will set the time to live for this Bullet. 
	 * 				A value of zero will dispose this Bullet on the next update cycle. 
	 * 				Negative values will deactivate the time to live.
	 * @see #setLifeTimer(int)
	 * @see #BULLET_LIFE_TIMER_STRAT
	 */
	public int getLifeTimer() {
		return lifeTimer;
	}
	
	/**
	 * <p>Changes the armed state of this {@link Bullet}. When a bullet is not armed it can not hit any 
	 * {@link Target targets}. If the Bullet is armed it will hit targets when they collide with the 
	 * Bullet.</p>
	 * <p>Please note that the armed state is used by the {@link #BULLET_HIT_ON_COLLISION_STRAT}. If 
	 * you choose to remove that strategy and replace it with a custom {@link EntityCollisionStrategy} 
	 * it is up to you to honor the armed state of the Bullet or to ignore it.</p>
	 * <p>A newly created Bullet is armed by default.</p>
	 * @param value		True to make this Bullet armed. False to disarm this bullet.
	 * @see #isArmed()
	 * @see #BULLET_HIT_ON_COLLISION_STRAT
	 */
	public void setArmed(boolean value) {
		armed = value;
	}
	
	/**
	 * <p>Returns whether this {@link Bullet} is armed or not. When a bullet is not armed it can not 
	 * hit any {@link Target targets}. If the Bullet is armed it will hit targets when they collide 
	 * with the Bullet.</p>
	 * <p>Please note that the armed state is used by the {@link #BULLET_HIT_ON_COLLISION_STRAT}. If 
	 * you choose to remove that strategy and replace it with a custom {@link EntityCollisionStrategy} 
	 * it is up to you to honor the armed state of the Bullet or to ignore it.</p>
	 * <p>A newly created Bullet is armed by default.</p>
	 * @return 			True if this Bullet is armed. False if this Bullet is not armed.
	 * @see #isArmed()
	 * @see #BULLET_HIT_ON_COLLISION_STRAT
	 */
	public boolean isArmed() {
		return armed;
	}
	
	/**
	 * <p>Sets the only-hit-alive state of this {@link Bullet}. When a Bullet is set to only-hit-alive 
	 * it will only hit those {@link Target targets} which are considered alive as returned by the 
	 * {@link Target#isAlive()} method. If only-hit-alive is set to false the Bullet will hit any target 
	 * regardless of whether it is alive or dead.</p>
	 * <p>Please note that the only-hit-alive state is used by the {@link #BULLET_HIT_ON_COLLISION_STRAT}. 
	 * If you choose to remove that strategy and replace it with a custom {@link EntityCollisionStrategy} 
	 * it is up to you to honor the only-hit-alive state of the Bullet or to ignore it.</p>
	 * <p>A newly created Bullet will have only-hit-alive set to true by default.</p>
	 * @param value 	True to make this Bullet only hit living targets. False to make this Bullet hit 
	 * 					both living and dead targets.
	 * @see #isOnlyHitAlive()
	 * @see #BULLET_HIT_ON_COLLISION_STRAT
	 */
	public void setOnlyHitAlive(boolean value) {
		onlyHitAlive = value;
	}
	
	/**
	 * <p>Returns whether this {@link Bullet} will only hit living {@link Target targets} of whether it 
	 * will also hit dead targets. When a Bullet is set to only-hit-alive it will only hit those targets 
	 * which are considered alive as returned by the  {@link Target#isAlive()} method. If only-hit-alive 
	 * is set to false the Bullet will hit any target regardless of whether it is alive or dead.</p>
	 * <p>Please note that the only-hit-alive state is used by the {@link #BULLET_HIT_ON_COLLISION_STRAT}. 
	 * If you choose to remove that strategy and replace it with a custom {@link EntityCollisionStrategy} 
	 * it is up to you to honor the only-hit-alive state of the Bullet or to ignore it.</p>
	 * <p>A newly created Bullet will have only-hit-alive set to true by default.</p>
	 * @return			True if this Bullet will only hit living targets. False if this Bullet will hit 
	 * 					both living and dead targets.
	 * @see #setOnlyHitAlive(boolean)
	 * @see #BULLET_HIT_ON_COLLISION_STRAT
	 */
	public boolean isOnlyHitAlive() {
		return onlyHitAlive;
	}
	
	/**
	 * <p>Adds the given {@link BulletHitStrategy} to this Bullet. 
	 * When this Bullet hits any {@link Target Target's} while it has the 
	 * {@link #BULLET_HIT_ON_COLLISION_STRAT} added to it, it will notify all BulletHitStrategy's 
	 * by calling their {@link BulletHitStrategy#onHit(Bullet, Target)} method.</p>
	 * 
	 * <p>A BulletHitStrategy can be added to and removed from a Bullet while its 
	 * {@link BulletHitStrategy#onHit(Bullet, Target)} is being called without throwing a 
	 * {@link ConcurrentModificationException}. A BulletHitStrategy can also be added to any 
	 * number of Bullet's simultaneously.</p>
	 * 
	 * <p>You can use BulletHitStrategy's to add custom effects to Bullet hits.</p>
	 * @param strategy						a non-null BulletHitStrategy
	 * @throws IllegalArgumentException		if strategy is null
	 * @see #removeHitStrategy(BulletHitStrategy)
	 * @see BulletHitStrategy
	 * @see BulletHitStrategy#onHit(Bullet, Target)
	 * @see #BULLET_HIT_ON_COLLISION_STRAT
	 */
	public void addHitStrategy(BulletHitStrategy strategy) {
		if (strategy == null) {
			throw new IllegalArgumentException("strategy == null");
		}
		hitStrats.add(strategy);
	}
	
	/**
	 * <p>Removes the given {@link BulletHitStrategy} from this Bullet. 
	 * After the BulletHitStrategy was removed it will not be notified of any hits anymore.</p>
	 * 
	 * <p>If the BulletHitStrategy is removed while a hit is being propagated it may or may not 
	 * still be notified of the current hit.</p>
	 * 
	 * @param strategy						a non-null BulletHitStrategy
	 * @throws IllegalArgumentException		if strategy is null
	 * @see #addHitStrategy(BulletHitStrategy)
	 * @see BulletHitStrategy
	 * @see BulletHitStrategy#onHit(Bullet, Target)
	 * @see #BULLET_HIT_ON_COLLISION_STRAT
	 */
	public void removeHitStrategy(BulletHitStrategy strategy) {
		if (strategy == null) {
			throw new IllegalArgumentException("strategy == null");
		}
		hitStrats.remove(strategy);
	}
	
	/**
	 * <p>Sets the current target filter for this bullet. A target filter is a {@link Predicate} for 
	 * {@link Target targets} which decides whether a given target is an acceptable target for this 
	 * bullet or not. If the predicate returns {@code true} for a given target then this target will 
	 * be hit by the Bullet. If the predicate returns {@code false} the target will not be hit.</p>
	 * <p>The predicate is tested in the {@link #BULLET_HIT_ON_COLLISION_STRAT}. If you choose to remove 
	 * that strategy and replace it with a custom {@link EntityCollisionStrategy} it is up to you to 
	 * honor the target filter of the Bullet or to ignore it.</p>
	 * <p>For a newly created Bullet the target filter is {@code null}.</p>
	 * @param filter			either null or an instance of {@link Predicate}
	 * @see #getTargetFilter()
	 * @see #BULLET_HIT_ON_COLLISION_STRAT
	 */
	public void setTargetFilter(Predicate<Target> filter) {
		targetFilter = filter;
	}
	
	/**
	 * <p>Returns the current target filter for this bullet. A target filter is a {@link Predicate} for 
	 * {@link Target targets} which decides whether a given target is an acceptable target for this 
	 * bullet or not. If the predicate returns {@code true} for a given target then this target will 
	 * be hit by the Bullet. If the predicate returns {@code false} the target will not be hit.</p>
	 * <p>The predicate is tested in the {@link #BULLET_HIT_ON_COLLISION_STRAT}. If you choose to remove 
	 * that strategy and replace it with a custom {@link EntityCollisionStrategy} it is up to you to 
	 * honor the target filter of the Bullet or to ignore it.</p>
	 * <p>For a newly created Bullet the target filter is {@code null}.</p>
	 * @return			An instance of {@link Predicate} or null
	 * @see #setTargetFilter(Predicate)
	 * @see #BULLET_HIT_ON_COLLISION_STRAT
	 */
	public Predicate<Target> getTargetFilter() {
		return targetFilter;
	}
	
	/**
	 * <p>Calls the {@link BulletHitStrategy#onHit(Bullet, Target)} method of all 
	 * {@link BulletHitStrategy BulletHitStrategy's} that are currently added to this Bullet.</p>
	 * 
	 * <p>The first argument passed to the {@link BulletHitStrategy#onHit(Bullet, Target)} will be this 
	 * Bullet. The second argument will be the {@link Target} that was passed to this method.</p>
	 * 
	 * @param target						a non-null Target
	 * @throws IllegalArgumentException		if target is null
	 */
	public void fireOnHit(Target target) {
		if (target == null) {
			throw new IllegalArgumentException("target == null");
		}
		hitStrats.forEach((strat) -> strat.onHit(this, target));
	}
	
}