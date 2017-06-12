package edu.udo.cs.swtsf.core.player;

import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.core.Target;
import edu.udo.cs.swtsf.view.GraphicalElement;
import edu.udo.cs.swtsf.view.GraphicalElementFactory;
import edu.udo.cs.swtsf.view.Sprite;
import edu.udo.cs.swtsf.view.SpriteSet;
import edu.udo.cs.swtsf.view.ViewManager;

/**
 * <p>This {@link GraphicalElementFactory} can be used to display the {@link Player} of the game. 
 * It includes animations when moving or when the Player is protected by a shield and made 
 * {@link Target#setHitpointsReadOnly(boolean) invulnerable}.</p>
 * <p>This factory can only be used for instances of Player. It should not be used for 
 * any other type of {@link Entity}.</p>
 */
public class PlayerSpriteFactory implements GraphicalElementFactory {
	
	public static final String PLAYER_SPRITE_PATH = "Player";
	public static final String BOOSTER_SPRITE_PATH = "PlayerBooster";
	public static final String SMOKE_PUFF_SPRITE_PATH = "PuffOfSmoke";
	public static final int TIME_BETWEEN_SMOKE_PUFFS = 4;
	public static final double SMOKE_PUFF_MIN_SPEED = 2;
	public static final int CUTOUT_SIZE = 32;
	
	public GraphicalElement createForEntity(ViewManager view, Entity entity) {
		// We encapsulate the complex animation of the player within a new object
		return new PlayerAnimator(view, entity).spriteSet;
	}
	
	// This class exists only as a connection between the different animations and graphical elements
	private static class PlayerAnimator {
		
		private final Player player;
		// The parent node for shipSprite and boosterSprite. Animated by animateSpriteSet
		private final SpriteSet spriteSet;
		// the sprite for the ship itself
		private final Sprite shipSprite;
		// the sprite for the red flashing boosters used to animate the movement of the player
		private final Sprite boosterSprite;
		private boolean wasMoving;
		// the time until a new smoke puff is created during movement
		private int smokeTimer;
		// the shipSprite is toggling between being visible and invisible while the player can not be hurt
		private long shipVisibleTimer = 0;
		private boolean visible = true;
		
		public PlayerAnimator(ViewManager view, Entity entity) {
			this.player = (Player) entity;
			
			spriteSet = view.newEntitySpriteSet(entity);
			shipSprite = view.newSprite();
			boosterSprite = view.newSprite();
			
			shipSprite.setImagePath(PLAYER_SPRITE_PATH);
			shipSprite.setImageCutoutWidth(CUTOUT_SIZE);
			shipSprite.setImageCutoutHeight(CUTOUT_SIZE);
			shipSprite.setParent(spriteSet);
			// Makes the ship sprite flash in and out over time iff the player is invulnerable (isHitpointsReadOnly())
			shipSprite.setAnimator(this::animateShip);
			
			boosterSprite.setImagePath(BOOSTER_SPRITE_PATH);
			boosterSprite.setImageCutoutWidth(CUTOUT_SIZE);
			boosterSprite.setImageCutoutHeight(CUTOUT_SIZE);
			
			spriteSet.setAnimator(this::animateSpriteSet);
		}
		
		// Toggles the visibility of the player while the player can not be hurt.
		private void animateShip(GraphicalElement element, long millisSinceLastUpdate) {
			if (!player.isHitpointsReadOnly()) {
				shipVisibleTimer = 0;
				if (!visible) {
					visible = true;
					element.setScale(1);
				}
				return;
			}
			shipVisibleTimer += millisSinceLastUpdate;
			if (shipVisibleTimer >= 50) {
				shipVisibleTimer %= 50;
				visible = !visible;
			}
			// visibility is controlled by setting the scale to 1 or 0
			// a scale of 0 makes the sprite invisible
			element.setScale(visible ? 1 : 0);
		}
		
		private void animateSpriteSet(GraphicalElement element, long millisSinceLastUpdate) {
			// we add or remove the boosterSprite based on whether the player is moving or not
			boolean isMoving = player.hasMoveInput();
			if (isMoving != wasMoving) {
				if (isMoving) {
					boosterSprite.setParent(spriteSet);
				} else {
					boosterSprite.setParent(null);
				}
				wasMoving = isMoving;
			}
			// If the player is moving we periodically create a trail of smoke puffs behind the ship
			if (isMoving) {
				if (smokeTimer > 0) {
					smokeTimer--;
				} else if (player.getSpeed() >= SMOKE_PUFF_MIN_SPEED) {
					smokeTimer = TIME_BETWEEN_SMOKE_PUFFS;
					
					ViewManager viewMngr = spriteSet.getViewManager();
					Sprite smokePuffSprite = viewMngr.newSprite();
					smokePuffSprite.setAnimator(new SmokePuffAnimator());
					smokePuffSprite.setImagePath(SMOKE_PUFF_SPRITE_PATH);
					smokePuffSprite.setImageCutoutWidth(CUTOUT_SIZE);
					smokePuffSprite.setImageCutoutHeight(CUTOUT_SIZE);
					
					double rotation = player.getRotation();
					double playerSize = player.getSize();
					double direction = rotation + 180;
					double offsetX = Entity.getOffsetX(direction, playerSize / 2);
					double offsetY = Entity.getOffsetY(direction, playerSize / 2);
					
					smokePuffSprite.setTranslation(
							spriteSet.getTranslateX() + offsetX, 
							spriteSet.getTranslateY() + offsetY);
					smokePuffSprite.setScale(0.4);
					smokePuffSprite.setRotation(rotation);
					
					viewMngr.addSprite(smokePuffSprite);
				}
			}
		}
	}
}