package exercise.monsters;

import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.view.Animator;
import edu.udo.cs.swtsf.view.GraphicalElement;
import edu.udo.cs.swtsf.view.GraphicalElementFactory;
import edu.udo.cs.swtsf.view.Sprite;
import edu.udo.cs.swtsf.view.ViewManager;

/*
 * A default GraphicalElementFactory which can be used for all monster 
 * Sprites with the same simple animation cycle.
 * 
 * The Sprites created this way will all assume a cutout width and 
 * cutout height of 32 for each frame of animation. They will all assume, 
 * that the animation goes from left to right within the image used to 
 * display the animation.
 * 
 * The image to be used, the time in between animation frames and the 
 * total number of animation frames can be set in the constructor. The 
 * animation will be looping over and over.
 */
public class MonsterSpriteFactory implements GraphicalElementFactory {
	
	// The assumed width and height for all cutouts of all frames of animation 
	public static final int CUTOUT_SIZE = 32;
	
	// The path to the image file used for the animation
	private final String imageName;
	// The number of steps in the animation cycle
	private final int animStepCount;
	// The time (in milliseconds) between animation frames
	private final int animStepTime;
	
	public MonsterSpriteFactory(String imageName, int animationStepCount, int timeBetweenAnimationSteps) {
		this.imageName = imageName;
		animStepCount = animationStepCount;
		animStepTime = timeBetweenAnimationSteps;
	}
	
	public GraphicalElement createForEntity(ViewManager view, Entity entity) {
		Sprite sprite = view.newEntitySprite(entity);
		// Rotate the image to be upright.
		// If the monster (Entity) ever changes its rotation this value 
		// will be overwritten, but the simple monsters dont do that.
		sprite.setRotation(-90);
		sprite.setImagePath(imageName);
		sprite.setAnimator(new MonsterAnimator());
		
		return sprite;
	}
	
	public class MonsterAnimator implements Animator {
		
		private int timer;
		private int curStep = 0;
		
		public MonsterAnimator() {
			timer = animStepTime;
		}
		
		public void updateAnimation(GraphicalElement element, long millisSinceLastUpdate) {
			timer -= millisSinceLastUpdate;
			if (timer <= 0) {
				timer = animStepTime - timer % animStepTime;
				curStep = (curStep + 1) % animStepCount;
				// Update the animation by moving the cutout
				((Sprite) element).setImageCutoutX(curStep * CUTOUT_SIZE);
			}
		}
		
	}
	
}