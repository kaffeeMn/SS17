package exercise.starfighter;

import edu.udo.cs.swtsf.view.RgbColor;
import exercise.monsters.MonsterEasy;
import exercise.monsters.SpawnMonsters;

public class Stage1 extends SpawnMonsters {
	
	public Stage1() {
		setMaxMonstersAtOnce(5);
		setMonsterSpawnInterval(40);
		setSpawnDistanceToPlayer(300, 900);
		setTotalMonsterCount(50);
		setMonsterFactory((g) -> new MonsterEasy());
		
		setReactionAfterAllMonstersAreDead(g -> {
			g.addScript(new Stage2());
			g.addEntity(new TextMessage("Good Job!", RgbColor.GREEN, 2));
		});
	}
	
}