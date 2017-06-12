package edu.udo.cs.swtsf.view;

import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.core.EntityBehaviorStrategy;
import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.GameScript;
import edu.udo.cs.swtsf.core.PickUp;
import edu.udo.cs.swtsf.core.Target;
import edu.udo.cs.swtsf.core.player.Player;

/**
 * <p>Implementations of this class are used to setup a {@link Game} with all 
 * of its {@link GameScript scripts}, {@link Target monsters}, {@link PickUp items} 
 * and {@link EntityBehaviorStrategy controls}. This interface is also responsible 
 * for setting up the {@link GraphicalElement graphical representations} of all game 
 * elements in the form of {@link GraphicalElementFactory factories}.</p>
 */
public interface GameInitializer {
	
	/**
	 * <p>This method is called before the game is properly started. 
	 * It allows the {@link GameInitializer} to 
	 * {@link ViewManager#setFactoryForClass(Class, GraphicalElementFactory) setup} 
	 * {@link GraphicalElementFactory factories} for game {@link Entity entities} 
	 * or to {@link ViewManager#addHudElement(HudElement) define} 
	 * {@link HudElement HudElements}.</p>
	 * <p>The {@link Game} itself does not yet exist when this method is called and 
	 * can not be modified at this point in time.</p>
	 * @param view		a non-null instance of ViewManager
	 * @see ViewManager
	 * @see GraphicalElementFactory
	 * @see GraphicalElement
	 * @see HudElement
	 * @see ViewManager#setBackgroundImagePath(String)
	 * @see ViewManager#addHudElement(HudElement)
	 * @see ViewManager#setFactoryForClass(Class, GraphicalElementFactory)
	 */
	public void beforeGameStart(ViewManager view);
	
	/**
	 * <p>This method is called immediately after the {@link Game} is initialized 
	 * and the {@link Player} entity has been {@link Game#getPlayer() created}.</p>
	 * <p>This method is used to setup all kinds of {@link GameScript scripts} 
	 * like the spawning of {@link Target monsters} or useful {@link PickUp items}. 
	 * This is also the time to add {@link EntityBehaviorStrategy additional game 
	 * play functionality} to the {@link Game#getPlayer() player instance}.</p>
	 * @param view		a non-null instance of ViewManager
	 * @param game		a non-null instance of Game
	 */
	public void atGameStart(ViewManager view, Game game);
	
}