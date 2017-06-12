package edu.udo.cs.swtsf.core;

import edu.udo.cs.swtsf.core.player.Player;

/**
 * 
 */
public abstract class PickUp extends Entity {
	
	/**
	 * This {@link EntityCollisionStrategy} will check for a collision between the host, which 
	 * must be a {@link PickUp} and the {@link Player}. When a Player collides with the host 
	 * PickUp and neither the Player nor the host is disposed the PickUp will be disposed and 
	 * the {@link #onPickUp(Player)} method for the host PickUp will be called.<br>
	 * <br>
	 * 
	 * This is a default strategy for all PickUp's. It will automatically be added when the 
	 * PickUp's constructor is called.<br>
	 * 
	 * @see #onPickUp(Player)
	 * @see #addCollisionStrategy(EntityCollisionStrategy)
	 * @see #removeCollisionStrategy(EntityCollisionStrategy)
	 */
	public static final EntityCollisionStrategy PLAYER_PICK_UP_STRAT = 
			(self, other) -> 
	{
		if (!self.isDisposed() && !other.isDisposed() 
				&& other instanceof Player) 
		{
			Player player = (Player) other;
			PickUp pickUp = (PickUp) self;
			
			pickUp.onPickUp(player);
			pickUp.dispose();
		}
	};
	/**
	 * This {@link EntityBehaviorStrategy} will count down the {@link #lifeTimer} of a {@link PickUp} 
	 * until it reaches zero. When the lifeTimer has reached zero the PickUp will be disposed.<br>
	 * If the lifeTimer has a negative value nothing will happen.<br>
	 * <br>
	 * 
	 * This is a default strategy for all PickUp's. It will automatically be added when the 
	 * PickUp's constructor is called.<br>
	 * 
	 * @see #setLifeTimer(int)
	 * @see #getLifeTimer()
	 * @see #addBehaviorStrategy(EntityBehaviorStrategy)
	 * @see #removeBehaviorStrategy(EntityBehaviorStrategy)
	 */
	public static final EntityBehaviorStrategy PICKUP_LIFE_TIMER_STRAT = 
			(self) -> 
	{
		PickUp pickUp = (PickUp) self;
		int lifeTimer = pickUp.getLifeTimer();
		if (lifeTimer > 0) {
			pickUp.setLifeTimer(lifeTimer - 1);
		} else if (lifeTimer == 0) {
			pickUp.dispose();
		}
	};
	
	/**
	 * The {@link PickUp PickUp's} time to live. The {@link #PICKUP_LIFE_TIMER_STRAT} will subtract 1 from this 
	 * value until it reaches zero. When it reaches zero the Bullet will be disposed by the 
	 * {@link #PICKUP_LIFE_TIMER_STRAT}..<br>
	 */
	private int lifeTimer = -1;
	
	/**
	 * Initializes this {@link PickUp} with default values.<br>
	 * The size of a PickUp is 32 by default. A PickUp has a {@link TimedLife} behavior 
	 * with a life time of 1000. Finally the {@link #PLAYER_PICK_UP_STRAT} will also be 
	 * added to every PickUp.<br>
	 */
	{
		setRotation(-90);
		setSize(32);
		addCollisionStrategy(PLAYER_PICK_UP_STRAT);
		addBehaviorStrategy(PICKUP_LIFE_TIMER_STRAT);
	}
	
	/**
	 * This method is called by the {@link #PLAYER_PICK_UP_STRAT} when this {@link PickUp} 
	 * is collected by the {@link Player}. By default this method will add this PickUp to 
	 * the Player. A sub class may decide to overwrite this method to implement a different 
	 * behavior when picked up.<br>
	 * 
	 * @param player						the non-null Player which picked up this PickUp 
	 * @throws IllegalArgumentException		if player is null
	 */
	protected void onPickUp(Player player) {
		if (player == null) {
			throw new IllegalArgumentException("player == null");
		}
		player.addPickUp(this);
	}
	
	/**
	 * Sets the current {@link #lifeTimer} of this {@link PickUp}. The lifeTimer will be counted 
	 * down by 1 in each update cycle until it reaches 0. When the lifeTimer reaches 0 the PickUp 
	 * will be disposed. This will only happen if the {@link #PICKUP_LIFE_TIMER_STRAT} was added 
	 * to this PickUp. A negative value for the lifeTimer will deactivate the lifeTimer feature.<br>
	 * <br>
	 * 
	 * Please note that the {@link #PICKUP_LIFE_TIMER_STRAT} is added to all PickUp's by default. 
	 * To deactivate this feature for a custom PickUp implementation you have to remove the 
	 * {@link #PICKUP_LIFE_TIMER_STRAT} from the PickUp.<br>
	 * 
	 * @param value		any integer value. A positive value will set the time to live for this PickUp. 
	 * 					A value of zero will dispose this PickUp on the next update cycle. 
	 * 					Negative values will deactivate the time to live.
	 * @see #getLifeTimer()
	 * @see #PICKUP_LIFE_TIMER_STRAT
	 */
	public void setLifeTimer(int value) {
		lifeTimer = value;
	}
	
	/**
	 * Returns the current {@link #lifeTimer} of this {@link PickUp}. The lifeTimer will be counted 
	 * down by 1 in each update cycle until it reaches 0. When the lifeTimer reaches 0 the PickUp 
	 * will be disposed. This will only happen if the {@link #PICKUP_LIFE_TIMER_STRAT} was added 
	 * to this PickUp. A negative value for the lifeTimer will deactivate the lifeTimer feature.<br>
	 * <br>
	 * 
	 * Please note that the {@link #PICKUP_LIFE_TIMER_STRAT} is added to all PickUp's by default. 
	 * To deactivate this feature for a custom PickUp implementation you have to remove the 
	 * {@link #PICKUP_LIFE_TIMER_STRAT} from the PickUp.<br>
	 * 
	 * @return		any integer value. A positive value will set the time to live for this PickUp. 
	 * 				A value of zero will dispose this PickUp on the next update cycle. 
	 * 				Negative values will deactivate the time to live.
	 * @see #setLifeTimer(int)
	 * @see #PICKUP_LIFE_TIMER_STRAT
	 */
	public int getLifeTimer() {
		return lifeTimer;
	}
	
}