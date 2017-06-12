package exercise.pickups;

import edu.udo.cs.swtsf.core.PickUp;
import edu.udo.cs.swtsf.core.player.Player;

public class PickUpRocket extends PickUp {
	
	/*
	 * We make this protected method from PickUp public to 
	 * make it usable by the Cheats.
	 */
	public void onPickUp(Player player) {
		super.onPickUp(player);
	}
	
}