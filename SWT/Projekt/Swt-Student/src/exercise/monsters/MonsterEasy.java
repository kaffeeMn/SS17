package exercise.monsters;

public class MonsterEasy extends AbstractMonster {
	
	public MonsterEasy() {
		setMaxHitpoints(2);
		setHitpoints(getMaxHitpoints());
		
		delayBetweenAttacks = 50;
		damagePerHit = 1;
		maxSpeed = 3;
		acceleration = 0.05;
		
		addDefaultStrategies();
	};
	
}