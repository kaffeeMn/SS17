package edu.udo.cs.swtsf.view;

import edu.udo.cs.swtsf.core.Entity;

/***
 * <p>Instances of this class represent factory objects which create 
 * {@link GraphicalElement GraphicalElement's} for {@link Entity game Entities}.</p>
 * <p>GraphicalElementFactory's should be created at game start and registered 
 * at the {@link ViewManager} via the {@link ViewManager#setFactoryForClass(Class, 
 * GraphicalElementFactory) setFactoryForClass} method. A GraphicalElementFactory 
 * registered in this way will be responsible for creating the GraphicalElements of 
 * all Entities of that class.</p>
 * <p>This interface is an implementation of the Factory-Object pattern.</p>
 * @see ViewManager
 * @see ViewManager#setFactoryForClass(Class, GraphicalElementFactory)
 * @see ViewManager#getFactoryForClass(Class)
 * @see #createForEntity(ViewManager, Entity)
 * @see ViewManager#newSprite()
 * @see ViewManager#newSpriteSet()
 * @see ViewManager#newEntitySprite(Entity)
 * @see ViewManager#newEntitySpriteSet(Entity)
 */
@FunctionalInterface
public interface GraphicalElementFactory {
	
	/**
	 * <p>Creates a new {@link GraphicalElement} for the given {@link Entity} and 
	 * returns it. The element is created by one of the factory methods of the 
	 * {@link ViewManager}.</p>
	 * <p>This method may return {@code null} in which case the Entity will not 
	 * get any graphical representation on screen.</p> 
	 * @param view						a non-null ViewManager
	 * @param entity					a non-null Entity
	 * @return							a new instance of GraphicalElement or null
	 * @see ViewManager#newSprite()
	 * @see ViewManager#newSpriteSet()
	 * @see ViewManager#newEntitySprite(Entity)
	 * @see ViewManager#newEntitySpriteSet(Entity)
	 */
	public GraphicalElement createForEntity(ViewManager view, Entity entity);
	
}