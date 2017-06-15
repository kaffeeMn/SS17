package exercise.starfighter;

import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.core.EntityBehaviorStrategy;
import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.player.Player;
import edu.udo.cs.swtsf.view.RgbColor;

public class Guard implements EntityBehaviorStrategy{
    
    public static final int COOLDOWN = 400;
    public static final int LIFESPAN = 100;
    private static final GameKey KEY = GameKey.G;
    private int coolDownTimer = 0;
    private int lifeSpan = 0;

    public void act(Entitiy host){
        if(lifeSpan > 0){
            // TODO: remove all damage
            --lifeSpan;
        }
        if(coolDownTimer > 0){
            --coolDownTimer;
            return;
        }
        if(game.isPressed(KEY)){
            lifeSpan = LIFESPAN;
            coolDownTimer = COOLDOWN;
        }
    }
}
