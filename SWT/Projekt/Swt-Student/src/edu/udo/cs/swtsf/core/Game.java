package edu.udo.cs.swtsf.core;

import java.util.ConcurrentModificationException;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.udo.cs.swtsf.core.player.Player;
import edu.udo.cs.swtsf.util.Group;
import edu.udo.cs.swtsf.util.GroupFactory;
import edu.udo.cs.swtsf.view.ViewManager;

/**
 * <p>The {@link Game} contains all game objects, called {@link Entity Entities}, as 
 * well as {@link GameScript scripts} that control the flow of the game.</p>
 * <p>The {@link Game} is responsible for updating all game objects and scripts, to 
 * check for collisions between game objects, to expose the current 
 * {@link GameKey player inputs} and to relay information about the game via 
 * {@link GameObserver GameObserver's}.</p>
 * <p>There should only be a single instance of {@link Game} per game being played.</p>
 */
public class Game {
	
	/**
	 * Contains all {@link GameObserver observers}.
	 * @see #addObserver(GameObserver)
	 * @see #removeObserver(GameObserver)
	 * @see #fireEntityAddedEvent(Entity)
	 * @see #fireEntityRemovedEvent(Entity)
	 */
	private final Group<GameObserver> observerList = GroupFactory.get().createNewGroup();
	/**
	 * Contains all {@link GameScript scripts} that control the game logic.
	 * @see #addScript(GameScript)
	 * @see #removeScript(GameScript)
	 * @see #update()
	 */
	private final Group<GameScript> scripts = GroupFactory.get().createNewGroup();
	/**
	 * Contains all game {@link Entity entities}.
	 * @see #addEntity(Entity)
	 * @see #update()
	 */
	private final Group<Entity> entities = GroupFactory.get().createNewGroup();
	/**
	 * Contains the keys that were pressed for the current update cycle.
	 * @see #bufferKey(GameKey)
	 * @see #update()
	 */
	private final Set<GameKey> pressedKeys = EnumSet.noneOf(GameKey.class);
	/**
	 * A reference to the current {@link Player} entity. This is initialized once 
	 * to a non-null value and never changed.
	 */
	private final Player player;
	private double mouseX;
	private double mouseY;
	
	/*
	 * Creates a new Player object and adds it as an Entity to this Game.
	 */
	public Game(Supplier<? extends Player> playerFactory) {
		player = playerFactory == null ? new Player() : playerFactory.get();
		addEntity(player);
	}
	
	/**
	 * <p>Returns the instance of {@link Player} that represents the players game entity.</p>
	 * <p>This method will never return null.</p>
	 * @return a non-null Player instance
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * <p>Called by the {@link ViewManager} to set the current mouse coordinates. This method 
	 * should be called exactly once before each update cycle begins.</p>
	 * @param x		the new X-coordinate of the mouse as will be returned by {@link #getMouseX()}
	 * @param y		the new Y-coordinate of the mouse as will be returned by {@link #getMouseY()}
	 * @see #getMouseX()
	 * @see #getMouseY()
	 * @see #bufferKey(GameKey)
	 * @see #isPressed(GameKey)
	 * @see #update()
	 */
	public void setMousePosition(double x, double y) {
		mouseX = x;
		mouseY = y;
	}
	
	/**
	 * <p>Returns the current X-coordinate of the mouse. This property should be set by the 
	 * {@link ViewManager} before each update cycle.</p>
	 * <p>This value will be 0 iff the mouse is in the top left of the game window. It will be 
	 * negative if the mouse is outside of the game window to the left. The X-coordinate grows 
	 * as the mouse moves to the right.</p>
	 * @return		the current mouse X-coordinate
	 * @see #setMousePosition(double, double)
	 * @see #getMouseY()
	 */
	public double getMouseX() {
		return mouseX;
	}
	
	/**
	 * <p>Returns the current Y-coordinate of the mouse. This property should be set by the 
	 * {@link ViewManager} before each update cycle.</p>
	 * <p>This value will be 0 iff the mouse is in the top left of the game window. It will be 
	 * negative if the mouse is above the game window. The Y-coordinate grows as the mouse 
	 * moves to the bottom.</p>
	 * @return		the current mouse Y-coordinate
	 * @see #setMousePosition(double, double)
	 * @see #getMouseX()
	 */
	public double getMouseY() {
		return mouseY;
	}
	
	/**
	 * <p>Called by the {@link ViewManager} to add a {@link GameKey} to the key buffer of the 
	 * game. The key buffer contains all the GameKey's that have been pressed in between two 
	 * update cycles of the game. At the end of all update cycles the key buffer will be cleared 
	 * and the game will await the next key presses for the next update cycle.</p>
	 * <p>This method should only be called by the ViewManager in between two update cycles.</p>
	 * @param key							the key that was pressed. This should not be null.
	 * @throws IllegalArgumentException		if key is null
	 * @see #isPressed(GameKey)
	 * @see #update()
	 */
	public void bufferKey(GameKey key) {
		if (key == null) {
			throw new IllegalArgumentException("key == null");
		}
		pressedKeys.add(key);
	}
	
	/**
	 * <p>Returns true if the given {@link GameKey} is pressed during this update cycle. 
	 * Otherwise returns false.</p>
	 * @param key							the non-null GameKey to check for.
	 * @throws IllegalArgumentException		if key is null.
	 * @return								true if key is currently pressed; otherwise false.
	 * @see #bufferKey(GameKey)
	 * @see #update()
	 */
	public boolean isPressed(GameKey key) {
		if (key == null) {
			throw new IllegalArgumentException("key == null");
		}
		return pressedKeys.contains(key);
	}
	
	/**
	 * Performs a single update cycle of the game. This will update all 
	 * {@link GameScript game scripts}, the {@link EntityBehaviorStrategy behaviors} 
	 * and the movement of each {@link Entity}, and check for collisions between 
	 * {@link Entity entities}.<br>
	 * After an update cycle all buffered keys are cleared.<br>
	 * @see #areOverlapping(Entity, Entity)
	 * @see #bufferKey(GameKey)
	 */
	public void update() {
		// Update all GameScript's
		scripts.forEach((script) -> script.onUpdate(this));
		
		// Check for collisions between all entities
		entities.forEachTuple((a, b) -> {
			if (!a.hasCollisionStrategies() && !b.hasCollisionStrategies()) {
				return;
			}
			boolean aWasDisposed = a.isDisposed();
			boolean bWasDisposed = b.isDisposed();
			
			if (areOverlapping(a, b)) {
				a.onCollision(b);
				b.onCollision(a);
			}
			if (!aWasDisposed && a.isDisposed()) {
				entities.remove(a);
				a.setCurrentGame(null);
				fireEntityRemovedEvent(a);
			}
			if (!bWasDisposed && b.isDisposed()) {
				entities.remove(b);
				b.setCurrentGame(null);
				fireEntityRemovedEvent(b);
			}
		});
		
		// Update all entities
		entities.forEach((entity) -> {
			entity.updatePositionByVelocity();
			if (!entity.hasBehaviorStrategies()) {
				return;
			}
			entity.updateBehaviors();
			if (entity.isDisposed()) {
				entities.remove(entity);
				entity.setCurrentGame(null);
				fireEntityRemovedEvent(entity);
			}
		});
		
		// Clear pressed keys for next cycle
		pressedKeys.clear();
	}
	
	/**
	 * Returns true if the {@link Entity entities} {@code a} and {@code b} overlap 
	 * with their diameters. Otherwise returns false.
	 * @param a							a non-null entity
	 * @param b							a non-null entity
	 * @throws NullPointerException		if either {@code a} or {@code b} are null
	 * @return							true if the diameters of {@code a} and {@code b} overlap, else false
	 */
	public boolean areOverlapping(Entity a, Entity b) {
		if (a == null) {
			throw new NullPointerException("a == null");
		}
		if (b == null) {
			throw new NullPointerException("b == null");
		}
		double aX = a.getX();
		double aY = a.getY();
		double bX = b.getX();
		double bY = b.getY();
		double distanceX = Math.abs(aX - bX);
		double distanceY = Math.abs(aY - bY);
		double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
		double radius = a.getSize() / 2 + b.getSize() / 2;
		return distance <= radius;
	}
	
	/**
	 * Adds the given {@link Entity} to this game. If an {@link Entity} was added to 
	 * the game it will automatically be updated in every update cycle of the game. 
	 * It will also be considered for collision detection and can be iterated over 
	 * in range queries.<br>
	 * An {@link Entity} will automatically be removed from the game when it is 
	 * disposed during or before an update cycle of the game.<br>
	 * After an {@link Entity} was added to the game the 
	 * {@link GameObserver#onEntityAdded(Game, Entity)} method will be called on all 
	 * {@link GameObserver observers} currently registered at the game instance.<br>
	 * 
	 * @param entity					the entity that is being added
	 * @throws NullPointerException		if entity is null
	 * @throws IllegalStateException	if entity already belongs to a different game, 
	 * 									or if entity was already added to this game before
	 * @see #update()
	 * @see GameObserver
	 * @see #addObserver(GameObserver)
	 * @see #removeObserver(GameObserver)
	 * @see GameObserver#onEntityAdded(Game, Entity)
	 * @see GameObserver#onEntityRemoved(Game, Entity)
	 */
	public void addEntity(Entity entity) {
		if (entity == null) {
			throw new NullPointerException("entity == null");
		}
		if (entity.getCurrentGame() != null) {
			throw new IllegalStateException(
					entity+".getCurrentGame() == "+entity.getCurrentGame());
		}
		entity.setCurrentGame(this);
		// If the Entity is already disposed before its first update it 
		// will never be added to the Entities group
		if (entity.isDisposed()) {
			fireEntityAddedEvent(entity);
			entity.setCurrentGame(null);
			fireEntityRemovedEvent(entity);
		} else {
			entities.add(entity);
			fireEntityAddedEvent(entity);
		}
	}
	
	/**
	 * <p>Returns a new {@link EntityStream} originally containing all 
	 * {@link Entity Entities} in the game which are not {@link Entity#isDisposed() 
	 * disposed}. The EntityStream can be used to iterate over all Entities or 
	 * over a subset of Entities based on various filters.</p>
	 * @return		a new non-null {@link EntityStream}
	 * @see EntityStream
	 * @see EntityStream#forEach(Consumer)
	 */
	public EntityStream<Entity> getAllEntities() {
		return new EntityStream<Entity>() {
			public void forEach(Consumer<Entity> action) {
				entities.forEach(e -> {
					if (!e.isDisposed()) {
						action.accept(e);
					}
				});
			}
		};
	}
	
	/**
	 * <p>Adds a {@link GameScript script} to this game. A GameScript is code which 
	 * is executed once in every update cycle. You can use GameScripts for any code 
	 * which is not tied to individual {@link Entity Entities} but common to the 
	 * entire game.</p>
	 * @param script					a non-null instance of GameScript
	 * @throws IllegalStateException	if script was already added to this game before
	 * @see GameScript
	 * @see GameScript#onUpdate(Game)
	 * @see #update()
	 */
	public void addScript(GameScript script) {
		if (scripts.contains(script)) {
			throw new IllegalStateException("GameScript ("+script+") was added twice to the same game.");
		}
		scripts.add(script);
	}
	
	/**
	 * <p>Removes a {@link GameScript script} from this game. A removed GameScript will 
	 * no longer be executed but may be added to the game again at a later point in time. 
	 * At this point execution will continue.</p>
	 * @param script					a non-null instance of GameScript
	 * @throws IllegalStateException	if script wasn't added to this game before
	 * @see GameScript
	 * @see GameScript#onUpdate(Game)
	 * @see #update()
	 */
	public void removeScript(GameScript script) {
		if (!scripts.contains(script)) {
			throw new IllegalStateException("GameScript ("+script+") could not be removed from game. It was not added before.");
		}
		scripts.remove(script);
	}
	
	/**
	 * <p>Adds a {@link GameObserver} to this game. A GameObserver will be notified 
	 * every time an {@link Entity} has been added to or removed from this game.</p>
	 * <p>Observers can be added and removed at any time without a 
	 * {@link ConcurrentModificationException} being thrown.</p>
	 * @param observer		a non-null GameObserver
	 * @see #removeObserver(GameObserver)
	 * @see GameObserver#onEntityAdded(Game, Entity)
	 * @see GameObserver#onEntityRemoved(Game, Entity)
	 */
	public void addObserver(GameObserver observer) {
		observerList.add(observer);
	}
	
	/**
	 * <p>Removes a {@link GameObserver} from this game. The observer will no longer 
	 * be notified of any events.</p>
	 * <p>Observers can be added and removed at any time without a 
	 * {@link ConcurrentModificationException} being thrown.</p>
	 * @param observer		a non-null GameObserver
	 * @see #addObserver(GameObserver)
	 * @see GameObserver#onEntityAdded(Game, Entity)
	 * @see GameObserver#onEntityRemoved(Game, Entity)
	 */
	public void removeObserver(GameObserver observer) {
		observerList.remove(observer);
	}
	
	/**
	 * <p>Notifies all {@link GameObserver observers} of the recently added {@link Entity}.</p>
	 * <p>This method is supposed to be used only internally.</p>
	 * @param entity	a non-null Entity which has just been added
	 */
	private void fireEntityAddedEvent(Entity entity) {
		observerList.forEach((obs) -> obs.onEntityAdded(this, entity));
	}
	
	/**
	 * <p>Notifies all {@link GameObserver observers} of the recently removed {@link Entity}.</p>
	 * <p>This method is supposed to be used only internally.</p>
	 * @param entity	a non-null Entity which has just been removed
	 */
	private void fireEntityRemovedEvent(Entity entity) {
		observerList.forEach((obs) -> obs.onEntityRemoved(this, entity));
	}
	
}