package exercise.monsters;

public class MonsterSpeedup extends AbstractMonster{
    
    public class MonsterSpeedup(){
        maxSpeed = 1;
        damagePerHit = 2;
        setMaxHitpoints(8);
        setHitpoints(getMaxHitpoints()); 
        addDefaultStrategies();
    }
    //TODO: find method that is called when monster is hit
}
