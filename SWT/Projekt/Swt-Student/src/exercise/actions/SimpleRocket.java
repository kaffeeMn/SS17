package exercise.actions;

import edu.udo.cs.swtsf.core.Bullet;
import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.core.EntityBehaviorStrategy;
import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.GameKey;
import edu.udo.cs.swtsf.core.Target;
import edu.udo.cs.swtsf.core.player.Player;
import edu.udo.cs.swtsf.view.GraphicalElementFactory;
import edu.udo.cs.swtsf.view.Sprite;
import exercise.pickups.PickUpRocket;
import exercise.starfighter.Explosion;

public class SimpleRocket implements EntityBehaviorStrategy {
	
	// The minimum time you have to wait in between two uses of the Rocket
	public static final int COOLDOWN = 50;
	// The time the Rocket is moving before it explodes
	public static final int ROCKET_LIFE_TIME = 40;
	// The speed the Rocket is moving at
	public static final int ROCKET_SPEED = 4;
	// The size of the Rocket. Purely aesthetical
	public static final int ROCKET_SIZE = 24;
	// The radius of the explosion. All monsters within this radius around the Rocket will be damaged when it explodes
	public static final int EXPLOSION_RADIUS = 125;
	// The damage dealt to each monster in the explosion
	public static final int EXPLOSION_DAMAGE = 4;
	// The key to press to fire the Rocket
	public static final GameKey KEY = GameKey.R;
	
	// Counts down the time until the Rocket can be used again
	private int coolDownTimer = 0;
	
	// Called once in each update cycle. The host is typically the Player 
	// (or whatever Entity this behaviour was added to)
	public void act(Entity host) {
		// count down cooldown
		if (coolDownTimer > 0) {
			coolDownTimer--;
			return;
		}
		Player player = (Player) host;
		if (player.getPickUpCount(PickUpRocket.class) <= 0) {
			return;
		}
		
		Game game = host.getCurrentGame();
		// test for key press
		if (game.isPressed(KEY)) {
			// create Rocket, add it to game and add cooldown
			Rocket rocket = new Rocket(host);
			game.addEntity(rocket);
			coolDownTimer = COOLDOWN;
			player.removePickUp(PickUpRocket.class);
		}
	}
	
	// The Rocket implementation as a Bullet
	public static class Rocket extends Bullet {
		
		// sourceEntity is the Entity which created the Rocket, typically the Player
		public Rocket(Entity sourceEntity) {
			super(sourceEntity);
			setLifeTimer(ROCKET_LIFE_TIME);
			setSize(ROCKET_SIZE);
			setSpeedForward(ROCKET_SPEED);
			
			// We remove all the default strategies of Rocket. We use our own strategy to control the Rocket
			removeCollisionStrategy(BULLET_HIT_ON_COLLISION_STRAT);
			removeBehaviorStrategy(BULLET_LIFE_TIMER_STRAT);
			removeHitStrategy(BULLET_DAMAGE_ON_HIT_STRAT);
			
			// This is the Rocket functionality
			addBehaviorStrategy(host -> {
				Rocket rocket = (Rocket) host; // always a safe cast
				
				// We get the lifeTimer of the Rocket and count it down
				int lifeTime = rocket.getLifeTimer() - 1;
				rocket.setLifeTimer(lifeTime);
				if (lifeTime <= 0) {
					
					// LifeTimer has run out, Rocket will detonate now
					Game game = rocket.getCurrentGame();
					
					// This Explosion entity is purely cosmetic. It is used to create 
					// the visual representation of an Explosion on screen.
					Explosion expl = new Explosion(EXPLOSION_RADIUS * 2);
					expl.setPosition(rocket);
					game.addEntity(expl);
					
					// We iterate over all game Entities, pick the victims of the explosion 
					// and reduce their hitpoints
					game.getAllEntities()
						.without(rocket.getSourceEntity())
						.ofType(Target.class)
						.withinRadiusOfEntity(rocket, EXPLOSION_RADIUS)
						.forEach(t -> t.addHitpoints(-EXPLOSION_DAMAGE));
					
					// We dispose the Rocket. It is no longer needed.
					rocket.dispose();
				}
			});
		}
	}
	
	// This GraphicalElementFactory can be used to display a Rocket in the game
	public static final GraphicalElementFactory ROCKET_SPRITE_FACTORY = 
			(viewManager, rocket) -> {
				Sprite sprite = viewManager.newEntitySprite(rocket);
				sprite.setImageCutout(32*4, 0, 32, 32);
				sprite.setImagePath("Rocket");
				return sprite;
			};
}