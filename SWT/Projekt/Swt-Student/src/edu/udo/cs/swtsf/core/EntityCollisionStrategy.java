package edu.udo.cs.swtsf.core;

import java.util.ConcurrentModificationException;

/**
 * <p>{@link EntityCollisionStrategy EntityCollisionStrategy's} can be used to react to 
 * the collision between two {@link Entity Entities}. An EntityCollisionStrategy 
 * can be added to an Entity by calling the 
 * {@link Entity#addCollisionStrategy(EntityCollisionStrategy)} method. If a 
 * strategy is added to an Entity this entity will be called the host of 
 * the strategy. The host will then call the 
 * {@link EntityCollisionStrategy#onCollisionWith(Entity, Entity)} 
 * method for every other Entity with which it collided. The first argument to the 
 * onCollisionWith method will be the host. The second argument will be the other 
 * entity that has collided with the host.</p>
 * 
 * <p>An EntityCollisionStrategy can be added to any number of Entity objects at once. 
 * its {@link EntityCollisionStrategy#onCollisionWith(Entity, Entity)} method will 
 * be called by each host separately when a collision occurs.<br>
 * EntityCollisionStrategys can be added to and removed from a host during a 
 * collision without throwing any {@link ConcurrentModificationException 
 * ConcurrentModificationExceptions}.</p>
 * 
 * <p>As the name suggests, this interface is an implementation of the strategy pattern.</p>
 * 
 * @see Entity#addCollisionStrategy(EntityCollisionStrategy)
 * @see Entity#removeCollisionStrategy(EntityCollisionStrategy)
 */
public interface EntityCollisionStrategy {
	
	/**
	 * <p>Called by an {@link Entity} every time it collided with another Entity. 
	 * The first argument will always be the host that this EntityCollisionStrategy 
	 * was added to. The second argument will be the Entity which collided with the 
	 * host. This method will be called exactly once for each other Entity which is 
	 * currently colliding with the host in every update cycle.</p>
	 * 
	 * @param host		the host of the EntityCollisionStrategy. Guaranteed to not be null.
	 * @param other		the other Entity that has collided with the host. Guaranteed to not be null.
	 * @see Entity#updatePositionByVelocity()
	 * @see Game#update()
	 */
	public void onCollisionWith(Entity host, Entity other);
	
}