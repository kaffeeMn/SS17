package exercise.monsters;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.core.EntityObserver;
import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.GameScript;
import edu.udo.cs.swtsf.core.player.Player;

/*
 * This script can be used to spawn monsters continuously at random 
 * locations around the players current position.
 * The script can be set up to never spawn more than a certain number 
 * of monsters at once or to wait a certain time between spawning 
 * monsters.
 * 
 * The script can be set to spawn only a certain number of monsters 
 * in total. It may call a callback in case all monsters have been 
 * spawned and subsequently removed (killed) from the game.
 */
public class SpawnMonsters implements GameScript {
	
	// This observer is added to all monsters created by this script
	private final EntityObserver monsterObs = new EntityObserver() {
		public void onRemovedFromGame(Game game, Entity host) {
			// Registers when the monster is removed from the game
			SpawnMonsters.this.onMonsterRemoved(host);
		}
	};
	// Used to generate random numbers for the monster positions
	private final Random random				= new Random();
	// Used to generate the monsters to be spawned
	private Function<Game, Entity> monsterFact		= (game) -> new MonsterEasy();
	// Called after all monsters in this script have been spawned and removed.
	private Consumer<Game> afterEffect		= null;
	// The number of monsters currently in the game
	private int monstersInGame				= 0;
	// The maximum number of monsters that can be spawned
	private int totalMonsterCount			= 20;
	// The maximum number of monsters spawned simultaneously
	private int maxMonstersInGame			= 5;
	// The time in between spawning monsters
	private int spawnInterval				= 40;
	// This timer counts down until the next monster can be spawned
	private int spawnTimer					= 0;
	// How close a spawned monster can be relative to the players position
	private int spawnMinDistanceToPlayer	= 300;
	// How far away a spawned monster can be relative to the players position
	private int spawnMaxDistanceToPlayer	= 900;
	
	public void setTotalMonsterCount(int value) {
		if (value < 1) {
			throw new IllegalArgumentException("value="+value);
		}
		totalMonsterCount = value;
	}
	
	public void setMaxMonstersAtOnce(int value) {
		if (value < 1) {
			throw new IllegalArgumentException("value="+value);
		}
		maxMonstersInGame = value;
	}
	
	public void setMonsterSpawnInterval(int value) {
		if (value < 1) {
			throw new IllegalArgumentException("value="+value);
		}
		spawnInterval = value;
	}
	
	public void setSpawnDistanceToPlayer(int minimum, int maximum) {
		if (minimum < 1 || maximum < minimum) {
			throw new IllegalArgumentException("minimum="+minimum+"; maximum="+maximum);
		}
		spawnMinDistanceToPlayer = minimum;
		spawnMaxDistanceToPlayer = maximum;
	}
	
	public void setMonsterFactory(Function<Game, Entity> value) {
		if (value == null) {
			throw new IllegalArgumentException("value == null");
		}
		monsterFact = value;
	}
	
	public void setReactionAfterAllMonstersAreDead(Consumer<Game> value) {
		afterEffect = value;
	}
	
	public void onUpdate(Game game) {
		// check if any more monsters are supposed to be spawned
		if (totalMonsterCount <= 0) {
			// check if all spawned monsters have already been removed
			if (monstersInGame <= 0) {
				// remove this script from the game
				game.removeScript(this);
				// call the afterEffect if it has been set
				if (afterEffect != null) {
					afterEffect.accept(game);
				}
			}
			// We stop here because no more monsters need to be spawned
			return;
		}
		// check if we can spawn more monsters
		if (monstersInGame < maxMonstersInGame) {
			spawnTimer++;
			// check if the time has come to spawn a monster
			if (spawnTimer >= spawnInterval) {
				Player player = game.getPlayer();
				// generate the random monster location based on the current player position
				double monsterX = getRandomXNearPlayer(player);
				double monsterY = getRandomYNearPlayer(player);
				
				// Create the monster
				Entity monster = monsterFact.apply(game);
				monster.addObserver(monsterObs);
				monster.setPosition(monsterX, monsterY);
				game.addEntity(monster);
				
				monstersInGame++;
				totalMonsterCount--;
				
				spawnTimer = 0;
			}
		}
	}
	
	private double getRandomXNearPlayer(Player player) {
		double offset = getRandomOffset(
				spawnMinDistanceToPlayer, 
				spawnMaxDistanceToPlayer);
		if (random.nextBoolean()) {
			offset = -offset;
		}
		return player.getX() + offset;
	}
	
	private double getRandomYNearPlayer(Player player) {
		double offset = getRandomOffset(
				spawnMinDistanceToPlayer, 
				spawnMaxDistanceToPlayer);
		if (random.nextBoolean()) {
			offset = -offset;
		}
		return player.getY() + offset;
	}
	
	private double getRandomOffset(int min, int max) {
		return min + random.nextInt(max - min + 1);
	}
	
	// called from the monsterObs when a Monster is removed from the game
	private void onMonsterRemoved(Entity monster) {
		monster.removeObserver(monsterObs);
		monstersInGame--;
	}
	
}