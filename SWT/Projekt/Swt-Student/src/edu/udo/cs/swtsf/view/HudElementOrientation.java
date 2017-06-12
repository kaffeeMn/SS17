package edu.udo.cs.swtsf.view;

/**
 * <p>Defines how {@link HudElement HudElements} are placed on the screen. 
 * Use the values of this enum as arguments to 
 * {@link HudElement#setOrientation(HudElementOrientation)} and 
 * {@link HudElement#HudElement(HudElementOrientation, String, int, int, int, int)}</p>
 */
public enum HudElementOrientation {
	
	/**
	 * <p>The HudElement will be placed on the top of the screen 
	 * going from the left to the right.</p>
	 */
	TOP,
	/**
	 * <p>The HudElement will be placed on the bottom of the screen 
	 * going from the left to the right.</p>
	 */
	BOTTOM,
	;
	
}