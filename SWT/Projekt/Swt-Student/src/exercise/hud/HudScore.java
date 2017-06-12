package exercise.hud;

import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.player.Player;
import edu.udo.cs.swtsf.core.player.PlayerObserver;
import edu.udo.cs.swtsf.view.HudElement;
import edu.udo.cs.swtsf.view.HudElementOrientation;
import edu.udo.cs.swtsf.view.ViewManager;

public class HudScore extends HudElement implements PlayerObserver {

	public HudScore() {
		super(HudElementOrientation.TOP, "HUD/Score", 0, 0, 32, 32);
	}
	
	@Override
	public void onScoreChanged(Player player, int value) {
		setText(Integer.toString(player.getScore()));
	}
	
	protected void afterAdded(ViewManager view, Game game) {
		onScoreChanged(game.getPlayer(), 0);
	}
}