package edu.udo.cs.swtsf.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.player.Player;

public abstract class ViewManager {
	
	/**
	 * Holds the mapping from {@link Class}-Objects to {@link GraphicalElementFactory 
	 * GraphicalElementFactories}. 
	 * @see #setFactoryForClass(Class, GraphicalElementFactory)
	 * @see #getFactoryForClass(Class)
	 */
	protected final Map<Class<?>, GraphicalElementFactory> 
		spriteFactoryMap = new HashMap<>();
	/**
	 * A List of all {@link HudElement HudElements} that are displayed.
	 * @see #addHudElement(HudElement)
	 * @see #removeHudElement(HudElement)
	 */
	protected final List<HudElement> hudList = new ArrayList<>();
	/**
	 * The path for the background image file.
	 * @see #setBackgroundImagePath(String)
	 * @see #getBackgroundImagePath()
	 */
	protected String bgImgPath;
	/**
	 * Used to create the {@link Player} of the {@link Game} the moment the game is started.
	 */
	protected Supplier<? extends Player> playerFactory;
	
	/**
	 * <p>Changes the position of the camera so that the given point is in the center of 
	 * the visible area on screen.</p>
	 * @param offsetX		an X-coordinate
	 * @param offsetY		an Y-coordinate
	 */
	public abstract void setCameraCenter(double offsetX, double offsetY);
	
	/**
	 * <p>Returns X-coordinate of the center of the visible area on screen (the camera).</p> 
	 * <p>Positive X-coordinates go to the right side of the screen, negative X-coordinates 
	 * go towards the left.</p>
	 * @return		any double value is a valid return value
	 */
	public abstract double getCameraCenterX();
	
	/**
	 * <p>Returns Y-coordinate of the center of the visible area on screen (the camera).</p>
	 * <p>Positive Y-coordinates go to the bottom of the screen, negative Y-coordinates 
	 * go towards the top.</p> 
	 * @return		any double value is a valid return value
	 */
	public abstract double getCameraCenterY();
	
	/**
	 * <p>Returns the width (size on the X-axis) of the visible area on screen (the camera).</p>
	 * @return		a positive double value
	 */
	public abstract double getCameraWidth();
	
	/**
	 * <p>Returns the height (size on the Y-axis) of the visible area on screen (the camera).</p>
	 * @return		a positive double value
	 */
	public abstract double getCameraHeight();
	
	/**
	 * <p>Returns the lowest X-coordinate which is visible on screen.</p>
	 * @return		getCameraCenterX() - getCameraWidth() / 2
	 */
	public double getCameraLeftX() {
		return getCameraCenterX() - getCameraWidth() / 2;
	}
	
	/**
	 * <p>Returns the highest X-coordinate which is visible on screen.</p>
	 * @return		getCameraCenterX() + getCameraWidth() / 2
	 */
	public double getCameraRightX() {
		return getCameraCenterX() + getCameraWidth() / 2;
	}
	
	/**
	 * <p>Returns the lowest Y-coordinate which is visible on screen.</p>
	 * @return		getCameraCenterY() - getCameraHeight() / 2
	 */
	public double getCameraTopY() {
		return getCameraCenterY() - getCameraHeight() / 2;
	}
	
	/**
	 * <p>Returns the highest Y-coordinate which is visible on screen.</p>
	 * @return		getCameraCenterY() + getCameraHeight() / 2
	 */
	public double getCameraBottomY() {
		return getCameraCenterY() + getCameraHeight() / 2;
	}
	
	/**
	 * <p>Displays a text with a given color across the center of the screen 
	 * for a certain time.</p>
	 * <p>The text will be displayed above the background image but below any 
	 * other graphical elements.</p>
	 * @param text							a non-null String
	 * @param timeInSeconds					how many seconds the text is supposed 
	 * 										to last before it vanishes
	 * @param color							a non-null Color for the text
	 * @throws IllegalArgumentException		if either text or color is null or if 
	 * 										timeInSeconds is less than or equal to 0
	 */
	public abstract void showText(String text, int timeInSeconds, RgbColor color);
	
	/**
	 * <p>Creates and returns a new instance of {@link Sprite} which is compatible 
	 * with this {@link ViewManager}. The Sprite has not yet been added to this 
	 * ViewManager. This method is supposed to be called by a {@link GraphicalElementFactory} 
	 * when creating {@link GraphicalElement graphical elements} for game {@link Entity entities}.</p>
	 * <p>This is an implementation of the Abstract-Factory pattern</p>
	 * @return			a non-null instance of Sprite which is compatible with this ViewManager
	 * @see #newEntitySprite(Entity)
	 * @see #newSpriteSet()
	 * @see #newEntitySpriteSet(Entity)
	 */
	public abstract Sprite newSprite();
	
	/**
	 * <p>Creates and returns a new instance of {@link Sprite} which is compatible 
	 * with this {@link ViewManager}. The Sprite has not yet been added to this 
	 * ViewManager. This method is supposed to be called by a {@link GraphicalElementFactory} 
	 * when creating {@link GraphicalElement graphical elements} for game {@link Entity entities}.</p>
	 * <p>The difference between this method and {@link #newSprite()} is that the Sprite returned 
	 * by this method will automatically update its {@link GraphicalElement#setTranslation(double, double) 
	 * position}, {@link GraphicalElement#setScale(double) scale} and 
	 * {@link GraphicalElement#setRotation(double) rotation} to match the {@link Entity#getX() position}, 
	 * {@link Entity#getSize() scale} and {@link Entity#getRotation() rotation} of the given {@link Entity}.</p>
	 * <p>This is an implementation of the Abstract-Factory pattern</p>
	 * @return			a non-null instance of Sprite which is compatible with 
	 * 					this ViewManager and is synchronized with the Entity
	 * @see #newSprite()
	 * @see #newSpriteSet()
	 * @see #newEntitySpriteSet(Entity)
	 */
	public abstract Sprite newEntitySprite(Entity entity);
	
	/**
	 * <p>Creates and returns a new instance of {@link SpriteSet} which is compatible 
	 * with this {@link ViewManager}. The SpriteSet has not yet been added to this 
	 * ViewManager. This method is supposed to be called by a {@link GraphicalElementFactory} 
	 * when creating {@link GraphicalElement graphical elements} for game {@link Entity entities}.</p>
	 * <p>This is an implementation of the Abstract-Factory pattern</p>
	 * @return			a non-null instance of SpriteSet which is compatible with this ViewManager
	 * @see #newSprite()
	 * @see #newEntitySprite(Entity)
	 * @see #newEntitySpriteSet(Entity)
	 */
	public abstract SpriteSet newSpriteSet();
	
	/**
	 * <p>Creates and returns a new instance of {@link SpriteSet} which is compatible 
	 * with this {@link ViewManager}. The SpriteSet has not yet been added to this 
	 * ViewManager. This method is supposed to be called by a {@link GraphicalElementFactory} 
	 * when creating {@link GraphicalElement graphical elements} for game {@link Entity entities}.</p>
	 * <p>The difference between this method and {@link #newSpriteSet()} is that the SpriteSet returned 
	 * by this method will automatically update its {@link GraphicalElement#setTranslation(double, double) 
	 * position}, {@link GraphicalElement#setScale(double) scale} and 
	 * {@link GraphicalElement#setRotation(double) rotation} to match the {@link Entity#getX() position}, 
	 * {@link Entity#getSize() scale} and {@link Entity#getRotation() rotation} of the given {@link Entity}.</p>
	 * <p>This is an implementation of the Abstract-Factory pattern</p>
	 * @return			a non-null instance of SpriteSet which is compatible with 
	 * 					this ViewManager and is synchronized with the Entity
	 * @see #newSprite()
	 * @see #newEntitySprite(Entity)
	 * @see #newSpriteSet()
	 */
	public abstract SpriteSet newEntitySpriteSet(Entity entity);
	
	/**
	 * <p>An extension method which is called by the {@link #addSprite(GraphicalElement)} 
	 * method to ease the implementation of subclasses.</p>
	 * <p>This method may be overwritten without the need to call super.</p>
	 * @param element	the {@link GraphicalElement} which was just added
	 * @see #addSprite(GraphicalElement)
	 */
	protected void addSpriteInternal(GraphicalElement element) {}
	
	/**
	 * <p>An extension method which is called by the {@link #removeSprite(GraphicalElement)} 
	 * method to ease the implementation of subclasses.</p>
	 * <p>This method may be overwritten without the need to call super.</p>
	 * @param element	the {@link GraphicalElement} which was just removed
	 * @see #removeSprite(GraphicalElement)
	 */
	protected void removeSpriteInternal(GraphicalElement element) {}
	
	/**
	 * <p>Changes the {@link GraphicalElementFactory} which is used to create the 
	 * {@link GraphicalElement GraphicalElements} for all game {@link Entity entities} which 
	 * are instances of the given class.</p>
	 * @param modelClass		the class of game {@link Entity} for which graphical elements 
	 * 							are created by the given factory. This should not be null.
	 * @param factory			the {@link GraphicalElementFactory factory} which creates 
	 * 							the graphical representations for all Entities of the given type. 
	 * 							This should not be null.
	 * @see #getFactoryForClass(Class)
	 */
	public void setFactoryForClass(
			Class<? extends Entity> modelClass, 
			GraphicalElementFactory factory) 
	{
		if (modelClass == null) {
			throw new IllegalArgumentException("modelClass == null");
		}
		if (factory == null) {
			throw new IllegalArgumentException("factory == null");
		}
		spriteFactoryMap.put(modelClass, factory);
	}
	
	/**
	 * <p>Returns the {@link GraphicalElementFactory} which is responsible for 
	 * creating {@link GraphicalElement GraphicalElements} for all {@link Entity 
	 * Entities} of the given {@link Class type}.</p>
	 * @param modelClass		a non-null {@link Class} of entities. This should not be null.
	 * @return					a GraphicalElementFactory instance or null
	 * @see #setFactoryForClass(Class, GraphicalElementFactory)
	 */
	public GraphicalElementFactory getFactoryForClass(
			Class<? extends Entity> modelClass) 
	{
		if (modelClass == null) {
			throw new IllegalArgumentException("modelClass == null");
		}
		return spriteFactoryMap.get(modelClass);
	}
	
	/**
	 * <p>Adds the given {@link GraphicalElement} to the game view. The element 
	 * will be displayed from now on.</p>
	 * @param element			a non-null GraphicalElement
	 * @throws					IllegalArgumentException if element is null
	 * @throws					IllegalStateException if element was already added to a view
	 * @see #removeSprite(GraphicalElement)
	 */
	public void addSprite(GraphicalElement element) {
		if (element == null) {
			throw new IllegalArgumentException("element == null");
		}
		if (element.getViewManager() != null) {
			throw new IllegalArgumentException("element.getViewManager()="+element.getViewManager());
		}
		element.setViewManager(this);
		addSpriteInternal(element);
	}
	
	/**
	 * <p>Removes the given {@link GraphicalElement} from the game view. The element 
	 * will no longer be displayed from now on.</p>
	 * @param element			a non-null GraphicalElement
	 * @throws					IllegalArgumentException if element is null
	 * @throws					IllegalStateException if element was not added to this view before
	 * @see #addSprite(GraphicalElement)
	 */
	public void removeSprite(GraphicalElement element) {
		if (element == null) {
			throw new IllegalArgumentException("element == null");
		}
		if (element.getViewManager() != this) {
			throw new IllegalStateException("element.getViewManager()="+element.getViewManager());
		}
		element.setViewManager(null);
		removeSpriteInternal(element);
	}
	
	/**
	 * <p>Sets the path String for the background image. The background image is 
	 * a regular image file which is painted across the background of the game in a 
	 * tiled pattern. The background image should not be too small. An image size of 
	 * <u>at least</u> 128x128 is recommended.</p>
	 * 
	 * <p>The path is relative to the 'images' folder of the game. It uses the 
	 * default java {@link File file} system notation. The file extension is not part 
	 * of the path String but is always assumed to be '.png'.</p>
	 * 
	 * The following are acceptable path Strings:
	 * <pre>
	 * Image
	 * folder/Image
	 * folder/subfolder/Image
	 * </pre>
	 * 
	 * These are incorrect:
	 * <pre>
	 * Image.jpg
	 * Image.gif
	 * Images/Image
	 * Images/folder/Image
	 * </pre>
	 * 
	 * @param imgPath		the path String for the image or null if no image should be used
	 * @see #getBackgroundImagePath()
	 */
	public void setBackgroundImagePath(String imgPath) {
		bgImgPath = imgPath;
		setBackgroundImagePathInternal();
	}
	
	/**
	 * <p>This method is implemented by the concrete {@link ViewManager} implementations. 
	 * They may be used to perform implementation dependent code.</p>
	 * <p>This method is called by {@link #setBackgroundImagePath(String)} after the 
	 * path has been set.</p>
	 * @see #setBackgroundImagePath(String)
	 * @see #getBackgroundImagePath()
	 */
	protected void setBackgroundImagePathInternal() {}
	
	/**
	 * <p>Returns the path String for the background image. The background image is 
	 * a regular image file which is painted across the background of the game in a 
	 * tiled pattern. The background image should not be too small. An image size of 
	 * <u>at least</u> 128x128 is recommended.</p>
	 * 
	 * <p>The path is relative to the path from where the game is started (where the 
	 * JVM is run). It uses the default java {@link File file} system notation. The 
	 * file extension is part of the path String.</p>
	 * 
	 * @return a String representing the path to th background image or null if not set
	 * @see #setBackgroundImagePath(String)
	 */
	public String getBackgroundImagePath() {
		return bgImgPath;
	}
	
	/**
	 * <p>Moves the games camera so that the given {@link Entity} is visible 
	 * on screen. The camera may not need to move depending on its position 
	 * relative to the entity.</p>
	 * @param entity		the entity to move to. Must not be null.
	 * @see #setCameraCenter(double, double)
	 * @see #getCameraLeftX()
	 * @see #getCameraRightX()
	 * @see #getCameraBottomY()
	 * @see #getCameraTopY()
	 * @see #getCameraWidth()
	 * @see #getCameraHeight()
	 * @see #getCameraCenterX()
	 * @see #getCameraCenterY()
	 */
	public void panCameraToEntity(Entity entity) {
		if (entity == null) {
			throw new IllegalArgumentException("entity == null");
		}
		double playerX = entity.getX();
		double playerY = entity.getY();
		
		double cameraW = getCameraWidth();
		double cameraH = getCameraHeight();
		double cameraX = -(getCameraCenterX() - cameraW / 2);
		double cameraY = -(getCameraCenterY() - cameraH / 2);
		double cameraPanX = 0;
		double cameraPanY = 0;
		double cameraMaxDistanceX = cameraW / 4;
		double cameraMaxDistanceY = cameraH / 4;
		
		double distanceX = Math.abs(cameraX - playerX);
		double distanceY = Math.abs(cameraY - playerY);
		
		if (distanceX > cameraMaxDistanceX) {
			cameraPanX = distanceX - cameraMaxDistanceX;
			if (playerX < cameraX) {
				cameraPanX = -cameraPanX;
			}
		}
		if (distanceY > cameraMaxDistanceY) {
			cameraPanY = distanceY - cameraMaxDistanceY;
			if (playerY < cameraY) {
				cameraPanY = -cameraPanY;
			}
		}
		if (cameraPanX != 0 || cameraPanY != 0) {
			setCameraCenter(cameraX + cameraPanX, cameraY + cameraPanY);
		}
	}
	
	/**
	 * <p>Adds the given {@link HudElement} to this view. After being added the 
	 * element will be displayed on screen and its 
	 * {@link HudElement#afterAdded(ViewManager, Game)} method will be called with 
	 * this {@link ViewManager} and its associated {@link #getGame() game} as 
	 * first and second arguments respectively.</p>
	 * @param element			the {@link HudElement} to be added
	 * @see #removeHudElement(HudElement)
	 * @see #getHudElements()
	 * @see HudElement#afterAdded(ViewManager, Game)
	 */
	public void addHudElement(HudElement element) {
		if (element == null) {
			throw new IllegalArgumentException("element == null");
		}
		hudList.add(element);
		addHudElementInternal(element);
		element.afterAdded(this, getGame());
	}
	
	/**
	 * <p>This method is implemented by the concrete {@link ViewManager} implementations. 
	 * They may be used to perform implementation dependent code.</p>
	 * <p>This method is called by {@link #addHudElement(HudElement)} after the 
	 * element has been added to the internal {@link #hudList} but before the 
	 * {@link HudElement#afterAdded(ViewManager, Game)} method of the element has been 
	 * called.</p>
	 * @param element		the added HudElement
	 */
	protected void addHudElementInternal(HudElement element) {}
	
	/**
	 * <p>Removes the given {@link HudElement} from this view. The element will 
	 * no longer be displayed and its {@link HudElement#afterRemoved(ViewManager, Game)} 
	 * method will be called with this {@link ViewManager} and its associated 
	 * {@link #getGame() game} as first and second arguments respectively.</p>
	 * @param element			the {@link HudElement} to be removed
	 * @see #addHudElement(HudElement)
	 * @see #getHudElements()
	 * @see HudElement#afterRemoved(ViewManager, Game)
	 */
	public void removeHudElement(HudElement element) {
		if (element == null) {
			throw new IllegalArgumentException("element == null");
		}
		hudList.remove(element);
		removeHudElementInternal(element);
		element.afterRemoved(this, getGame());
	}
	
	/**
	 * <p>This method is implemented by the concrete {@link ViewManager} implementations. 
	 * They may be used to perform implementation dependent code.</p>
	 * <p>This method is called by {@link #removeHudElement(HudElement)} after the 
	 * element has been removed from the internal {@link #hudList} but before the 
	 * {@link HudElement#afterRemoved(ViewManager, Game)} method of the element has been 
	 * called.</p>
	 * @param element		the removed HudElement
	 */
	protected void removeHudElementInternal(HudElement element) {}
	
	/**
	 * <p>Creates a new immutable {@link List} of all {@link HudElement HudElements}
	 * in the view and returns it. No assumptions should be made about the type of 
	 * the List.</p>
	 * @return		a new immutable List of all HudElements
	 * @see #addHudElement(HudElement)
	 * @see #removeHudElement(HudElement)
	 */
	public List<HudElement> getHudElements() {
		return Collections.unmodifiableList(hudList);
	}
	
	/**
	 * <p>Returns the {@link Game} that is being displayed by this {@link ViewManager}.</p>
	 * <p>This method will return {@code null} if the Game has not properly been started yet.</p>
	 * @return		null or an instance of {@link Game}
	 * @see #setPlayerFactory(Supplier)
	 */
	protected abstract Game getGame();
	
	/**
	 * <p>Sets the factory used to create the {@link Player} of the {@link Game} that 
	 * will be displayed by this {@link ViewManager}. This method has no impact on a 
	 * running Game. If this method is called before the Game is started the player 
	 * factory will be passed to the {@link Game#Game(Supplier) constructor} of Game.</p>
	 * 
	 * <p>If the player factory has not been set or is set to {@code null} an instance 
	 * of {@link Player} will be used for the Player of the game.</p>
	 * @param factory		a {@link Supplier} for {@link Player} instances or null
	 * @see Player
	 * @see Supplier
	 * @see Game#Game(Supplier)
	 */
	public void setPlayerFactory(Supplier<? extends Player> factory) {
		playerFactory = factory;
	}
	
}