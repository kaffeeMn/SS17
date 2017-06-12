package exercise.starfighter;

import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.view.RgbColor;

/**
 * <p>Instances of {@link TextMessage} are used to display a text across the 
 * screen during the game. They are supposed to be used together with the 
 * {@link TextMessageSpriteFactory} to work correctly.</p>
 */
public class TextMessage extends Entity {
	
	private final String text;
	private final RgbColor color;
	private final int durInSec;
	
	/**
	 * <p>Constructs a new {@link TextMessage} with the given text, {@link RgbColor 
	 * color} and duration. After a TextMessage was constructed its properties can 
	 * no longer be changed.</p>
	 * @param text					the text displayed
	 * @param color					the color of the displayed text. This can be one of the constants of {@link RgbColor}.
	 * @param durationInSeconds		the duration (in frames) for which the text is supposed to be displayed
	 * @throws IllegalArgumentException		if text or color are null, or if durationInSeconds is less than 1
	 */
	public TextMessage(String text, RgbColor color, int durationInSeconds) {
		super();
		if (text == null) {
			throw new IllegalArgumentException("text == null");
		}
		if (color == null) {
			throw new IllegalArgumentException("color == null");
		}
		if (durationInSeconds < 1) {
			throw new IllegalArgumentException("durationInSeconds < 1");
		}
		this.text = text;
		this.color = color;
		this.durInSec = durationInSeconds;
	}
	
	/**
	 * <p>Returns the text displayed by this {@link TextMessage}.</p>
	 * @return		a non-null String
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * <p>Returns the {@link RgbColor} of the displayed text.</p>
	 * @return		a non-null color instance
	 */
	public RgbColor getColor() {
		return color;
	}
	
	/**
	 * <p>Returns the duration (in frames) for which this {@link TextMessage} is displayed on screen.</p>
	 * @return		a positive integer value
	 */
	public int getDurationInSeconds() {
		return durInSec;
	}
	
	/**
	 * <p>Always returns true because a {@link TextMessage} is not supposed to exist in the {@link Game}.</p>
	 */
	@Override
	public boolean isDisposed() {
		return true;
	}
	
}