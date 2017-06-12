package exercise.starfighter;

import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.view.Animator;
import edu.udo.cs.swtsf.view.GraphicalElement;
import edu.udo.cs.swtsf.view.GraphicalElementFactory;
import edu.udo.cs.swtsf.view.Sprite;
import edu.udo.cs.swtsf.view.ViewManager;

/*
 * Creates the explosion Sprite and animation.
 */
public class ExplosionSpriteFactory implements GraphicalElementFactory {
	
	// All frames of the explosion animation have a size of 32x32 pixels
	private static final int IMG_CUTOUT_W = 32;
	private static final int IMG_CUTOUT_H = 32;
	// The final frame of the animation has index 7 (we have 8 frames of animation)
	private static final int ANIMATION_MAX_FRAME = 7;
	// The time (in milliseconds) between frames of the explosion animation
	private static final long TIME_BETWEEN_FRAMES = 50;
	
	public GraphicalElement createForEntity(ViewManager view, Entity entity) {
		Sprite sprite = view.newSprite();
		
		// An explosion of size 32 is regular size for the animation
		sprite.setScale(entity.getSize() / 32.0);
		sprite.setTranslation(entity.getX(), entity.getY());
		// The explosion is removed from the game immediately. 
		// The Sprite should last until the animation is done.
		sprite.setRemoveWithEntity(false);
		sprite.setAnimator(new ExplosionAnimator());
		// Setup the explosion image and cutout for the first frame of animation
		sprite.setImagePath("Explosion");
		sprite.setImageCutoutX(0);
		sprite.setImageCutoutY(0);
		sprite.setImageCutoutWidth(IMG_CUTOUT_W);
		sprite.setImageCutoutHeight(IMG_CUTOUT_H);
		
		return sprite;
	}
	
	// Used to animate the explosion over time
	private static class ExplosionAnimator implements Animator {
		
		private int frameIndex = 0;
		private long timePassed;
		
		public void updateAnimation(GraphicalElement element, long millis) {
			timePassed += millis;
			int previousFrameIndex = frameIndex;
			// calculate next frame index in animation sequence
			frameIndex += timePassed / TIME_BETWEEN_FRAMES;
			timePassed %= TIME_BETWEEN_FRAMES;
			// If the index is out of range the animation is over
			if (frameIndex > ANIMATION_MAX_FRAME) {
				// remove the element from the view
				element.removeFromView();
			} else if (previousFrameIndex != frameIndex) {
				// update the cutout rectangle of the Sprite
				((Sprite) element).setImageCutoutX(frameIndex * IMG_CUTOUT_W);
			}
		}
		
	}
	
}