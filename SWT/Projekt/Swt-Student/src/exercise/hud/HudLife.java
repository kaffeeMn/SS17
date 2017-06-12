package exercise.hud;

import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.Target;
import edu.udo.cs.swtsf.core.TargetObserver;
import edu.udo.cs.swtsf.view.HudElement;
import edu.udo.cs.swtsf.view.HudElementOrientation;
import edu.udo.cs.swtsf.view.ViewManager;

/*
 * Displays the players life as a HUD-element at the top of the screen
 */
public class HudLife extends HudElement implements TargetObserver {

	public HudLife() {
		setOrientation(HudElementOrientation.TOP);// position on screen
		setImagePath("HUD/PlayerLife");// path to image file
		setImageCutout(0, 0, 32, 32);// cutout rectangle of image
	}
	
	// Method of TargetObserver. Called every time after the players hitpoints have changed
	public void onAfterHitpointsChanged(Target target, int delta) {
		setText(Integer.toString(target.getHitpoints()));
	}
	
	// Extension method of HudElement. Called after this has been added to the view
	protected void afterAdded(ViewManager view, Game game) {
		// Update initial value at game start
		onAfterHitpointsChanged(game.getPlayer(), 0);
	}
}