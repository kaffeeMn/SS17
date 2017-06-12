package edu.udo.cs.swtsf.core;

import java.util.ConcurrentModificationException;

import edu.udo.cs.swtsf.util.Group;
import edu.udo.cs.swtsf.util.GroupFactory;

/**
 * <p>This class is the base for all game objects. It provides basic functionality for movement, 
 * rotation, collision and customizable behavior.</p>
 * 
 * <p>The behavior of an {@link Entity} can be controlled with {@link EntityBehaviorStrategy 
 * EntityBehaviorStrategy's} and {@link EntityCollisionStrategy EntityCollisionStrategy's}. 
 * An Entity can have any number of these strategies added to itself at any point in time.</p>
 * 
 * <p>The movement of an Entity is controlled by the {@link #getVelocityX() x-velocity} and 
 * {@link #getVelocityY() y-velocity}. The velocity is added to the position in each update cycle 
 * after the {@link EntityBehaviorStrategy behaviors} have been updated.<br>
 * The movement is independent of the {@link #getRotation() rotation} and {@link #getSize() size}.</p>
 * 
 * <p>An Entity can be added to a {@link Game} with the {@link Game#addEntity(Entity)} method. An 
 * Entity can only ever be added to one Game and it will be part of that Game until it is 
 * {@link #isDisposed() disposed}. When disposed the Entity will automatically be removed from 
 * the Game. Once disposed an Entity can never be un-disposed and re-used in a Game.</p>
 * 
 * <p>Individual instances can be observed by {@link EntityObserver EntityObserver's} which are 
 * added with the {@link #addObserver(EntityObserver)} method.</p>
 * 
 * <p>Every Entity in the Game has an identification number which can be queried by 
 * {@link #getID()}. This number is unique for all Entities created within the same JVM, 
 * independent of the Game they are used in. The ID of an Entity will never change after 
 * the Entity was created.</p>
 * 
 * @see EntityBehaviorStrategy
 * @see EntityCollisionStrategy
 * @see EntityObserver
 * @see Game
 * @see Game#addEntity(Entity)
 */
public abstract class Entity {
	
	/**
	 * The initial {@link #getSize() size} of all {@link Entity Entities}.<br>
	 * @see #size
	 * @see #setSize(int)
	 * @see #getSize()
	 */
	public static final int DEFAULT_ENTITY_SIZE = 32;
	
	public static final double DEGREES_EAST = 0;
	public static final double DEGREES_SOUTH_EAST = 45;
	public static final double DEGREES_SOUTH = 90;
	public static final double DEGREES_SOUTH_WEST = 135;
	public static final double DEGREES_WEST = 180;
	public static final double DEGREES_NORTH_WEST = 225;
	public static final double DEGREES_NORTH = 270;
	public static final double DEGREES_NORTH_EAST = 315;
	
	/**
	 * Used to determine the unique ID of each Entity.
	 * @see #id
	 */
	private static int GLOBAL_ID = 0;
	
	/**
	 * Contains all currently registered {@link EntityObserver observers}.
	 * @see #addObserver(EntityObserver)
	 * @see #removeObserver(EntityObserver)
	 * @see #firePositionChangedEvent()
	 * @see #fireVelocityChangedEvent()
	 * @see #fireRotationChangedEvent()
	 * @see #fireSizeChangedEvent()
	 */
	private final Group<EntityObserver> observers 
							= GroupFactory.get().createNewGroup();
	/**
	 * Contains all {@link EntityBehaviorStrategy behaviors}. This is used by the 
	 * {@link #updatePositionByVelocity()} method.
	 * @see #addBehaviorStrategy(EntityBehaviorStrategy)
	 * @see #removeBehaviorStrategy(EntityBehaviorStrategy)
	 * @see #updateBehaviors()
	 */
	private final Group<EntityBehaviorStrategy> behaviorStrategies 
							= GroupFactory.get().createNewGroup();
	/**
	 * Contains all {@link EntityCollisionStrategy collision strategies}. This is 
	 * used by the {@link #onCollision(Entity)} method.
	 * @see #addCollisionStrategy(EntityCollisionStrategy)
	 * @see #removeCollisionStrategy(EntityCollisionStrategy)
	 * @see #onCollision(Entity)
	 */
	private final Group<EntityCollisionStrategy> collisionStrategies 
							= GroupFactory.get().createNewGroup();
	/**
	 * The unique ID of this Entity.
	 * @see #GLOBAL_ID
	 */
	private final int id = GLOBAL_ID++;
	
	/**
	 * Reference to the {@link Game} this Entity was added to. This is null at first 
	 * until the Entity was added to a Game or after the Entity has already been 
	 * {@link #isDisposed() disposed}.
	 * @see #setCurrentGame(Game)
	 * @see #getCurrentGame()
	 */
	private Game currentGame;
	/**
	 * True if this {@link Entity} is disposed. False by default. This value should 
	 * not be set to false anywhere in the code. Since it should only ever change 
	 * once from false to true.
	 * @see #dispose()
	 * @see #isDisposed()
	 */
	private boolean disposed;
	/**
	 * The area of this Entity is always a circle centered at {@link #positionX} and 
	 * {@link #positionY} and with a <b>diameter</b> of {@link #size}.
	 * @see #setPosition(double, double)
	 * @see #getX()
	 * @see #getY()
	 */
	private double positionX;
	/**
	 * @see #positionX
	 */
	private double positionY;
	/**
	 * How far this {@link Entity} will move on the X-Axis in each update cycle.
	 * @see #setVelocity(double, double)
	 * @see #getVelocityX()
	 * @see #updatePositionByVelocity()
	 */
	private double velocityX;
	/**
	 * How far this {@link Entity} will move on the Y-Axis in each update cycle.
	 * @see #setVelocity(double, double)
	 * @see #getVelocityY()
	 * @see #updatePositionByVelocity()
	 */
	private double velocityY;
	/**
	 * <p>The area of this Entity is always a circle centered at {@link #positionX} and 
	 * {@link #positionY} and with a <b>diameter</b> of {@link #size}.</p>
	 * 
	 * <p>Default value is {@link #DEFAULT_ENTITY_SIZE}.</p>
	 * @see #setSize(int)
	 * @see #getSize()
	 */
	private int size = DEFAULT_ENTITY_SIZE;
	/**
	 * <p>The rotation of an Entity is not used by any standard features but might be 
	 * used by the view or sub-classes.</p>
	 * @see #setRotation(double)
	 * @see #getRotation()
	 */
	private double rotation;
	
	/**
	 * <p>Adds the {@link #setVelocity(double, double) velocity} to the 
	 * {@link #setPosition(double, double) position} of this Entity.</p>
	 * 
	 * <p>This method is called exactly once in every update cycle by the {@link Game} 
	 * when its {@link Game#update()} method is invoked.</p>
	 * 
	 * @see #isMoving()
	 * @see #getVelocityX()
	 * @see #getVelocityY()
	 * @see Game#update()
	 */
	final void updatePositionByVelocity() {
		if (isMoving()) {
			positionX += velocityX;
			positionY += velocityY;
			firePositionChangedEvent();
		}
	}
	
	/**
	 * <p>Updates all {@link EntityBehaviorStrategy behaviors} of this {@link Entity}.</p>
	 * 
	 * <p>This method is called exactly once in every update cycle by the {@link Game} 
	 * when its {@link Game#update()} method is invoked.</p>
	 * 
	 * @see #addBehaviorStrategy(EntityBehaviorStrategy)
	 * @see #removeBehaviorStrategy(EntityBehaviorStrategy)
	 * @see EntityBehaviorStrategy#act(Entity)
	 * @see Game#update()
	 */
	final void updateBehaviors() {
		behaviorStrategies.forEach(strat -> {
			if (!Entity.this.isDisposed() && Entity.this.getCurrentGame() != null) {
				strat.act(this);
			}
		});
	}
	
	/**
	 * <p>Calls the {@link EntityCollisionStrategy#onCollisionWith(Entity, Entity)} method of 
	 * all {@link EntityCollisionStrategy EntityCollisionStrategy's} that are currently 
	 * added to this {@link Entity}. The first argument passed to the onCollisionWith method 
	 * will be this Entity. The second argument will be the Entity {@code other} that is 
	 * passed to this method by the {@link Game}.</p>
	 * 
	 * <p>The collision detection is done by the Game during the {@link Game#update()} method. 
	 * When the Game notices a collision between this Entity and another the Game will call 
	 * this method.</p>
	 * 
	 * @param other				the Entity which collided with this Entity
	 * @see #addCollisionStrategy(EntityCollisionStrategy)
	 * @see #removeCollisionStrategy(EntityCollisionStrategy)
	 * @see EntityCollisionStrategy#onCollisionWith(Entity, Entity)
	 * @see Game#update()
	 * @see Game#areOverlapping(Entity, Entity)
	 */
	final void onCollision(Entity other) {
		collisionStrategies.forEach(strat -> {
			if (!Entity.this.isDisposed() && Entity.this.getCurrentGame() != null) {
				strat.onCollisionWith(this, other);
			}
		});
	}
	
	/**
	 * <p>Adds the given {@link EntityBehaviorStrategy} to this {@link Entity}. For all added 
	 * EntityBehaviorStrategy's the {@link EntityBehaviorStrategy#act(Entity)} method will 
	 * be called with this {@link Entity} as the argument in every update cycle of the 
	 * {@link Game}.</p>
	 * 
	 * <p>Adding an EntityBehaviorStrategy during an update cycle will not throw any 
	 * {@link ConcurrentModificationException ConcurrentModificationException's}.</p>
	 * 
	 * @param strategy						a non-null EntityBehaviorStrategy
	 * @throws IllegalArgumentException		if strategy is null
	 * @see #removeBehaviorStrategy(EntityBehaviorStrategy)
	 * @see #updateBehaviors()
	 * @see EntityBehaviorStrategy#act(Entity)
	 * @see Game#update()
	 */
	public void addBehaviorStrategy(EntityBehaviorStrategy strategy) {
		if (strategy == null) {
			throw new IllegalArgumentException("strategy == null");
		}
		behaviorStrategies.add(strategy);
	}
	
	/**
	 * <p>Removes the given {@link EntityBehaviorStrategy} from this {@link Entity}. The strategy 
	 * will no longer be called in each update cycle. If the strategy was removed during an 
	 * ongoing update cycle it is possible that the strategy will still be updated just this 
	 * once for the current update cycle.</p>
	 * 
	 * <p>Removing an EntityBehaviorStrategy during an update cycle will not throw any 
	 * {@link ConcurrentModificationException ConcurrentModificationException's}.</p>
	 * 
	 * @param strategy						a non-null EntityBehaviorStrategy
	 * @throws IllegalArgumentException		if strategy is null
	 * @see #addBehaviorStrategy(EntityBehaviorStrategy)
	 * @see #updateBehaviors()
	 * @see Game#update()
	 */
	public void removeBehaviorStrategy(EntityBehaviorStrategy strategy) {
		if (strategy == null) {
			throw new IllegalArgumentException("strategy == null");
		}
		behaviorStrategies.remove(strategy);
	}
	
	/**
	 * <p>Returns true if there are any {@link EntityBehaviorStrategy EntityBehaviorStrategy's} 
	 * attached to this Entity.</p> 
	 * @return	true if this Entity has any EntityBehaviorStrategy's to update
	 */
	public boolean hasBehaviorStrategies() {
		return !behaviorStrategies.isEmpty();
	}
	
	/**
	 * <p>Returns true if the given {@link EntityBehaviorStrategy} is currently added to 
	 * this {@link Entity}.</p>
	 * @param strategy			a non-null EntityBehaviorStrategy
	 * @return					true if strategy is added to this Entity, otherwise false
	 */
	public boolean hasBehaviorStrategy(EntityBehaviorStrategy strategy) {
		return behaviorStrategies.contains(strategy);
	}
	
	/**
	 * <p>Returns true if at least one {@link EntityBehaviorStrategy} of class strategyClass 
	 * is currently added to this {@link Entity}.</p>
	 * @param strategyClass		a non-null Class
	 * @return					true if a strategy of type strategyClass is added to this Entity, otherwise false
	 */
	public <T extends EntityBehaviorStrategy> 
		boolean hasBehaviorStrategy(Class<T> strategyClass) 
	{
		return getBehaviorStrategy(strategyClass) != null;
	}
	
	/**
	 * <p>Returns the first {@link EntityBehaviorStrategy} of type strategyClass that is 
	 * currently added to this {@link Entity}. If no such strategy exists null is returned.</p>
	 * Equal to:
	 * <pre>
	 * {@code getBehaviorStrategy(strategyClass, true)}
	 * </pre>
	 * @param strategyClass		a non-null Class
	 * @return					an EntityBehaviorStrategy of type strategyClass or null
	 * @see #getBehaviorStrategy(Class, boolean)
	 */
	public <T extends EntityBehaviorStrategy> 
		T getBehaviorStrategy(Class<T> strategyClass) 
	{
		return getBehaviorStrategy(strategyClass, true);
	}
	
	/**
	 * <p>Returns the first {@link EntityBehaviorStrategy} of type strategyClass that is 
	 * currently added to this {@link Entity}. If no such strategy exists null is returned.</p>
	 * <p>If exactMatch is true the returned strategy is exactly of type strategyClass and 
	 * not a subtype of strategyClass. If exactMatch is false the returned strategy may be 
	 * a subclass of strategyClass. In either case the check 
	 * {@link Class#isInstance(Object) strategyClass.isInstance(Object)} will return true 
	 * for the returned strategy.</p>
	 * @param strategyClass		a non-null Class
	 * @param exactMatch		if false the returned value may be a subtype of strategyClass
	 * @return					an EntityBehaviorStrategy of type strategyClass or null
	 * @see #getBehaviorStrategy(Class)
	 */
	@SuppressWarnings("unchecked")
	public <T extends EntityBehaviorStrategy> 
		T getBehaviorStrategy(Class<T> strategyClass, boolean exactMatch) 
	{
		if (strategyClass == null) {
			throw new IllegalArgumentException("strategyClass == null");
		}
		return (T) behaviorStrategies.getFirstMatch(strat -> {
			if (exactMatch) {
				return strat.getClass().equals(strategyClass);
			} else {
				return strategyClass.isInstance(strat);
			}
		});
	}
	
	/**
	 * <p>Adds the given {@link EntityCollisionStrategy} to this {@link Entity}. Every 
	 * time this Entity collides with another Entity the {@link EntityCollisionStrategy#onCollisionWith(Entity, Entity)} 
	 * method of all EntityCollisionStrategy's added to this Entity will be called with this 
	 * Entity and the colliding Entity as arguments.</p>
	 * 
	 * <p>Adding an EntityCollisionStrategy during an update cycle will not throw any 
	 * {@link ConcurrentModificationException ConcurrentModificationException's}.</p>
	 * 
	 * @param strategy						a non-null EntityCollisionStrategy
	 * @throws IllegalArgumentException		if strategy is null
	 * @see #removeCollisionStrategy(EntityCollisionStrategy)
	 * @see #onCollision(Entity)
	 * @see EntityCollisionStrategy#onCollisionWith(Entity, Entity)
	 * @see Game#update()
	 */
	public void addCollisionStrategy(EntityCollisionStrategy strategy) {
		if (strategy == null) {
			throw new IllegalArgumentException("strategy == null");
		}
		collisionStrategies.add(strategy);
	}
	
	/**
	 * <p>Removes the given {@link EntityCollisionStrategy} from this {@link Entity}. The strategy 
	 * will no longer be called in each update cycle. If the strategy was removed during an 
	 * ongoing update cycle it is possible that the strategy will still be updated just this 
	 * once for the current update cycle.</p>
	 * 
	 * <p>Removing an EntityCollisionStrategy during an update cycle will not throw any 
	 * {@link ConcurrentModificationException ConcurrentModificationException's}.</p>
	 * 
	 * @param strategy						a non-null EntityCollisionStrategy
	 * @throws IllegalArgumentException		if strategy is null
	 * @see #addCollisionStrategy(EntityCollisionStrategy)
	 * @see #onCollision(Entity)
	 * @see Game#update()
	 */
	public void removeCollisionStrategy(EntityCollisionStrategy strategy) {
		if (strategy == null) {
			throw new IllegalArgumentException("strategy == null");
		}
		collisionStrategies.remove(strategy);
	}
	
	/**
	 * <p>Returns true if there are any {@link EntityCollisionStrategy 
	 * EntityCollisionStrategy's} attached to this Entity.</p> 
	 * @return	true if this Entity has any EntityCollisionStrategy's to update
	 */
	public boolean hasCollisionStrategies() {
		return !collisionStrategies.isEmpty();
	}
	
	/**
	 * <p>Returns true if the given {@link EntityCollisionStrategy} is currently added to 
	 * this {@link Entity}.</p>
	 * @param strategy			a non-null EntityCollisionStrategy
	 * @return					true if strategy is added to this Entity, otherwise false
	 */
	public boolean hasCollisionStrategy(EntityCollisionStrategy strategy) {
		return collisionStrategies.contains(strategy);
	}
	
	/**
	 * <p>Returns true if at least one {@link EntityCollisionStrategy} of class strategyClass 
	 * is currently added to this {@link Entity}.</p>
	 * @param strategyClass		a non-null Class
	 * @return					true if a strategy of type strategyClass is added to this Entity, otherwise false
	 */
	public <T extends EntityCollisionStrategy> 
		boolean hasCollisionStrategy(Class<T> strategyClass) 
	{
		return getCollisionStrategy(strategyClass) != null;
	}
	
	/**
	 * <p>Returns the first {@link EntityCollisionStrategy} of type strategyClass that is 
	 * currently added to this {@link Entity}. If no such strategy exists null is returned.</p>
	 * Equal to:
	 * <pre>
	 * {@code getCollisionStrategy(strategyClass, true)}
	 * </pre>
	 * @param strategyClass		a non-null Class
	 * @return					an EntityCollisionStrategy of type strategyClass or null
	 * @see #getCollisionStrategy(Class, boolean)
	 */
	public <T extends EntityCollisionStrategy> 
		T getCollisionStrategy(Class<T> strategyClass) 
	{
		return getCollisionStrategy(strategyClass, true);
	}
	
	/**
	 * <p>Returns the first {@link EntityCollisionStrategy} of type strategyClass that is 
	 * currently added to this {@link Entity}. If no such strategy exists null is returned.</p>
	 * <p>If exactMatch is true the returned strategy is exactly of type strategyClass and 
	 * not a subtype of strategyClass. If exactMatch is false the returned strategy may be 
	 * a subclass of strategyClass. In either case the check 
	 * {@link Class#isInstance(Object) strategyClass.isInstance(Object)} will return true 
	 * for the returned strategy.</p>
	 * @param strategyClass		a non-null Class
	 * @param exactMatch		if false the returned value may be a subtype of strategyClass
	 * @return					an EntityCollisionStrategy of type strategyClass or null
	 * @see #getCollisionStrategy(Class)
	 */
	@SuppressWarnings("unchecked")
	public <T extends EntityCollisionStrategy> 
		T getCollisionStrategy(Class<T> strategyClass, boolean exactMatch) 
	{
		return (T) behaviorStrategies.getFirstMatch(strat -> {
			if (exactMatch) {
				return strat.getClass().equals(strategyClass);
			} else {
				return strategyClass.isInstance(strat);
			}
		});
	}
	
	
	/**
	 * <p>Sets the current {@link Game}. This method is supposed to be called by the 
	 * {@link Game#addEntity(Entity)} only. No checking is done to ensure this method is 
	 * not called illegitimately.</p>
	 * @param game						the Game that should become the new currentGame
	 * @throws IllegalStateException	if getCurrentGame() does not return null and game is not null either
	 * @see #getCurrentGame()
	 */
	void setCurrentGame(Game game) {
		if (currentGame != null && game != null) {
			throw new IllegalStateException("currentGame != null");
		}
		Game oldGame = currentGame;
		currentGame = game;
		if (oldGame != null) {
			fireRemovedFromGameEvent(oldGame);
		}
		if (currentGame != null) {
			fireAddedToGameEvent();
		}
	}
	
	/**
	 * <p>Returns the {@link Game} that this {@link Entity} is currently a part of. If this Entity 
	 * has not yet been added to any Game via the {@link Game#addEntity(Entity)} method this 
	 * method will return null.</p>
	 * @return	the Game that this Entity is a part of or null if no such Game exists.
	 */
	public Game getCurrentGame() {
		return currentGame;
	}
	
	/**
	 * <p>Disposes this {@link Entity} if it has not been disposed already. When an Entity is 
	 * disposed it will be removed from the {@link Game} at some point during the next 
	 * update cycle. Once disposed, an Entity can never be brought back into the Game.</p>
	 * <p>This is the only way to remove an Entity from the Game.</p>
	 * @see #isDisposed()
	 */
	public void dispose() {
		disposed = true;
	}
	
	/**
	 * <p>Returns true if this {@link Entity} is disposed. If a disposed Entity is part of a 
	 * {@link Game} will be removed at some point during the next update cycle. Once disposed, 
	 * an Entity can never be brought back into the Game.</p>
	 * @return	true if this Entity has been disposed
	 * @see #dispose()
	 */
	public boolean isDisposed() {
		return disposed;
	}
	
	/**
	 * <p>Sets the size of this {@link Entity} equal to the size of the {@code other} Entity.</p>
	 * @param other							a non-null Entity
	 * @throws IllegalArgumentException		if other is null
	 * @see #setSize(int)
	 * @see #getSize()
	 */
	public void setSize(Entity other) {
		if (other == null) {
			throw new IllegalArgumentException("other == null");
		}
		setSize(other.getSize());
	}
	
	/**
	 * <p>Sets the size of this {@link Entity} to value. The size is the diameter of the circle 
	 * around the {@link #setPosition(double, double) position} of this Entity. Two Entities 
	 * {@link #onCollision(Entity) collide} when the circles around their positions overlap.</p>
	 * @param value							the new size of this Entity
	 * @throws IllegalArgumentException		if value is less then 1
	 * @see #setSize(Entity)
	 * @see #getSize()
	 */
	public void setSize(int value) {
		if (value < 1) {
			throw new IllegalArgumentException("value < 1");
		}
		if (getSize() != value) {
			size = value;
			fireSizeChangedEvent();
		}
	}
	
	/**
	 * <p>Returns the current size of this {@link Entity}. The size is the diameter of the circle 
	 * around the {@link #setPosition(double, double) position} of this Entity. Two Entities 
	 * {@link #onCollision(Entity) collide} when the circles around their positions overlap.</p>
	 * <p>The size of an Entity is never below 1.</p>
	 * @return	the size of this Entity. Always greater then or equal to 1.<br>
	 * @see #setSize(int)
	 * @see #setSize(Entity)
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * <p>Sets the rotation of this {@link Entity} equal to the rotation of the {@code other} Entity.</p>
	 * @param other							a non-null Entity
	 * @throws IllegalArgumentException		if other is null
	 * @see #setRotation(double)
	 * @see #getRotation()
	 */
	public void setRotation(Entity other) {
		if (other == null) {
			throw new IllegalArgumentException("other == null");
		}
		setRotation(other.getRotation());
	}
	
	/**
	 * <p>Sets the rotation of this {@link Entity} to value. The rotation is always between 0 and 360 
	 * with a rotation of 0 pointing to the right-hand side of the screen.</p>
	 * <p>The rotation of an Entity has no effect on any core mechanics but it can be used by sub 
	 * classes if needed.</p>
	 * @param value							the new rotation of this Entity
	 * @see #setRotation(Entity)
	 * @see #getRotation()
	 */
	public void setRotation(double value) {
		if (getRotation() != value) {
			rotation = value % 360;
			if (rotation < 0) {
				rotation += 360;
			}
			fireRotationChangedEvent();
		}
	}
	
	/**
	 * <p>Returns the current rotation of this {@link Entity}. The rotation is always between 0 and 360 
	 * with a rotation of 0 pointing to the right-hand side of the screen.</p>
	 * <p>The rotation of an Entity has no effect on any core mechanics but it can be used by sub 
	 * classes if needed.</p>
	 * @return	the rotation of this Entity. Always between 0 and 360.<br>
	 * @see #setRotation(double)
	 * @see #setRotation(Entity)
	 */
	public double getRotation() {
		return rotation;
	}
	
	/**
	 * <p>Sets the position of this {@link Entity} equal to the position of the {@code other} Entity.</p>
	 * @param other							a non-null Entity
	 * @throws IllegalArgumentException		if other is null
	 * @see #setPosition(double, double)
	 * @see #getX()
	 * @see #getY()
	 */
	public void setPosition(Entity other) {
		if (other == null) {
			throw new IllegalArgumentException("other == null");
		}
		setPosition(other.getX(), other.getY());
	}
	
	/**
	 * <p>Sets the position of this {@link Entity} to the point defined by the x and y parameters.</p>
	 * @param x								the x-coordinate of the new position. Both negative and 
	 * 										positive values as well as 0.00 are valid.
	 * @param y								the y-coordinate of the new position. Both negative and 
	 * 										positive values as well as 0.00 are valid.
	 * @see #setPosition(Entity)
	 * @see #getX()
	 * @see #getY()
	 */
	public void setPosition(double x, double y) {
		if (getX() != x || getY() != y) {
			positionX = x;
			positionY = y;
			firePositionChangedEvent();
		}
	}
	
	/**
	 * <p>Returns the x-coordinate of this {@link Entity}. The position of an Entity is define by 
	 * its x- and y-coordinate. The {@link #getSize() size} of an Entity is the diameter of the 
	 * circle around the position of this Entity. Two Entities {@link #onCollision(Entity) collide} 
	 * when the circles around their positions overlap.</p>
	 * <p>The returned value can be negative.</p>
	 * @return	the position of this Entity on the X-axis
	 * @see #setPosition(double, double)
	 * @see #setPosition(Entity)
	 * @see #getY()
	 */
	public double getX() {
		return positionX;
	}
	
	/**
	 * <p>Returns the y-coordinate of this {@link Entity}. The position of an Entity is define by 
	 * its x- and y-coordinate. The {@link #getSize() size} of an Entity is the diameter of the 
	 * circle around the position of this Entity. Two Entities {@link #onCollision(Entity) collide} 
	 * when the circles around their positions overlap.</p>
	 * <p>The returned value can be negative.</p>
	 * @return	the position of this Entity on the Y-axis
	 * @see #setPosition(double, double)
	 * @see #setPosition(Entity)
	 * @see #getX()
	 */
	public double getY() {
		return positionY;
	}
	
	/**
	 * <p>Sets the velocity of this {@link Entity} equal to the velocity of the {@code other} Entity.</p>
	 * @param other							a non-null Entity
	 * @throws IllegalArgumentException		if other is null
	 * @see #setVelocity(double, double)
	 * @see #getVelocityX()
	 * @see #getVelocityY()
	 */
	public void setVelocity(Entity other) {
		if (other == null) {
			throw new IllegalArgumentException("other == null");
		}
		setVelocity(other.getVelocityX(), other.getVelocityY());
	}
	
	/**
	 * <p>Sets the distance that this {@link Entity} will move during each update cycle. 
	 * The move distance on the X- and Y-axis can be set separately.</p>
	 * @param x			the distance moved on the X-axis per update cycle
	 * @param y			the distance moved on the Y-axis per update cycle
	 * @see #setVelocity(Entity)
	 * @see #addVelocity(Entity)
	 * @see #addVelocity(double, double)
	 * @see #setSpeedForward(double)
	 * @see #setSpeedBackwards(double)
	 * @see #setSpeedDirectional(double, double)
	 * @see #moveTowards(Entity, double)
	 * @see #moveTowards(double, double, double)
	 * @see #getVelocityX()
	 * @see #getVelocityY()
	 */
	public void setVelocity(double x, double y) {
		if (getVelocityX() != x || getVelocityY() != y) {
			velocityX = x;
			velocityY = y;
			fireVelocityChangedEvent();
		}
	}
	
	/**
	 * <p>Adds the velocity of the given {@link Entity} to this Entities velocity.</p>
	 * Equal to:
	 * <pre>
	 * {@code addVelocity(other.getVelocityX(), other.getVelocityY());}
	 * </pre>
	 * @param other							a non-null Entity
	 * @throws IllegalArgumentException		if other is null
	 * @see #setVelocity(double, double)
	 * @see #getVelocityX()
	 * @see #getVelocityY()
	 */
	public void addVelocity(Entity other) {
		if (other == null) {
			throw new IllegalArgumentException("other == null");
		}
		addVelocity(other.getVelocityX(), other.getVelocityY());
	}
	
	/**
	 * <p>Adds the given values to this {@link Entity Entities} velocity.</p>
	 * Equal to:
	 * <pre>
	 * {@code setVelocity(getVelocityX() + x, getVelocityY() + y);}
	 * </pre>
	 * @param x								the velocity gain on the X-axis
	 * @param y								the velocity gain on the Y-axis
	 * @see #setVelocity(double, double)
	 * @see #getVelocityX()
	 * @see #getVelocityY()
	 */
	public void addVelocity(double x, double y) {
		setVelocity(getVelocityX() + x, getVelocityY() + y);
	}
	
	/**
	 * <p>Returns the distance this Entity will travel on the X-axis in the next update cycle.</p>
	 * <p>Negative values go to the left, positive values go to the right.</p>
	 * @return				the distance moved per update cycle on the X-axis
	 * @see #getVelocityY()
	 * @see #getSpeed()
	 * @see #getMoveDirection()
	 */
	public double getVelocityX() {
		return velocityX;
	}
	
	/**
	 * <p>Returns the distance this Entity will travel on the Y-axis in the next update cycle.</p>
	 * <p>Negative values go up, positive values go down.</p>
	 * @return				the distance moved per update cycle on the Y-axis
	 * @see #getVelocityX()
	 * @see #getSpeed()
	 * @see #getMoveDirection()
	 */
	public double getVelocityY() {
		return velocityY;
	}
	
	/**
	 * <p>Returns the distance between the position of this {@link Entity} to the given point. 
	 * The returned distance is never negative.</p>
	 * <p>The distance from one point to itself is always 0.</p>
	 * @param x		the x-coordinate of the point
	 * @param y		the y-coordinate of the point
	 * @return		the distance
	 */
	public double getDistanceTo(double x, double y) {
		return Math.hypot(
				Math.abs(getX() - x), 
				Math.abs(getY() - y)
			);
	}
	
	/**
	 * Equal to:
	 * <pre>
	 * {@code getDistanceTo(other.getX(), other.getY());}
	 * </pre>
	 * @param other							a non-null Entity
	 * @throws IllegalArgumentException		if other is null
	 * @return								the distance from this Entity to the other Entity
	 */
	public double getDistanceTo(Entity other) {
		if (other == null) {
			throw new IllegalArgumentException("other == null");
		}
		return getDistanceTo(other.getX(), other.getY());
	}
	
	/**
	 * <p>Returns the angle in degrees from the position of this {@link Entity} to the given point. 
	 * The returned angle is always between 0 and 360.</p>
	 * <p>The angle from one point to itself is 0.</p>
	 * @param x		the x-coordinate of the point
	 * @param y		the y-coordinate of the point
	 * @return		an angle in degrees
	 */
	public double getAngleTo(double x, double y) {
		double result = Math.toDegrees(Math.atan2(y - getY(), x - getX()));
		if (result < 0) {
			result += 360;
		}
		return result;
	}
	
	/**
	 * Equal to:
	 * <pre>
	 * {@code getAngleTo(other.getX(), other.getY());}
	 * </pre>
	 * @param other							a non-null Entity
	 * @throws IllegalArgumentException		if other is null
	 * @return								an angle in degrees from this Entity to the other Entity
	 */
	public double getAngleTo(Entity other) {
		if (other == null) {
			throw new IllegalArgumentException("other == null");
		}
		return getAngleTo(other.getX(), other.getY());
	}
	
	/**
	 * Equal to:
	 * <pre>
	 * {@code setRotation(getAngleTo(x, y));}
	 * </pre>
	 * @param x		 			the x-coordinate of a point
	 * @param y		 			the y-coordinate of a point
	 */
	public void rotateTowards(double x, double y) {
		setRotation(getAngleTo(x, y));
	}
	
	/**
	 * Equal to:
	 * <pre>
	 * {@code rotateTowards(other.getX(), other.getY());}
	 * </pre>
	 * @param other							a non-null Entity
	 * @throws IllegalArgumentException		if other is null
	 */
	public void rotateTowards(Entity other) {
		if (other == null) {
			throw new IllegalArgumentException("other == null");
		}
		rotateTowards(other.getX(), other.getY());
	}
	
	/**
	 * Equal to:
	 * <pre>
	 * {@code moveDirectional(getAngleTo(x, y), speed);}
	 * </pre>
	 * @param x		 			the x-coordinate of a point
	 * @param y		 			the y-coordinate of a point
	 * @param speed 			the distance traveled per update cycle
	 */
	public void moveTowards(double x, double y, double speed) {
		setSpeedDirectional(getAngleTo(x, y), speed);
	}
	
	/**
	 * Equal to:
	 * <pre>
	 * {@code moveTowards(other.getX(), other.getY(), speed);}
	 * </pre>
	 * @param other							a non-null Entity
	 * @param speed 						the distance traveled per update cycle
	 * @throws IllegalArgumentException		if other is null
	 */
	public void moveTowards(Entity other, double speed) {
		if (other == null) {
			throw new IllegalArgumentException("other == null");
		}
		moveTowards(other.getX(), other.getY(), speed);
	}
	
	/**
	 * Equal to:
	 * <pre>
	 * {@code moveDirectional(getRotation(), speed);}
	 * </pre>
	 * @param speed 			the distance traveled per update cycle
	 */
	public void setSpeedForward(double speed) {
		setSpeedDirectional(getRotation(), speed);
	}
	
	/**
	 * Equal to:
	 * <pre>
	 * {@code moveDirectional(-getRotation(), speed);}
	 * </pre>
	 * @param speed 			the distance traveled per update cycle
	 */
	public void setSpeedBackwards(double speed) {
		setSpeedDirectional(-getRotation(), speed);
	}
	
	/**
	 * Equal to:
	 * <pre>
	 * <code>{@link #setVelocity(double, double) setVelocity}(
	 * 	getOffsetX(angleInDegree, speed), 
	 * 	getOffsetY(angleInDegree, speed)
	 * );</code>
	 * </pre>
	 * @param angleInDegree		an angle in degrees
	 * @param speed 			the distance traveled per update cycle
	 * @see #setVelocity(double, double)
	 */
	public void setSpeedDirectional(double angleInDegree, double speed) {
		double velX = getOffsetX(angleInDegree, speed);
		double velY = getOffsetY(angleInDegree, speed);
		setVelocity(velX, velY);
	}
	
	/**
	 * Equal to:
	 * <pre>
	 * {@code addMoveDirectional(getRotation(), speed);}
	 * </pre>
	 * @param speed 			the distance traveled per update cycle
	 */
	public void addSpeedForward(double speed) {
		addSpeedDirectional(getRotation(), speed);
	}
	
	/**
	 * Equal to:
	 * <pre>
	 * {@code addMoveDirectional(-getRotation(), speed);}
	 * </pre>
	 * @param speed 			the distance traveled per update cycle
	 */
	public void addSpeedBackwards(double speed) {
		addSpeedDirectional(-getRotation(), speed);
	}
	
	/**
	 * Equal to:
	 * <pre>
	 * {@code addVelocity(
	 * 	getOffsetX(angleInDegree, speed), 
	 * 	getOffsetY(angleInDegree, speed)
	 * );}
	 * </pre>
	 * <pre>
	 * Cheat Sheet:
	 * {@link #DEGREES_NORTH North}	:= 270°
	 * {@link #DEGREES_WEST West}	:= 180°
	 * {@link #DEGREES_SOUTH South}	:=  90°
	 * {@link #DEGREES_EAST East}	:=   0°
	 * </pre>
	 * @param angleInDegree		an angle in degrees
	 * @param speed 			the distance traveled per update cycle
	 */
	public void addSpeedDirectional(double angleInDegree, double speed) {
		double velX = getOffsetX(angleInDegree, speed);
		double velY = getOffsetY(angleInDegree, speed);
		addVelocity(velX, velY);
	}
	
	public void turnTowards(Entity other, double turnDistance) {
		turnTowards(getAngleTo(other), turnDistance);
	}
	
	public void turnTowards(double x, double y, double turnDistance) {
		turnTowards(x, y, turnDistance);
	}
	
	public void turnTowards(double targetAngle, double turnDistance) {
		double curAngle = getRotation();
		
		double diff = (targetAngle - curAngle + 360) % 360;
		double diffAbs = Math.abs(targetAngle - curAngle);
		if (diffAbs < turnDistance) {
			setRotation(targetAngle);
		} else if (diff < 180) {
			setRotation(curAngle + turnDistance);
		} else {
			setRotation(curAngle - turnDistance);
		}
	}
	
	/**
	 * Equal to:
	 * <pre>
	 * {@code setVelocity(getVelocityX() * factor, getVelocityY() * factor);}
	 * </pre>
	 * @param factor	any double value
	 */
	public void multiplyVelocity(double factor) {
		setVelocity(getVelocityX() * factor, getVelocityY() * factor);
	}
	
	/**
	 * <p>Returns the angle in degrees that this {@link Entity} is currently moving at.</p>
	 * <p>If this Entity is not {@link #isMoving() moving} the returned value will be 0.</p>
	 * <pre>
	 * Cheat Sheet:
	 * {@link #DEGREES_NORTH North}	:= 270°
	 * {@link #DEGREES_WEST West}	:= 180°
	 * {@link #DEGREES_SOUTH South}	:=  90°
	 * {@link #DEGREES_EAST East}	:=   0°
	 * </pre>
	 * @return							the distance this Entity is traveling per update cycle
	 * @throws IllegalStateException	if this Entity is not moving at all
	 * @see #isMoving()
	 * @see #getSpeed()
	 * @see #setVelocity(double, double)
	 * @see #getVelocityX()
	 * @see #getVelocityY()
	 */
	public double getMoveDirection() {
		if (!isMoving()) {
			throw new IllegalStateException("isMoving() == false");
		}
		double result = Math.toDegrees(Math.atan2(getVelocityY(), getVelocityX()));
		if (result < 0) {
			result += 360;
		}
		return result;
	}
	
	/**
	 * <p>Returns the distance this {@link Entity} will be moved by in the next update cycle.</p>
	 * <p>If this Entity is not {@link #isMoving() moving} the returned value will be 0.</p>
	 * @return	the distance this Entity is traveling per update cycle
	 * @see #isMoving()
	 * @see #getMoveDirection()
	 * @see #setVelocity(double, double)
	 * @see #getVelocityX()
	 * @see #getVelocityY()
	 */
	public double getSpeed() {
		return Math.hypot(Math.abs(getVelocityX()), Math.abs(getVelocityY()));
	}
	
	/**
	 * <p>Returns {@code true} if this {@link Entity} will be moved in the next update cycle of 
	 * the {@link Game}. An Entity is moved only if its {@link #getVelocityX() velocity on the 
	 * x-axis} and/or its {@link #getVelocityX() velocity on the y-axis} is not 0.</p>
	 * @return	true if either getVelocityX() and/or getVelocityY() is not 0
	 * @see #updatePositionByVelocity()
	 * @see #getSpeed()
	 * @see #getMoveDirection()
	 * @see #setVelocity(double, double)
	 * @see #getVelocityX()
	 * @see #getVelocityY()
	 */
	public boolean isMoving() {
		return getVelocityX() != 0 || getVelocityY() != 0;
	}
	
	/**
	 * <p>Adds the given {@link EntityObserver} to the observers of this {@link Entity}.</p>
	 * <p>Once added, an EntityObserver will be notified of any changes to the position, 
	 * size, rotation and velocity of this Entity.</p>
	 * <p>Observers can be added and removed at any time without a 
	 * {@link ConcurrentModificationException} being thrown. Observers can also be added to 
	 * multiple {@link Entity entities} at once but not more than once to the same Entity.</p>
	 * @param observer					a non-null EntityObserver
	 * @throws NullPointerException		if observer is null
	 * @see #removeObserver(EntityObserver)
	 */
	public void addObserver(EntityObserver observer) {
		if (observer == null) {
			throw new NullPointerException("observer == null");
		}
		observers.add(observer);
	}
	
	/**
	 * <p>Removes the given {@link EntityObserver} from the observers of this {@link Entity}.</p>
	 * <p>After it has been removed, the EntityObserver will no longer be notified of any 
	 * changes to this Entity.</p>
	 * <p>Observers can be added and removed at any time without a 
	 * {@link ConcurrentModificationException} being thrown. Observers can also be added to 
	 * multiple {@link Entity entities} at once but not more than once to the same Entity.</p>
	 * @param observer					a non-null EntityObserver
	 * @throws NullPointerException		if observer is null
	 * @see #addObserver(EntityObserver)
	 */
	public void removeObserver(EntityObserver observer) {
		if (observer == null) {
			throw new NullPointerException("observer == null");
		}
		observers.remove(observer);
	}
	
	/**
	 * Used internally. Calls the {@link EntityObserver#onPositionChanged(Entity)} method for 
	 * all {@link EntityObserver observers} in {@link #observers} with {@code this} as the 
	 * argument.
	 * @see #setPosition(double, double)
	 * @see #updatePositionByVelocity()
	 */
	private void firePositionChangedEvent() {
		observers.forEach((obs) -> obs.onPositionChanged(this));
	}
	
	/**
	 * Used internally. Calls the {@link EntityObserver#onSizeChanged(Entity)} method for 
	 * all {@link EntityObserver observers} in {@link #observers} with {@code this} as the 
	 * argument.
	 * @see #setSize(int)
	 */
	private void fireSizeChangedEvent() {
		observers.forEach((obs) -> obs.onSizeChanged(this));
	}
	
	/**
	 * Used internally. Calls the {@link EntityObserver#onRotationChanged(Entity)} method for 
	 * all {@link EntityObserver observers} in {@link #observers} with {@code this} as the 
	 * argument.
	 * @see #setRotation(double)
	 */
	private void fireRotationChangedEvent() {
		observers.forEach((obs) -> obs.onRotationChanged(this));
	}
	
	/**
	 * Used internally. Calls the {@link EntityObserver#onVelocityChanged(Entity)} method for 
	 * all {@link EntityObserver observers} in {@link #observers} with {@code this} as the 
	 * argument.
	 * @see #setVelocity(double, double)
	 */
	private void fireVelocityChangedEvent() {
		observers.forEach((obs) -> obs.onVelocityChanged(this));
	}
	
	private void fireAddedToGameEvent() {
		Game game = getCurrentGame();
		observers.forEach((obs) -> obs.onAddedToGame(game, this));
	}
	
	private void fireRemovedFromGameEvent(Game game) {
		observers.forEach((obs) -> obs.onRemovedFromGame(game, this));
	}
	
	/**
	 * <p>Returns the unique identification number of this {@link Entity}. The returned 
	 * number is unique for this Entity. No other Entity created within the same JVM will 
	 * have the same number.</p>
	 * <p>The number returned by this method will never change within the life cycle of 
	 * this object.</p>
	 * @return	a unique number for this Entity
	 */
	public final int getID() {
		return id;
	}
	
	/**
	 * <p>The hash code of an Entity is based on the {@link #getID() ID}. It is thus 
	 * guaranteed to be unique for each Entity.</p>
	 */
	public int hashCode() {
		return getID();
	}
	
	/**
	 * <p>Two Entities are considered equal when they have the same {@link #getID() ID}.</p>
	 * <p>Since there can never be two Entities within the same JVM with the same ID this 
	 * method will only return true if {@code obj == this}.</p>
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || !(obj instanceof Entity)) {
			return false;
		}
		return getID() == ((Entity) obj).getID();
	}
	
	/**
	 * <p>Returns the {@link Class#getSimpleName() simple name} of this {@link Entity Entities} 
	 * class, followed by the {@link #getID() ID} within brackets.</p>
	 * Examples: 
	 * <pre>
	 * {@code Player[42]}
	 * {@code Bullet[815]}
	 * </pre>
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("[");
		sb.append(id);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * <p>Calculates the x-coordinate of a point {@code distance} away at an angle of 
	 * {@code angleInDegrees}. The angle is given in degrees. Any values are valid for 
	 * {@code distance} and {@code angleInDegrees}. Negative angles, or angles above 
	 * 360° will automatically be converted.</p>
	 * <pre>
	 * Cheat Sheet:
	 * {@link #DEGREES_NORTH North}	:= 270°
	 * {@link #DEGREES_WEST West}	:= 180°
	 * {@link #DEGREES_SOUTH South}	:=  90°
	 * {@link #DEGREES_EAST East}	:=   0°
	 * </pre>
	 * @param angleInDegree		an angle measured in degrees. An angle of 0° is to the right
	 * @param distance			any distance
	 * @return					The x-coordinate of the offset point
	 * @see #getOffsetY(double, double)
	 */
	public static double getOffsetX(double angleInDegree, double distance) {
		return distance * Math.cos(Math.toRadians(angleInDegree));
	}
	
	/**
	 * <p>Calculates the y-coordinate of a point {@code distance} away at an angle of 
	 * {@code angleInDegrees}. The angle is given in degrees. Any values are valid for 
	 * {@code distance} and {@code angleInDegrees}. Negative angles, or angles above 
	 * 360° will automatically be converted.</p>
	 * <pre>
	 * Cheat Sheet:
	 * {@link #DEGREES_NORTH North}	:= 270°
	 * {@link #DEGREES_WEST West}	:= 180°
	 * {@link #DEGREES_SOUTH South}	:=  90°
	 * {@link #DEGREES_EAST East}	:=   0°
	 * </pre>
	 * @param angleInDegree		an angle measured in degrees. An angle of 0° is to the right
	 * @param distance			any distance
	 * @return					The y-coordinate of the offset point
	 * @see #getOffsetX(double, double)
	 */
	public static double getOffsetY(double angleInDegree, double distance) {
		return distance * Math.sin(Math.toRadians(angleInDegree));
	}
	
}