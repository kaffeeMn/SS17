package exercise.monsters;

import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.core.EntityBehaviorStrategy;
import edu.udo.cs.swtsf.core.EntityCollisionStrategy;
import edu.udo.cs.swtsf.core.Target;
import edu.udo.cs.swtsf.core.player.Player;

public class AbstractMonster extends Target {
	
	// the damage dealt on a collision
	protected int damagePerHit;
	// the time in between collisions which can deal damage
	protected int delayBetweenAttacks;
	// the maximum speed possible
	protected double maxSpeed;
	// the acceleration per update cycle
	protected double acceleration;
	
	protected DamagePlayerOnCollision damageStrat;
	protected MoveTowardsPlayer moveStrat;
	
	{
		setSize(32);
	}
	
	protected void addDefaultStrategies() {
		damageStrat = new DamagePlayerOnCollision();
		moveStrat = new MoveTowardsPlayer();
		addBehaviorStrategy(damageStrat);
		addCollisionStrategy(damageStrat);
		addBehaviorStrategy(moveStrat);
	}
	
	protected void removeDefaultStrategies() {
		removeBehaviorStrategy(damageStrat);
		removeCollisionStrategy(damageStrat);
		removeBehaviorStrategy(moveStrat);
		damageStrat = null;
		moveStrat = null;
	}
	
	/*
	 * This Strategy is used by different kinds of monsters in the game to cause 
	 * damage to the Player when the Player and the Monster collide.
	 * 
	 * This class is both a CollisionStrategy and BehaviorStrategy. The CollisionStrategy 
	 * is used to be notified of collisions and deal damage. The BehaviorStrategy is 
	 * needed to count the time in between collisions to give the player a little bit of 
	 * time to get away before more damage is dealt.
	 */
	public class DamagePlayerOnCollision 
		implements EntityCollisionStrategy, EntityBehaviorStrategy 
	{
		// counts down the time until damage can be dealt again
		private int damageTimer;
		
		public void act(Entity self) {
			if (damageTimer > 0) {
				damageTimer--;
			}
		}
		
		public void onCollisionWith(Entity self, Entity other) {
			if (damageTimer <= 0) {
				// check if this is a collision with the player
				if (other == self.getCurrentGame().getPlayer()) {
					Player player = (Player) other;
					// adding negative hitpoints will decrease the current hitpoints
					player.addHitpoints(-damagePerHit);
					// set timer to wait until damage can be dealt again
					damageTimer = delayBetweenAttacks;
				}
			}
		}
	}
	
	/*
	 * This strategy is used to control the movement of monsters. 
	 * This BehaviorStrategy will make the host move towards the player of the game. 
	 * The host will start with a movement speed of 0.0 and accelerate until the 
	 * maximum speed is reached. If the player comes within collision distance to 
	 * the host the host will stop moving momentarily.
	 * 
	 * If the player is dead or not set the host will stop. 
	 */
	public class MoveTowardsPlayer implements EntityBehaviorStrategy {
		
		public void act(Entity self) {
			// get the player instance
			Player player = self.getCurrentGame().getPlayer();
			if (player != null && player.isAlive()) {
				// calculate distance to the player
				double distanceToPlayer = self.getDistanceTo(player);
				if (distanceToPlayer < player.getSize()) {
					// player is within collision distance => stop movement
					self.setVelocity(0, 0);
				} else {
					// calculate new movement speed
					double currentSpeed = Math.min(maxSpeed, self.getSpeed() + acceleration);
					// issue the move command to the host
					self.moveTowards(player, currentSpeed);
				}
			} else {
				// no player => stop movement
				self.setVelocity(0, 0);
			}
		}
		
	}
	
}