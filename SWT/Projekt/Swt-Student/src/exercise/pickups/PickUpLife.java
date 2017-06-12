package exercise.pickups;

import edu.udo.cs.swtsf.core.PickUp;
import edu.udo.cs.swtsf.core.player.Player;

public class PickUpLife extends PickUp {
	
	protected void onPickUp(Player player) {
		player.addHitpoints(1);
	}
	
}