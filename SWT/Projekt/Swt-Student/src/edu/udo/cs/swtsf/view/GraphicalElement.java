package edu.udo.cs.swtsf.view;

import edu.udo.cs.swtsf.core.Entity;

/**
 * <p>This class represents a graphical game element. It is a node within 
 * the scene graph of the game.</p>
 * <p>Each GraphicalElement has a {@link #getTranslateX() translation}, 
 * {@link #getScale() scaling} and {@link #getRotation() rotation} within 
 * the local coordinate system of its {@link #getParent() parent}.</p>
 * <p>A GraphicalElement can be animated by an {@link #getAnimator() animator}.</p>
 * <p>This class, together with the {@link Sprite} and {@link SpriteSet} classes, 
 * implements the compositum pattern as a scene graph. For more information refer to
 * <pre>
 * <a href=https://en.wikipedia.org/wiki/Composite_pattern>Wikipedia Composite Pattern</a>
 * <a href=https://en.wikipedia.org/wiki/Scene_graph>Wikipedia Scene Graph</a></pre></p>
 */
public abstract class GraphicalElement {
	
	/**
	 * The {@link ViewManager} that this GraphicalElement is registered to.
	 * This is null before the GraphicalElement was added to a ViewManager or 
	 * after it has been removed.
	 */
	private ViewManager viewMngr;
	/**
	 * The parent node in the SceneGraph. Each GraphicalElement has a parent 
	 * except for the root node for which this value is null. A GraphicalElement 
	 * which was not yet added to the SceneGraph might also have a null parent.
	 */
	protected SpriteSet parent;
	/**
	 * The Animator is used to update the state of the GraphicalElement periodically.
	 * This is null by default.
	 */
	protected Animator animator;
	/**
	 * Property of all nodes within the SceneGraph. Translation on the X-axis of the 
	 * local coordinate system.
	 */
	protected double translationX = 0;
	/**
	 * Property of all nodes within the SceneGraph. Translation on the Y-axis of the 
	 * local coordinate system.
	 */
	protected double translationY = 0;
	/**
	 * Property of all nodes within the SceneGraph. Scale factor within the local 
	 * coordinate system.
	 */
	protected double scale = 1.0;
	/**
	 * Property of all nodes within the SceneGraph. rotation within the local 
	 * coordinate system.
	 */
	protected double rotation = 0.0;
	/**
	 * If true this GraphicalElement should be removed when its game {@link Entity} 
	 * is removed from the game. Not all GraphicalElements have an Entity associated 
	 * with it. This attribute is only important for those which do.
	 */
	protected boolean removeWithEntity = true;
	
	/**
	 * Does nothing.
	 */
	protected GraphicalElement() {
		// intentionally left blank. This base class should not be constructed.
	}
	
	/**
	 * <p>Sets the {@link ViewManager} for this GraphicalElement. This method is 
	 * called by the ViewManager itself. Do <u><b>not</b></u> call this method unless 
	 * you are writing a view implementation.</p>
	 * @param viewManager		an instance of ViewManager or null
	 * @see #getViewManager()
	 */
	protected void setViewManager(ViewManager viewManager) {
		viewMngr = viewManager;
	}
	
	/**
	 * <p>Returns the current {@link ViewManager} for this GraphicalElement.</p>
	 * <p>If this GraphicalElement is currently part of a scene graph this 
	 * method will return an instance (non-null) of ViewManager. If this 
	 * GraphicalElement is currently <b>not</b> part of a scene graph this 
	 * method will return {@code null}.</p>
	 * @return	an instance of {@link ViewManager} or null
	 * @see #setViewManager(ViewManager)
	 */
	public ViewManager getViewManager() {
		return viewMngr;
	}
	
	/**
	 * <p>Removes this {@link GraphicalElement} from its {@link ViewManager}. This 
	 * will only work if this GraphicalElement has previously been added to a 
	 * ViewManager.</p>
	 * Equal to:
	 * <pre>getViewManager().removeSprite(this);</pre>
	 * @throws IllegalStateException	if getViewManager() == null
	 * @see #getViewManager()
	 * @see ViewManager#removeSprite(GraphicalElement)
	 */
	public void removeFromView() {
		if (getViewManager() == null) {
			throw new IllegalStateException("getViewManager() == null");
		}
		getViewManager().removeSprite(this);
	}
	
	/**
	 * <p>Sets the parent of this GraphicalElement within the scene graph. 
	 * A parent can only be a {@link SpriteSet} or a subclass of SpriteSet.</p>
	 * <p>If this GraphicalElement already has a parent it will first be 
	 * removed from its previous parent by calling the 
	 * {@link SpriteSet#remove(GraphicalElement)} method before being added 
	 * to the new parent by calling {@link SpriteSet#add(GraphicalElement)}.</p>
	 * <p>If the parent is set to {@code null} this GraphicalElement will be 
	 * removed from the scene graph and no longer have a parent</p>
	 * @param spriteSet			an instance of {@link SpriteSet} or null
	 * @see #getParent()
	 * @see SpriteSet#add(GraphicalElement)
	 * @see SpriteSet#remove(GraphicalElement)
	 */
	public void setParent(SpriteSet spriteSet) {
		if (getParent() != null) {
			getParent().remove(this);
		}
		parent = spriteSet;
		if (getParent() != null) {
			getParent().add(this);
		}
	}
	
	/**
	 * <p>Returns the current parent of this GraphicalElement in the scene graph.</p>
	 * @return				a {@link SpriteSet} or null
	 * @see #setParent(SpriteSet)
	 */
	public SpriteSet getParent() {
		return parent;
	}
	
	/**
	 * <p>Sets the current {@link Animator} for this GraphicalElement.</p>
	 * @param value		an instance of {@link Animator} or null
	 * @see #getAnimator()
	 * @see Animator#updateAnimation(GraphicalElement, long)
	 */
	public void setAnimator(Animator value) {
		animator = value;
	}
	
	/**
	 * <p>Returns the current {@link Animator} of this GraphicalElement.</p>
	 * <p>By default the animator of a GraphicalElement is {@code null}</p>
	 * @return	an instance of {@link Animator} or null
	 * @see #setAnimator(Animator)
	 * @see Animator#updateAnimation(GraphicalElement, long)
	 */
	public Animator getAnimator() {
		return animator;
	}
	
	/**
	 * <p>Sets whether or not this GraphicalElement should be removed 
	 * when the {@link Entity} for which this element was originally 
	 * created is removed from the game.</p>
	 * <p>If this GraphicalElement was not created for an Entity, or if 
	 * the Entity was already removed in the past while 
	 * {@link #isRemoveWithEntity()} was false, then the value of this 
	 * property has no effect.</p>
	 * @param value		true if this GraphicalElement should be removed 
	 * 					with its Entity, false if it should not be
	 * @see #isRemoveWithEntity()
	 * @see ViewManager#newEntitySprite(Entity)
	 * @see ViewManager#newEntitySpriteSet(Entity)
	 */
	public void setRemoveWithEntity(boolean value) {
		removeWithEntity = value;
	}
	
	/**
	 * <p>Returns whether or not this GraphicalElement should be removed 
	 * when the {@link Entity} for which this element was originally 
	 * created is removed from the game.</p>
	 * <p>If this GraphicalElement was not created for an Entity, or if 
	 * the Entity was already removed in the past while 
	 * {@link #isRemoveWithEntity()} was false, then the value of this 
	 * property has no effect.</p>
	 * @return			true if this GraphicalElement should be removed 
	 * 					with its Entity, false if it should not be
	 * @see #setRemoveWithEntity(boolean)
	 * @see ViewManager#newEntitySprite(Entity)
	 * @see ViewManager#newEntitySpriteSet(Entity)
	 */
	public boolean isRemoveWithEntity() {
		return removeWithEntity;
	}
	
	/**
	 * <p>Sets the translation of this GraphicalElement within the local 
	 * coordinate system of its parent along the X- and Y-axis.</p>
	 * @param x								the translation along the X-axis
	 * @param y								the translation along the Y-axis
	 * @throws IllegalArgumentException		if either x or y is {@link Double#NaN}, 
	 * 										{@link Double#POSITIVE_INFINITY} 
	 * 										or {@link Double#NEGATIVE_INFINITY}
	 * @see #getTranslateX()
	 * @see #getTranslateY()
	 */
	public void setTranslation(double x, double y) {
		if (x == Double.NaN || Double.isInfinite(x)) {
			throw new IllegalArgumentException("x="+x);
		}
		if (y == Double.NaN || Double.isInfinite(y)) {
			throw new IllegalArgumentException("y="+y);
		}
		translationX = x;
		translationY = y;
	}
	
	/**
	 * <p>Returns the local translation along the X-axis of this GraphicalElement 
	 * within the coordinate system of its parent.</p>
	 * <p>The default value is 0.0.</p>
	 * @return	any double value except for {@link Double#NaN}, {@link Double#POSITIVE_INFINITY} 
	 * 			and {@link Double#NEGATIVE_INFINITY}
	 * @see #setTranslation(double, double)
	 * @see #getTranslateY()
	 */
	public double getTranslateX() {
		return translationX;
	}
	
	/**
	 * <p>Returns the local translation along the Y-axis of this GraphicalElement 
	 * within the coordinate system of its parent.</p>
	 * <p>The default value is 0.0.</p>
	 * @return	any double value except for {@link Double#NaN}, {@link Double#POSITIVE_INFINITY} 
	 * 			and {@link Double#NEGATIVE_INFINITY}
	 * @see #setTranslation(double, double)
	 * @see #getTranslateX()
	 */
	public double getTranslateY() {
		return translationY;
	}
	
	/**
	 * <p>Sets the scale (size) of this GraphicalElement within the local 
	 * coordinate system of its parent.</p>
	 * @param value							a non-negative double value except for {@link Double#NaN}, 
	 * 										and {@link Double#NEGATIVE_INFINITY}
	 * @throws IllegalArgumentException		if values is negative or {@link Double#NaN}, 
	 * @see #getScale()
	 */
	public void setScale(double value) {
		if (value == Double.NaN || value < 0) {
			throw new IllegalArgumentException("value="+value);
		}
		scale = value;
	}
	
	/**
	 * <p>Returns the local scale (size) of this GraphicalElement within the 
	 * coordinate system of its parent.</p>
	 * <p>The default scale is 1.0.</p>
	 * @return	a non-negative double value except for {@link Double#NaN} and {@link Double#NEGATIVE_INFINITY}
	 * @see #setScale(double)
	 */
	public double getScale() {
		return scale;
	}
	
	/**
	 * <p>Sets the rotation of this GraphicalElement within the local 
	 * coordinate system of its parent.</p>
	 * @param value							any double value except for {@link Double#NaN}, 
	 * 										{@link Double#POSITIVE_INFINITY} 
	 * 										and {@link Double#NEGATIVE_INFINITY}
	 * @throws IllegalArgumentException		if values is either {@link Double#NaN}, 
	 * 										{@link Double#POSITIVE_INFINITY} 
	 * 										or {@link Double#NEGATIVE_INFINITY}
	 * @see #getRotation()
	 */
	public void setRotation(double value) {
		if (value == Double.NaN || Double.isInfinite(value)) {
			throw new IllegalArgumentException("value="+value);
		}
		rotation = value;
	}
	
	/**
	 * <p>Returns the local rotation of this GraphicalElement within the 
	 * coordinate system of its parent.</p>
	 * <p>The default rotation is 0.0.</p>
	 * @return	any double value except for {@link Double#NaN}, {@link Double#POSITIVE_INFINITY} 
	 * 			and {@link Double#NEGATIVE_INFINITY}
	 * @see #setRotation(double)
	 */
	public double getRotation() {
		return rotation;
	}
	
}