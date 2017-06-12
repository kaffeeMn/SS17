package exercise.hud;

import java.util.ArrayList;
import java.util.List;

import edu.udo.cs.swtsf.core.player.LaserUpgrade;
import edu.udo.cs.swtsf.core.player.Player;
import edu.udo.cs.swtsf.core.player.PlayerObserver;
import edu.udo.cs.swtsf.view.HudElement;
import edu.udo.cs.swtsf.view.HudElementOrientation;
import edu.udo.cs.swtsf.view.ViewManager;

public class HudLaserUpgrade implements PlayerObserver {
	
	private final List<HudElement> elems = new ArrayList<>();
	private final ViewManager view;
	private final Class<? extends LaserUpgrade> laserCls;
	private final String imgPath;
	
	public HudLaserUpgrade(ViewManager viewManager, 
			Class<? extends LaserUpgrade> laserClass) 
	{
		view = viewManager;
		laserCls = laserClass;
		imgPath = "HUD/"+laserClass.getSimpleName();
	}
	
	public void onLaserUpgradeAdded(Player player, LaserUpgrade upgrade) {
		if (laserCls.isInstance(upgrade)) {
			HudElement elem = new HudElement(HudElementOrientation.BOTTOM, imgPath, 0, 0, 32, 32);
			elems.add(elem);
			view.addHudElement(elem);
		}
	}
	
	public void onLaserUpgradeRemoved(Player player, LaserUpgrade upgrade) {
		if (laserCls.isInstance(upgrade)) {
			HudElement elem = elems.remove(elems.size() - 1);
			view.removeHudElement(elem);
		}
	}
}