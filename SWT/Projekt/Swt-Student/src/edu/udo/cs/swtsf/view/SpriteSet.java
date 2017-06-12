package edu.udo.cs.swtsf.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>A SpriteSet is an inner node in the scene graph and used to structure other 
 * {@link GraphicalElement graphical elements} into groups. GraphicalElements can 
 * be {@link #add(GraphicalElement) added} to and {@link #remove(GraphicalElement) removed} 
 * from the SpriteSet and all current children can be queried via the 
 * {@link #getChildren()} method.</p>
 * <p>A SpriteSet does not have any visible representation on screen. To show something 
 * visible on screen use {@link Sprite Sprites}</p>
 */
public abstract class SpriteSet extends GraphicalElement {
	
	/**
	 * This List contains all children of this SpriteSet in the order 
	 * in which they were added.
	 * The ordering of the List is important because children will be 
	 * painted in the same order. Children which were added later will 
	 * be painted on top of children which were added earlier.
	 */
	protected final List<GraphicalElement> children = new ArrayList<>();
	
	/**
	 * Does nothing.
	 */
	protected SpriteSet() {
		super();
	}
	
	/**
	 * <p>Calls super and {@link GraphicalElement#setViewManager(ViewManager) sets} 
	 * the {@link ViewManager} for all {@link #getChildren() children} of this 
	 * SpriteSet.</p>
	 * @see #getChildren()
	 */
	protected void setViewManager(ViewManager viewManager) {
		super.setViewManager(viewManager);
		for (GraphicalElement node : children) {
			node.setViewManager(viewManager);
		}
	}
	
	/**
	 * <p>Adds the given {@link GraphicalElement} to the children of 
	 * this SpriteSet.</p>
	 * <p>This method is used internally and called from within the 
	 * {@link GraphicalElement#setParent(SpriteSet)} method and should not be 
	 * called from anywhere else.</p>
	 * @param element						a non-null GraphicalElement which has 
	 * 										this SpriteSet as its parent
	 * @throws IllegalArgumentException		if element is null, 
	 * 										if this SpriteSet is not the parent of element
	 * 										or if element was already added before
	 * @see GraphicalElement#setParent(SpriteSet)
	 * @see GraphicalElement#getParent()
	 * @see #remove(GraphicalElement)
	 * @see #getChildren()
	 */
	protected void add(GraphicalElement element) {
		if (element == null) {
			throw new IllegalArgumentException("element == null");
		}
		if (element.getParent() != this) {
			throw new IllegalArgumentException("element.getParent != this");
		}
		if (!children.add(element)) {
			throw new IllegalArgumentException("children.contains(element) == true");
		}
		element.setViewManager(getViewManager());
	}
	
	/**
	 * <p>Removes the given {@link GraphicalElement} from the children of 
	 * this SpriteSet.</p>
	 * <p>This method is used internally and called from within the 
	 * {@link GraphicalElement#setParent(SpriteSet)} method and should not be 
	 * called from anywhere else.</p>
	 * @param element						a non-null GraphicalElement which has 
	 * 										this SpriteSet as its parent
	 * @throws IllegalArgumentException		if element is null, 
	 * 										if this SpriteSet is not the parent of element
	 * 										or if element was not added before
	 * @see GraphicalElement#setParent(SpriteSet)
	 * @see GraphicalElement#getParent()
	 * @see #add(GraphicalElement)
	 * @see #getChildren()
	 */
	protected void remove(GraphicalElement element) {
		if (element == null) {
			throw new IllegalArgumentException("element == null");
		}
		if (element.getParent() != this) {
			throw new IllegalArgumentException("element.getParent != this");
		}
		if (!children.remove(element)) {
			throw new IllegalArgumentException("children.contains(element) == false");
		}
		element.setViewManager(null);
	}
	
	/**
	 * <p>Returns all children of this node in the scene graph.</p>
	 * <p>This method may return an empty collection but it will never 
	 * return {@code null}</p>
	 * @return	a non-null collection of {@link GraphicalElement GraphicalElements}
	 * @see GraphicalElement#setParent(SpriteSet)
	 * @see GraphicalElement#getParent()
	 * @see #add(GraphicalElement)
	 * @see #remove(GraphicalElement)
	 */
	public Collection<GraphicalElement> getChildren() {
		return Collections.unmodifiableCollection(children);
	}
	
}