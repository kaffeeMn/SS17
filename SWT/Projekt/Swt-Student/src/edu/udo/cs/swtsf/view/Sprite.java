package edu.udo.cs.swtsf.view;

/**
 * <p>A Sprite is used to display a portion of an image on the screen. A Sprite 
 * has an {@link #getImagePath() image path}, which is a file path to an image file,  
 * as well as a rectangular area called the cutout defined by its 
 * {@link #getImageCutoutY() upper} {@link #getImageCutoutX() left} corner, 
 * {@link #getImageCutoutWidth() width} and {@link #getImageCutoutHeight() height}. 
 * The cutout is the portion of the image which will be displayed on screen by the 
 * Sprite.</p>
 * <p>A Sprite represents a leaf node in a scene graph. It can not have any children 
 * of its own.</p>
 */
public abstract class Sprite extends GraphicalElement {
	
	/**
	 * The path to the image file which this Sprite displays.
	 * The path does not include the suffix (.png). 
	 */
	protected String imagePath = "";
	/**
	 * The X-coordinate of the upper left corner of the area of the image, 
	 * which is displayed by this sprite.
	 */
	protected int imageCutoutX;
	/**
	 * The Y-coordinate of the upper left corner of the area of the image, 
	 * which is displayed by this sprite.
	 */
	protected int imageCutoutY;
	/**
	 * The width of the area of the image, which is displayed by this sprite.
	 */
	protected int imageCutoutWidth = 1;
	/**
	 * The height of the area of the image, which is displayed by this sprite.
	 */
	protected int imageCutoutHeight = 1;
	
	/**
	 * Does nothing.
	 */
	protected Sprite() {
		super();
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
		imageCutoutX = value;
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
		return imageCutoutX;
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
		imageCutoutY = value;
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
		return imageCutoutY;
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
		imageCutoutWidth = value;
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
		return imageCutoutWidth;
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
		imageCutoutHeight = value;
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
		return imageCutoutHeight;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Sprite [imagePath=");
		builder.append(imagePath);
		builder.append(", translationX=");
		builder.append(translationX);
		builder.append(", translationY=");
		builder.append(translationY);
		builder.append(", scale=");
		builder.append(scale);
		builder.append(", rotation=");
		builder.append(rotation);
		builder.append("]");
		return builder.toString();
	}
	
}