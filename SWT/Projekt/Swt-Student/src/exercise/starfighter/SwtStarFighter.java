package exercise.starfighter;

import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.player.BasicBullet;
import edu.udo.cs.swtsf.core.player.Player;
import edu.udo.cs.swtsf.core.player.PlayerLaserSpriteFactory;
import edu.udo.cs.swtsf.core.player.PlayerSpriteFactory;
import edu.udo.cs.swtsf.view.GameInitializer;
import edu.udo.cs.swtsf.view.RgbColor;
import edu.udo.cs.swtsf.view.ViewManager;
import exercise.actions.SimpleRocket;
import exercise.hud.HudLaserUpgrade;
import exercise.hud.HudLife;
import exercise.hud.HudPickUpCount;
import exercise.hud.HudScore;
import exercise.lasers.LaserUpgradeDamage;
import exercise.monsters.MonsterEasy;
import exercise.monsters.MonsterSpriteFactory;
import exercise.pickups.PickUpDamage;
import exercise.pickups.PickUpLife;
import exercise.pickups.PickUpRocket;
import exercise.pickups.PickUpSpriteFactory;
import exercise.pickups.ScriptSpawnPickUps;

public class SwtStarFighter implements GameInitializer {
	
	public void beforeGameStart(ViewManager view) {
		view.setBackgroundImagePath("SpaceBackground");
		
		view.setFactoryForClass(Player.class, 
				new PlayerSpriteFactory());
		view.setFactoryForClass(BasicBullet.class, 
				new PlayerLaserSpriteFactory());
		view.setFactoryForClass(Explosion.class, 
				new ExplosionSpriteFactory());
		view.setFactoryForClass(TextMessage.class, 
				new TextMessageSpriteFactory());
		view.setFactoryForClass(MonsterEasy.class, 
				new MonsterSpriteFactory("Monsters/GreenMonster", 2, 800));
		view.setFactoryForClass(SimpleRocket.Rocket.class, 
				SimpleRocket.ROCKET_SPRITE_FACTORY);
		view.setFactoryForClass(PickUpLife.class, 
				new PickUpSpriteFactory("PickUps/LifePickUp"));
		view.setFactoryForClass(PickUpRocket.class, 
				new PickUpSpriteFactory("PickUps/RocketPickUp"));
		view.setFactoryForClass(PickUpDamage.class, 
				new PickUpSpriteFactory("PickUps/DamagePickUp"));
	}
	
	public void atGameStart(ViewManager view, Game game) {
		Player player = game.getPlayer();
		
		player.addPickUp(PickUpRocket.class, 1);
		player.addBehaviorStrategy(new SimpleRocket());
		
		// These control the default HUD (HUD := Heads-Up-Display) of the game
		// Adds a Score display to the HUD
		HudScore hudScore = new HudScore();
		player.addPlayerObserver(hudScore);
		view.addHudElement(hudScore);
		// Adds a Player-Life display to the HUD
		HudLife hudLife = new HudLife();
		player.addTargetObserver(hudLife);
		view.addHudElement(hudLife);
		// These display the number of PickUps of certain types owned by the player
		HudPickUpCount hudPickUpRocket = new HudPickUpCount(PickUpRocket.class);
		player.addPlayerObserver(hudPickUpRocket);
		view.addHudElement(hudPickUpRocket);
		
		// These display the number of LaserUpgrades of certain types owned by the player
		HudLaserUpgrade hudUpgradeDamage = new HudLaserUpgrade(view, LaserUpgradeDamage.class);
		player.addPlayerObserver(hudUpgradeDamage);
		
		// This makes sure the player can never move too far from the center of the coordinate system
		// This is not technically needed but quite useful for overall game difficulty
		player.addBehaviorStrategy(new PlayerSpaceBoundary());
		
		// Adds a script to the game which will display an explosion whenever a target or a bullet is 
		// destroyed in the game
		game.addObserver(new MonsterAndBulletExplosion());
		
		// These are stages of Monsters that are automatically being spawned by the game.
		game.addScript(new Stage1());
		// Shows blinking text across the screen
		view.showText("Brace Yourself! Monsters are coming!", 2, RgbColor.RED);
		
		// This script will create PickUps for the Player after destroying enough monsters
		ScriptSpawnPickUps spawnPickUps = new ScriptSpawnPickUps();
		spawnPickUps.definePickUp( 450,	g -> new PickUpLife());		// every 450 score spawn life +1
		spawnPickUps.definePickUp( 750,	g -> new PickUpRocket());	// every 750 score spawn rockets
		spawnPickUps.definePickUp(1000,	g -> new PickUpDamage());	// every 1000 score spawn a laser upgrade for damage
		game.getPlayer().addPlayerObserver(spawnPickUps);
	}
	
}