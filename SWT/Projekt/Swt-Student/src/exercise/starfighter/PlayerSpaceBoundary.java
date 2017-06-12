package exercise.starfighter;

import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.core.EntityBehaviorStrategy;
import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.player.Player;
import edu.udo.cs.swtsf.view.RgbColor;

public class PlayerSpaceBoundary implements EntityBehaviorStrategy {
	
	public final double DISTANCE_FIRST_WARNING = 2500;
	public final double DISTANCE_SECOND_WARNING = 3000;
	public final double DISTANCE_KILL = 3500;
	private final int WARNING_WAIT_DELAY = 80;
	
	private int warningWaitTimer = 0;
	
	public void act(Entity host) {
		if (host.isDisposed()) {
			return;
		}
		double distanceToOrigin = host.getDistanceTo(0, 0);
		if (distanceToOrigin > DISTANCE_KILL) {
			((Player) host).destroy();
			Explosion explosion = new Explosion(64);
			explosion.setPosition(host);
			host.getCurrentGame().addEntity(explosion);
		}
		if (warningWaitTimer > 0) {
			warningWaitTimer--;
			return;
		}
		if (distanceToOrigin > DISTANCE_SECOND_WARNING) {
			TextMessage warningMsg = new TextMessage("LAST WARNING! TURN AROUND!", RgbColor.RED, 2);
			Game game = host.getCurrentGame();
			game.addEntity(warningMsg);
			
			warningWaitTimer = WARNING_WAIT_DELAY;
		} else if (distanceToOrigin > DISTANCE_FIRST_WARNING) {
			TextMessage warningMsg = new TextMessage("TURN AROUND! LEAVING SAFE SPACE!", RgbColor.RED, 2);
			Game game = host.getCurrentGame();
			game.addEntity(warningMsg);
			
			warningWaitTimer = WARNING_WAIT_DELAY;
		}
	}
	
}