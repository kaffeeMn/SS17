package edu.udo.cs.swtsf.core;

import java.util.ConcurrentModificationException;

import edu.udo.cs.swtsf.core.player.PlayerObserver;

/**
 * <p>GameObserver's can be registered at {@link Game} instances. Once registered 
 * the GameObserver will be notified of any {@link Entity Entities} which are 
 * added to or removed from the Game.</p>
 * 
 * <p>A Game can have any number of GameObserver's registered at the same time. 
 * No assumptions should be made about the order in which GameObserver's will 
 * be notified of the changes to the Game.</p>
 * 
 * <p>GameObserver's can be added to and removed from the Game during a notification 
 * without throwing any {@link ConcurrentModificationException 
 * ConcurrentModificationExceptions}.</p>
 * 
 * <p>As the name suggests, this interface is an implementation of the observer pattern.</p>
 * 
 * @see Game#addObserver(GameObserver)
 * @see Game#removeObserver(GameObserver)
 * @see EntityObserver
 * @see PlayerObserver
 */
public interface GameObserver {
	
	/**
	 * <p>Called immediately by the {@link Game} after an {@link Entity} was added 
	 * to it. The first argument will always be the Game to which the Entity was 
	 * added to. The second argument will be the Entity which was added.</p>
	 * 
	 * <p>This method is a default method with an empty body. An implementing 
	 * class does not have to implement this method if it is not interested in 
	 * being notified when an Entity is added. An implementation of this method 
	 * does not need to call super.</p>
	 * 
	 * @param game			the Game to which the Entity was added. This is never null
	 * @param entity		the Entity which was added. This is never null
	 * @see Game#addEntity(Entity)
	 */
	public default void onEntityAdded(Game game, Entity entity) {}
	
	/**
	 * <p>Called immediately by the {@link Game} after an {@link Entity} was removed 
	 * from it. The first argument will always be the Game which the Entity was 
	 * removed from. The second argument will be the Entity which was removed.</p>
	 * 
	 * <p>This method is a default method with an empty body. An implementing 
	 * class does not have to implement this method if it is not interested in 
	 * being notified when an Entity is removed. An implementation of this method 
	 * does not need to call super.</p>
	 * 
	 * @param game			the Game from which the Entity was removed. This is never null
	 * @param entity		the Entity which was removed. This is never null
	 * @see Entity#dispose()
	 * @see Entity#isDisposed()
	 */
	public default void onEntityRemoved(Game game, Entity entity) {}
	
}