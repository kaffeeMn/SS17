package exercise.starfighter;

import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.GameObserver;
import edu.udo.cs.swtsf.core.Target;
import edu.udo.cs.swtsf.core.player.BasicBullet;

/*
 * This GameObserver is responsible for creating explosions whenever a 
 * Bullet or Target is removed from the game.
 */
public class MonsterAndBulletExplosion implements GameObserver {
	
	/*
	 * This method comes from the GameObserver interface and is called by 
	 * the Game whenever an Entity is removed.
	 */
	public void onEntityRemoved(Game game, Entity entity) {
		// if the removed Entity is a Target (<- Monster) or a Bullet
		if (entity instanceof Target || entity instanceof BasicBullet) {
			// Create an explosion and set its size and position
			Explosion expl = new Explosion();
			expl.setSize(entity);
			expl.setPosition(entity);
			// Add the explosion to the game
			game.addEntity(expl);
		}
	}
	
}