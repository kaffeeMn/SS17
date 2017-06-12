package exercise.starfighter;

import edu.udo.cs.swtsf.core.Entity;

/**
 * Instances of this class are used to display explosions on the screen.
 */
public class Explosion extends Entity {
	
	/**
	 * Creates a new Explosion with the default size.
	 */
	public Explosion() {
		super();
	}
	
	/**
	 * <p>Creates an explosion with the given size.</p>
	 * Equal to:
	 * <pre>
	 * Explosion expl = new Explosion();
	 * expl.setSize(size);</pre>
	 * @param size
	 */
	public Explosion(int size) {
		this();
		setSize(size);
	}
	
	/**
	 * <p>Creates an explosion with the given size and velocity.</p>
	 * Equal to:
	 * <pre>
	 * Explosion expl = new Explosion();
	 * expl.setSize(size);
	 * expl.setVelocity(velocityX, velocityY);
	 * </pre>
	 * @param size
	 */
	public Explosion(int size, double velocityX, double velocityY) {
		this(size);
		setVelocity(velocityX, velocityY);
	}
	
	/**
	 * Always returns true. An explosion is not meant to last.
	 */
	public boolean isDisposed() {
		return true;
	}
}