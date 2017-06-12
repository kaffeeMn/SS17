package edu.udo.cs.swtsf.core.player;

import edu.udo.cs.swtsf.view.Animator;
import edu.udo.cs.swtsf.view.GraphicalElement;
import edu.udo.cs.swtsf.view.Sprite;

/*
 * This animator is used to animate the trail of smoke puffs created 
 * by the players ship. The smoke puffs last for a limited time until 
 * they are automatically removed.
 */
public class SmokePuffAnimator implements Animator {
	
	// the time (in milliseconds) that a single smoke puff exists
	public static final int SMOKE_PUFF_LIFE_TIME = 300;
	// the width and height for each animation frame of the smoke puff
	public static final int CUTOUT_SIZE = 32;
	// the last animation frame index in the animation sequence
	private static final int ANIMATION_MAX_FRAME = 3;
	// the time (in milliseconds) each frame of the animation sequence is displayed
	private static final int TIME_BETWEEN_FRAMES = 40;
	
	// the time before the animation starts
	private int timer = SMOKE_PUFF_LIFE_TIME;
	// the current step (index) in the animation sequence
	private int step = 0;
	// the time since the current animation frame was displayed
	private long timePassed;
	
	public void updateAnimation(GraphicalElement element, long millisSinceLastUpdate) {
		// count down before the animation starts
		if (timer > 0) {
			timer -= millisSinceLastUpdate;
			return;
		}
		timePassed += millisSinceLastUpdate;
		int oldStep = step;
		step += timePassed / TIME_BETWEEN_FRAMES;
		timePassed %= TIME_BETWEEN_FRAMES;
		
		if (step > ANIMATION_MAX_FRAME) {
			// animation has run through; dispose the Sprite
			element.getViewManager().removeSprite(element);
		} else if (oldStep != step) {
			// update animation frame (cutout)
			((Sprite) element).setImageCutoutX(step * CUTOUT_SIZE);
		}
	}
}