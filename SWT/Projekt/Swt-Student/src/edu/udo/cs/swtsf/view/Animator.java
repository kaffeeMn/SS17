package edu.udo.cs.swtsf.view;

/**
 * <p>An {@link Animator} controls the animation of one or more 
 * {@link GraphicalElement GraphicalElement's}. The 
 * {@link #updateAnimation(GraphicalElement, long)} method of an Animator 
 * is called periodically by the {@link ViewManager} and is given the 
 * {@link GraphicalElement} it is associated to and the time in milliseconds 
 * since the last update as arguments.</p>
 * 
 * <p>This interface is an implementation of the strategy pattern.</p>
 * 
 * @see GraphicalElement#setAnimator(Animator)
 */
public interface Animator {
	
	/**
	 * <p>Called periodically by the {@link ViewManager} to update the animation 
	 * of a {@link GraphicalElement}. This method is called for each GraphicalElement 
	 * that this Animator is attached to.</p>
	 * @param element					a GraphicalElement this Animator is attached to
	 * @param millisSinceLastUpdate		the approximate amount of milliseconds passed 
	 * 									since the last call to this method
	 */
	public void updateAnimation(GraphicalElement element, 
			long millisSinceLastUpdate);
	
}