package edu.udo.cs.swtsf.core.player;

import java.util.List;

import edu.udo.cs.swtsf.core.Bullet;
import edu.udo.cs.swtsf.core.BulletHitStrategy;
import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.core.EntityBehaviorStrategy;
import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.GameKey;
import edu.udo.cs.swtsf.core.Target;

/**
 * <p>This {@link EntityBehaviorStrategy} is used by the {@link Player} to implement the 
 * firing of the {@link Laser}. It {@link Game#isPressed(GameKey) checks} for {@link GameKey 
 * key} inputs, controls the {@link #laserCoolDownTime cooldown timer} and creates 
 * laser {@link Bullet Bullets} according to the {@link Player#getLaser() current Laser} of 
 * the player.</p>
 * <p>This strategy is also responsible for increasing the Players {@link Player#getScore() 
 * score} on a successful hit and for notifying all {@link PlayerObserver PlayerObservers} 
 * when the Player destroys a {@link Target}.</p>
 */
public class ShootLaserAction implements EntityBehaviorStrategy {
	
	/**
	 * Each destroyed {@link Target} gives this many points.
	 * @see #BULLET_SCORE_ON_HIT_STRAT
	 * @see Player#addScore(int)
	 */
	public static final int SCORE_BONUS_PER_MONSTER_HIT = 50;
	/**
	 * This {@link BulletHitStrategy} is responsible for adding the 
	 * {@link #SCORE_BONUS_PER_MONSTER_HIT} to the {@link Player#getScore() score} of 
	 * the {@link Player} on each hit.
	 */
	public static final BulletHitStrategy BULLET_SCORE_ON_HIT_STRAT = 
			new BulletScoreStrat(SCORE_BONUS_PER_MONSTER_HIT);
	/**
	 * The {@link GameKey} used to fire the {@link Laser}. This can be changed by 
	 * a key binding screen in the menu.
	 */
	public static GameKey INPUT_SHOOT_LASER = GameKey.SPACE;
	
	/**
	 * The time (in update cycles) between two consecutive shots.
	 * @see #act(Entity)
	 * @see Laser#getCooldownTime()
	 */
	private int laserCoolDownTime = 0;
	
	public void act(Entity self) {
		// self should always be a Player
		Player player = (Player) self;
		if (laserCoolDownTime > 0) {
			laserCoolDownTime--;
			return;
		}
		Game game = player.getCurrentGame();
		if (game.isPressed(INPUT_SHOOT_LASER)) {
			fireLaser(player);
		}
	}
	
	/**
	 * <p>Returns true if the cooldown has run out.</p>
	 * @return			{@link #laserCoolDownTime} == 0
	 */
	public boolean canFireLaser() {
		return laserCoolDownTime <= 0;
	}
	
	/**
	 * <p>Fires the {@link Laser} of the {@link Player}. Creates {@link Bullet Bullets}, positions 
	 * and initializes the Bullets and adds them to the {@link Game}. Also makes sure all 
	 * {@link PlayerObserver PlayerObservers} are notified of the fired Bullets.</p>
	 * @param player
	 */
	public void fireLaser(Player player) {
		Laser laser = player.getLaser();
		laserCoolDownTime = laser.getCooldownTime();
		
		int bulletLifeTime = laser.getBulletLifeTime();
		if (bulletLifeTime < 1) {
			bulletLifeTime = 1;
		}
		int bulletSize = laser.getBulletSize();
		if (bulletSize < 1) {
			bulletSize = 1;
		}
		int bulletDamage = laser.getDamage();
		if (bulletDamage < 0) {
			bulletDamage = 0;
		}
		double speed = laser.getBulletSpeed();
		if (speed < 0) {
			speed = 0;
		}
		Game game = player.getCurrentGame();
		
		List<Bullet> bullets = laser.createBullets(player);
		for (Bullet bullet : bullets) {
			bullet.setLifeTimer(bulletLifeTime);
			bullet.setSize(bulletSize);
			bullet.setDamage(bulletDamage);
			bullet.addHitStrategy(BULLET_SCORE_ON_HIT_STRAT);
			
			double bulletMoveDir = bullet.getRotation();
			double velBonusX = Entity.getOffsetX(bulletMoveDir, speed);
			double velBonusY = Entity.getOffsetY(bulletMoveDir, speed);
			bullet.addVelocity(velBonusX, velBonusY);
			
			game.addEntity(bullet);
		}
		laser.initializeBullets(bullets);
		player.fireLaserFired(bullets);
	}
	
	/**
	 * <p>Increments the {@link Player Players} {@link Player#getScore() score} on 
	 * each kill of a {@link Target}. Also notifies {@link PlayerObserver} instances 
	 * about a hit or a kill.</p>
	 */
	public static class BulletScoreStrat implements BulletHitStrategy {
		
		// How much the score is incremented on a kill
		private final int scoreBonus;
		
		public BulletScoreStrat(int scoreBonus) {
			this.scoreBonus = scoreBonus;
		}
		
		public void onHit(Bullet bullet, Target target) {
			Player player = (Player) bullet.getSourceEntity();
			if (target.isDead()) {
				player.addScore(scoreBonus);
				player.fireKilledEntity(target);
			} else {
				player.fireHitEntity(target);
			}
		}
		
	}
	
}