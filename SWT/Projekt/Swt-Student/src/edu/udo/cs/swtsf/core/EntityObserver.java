package edu.udo.cs.swtsf.core;

import java.util.ConcurrentModificationException;

import edu.udo.cs.swtsf.core.player.PlayerObserver;

/**
 * <p>An EntityObserver can be registered at any number of {@link Entity} 
 * objects. The Entity to which an EntityObserver is registered is called the 
 * host. Once registered the EntityObserver will immediately be notified of any 
 * changes to the position, size, rotation or velocity of the host Entity.<br>
 * An Entity can have any number of EntityObservers registered at the same time. 
 * No assumptions should be made about the order in which EntityObservers will 
 * be notified of the changes to the host.</p>
 * 
 * <p>EntityObservers can be added to and removed from a host during a notification 
 * without throwing any {@link ConcurrentModificationException 
 * ConcurrentModificationExceptions}.</p>
 * 
 * <p>As the name suggests, this interface is an implementation of the observer pattern.</p>
 * 
 * @see Entity#addObserver(EntityObserver)
 * @see Entity#removeObserver(EntityObserver)
 * @see PlayerObserver
 * @see GameObserver
 */
public interface EntityObserver {
	
	/**
	 * <p>Called immediately by the host {@link Entity} after the position of the 
	 * host has changed. The current position can be queried with the 
	 * {@link Entity#getX()} and {@link Entity#getY()} methods.</p>
	 * 
	 * <p>This method is a default method with an empty body. An implementing 
	 * class does not have to implement this method if it is not interested in 
	 * being notified of position changes. An implementation of this method 
	 * does not need to call super.</p>
	 * 
	 * @param host	the host of the observer. Guaranteed to not be null.
	 * @see Entity#setPosition(double, double)
	 * @see Entity#getX()
	 * @see Entity#getY()
	 */
	public default void onPositionChanged(Entity host) {}
	
	/**
	 * <p>Called immediately by the host {@link Entity} after the size of the 
	 * host has changed. The current size can be queried with the 
	 * {@link Entity#getSize()} method.</p>
	 * 
	 * <p>This method is a default method with an empty body. An implementing 
	 * class does not have to implement this method if it is not interested in 
	 * being notified of size changes. An implementation of this method 
	 * does not need to call super.</p>
	 * 
	 * @param host	the host of the observer. Guaranteed to not be null.
	 * @see Entity#setSize(int)
	 * @see Entity#getSize()
	 */
	public default void onSizeChanged(Entity host) {}
	
	/**
	 * <p>Called immediately by the host {@link Entity} after the rotation of the 
	 * host has changed. The current rotation can be queried with the 
	 * {@link Entity#getRotation()} method.</p>
	 * 
	 * <p>This method is a default method with an empty body. An implementing 
	 * class does not have to implement this method if it is not interested in 
	 * being notified of rotation changes. An implementation of this method 
	 * does not need to call super.</p>
	 * 
	 * @param host	the host of the observer. Guaranteed to not be null.
	 * @see Entity#setRotation(double)
	 * @see Entity#getRotation()
	 */
	public default void onRotationChanged(Entity host) {}
	
	/**
	 * <p>Called immediately by the host {@link Entity} after the velocity of the 
	 * host has changed. The current velocity can be queried with the 
	 * {@link Entity#getVelocityX()} and {@link Entity#getVelocityY()} methods.</p>
	 * 
	 * <p>This method is a default method with an empty body. An implementing 
	 * class does not have to implement this method if it is not interested in 
	 * being notified of velocity changes. An implementation of this method 
	 * does not need to call super.</p>
	 * 
	 * @param host	the host of the observer. Guaranteed to not be null.
	 * @see Entity#setVelocity(double, double)
	 * @see Entity#getVelocityX()
	 * @see Entity#getVelocityY()
	 */
	public default void onVelocityChanged(Entity host) {}
	
	/**
	 * <p>Called by the host {@link Entity} after the entity has been added 
	 * to a {@link Game}. This method is never going to be called twice without 
	 * a call to {@link #onRemovedFromGame(Game, Entity)} in between.</p>
	 * 
	 * <p>This method is a default method with an empty body. An implementing 
	 * class does not have to implement this method if it is not interested in 
	 * being notified when the Entity has been added to a game. An 
	 * implementation of this method does not need to call super.</p>
	 * 
	 * @param game	the game to which the host was just added. Guaranteed to not be null.
	 * @param host	the host of the observer. Guaranteed to not be null.
	 * @see Game#addEntity(Entity)
	 * @see GameObserver#onEntityAdded(Game, Entity)
	 * @see GameObserver#onEntityRemoved(Game, Entity)
	 * @see Game#addObserver(GameObserver)
	 * @see Entity#dispose()
	 * @see Entity#isDisposed()
	 */
	public default void onAddedToGame(Game game, Entity host) {}
	
	/**
	 * <p>Called by the host {@link Entity} after the entity has been removed 
	 * from a {@link Game}. This method is never going to be called twice without 
	 * a call to {@link #onAddedToGame(Game, Entity)} in between. Nor is it going 
	 * to be called without a prior call to {@link #onAddedToGame(Game, Entity)}.</p>
	 * 
	 * <p>This method is a default method with an empty body. An implementing 
	 * class does not have to implement this method if it is not interested in 
	 * being notified when the Entity has been added to a game. An 
	 * implementation of this method does not need to call super.</p>
	 * 
	 * @param game	the game which the host was just removed from. Guaranteed to not be null.
	 * @param host	the host of the observer. Guaranteed to not be null.
	 * @see Game#addEntity(Entity)
	 * @see GameObserver#onEntityAdded(Game, Entity)
	 * @see GameObserver#onEntityRemoved(Game, Entity)
	 * @see Game#addObserver(GameObserver)
	 * @see Entity#dispose()
	 * @see Entity#isDisposed()
	 */
	public default void onRemovedFromGame(Game game, Entity host) {}
	
}