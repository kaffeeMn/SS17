package edu.udo.cs.swtsf.core;

import java.util.ConcurrentModificationException;

/**
 * <p>{@link EntityBehaviorStrategy EntityBehaviorStrategy's} are used to control 
 * the actions and the behavior of {@link Entity} objects. An EntityBehaviorStrategy 
 * can be added to an Entity by calling the 
 * {@link Entity#addBehaviorStrategy(EntityBehaviorStrategy)} method. If a 
 * strategy is added to an Entity this entity will be called the host of 
 * the strategy. In every update cycle of the game the host will call the 
 * {@link EntityBehaviorStrategy#act(Entity)} method of all of its EntityBehaviorStrategy's 
 * with itself as argument.</p>
 * 
 * <p>The EntityBehaviorStrategy has access to the current {@link Game} 
 * instance via the {@link Entity#getCurrentGame()} method. The strategy can also 
 * use the {@link Entity#setVelocity(double, double)} method to control the 
 * movement of the host entity.<br>
 * An EntityBehaviorStrategy can be added to any number of Entity objects at once. 
 * its {@link EntityBehaviorStrategy#act(Entity)} method will be called by each host 
 * separately.<br>
 * EntityBehaviorStrategys can be added to and removed from a host during an update 
 * cycle without throwing any {@link ConcurrentModificationException 
 * ConcurrentModificationExceptions}.</p>
 * 
 * <p>As the name suggests, this interface is an implementation of the strategy pattern.</p>
 * 
 * @see Entity#addBehaviorStrategy(EntityBehaviorStrategy)
 * @see Entity#removeBehaviorStrategy(EntityBehaviorStrategy)
 */
public interface EntityBehaviorStrategy {
	
	/**
	 * <p>This method is called once per update cycle by all host {@link Entity Entities}.</p>
	 * @param host		the host entity that called this method. Guaranteed to not be null.
	 * @see Entity#updatePositionByVelocity()
	 * @see Game#update()
	 */
	public void act(Entity host);
	
}