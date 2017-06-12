package edu.udo.cs.swtsf.view;

/**
 * <p>Represents color information. Color is constructed with 3 integer values 
 * ranging from 0 to 255. A number of often used constant colors are also provided 
 * as public static final instances.</p>
 * 
 * <p>Colors are immutable and can not be changed after creation.</p>
 * 
 * @see #BLACK
 * @see #WHITE
 * @see #RED
 * @see #GREEN
 * @see #BLUE
 * @see #MAGENTA
 * @see #CYAN
 * @see #YELLOW
 */
public class RgbColor {
	
	/**
	 * Absolute Black. RGB-Values are (0, 0, 0).
	 */
	public static final RgbColor BLACK = new RgbColor(0, 0, 0);
	/**
	 * Absolute White. RGB-Values are (255, 255, 255).
	 */
	public static final RgbColor WHITE = new RgbColor(255, 255, 255);
	/**
	 * Absolute Red. RGB-Values are (255, 0, 0).
	 */
	public static final RgbColor RED = new RgbColor(255, 0, 0);
	/**
	 * Absolute Green. RGB-Values are (0, 255, 0).
	 */
	public static final RgbColor GREEN = new RgbColor(0, 255, 0);
	/**
	 * Absolute Blue. RGB-Values are (0, 0, 255).
	 */
	public static final RgbColor BLUE = new RgbColor(0, 0, 255);
	/**
	 * Absolute Magenta (Red-Blue). RGB-Values are (255, 0, 255).
	 */
	public static final RgbColor MAGENTA = new RgbColor(255, 0, 255);
	/**
	 * Absolute Cyan (Green-Blue). RGB-Values are (0, 255, 255).
	 */
	public static final RgbColor CYAN = new RgbColor(0, 255, 255);
	/**
	 * Absolute Yellow (Red-Green). RGB-Values are (255, 255, 0).
	 */
	public static final RgbColor YELLOW = new RgbColor(255, 255, 0);
	
	private final int red;
	private final int green;
	private final int blue;
	
	/**
	 * <p>Constructs a new {@link RgbColor} with the given color values. All parameters must 
	 * be between 0 and 255 (inclusive).</p>
	 * @param red			the amount of red. Between 0 and 255 (inclusive)
	 * @param green			the amount of green. Between 0 and 255 (inclusive)
	 * @param blue			the amount of blue. Between 0 and 255 (inclusive)
	 * @throws IllegalArgumentException		if either red, green or blue is outside of the valid range
	 */
	public RgbColor(int red, int green, int blue) {
		check("red", red);
		check("green", green);
		check("blue", blue);
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	/**
	 * <p>Constructs a new {@link RgbColor} with the given color values. All parameters must 
	 * be between 0.0 and 1.0 (inclusive).</p>
	 * @param red			the amount of red. Between 0.0 and 1.0 (inclusive)
	 * @param green			the amount of green. Between 0.0 and 1.0 (inclusive)
	 * @param blue			the amount of blue. Between 0.0 and 1.0 (inclusive)
	 * @throws IllegalArgumentException		if either red, green or blue is outside of the valid range
	 */
	public RgbColor(double red, double green, double blue) {
		this((int) (red * 255), (int) (green * 255), (int) (blue * 255));
	}
	
	/**
	 * Throws an {@link IllegalArgumentException} if the given value is below 0 or above 255.
	 * @param name			printed in the exception message
	 * @param value			the checked value
	 */
	protected void check(String name, int value) {
		if (value < 0 || value > 255) {
			throw new IllegalArgumentException(name+"="+value);
		}
	}
	
	/**
	 * <p>Returns the intensity of the red component of this color. The returned 
	 * value is between 0 for the lowest intensity and 255 for the highest intensity.</p>
	 * @return		an integer between 0 and 255 (inclusive)
	 */
	public int getRed() {
		return red;
	}
	
	/**
	 * <p>Returns the intensity of the green component of this color. The returned 
	 * value is between 0 for the lowest intensity and 255 for the highest intensity.</p>
	 * @return		an integer between 0 and 255 (inclusive)
	 */
	public int getGreen() {
		return green;
	}
	
	/**
	 * <p>Returns the intensity of the blue component of this color. The returned 
	 * value is between 0 for the lowest intensity and 255 for the highest intensity.</p>
	 * @return		an integer between 0 and 255 (inclusive)
	 */
	public int getBlue() {
		return blue;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + blue;
		result = prime * result + green;
		result = prime * result + red;
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof RgbColor)) {
			return false;
		}
		RgbColor other = (RgbColor) obj;
		return red == other.red && green == other.green && blue == other.blue;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RgbColor [r=");
		builder.append(red);
		builder.append(", g=");
		builder.append(green);
		builder.append(", b=");
		builder.append(blue);
		builder.append("]");
		return builder.toString();
	}
	
}