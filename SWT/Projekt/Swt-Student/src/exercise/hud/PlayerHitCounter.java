package exercise.hud;

import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.Target;
import edu.udo.cs.swtsf.core.TargetObserver;
import edu.udo.cs.swtsf.view.HudElement;
import edu.udo.cs.swtsf.view.HudElementOrientation;
import edu.udo.cs.swtsf.view.ViewManager;

public class PlayerHitCounter extends HudElement implements TargetObserver{
    private int hitCount;

    public PlayerHitCounter(){
        setOrientation(HudElementOrientation.TOP);
        setImagePath("HUD/Counter");
        setImageCutout(0, 0, 32, 32);
    }

    public void onAfterHitpointsChanged(Target target, int data){
        String text = String.format("Hits: %1$s", ++this.hitCount);
        setText(text);
    }

    protected void afterAdded(ViewManager view, Game game){
        this.hitCount = 0;
    }
}
