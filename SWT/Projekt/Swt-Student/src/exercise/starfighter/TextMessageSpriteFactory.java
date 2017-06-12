package exercise.starfighter;

import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.view.GraphicalElement;
import edu.udo.cs.swtsf.view.GraphicalElementFactory;
import edu.udo.cs.swtsf.view.ViewManager;

/**
 * <p>Displays a Text across the screen for each created {@link TextMessage}. 
 * This {@link GraphicalElementFactory} does not create {@link GraphicalElement 
 * GraphicalElements} at all. It uses the 
 * {@link ViewManager#showText(String, int, edu.udo.cs.swtsf.view.RgbColor)} 
 * method to display text on screen.</p>
 */
public class TextMessageSpriteFactory implements GraphicalElementFactory {
	
	/**
	 * Always returns {@code null}.
	 */
	public GraphicalElement createForEntity(ViewManager view, Entity entity) {
		TextMessage tm = (TextMessage) entity;
		view.showText(tm.getText(), tm.getDurationInSeconds(), tm.getColor());
		return null;
	}
	
}