package exercise.starfighter;

import edu.udo.cs.swtsf.view.RgbColor;
import exercise.monsters.MonsterEasy;
import exercise.monsters.SpawnMonsters;

public class Stage2 extends SpawnMonsters {
	
	public Stage2() {
		setMaxMonstersAtOnce(12);
		setMonsterSpawnInterval(20);
		setSpawnDistanceToPlayer(300, 900);
		setTotalMonsterCount(100);
		setMonsterFactory((g) -> new MonsterEasy());
		
		setReactionAfterAllMonstersAreDead(g -> {
			g.addEntity(new TextMessage("Congratulations!", RgbColor.GREEN, 2));
			g.getPlayer().destroy();
		});
	}
	
}