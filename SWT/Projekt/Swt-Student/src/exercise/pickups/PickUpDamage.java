package exercise.pickups;

import edu.udo.cs.swtsf.core.PickUp;
import edu.udo.cs.swtsf.core.player.Player;
import exercise.lasers.LaserUpgradeDamage;

public class PickUpDamage extends PickUp {
	
	public void onPickUp(Player player) {
		player.addLaserUpgrade(new LaserUpgradeDamage());
	}
	
}