package exercise.hud;

import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.PickUp;
import edu.udo.cs.swtsf.core.player.Player;
import edu.udo.cs.swtsf.core.player.PlayerObserver;
import edu.udo.cs.swtsf.view.HudElement;
import edu.udo.cs.swtsf.view.HudElementOrientation;
import edu.udo.cs.swtsf.view.ViewManager;

public class HudPickUpCount extends HudElement implements PlayerObserver {
	
	private final Class<? extends PickUp> pickUpCls;
	
	public HudPickUpCount(Class<? extends PickUp> pickUpClass) {
		super(HudElementOrientation.TOP, "HUD/"+pickUpClass.getSimpleName(), 0, 0, 32, 32);
		pickUpCls = pickUpClass;
	}
	
	public void onPickUpCountChanged(Player player, 
			Class<? extends PickUp> pickUpClass, int value) 
	{
		if (pickUpCls.equals(pickUpClass)) {
			int count = player.getPickUpCount(pickUpCls);
			setText(Integer.toString(count));
		}
	}
	
	protected void afterAdded(ViewManager view, Game game) {
		onPickUpCountChanged(game.getPlayer(), pickUpCls, 0);
	}
}