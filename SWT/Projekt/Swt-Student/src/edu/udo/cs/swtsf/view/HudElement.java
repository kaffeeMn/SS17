package edu.udo.cs.swtsf.view;

import edu.udo.cs.swtsf.core.Game;

public class HudElement {
	
	/**
	 * The orientation defines the position of the {@link HudElement} on screen. 
	 * This should never be null.
	 * @see #HudElement(HudElementOrientation, String, int, int, int, int)
	 * @see #setOrientation(HudElementOrientation)
	 */
	private HudElementOrientation orientation = HudElementOrientation.TOP;
	/**
	 * This text is displayed next to the icon of the HudElement. An empty text is not displayed. 
	 * This should never be null.
	 * @see #HudElement(HudElementOrientation, String, int, int, int, int)
	 * @see #setText(String)
	 */
	private String text = "";
	/**
	 * A file path to the image file used as the icon of the HudElement 
	 * This should never be null.
	 * @see #HudElement(HudElementOrientation, String, int, int, int, int)
	 * @see #setImagePath(String)
	 */
	private String imagePath;
	private int cutoutX;
	private int cutoutY;
	private int cutoutWidth;
	private int cutoutHeight;
	
	/**
	 * <p>Constructs a new {@link HudElement} with default values.</p>
	 * <p>The default orientation of a HudElement is {@link HudElementOrientation#TOP at the top}.<br>
	 * The default text is the {@link String#isEmpty() empty} String.<br>
	 * The default imagePath is {@code null} with a default cutout of (0, 0, 0, 0).</p>
	 */
	public HudElement() {
	}
	
	/**
	 * <p>Constructs a new {@link HudElement} with the given parameters.</p>
	 * @param orientation		the position of the element. Either at the {@link HudElementOrientation#TOP top} 
	 * 							or {@link HudElementOrientation#BOTTOM bottom} of the screen.
	 * @param imagePath			the path String to the image used to display the HudElement. The extension is 
	 * 							always assumed to be '.png' and can be omitted. The path always starts from the 
	 * 							'Images' folder of the game.
	 * @param cutoutX			the X-coordinate (in pixels) of the top-left corner of the cutout area of the image
	 * @param cutoutY			the Y-coordinate (in pixels) of the top-left corner of the cutout area of the image
	 * @param cutoutWidth		the horizontal size (in pixels) of the cutout area of the image
	 * @param cutoutHeight		the vertical size (in pixels) of the cutout area of the image
	 */
	public HudElement(HudElementOrientation orientation, String imagePath, 
			int cutoutX, int cutoutY, 
			int cutoutWidth, int cutoutHeight) 
	{
		setOrientation(orientation);
		setImagePath(imagePath);
		setImageCutoutX(cutoutX);
		setImageCutoutY(cutoutY);
		setImageCutoutWidth(cutoutWidth);
		setImageCutoutHeight(cutoutHeight);
	}
	
	/**
	 * <p>Sets the text that will be displayed by this {@link HudElement}.</p>
	 * @param value							a npn-null String
	 * @throws IllegalArgumentException		if value is null
	 * @see #getText()
	 */
	public void setText(String value) {
		if (value == null) {
			throw new IllegalArgumentException("value == null");
		}
		text = value;
	}
	
	/**
	 * <p>Returns the text currently displayed for this {@link HudElement}.</p>
	 * <p>The text may be empty but is never null.</p>
	 * @return				a non-null String
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * <p>Sets the {@link HudElementOrientation} of this {@link HudElement}.</p>
	 * @param value		the new orientation. Should not be null.
	 * @see #getOrientation()
	 */
	public void setOrientation(HudElementOrientation value) {
		if (value == null) {
			throw new IllegalArgumentException("value == null");
		}
		orientation = value;
	}
	
	/**
	 * <p>Returns either {@link HudElementOrientation#TOP} if this {@link HudElement} is 
	 * displayed along the top edge of the screen or {@link HudElementOrientation#BOTTOM} 
	 * if this HudElement is displayed along the bottom.</p>
	 * @return		a non-null HudElementOrientation
	 * @see #setOrientation(HudElementOrientation)
	 */
	public HudElementOrientation getOrientation() {
		return orientation;
	}
	
	/**
	 * <p>Sets the {@link #getImagePath() image path} of this Sprite.</p>
	 * @param value							a non-null String
	 * @throws IllegalArgumentException		if value is null
	 * @see #getImagePath()
	 * @see #setImageCutout(int, int, int, int)
	 * @see #getImageCutoutX()
	 * @see #getImageCutoutY()
	 * @see #getImageCutoutWidth()
	 * @see #getImageCutoutHeight()
	 */
	public void setImagePath(String value) {
		if (value == null) {
			throw new IllegalArgumentException("value == null");
		}
		imagePath = value;
	}
	
	/**
	 * <p>Returns the current image path of this Sprite. The image path is 
	 * a file path to the image file used to display this Sprite.</p>
	 * <p>The image path is never null but may be empty in which case no 
	 * image is going to be displayed.</p>
	 * @return		a non-null String
	 * @see #setImagePath(String)
	 * @see #setImageCutout(int, int, int, int)
	 * @see #getImageCutoutX()
	 * @see #getImageCutoutY()
	 * @see #getImageCutoutWidth()
	 * @see #getImageCutoutHeight()
	 */
	public String getImagePath() {
		return imagePath;
	}
	
	/**
	 * <p>Sets the cutout for this Sprite. The cutout is a rectangular area of 
	 * image file displayed by the Sprite.</p>
	 * @param x								the X-coordinate of the upper left corner of the cutout
	 * @param y								the Y-coordinate of the upper left corner of the cutout
	 * @param width							the width (in pixels) of the cutout
	 * @param height						the height (in pixels) of the cutout
	 * @throws IllegalArgumentException		if x and/or y is negative or if width and/or height is not positive
	 * @see #setImageCutoutX(int)
	 * @see #setImageCutoutY(int)
	 * @see #setImageCutoutWidth(int)
	 * @see #setImageCutoutHeight(int)
	 * @see #getImageCutoutX()
	 * @see #getImageCutoutY()
	 * @see #getImageCutoutWidth()
	 * @see #getImageCutoutHeight()
	 * @see #setImagePath(String)
	 * @see #getImagePath()
	 */
	public void setImageCutout(int x, int y, int width, int height) {
		setImageCutoutX(x);
		setImageCutoutY(y);
		setImageCutoutWidth(width);
		setImageCutoutHeight(height);
	}
	
	/**
	 * <p>Sets the X-coordinate for the upper left corner of the image cutout. 
	 * The cutout is the area of the image which is displayed by this sprite. </p>
	 * <p>To display the entire image call:</p>
	 * <pre>
	 * setImageCutout(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);</pre>
	 * @param value							a non-negative int value
	 * @throws IllegalArgumentException		if value is negative (< 0)
	 * @see #getImageCutoutX()
	 * @see #setImageCutout(int, int, int, int)
	 * @see #setImagePath(String)
	 * @see #getImagePath()
	 */
	public void setImageCutoutX(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("value < 0");
		}
		cutoutX = value;
	}
	
	/**
	 * <p>Returns the current X-coordinate of the image cutout. The cutout 
	 * is the area of the image which is displayed by this sprite. All dimensions 
	 * of the cutout are given in pixels of the image file used. The upper 
	 * left corner of the image file is at (0, 0).</p>
	 * <p>The X- and Y-coordinates of the cutout are always non-negative int values.</p>
	 * @return		a non-negative int value (greater than or equal to 0)
	 * @see #setImageCutoutX(int)
	 * @see #setImageCutout(int, int, int, int)
	 * @see #setImagePath(String)
	 * @see #getImagePath()
	 */
	public int getImageCutoutX() {
		return cutoutX;
	}
	
	/**
	 * <p>Sets the Y-coordinate for the upper left corner of the image cutout. 
	 * The cutout is the area of the image which is displayed by this sprite. </p>
	 * <p>To display the entire image call:</p>
	 * <pre>
	 * setImageCutout(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);</pre>
	 * @param value							a non-negative int value
	 * @throws IllegalArgumentException		if value is negative (< 0)
	 * @see #getImageCutoutY()
	 * @see #setImageCutout(int, int, int, int)
	 * @see #setImagePath(String)
	 * @see #getImagePath()
	 */
	public void setImageCutoutY(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("value < 0");
		}
		cutoutY = value;
	}
	
	/**
	 * <p>Returns the current Y-coordinate of the image cutout. The cutout 
	 * is the area of the image which is displayed by this sprite. All dimensions 
	 * of the cutout are given in pixels of the image file used. The upper 
	 * left corner of the image file is at (0, 0).</p>
	 * <p>The X- and Y-coordinates of the cutout are always non-negative int values.</p>
	 * @return		a non-negative int value (greater than or equal to 0)
	 * @see #setImageCutoutY(int)
	 * @see #setImageCutout(int, int, int, int)
	 * @see #setImagePath(String)
	 * @see #getImagePath()
	 */
	public int getImageCutoutY() {
		return cutoutY;
	}
	
	/**
	 * <p>Sets the width of the image cutout. The cutout is the area of the 
	 * image which is displayed by this sprite. </p>
	 * <p>To display the entire image call:</p>
	 * <pre>
	 * setImageCutout(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);</pre>
	 * @param value							an int value greater than 0
	 * @throws IllegalArgumentException		if value is not positive (< 1)
	 * @see #getImageCutoutWidth()
	 * @see #setImageCutout(int, int, int, int)
	 * @see #setImagePath(String)
	 * @see #getImagePath()
	 */
	public void setImageCutoutWidth(int value) {
		if (value < 1) {
			throw new IllegalArgumentException("value < 1");
		}
		cutoutWidth = value;
	}
	
	/**
	 * <p>Returns the current width of the image cutout. The cutout is the 
	 * area of the image which is displayed by this sprite. All dimensions 
	 * of the cutout are given in pixels of the image file used. The upper 
	 * left corner of the image file is at (0, 0).</p>
	 * <p>The width and height of the cutout are always positive int values.</p>
	 * @return		a positive int value (greater than 0)
	 * @see #setImageCutoutWidth(int)
	 * @see #setImageCutout(int, int, int, int)
	 * @see #setImagePath(String)
	 * @see #getImagePath()
	 */
	public int getImageCutoutWidth() {
		return cutoutWidth;
	}
	
	/**
	 * <p>Sets the height of the image cutout. The cutout is the area of the 
	 * image which is displayed by this sprite. </p>
	 * <p>To display the entire image call:</p>
	 * <pre>
	 * setImageCutout(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);</pre>
	 * @param value							an int value greater than 0
	 * @throws IllegalArgumentException		if value is not positive (< 1)
	 * @see #getImageCutoutHeight()
	 * @see #setImageCutout(int, int, int, int)
	 * @see #setImagePath(String)
	 * @see #getImagePath()
	 */
	public void setImageCutoutHeight(int value) {
		if (value < 1) {
			throw new IllegalArgumentException("value < 1");
		}
		cutoutHeight = value;
	}
	
	/**
	 * <p>Returns the current height of the image cutout. The cutout is the 
	 * area of the image which is displayed by this sprite. All dimensions 
	 * of the cutout are given in pixels of the image file used. The upper 
	 * left corner of the image file is at (0, 0).</p>
	 * <p>The width and height of the cutout are always positive int values.</p>
	 * @return		a positive int value (greater than 0)
	 * @see #setImageCutoutHeight(int)
	 * @see #setImageCutout(int, int, int, int)
	 * @see #setImagePath(String)
	 * @see #getImagePath()
	 */
	public int getImageCutoutHeight() {
		return cutoutHeight;
	}
	
	/**
	 * <p>This method is called by the {@link ViewManager} right after this {@link HudElement} 
	 * has been {@link ViewManager#addHudElement(HudElement) added} to the view. The arguments 
	 * will be the ViewManager that this HudElement was added to and the {@link Game} displayed 
	 * by the view. The Game may be {@code null} in case the Game has not yet been started.</p>
	 * <p>This method is an extension method and can safely be overwritten without a need to 
	 * call super.</p>
	 * @param view			the ViewManager that this HudElement was added to
	 * @param game			the Game displayed by the ViewManager, or null if the game was not yet started
	 * @see ViewManager#addHudElement(HudElement)
	 * @see #afterRemoved(ViewManager, Game)
	 */
	protected void afterAdded(ViewManager view, Game game) {}
	
	/**
	 * <p>This method is called by the {@link ViewManager} right after this {@link HudElement} 
	 * has been {@link ViewManager#removeHudElement(HudElement) removed} from the view. The arguments 
	 * will be the ViewManager that this HudElement was removed from and the {@link Game} displayed 
	 * by the view. The Game may be {@code null} in case the Game has not yet been started.</p>
	 * <p>This method is an extension method and can safely be overwritten without a need to 
	 * call super.</p>
	 * @param view			the ViewManager that this HudElement was removed from
	 * @param game			the Game displayed by the ViewManager, or null if the game was not yet started
	 * @see ViewManager#removeHudElement(HudElement)
	 * @see #afterAdded(ViewManager, Game)
	 */
	protected void afterRemoved(ViewManager view, Game game) {}
}