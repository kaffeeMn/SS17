package edu.udo.cs.swtsf.core.player;

import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.core.EntityBehaviorStrategy;
import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.GameKey;

/**
 * <p>This {@link EntityBehaviorStrategy} is used by the {@link Player} to implement the 
 * movement based on user input. It {@link Game#isPressed(GameKey) checks} for {@link GameKey 
 * key} inputs and changes the {@link Entity#getRotation() rotation} and {@link Entity#getSpeed() 
 * velocity} of the Player object accordingly.</p>
 */
public class MovePlayerAction implements EntityBehaviorStrategy {
	
	/**
	 * The {@link GameKey} used to move forward (speed up).
	 */
	public static GameKey INPUT_MOVE_FORWARD	= GameKey.UP;
	/**
	 * The {@link GameKey} used to slow down.
	 */
	public static GameKey INPUT_SLOW_DOWN		= GameKey.DOWN;
	/**
	 * The {@link GameKey} used to turn clockwise.
	 */
	public static GameKey INPUT_TURN_RIGHT		= GameKey.RIGHT;
	/**
	 * The {@link GameKey} used to turn counter-clockwise.
	 */
	public static GameKey INPUT_TURN_LEFT		= GameKey.LEFT;
	/**
	 * The speed (in angles degrees) at which the Player object is changing its rotation
	 */
	private double turnSpeed = 7;
	/**
	 * The maximum speed the Player can reach.
	 */
	private double moveMaxSpeed = 6;
	/**
	 * By how much (in percent) the Player can speed up per frame.
	 */
	private double moveSpeedGain = 0.10;
	/**
	 * By how much (in percent) the Player slows down per frame if not moving.
	 */
	private double regularSpeedLoss = 0.02;
	/**
	 * By how much (in percent) the Player slows down when actively slowing down.
	 */
	private double slowDownSpeedLoss = 0.08;
	
	private boolean moveForwardFlag = false;
	private boolean slowDownFlag = false;
	private boolean turnLeftFlag = false;
	private boolean turnRightFlag = false;
	
	public void act(Entity self) {
		Player player = (Player) self;
		Game game = player.getCurrentGame();
		
		// turning clockwise / counter-clockwise
		if (turnRightFlag || game.isPressed(INPUT_TURN_RIGHT)) {
			player.setRotation(player.getRotation() + turnSpeed);
		} else if (turnLeftFlag || game.isPressed(INPUT_TURN_LEFT)) {
			player.setRotation(player.getRotation() - turnSpeed);
		}
		
		// speeding up or slowing down
		if (moveForwardFlag || game.isPressed(INPUT_MOVE_FORWARD)) {
			speedPlayerUpBy(player, moveSpeedGain);
		} else if (slowDownFlag || game.isPressed(INPUT_SLOW_DOWN)) {
			slowPlayerDownBy(player, slowDownSpeedLoss);
		} else {
			slowPlayerDownBy(player, regularSpeedLoss);
		}
		moveForwardFlag = false;
		slowDownFlag = false;
		turnLeftFlag = false;
		turnRightFlag = false;
	}
	
	/**
	 * <p>Makes sure the {@link Player} will move forward in the same way as if the 
	 * user gave the {@link #INPUT_MOVE_FORWARD forward input}.</p>
	 */
	public void movePlayerForward() {
		moveForwardFlag = true;
	}
	
	/**
	 * <p>Makes sure the {@link Player} will be slowed down in the same way as if the 
	 * user gave the {@link #INPUT_SLOW_DOWN slow down input}.</p>
	 */
	public void slowPlayerDown() {
		slowDownFlag = true;
	}
	
	/**
	 * <p>Makes sure the {@link Player} will turn clockwise in the same way as if the 
	 * user gave the {@link #INPUT_TURN_RIGHT turn right input}.</p>
	 */
	public void turnPlayerClockwise() {
		turnRightFlag = true;
	}
	
	/**
	 * <p>Makes sure the {@link Player} will turn counter-clockwise in the same way 
	 * as if the user gave the {@link #INPUT_TURN_LEFT turn left input}.</p>
	 */
	public void turnPlayerCounterClockwise() {
		turnLeftFlag = true;
	}
	
	/**
	 * <p>Returns how the {@link Player} needs to turn in order to face the given targetAngle 
	 * within an error margin of epsilon. The returned value is 
	 * {@link TurnDirection#CLOCKWISE} if the player needs to turn clockwise, 
	 * {@link TurnDirection#COUNTER_CLOCKWISE} if the player needs to turn counter-clockwise, 
	 * or {@link TurnDirection#NONE} if the players rotation is already within the epsilon 
	 * distance of targetAngle and a rotation is not necessary.</p>
	 * @param player			the Player instance to turn
	 * @param targetAngle		the {@link Entity#getRotation() rotation} the Player should have
	 * @param epsilon			a margin of error between the current rotation and the targetAngle which is acceptable
	 * @return					either {@link TurnDirection#CLOCKWISE}, {@link TurnDirection#COUNTER_CLOCKWISE} 
	 * 							or {@link TurnDirection#NONE}
	 * 
	 * @see #turnPlayerClockwise()
	 * @see #turnPlayerCounterClockwise()
	 * @see #turnPlayerToAngle(Player, double)
	 */
	public TurnDirection getTurnDirectionTo(Player player, double targetAngle, double epsilon) {
		double curAngle = player.getRotation();
		
		double diff = (targetAngle - curAngle + 360) % 360;
		double diffAbs = Math.abs(targetAngle - curAngle);
		if (diffAbs < epsilon) {
			return TurnDirection.NONE;
		} else if (diff < 180) {
			return TurnDirection.CLOCKWISE;
		} else {
			return TurnDirection.COUNTER_CLOCKWISE;
		}
	}
	
	/**
	 * <p>Turns the {@link Entity#getRotation() rotation} of the {@link Player} towards 
	 * the target angle at a rate of the current {@link #turnSpeed}.</p>
	 * <p>Returns whether the rotation is already facing the target angle within a small 
	 * error margin.</p>
	 * @param player		the Player to turn
	 * @param angle			the target angle
	 * @return				true if the Player is now facing the angle; otherwise false
	 */
	public boolean turnPlayerToAngle(Player player, double angle) {
		player.turnTowards(angle, turnSpeed);
		return Math.abs(angle - player.getRotation()) < 0.1;
	}
	
	/**
	 * <p>Increases the {@link Player Players} current {@link Entity#getSpeed() speed} by 
	 * the given speedGain. This method will never increase the Players speed above the 
	 * current {@link #moveMaxSpeed maximum speed}.</p>
	 * @param player			the Player to speed up
	 * @param speedGain			by how much the Player is sped up
	 */
	public void speedPlayerUpBy(Player player, double speedGain) {
		double currentSpeed = player.getSpeed();
		player.addSpeedForward(speedGain);
		if (currentSpeed > moveMaxSpeed) {
			double factor = moveMaxSpeed / currentSpeed;
			player.multiplyVelocity(factor);
		}
		player.setHasMoveInput(true);
	}
	
	/**
	 * <p>Decreases the {@link Player Players} current {@link Entity#getSpeed() speed} by 
	 * the given slowDown.</p>
	 * @param player			the Player to slow down
	 * @param slowDown			by how much the Player is slowed down
	 */
	public void slowPlayerDownBy(Player player, double slowDown) {
		double currentSpeed = player.getSpeed();
		if (currentSpeed > 0) {
			double factor = (currentSpeed - slowDown) / currentSpeed;
			if (factor < 0) {
				player.setVelocity(0, 0);
			} else {
				player.multiplyVelocity(factor);
			}
			player.setHasMoveInput(false);
		}
	}
	
	/**
	 * This represents turn directions.
	 */
	public static enum TurnDirection {
		NONE,
		CLOCKWISE,
		COUNTER_CLOCKWISE,
		;
	}
	
}