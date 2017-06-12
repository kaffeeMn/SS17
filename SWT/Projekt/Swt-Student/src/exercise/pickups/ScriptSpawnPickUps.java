package exercise.pickups;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.PickUp;
import edu.udo.cs.swtsf.core.Target;
import edu.udo.cs.swtsf.core.player.Player;
import edu.udo.cs.swtsf.core.player.PlayerObserver;

/*
 * This PlayerObserver can be used to spawn PickUps when the players score changes.
 * Each type of PickUp can be tied to a certain score-requirement. Whenever the players 
 * score reaches a multiple of that requirement a PickUp of the specified type will be 
 * created at the location of the last Target killed by the player.
 * 
 * If the players current score is a multiple of the score-requirements for different 
 * PickUps, only the PickUp with the highest score requirement will be spawned.
 * 
 * For each type of PickUp a maximum spawn number can be defined. The PickUp will only 
 * be spawned that often by this script.
 */
public class ScriptSpawnPickUps implements PlayerObserver {
	
	/*
	 * Contains all PickUp types and their score-requirements
	 */
	private final List<SpawnPickUpData> dataList = new ArrayList<>();
	
	public void definePickUp(int scoreRequirement, Function<Game, PickUp> pickUpFact) {
		definePickUp(scoreRequirement, -1, pickUpFact);
	}
	
	public void definePickUp(int scoreRequirement, int maxCount, Function<Game, PickUp> pickUpFact) {
		if (pickUpFact == null) {
			throw new IllegalArgumentException("pickUpFact == null");
		}
		if (maxCount < 0 && maxCount != -1) {
			throw new IllegalArgumentException("maxCount="+maxCount);
		}
		// Create the entry for the dataList
		SpawnPickUpData newData = new SpawnPickUpData(scoreRequirement, pickUpFact, maxCount);
		
		// Check for PickUp data with equal scoreRequirement
		if (dataList.contains(newData)) {
			throw new IllegalArgumentException("scoreRequirement of '"
					+scoreRequirement+"' already defined");
		}
		// add new data and sort list
		dataList.add(newData);
		dataList.sort((d1, d2) -> Integer.compare(d1.scoreReq, d2.scoreReq));
	}
	
	// Called by the player when an Entity (Monster) was destroyed by the player
	@Override
	public void onPlayerKilledTarget(Player player, Target target) {
		int score = player.getScore();
		// player has no score
		if (score <= 0) {
			return;
		}
		// find highest value PickUp data for score
		SpawnPickUpData pickUpData = null;
		for (SpawnPickUpData data : dataList) {
			if (score % data.scoreReq == 0 
					&& (data.maxCount == -1 || data.maxCount > 0)) {
				pickUpData = data;
				break;
			}
		}
		// no PickUp data for score found => abort
		if (pickUpData == null) {
			return;
		}
		
		double nextPickUpX = target.getX();
		double nextPickUpY = target.getY();
		double nextPickUpVelX = target.getVelocityX() / 2;
		double nextPickUpVelY = target.getVelocityY() / 2;
		// maxCount is either -1 (for infinite spawns) or above 0
		if (pickUpData.maxCount > 0) {
			pickUpData.maxCount--;
		}
		// Create the PickUp and add it to the game
		PickUp nextPickUp = pickUpData.pickUpFact.apply(player.getCurrentGame());
		nextPickUp.setPosition(nextPickUpX, nextPickUpY);
		nextPickUp.setVelocity(nextPickUpVelX, nextPickUpVelY);
		player.getCurrentGame().addEntity(nextPickUp);
	}
	
	private static class SpawnPickUpData {
		// This needs to be final as it defines equality
		public final int scoreReq;
		// This should never be null
		public final Function<Game, PickUp> pickUpFact;
		// This is -1 if an infinite amount of PickUps can be spawned
		public int maxCount;
		
		public SpawnPickUpData(int scoreRquirement, Function<Game, PickUp> pickUpFactory, int maxCount) {
			scoreReq = scoreRquirement;
			pickUpFact = pickUpFactory;
			this.maxCount = maxCount;
		}
		
		// Equality is defined by the scoreReq field
		public int hashCode() {
			return scoreReq;
		}
		
		public boolean equals(Object obj) {
			if (!(obj instanceof SpawnPickUpData)) {
				return false;
			}
			SpawnPickUpData other = (SpawnPickUpData) obj;
			return scoreReq == other.scoreReq;
		}
	}
	
}