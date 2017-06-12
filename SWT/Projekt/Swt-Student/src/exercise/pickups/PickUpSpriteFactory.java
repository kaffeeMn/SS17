package exercise.pickups;

import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.view.GraphicalElement;
import edu.udo.cs.swtsf.view.GraphicalElementFactory;
import edu.udo.cs.swtsf.view.Sprite;
import edu.udo.cs.swtsf.view.ViewManager;

/*
 * This GraphicalElementFactory can be used to create 
 * GraphicalElements for PickUps.
 * 
 * The GraphicalElement created for the PickUps all use the same image file 
 * and display the entire image.
 */
public class PickUpSpriteFactory implements GraphicalElementFactory {
	
	// The path to the image file used for the Sprite
	private final String imagePath;
	
	public PickUpSpriteFactory(String imageFilePath) {
		this.imagePath = imageFilePath;
	}
	
	public GraphicalElement createForEntity(ViewManager view, Entity entity) {
		Sprite sprite = view.newEntitySprite(entity);
		// We rotate the Sprite to have the image displayed upright
		sprite.setRotation(-90);
		sprite.setImagePath(imagePath);
		
		return sprite;
	}
	
}