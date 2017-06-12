package edu.udo.cs.swtsf.core;

import java.util.ConcurrentModificationException;

/**
 * <p>GameScript's can be added to a {@link Game} via the 
 * {@link Game#addScript(GameScript)} method. They can be used to shape the game 
 * in a certain way and perform actions at timed intervals.</p>
 * <p>A GameScript {@link #onUpdate(Game) has a method} which is called exactly 
 * once at the very beginning of every update cycle of the Game. Use this method 
 * to perform game actions.</p>
 * <p>Any number of GameScript's may be added to a Game at once. They will all be 
 * executed in some arbitrary but deterministic way..<br>
 * GameScript's can be added to and removed from the Game during an update 
 * cycle without throwing any {@link ConcurrentModificationException 
 * ConcurrentModificationExceptions}.</p>
 * 
 * <p>This interface is an implementation of the command pattern, similar to the 
 * strategy pattern but with the difference that GameScript's are not integral to 
 * the functionality of the Game itself. They should rather be seen as additional 
 * code which is executed to augment the Game.</p>
 * 
 * @see Game
 * @see Game#addScript(GameScript)
 * @see Game#removeScript(GameScript)
 * @see Game#update()
 */
public interface GameScript {
	
	/**
	 * <p>This method is called exactly once at the very beginning of every update 
	 * cycle by the {@link Game}. The game will pass itself as argument to this 
	 * method.</p>
	 * <p>A GameScript may add additional GameScript's to the Game during the 
	 * execution of this method. A GameScript's may also remove itself or other 
	 * GameScript's from the Game during the execution of this method.</p>
	 * @param game
	 * @see Game#update()
	 */
	public void onUpdate(Game game);
	
}