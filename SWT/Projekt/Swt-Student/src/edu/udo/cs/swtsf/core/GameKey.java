package edu.udo.cs.swtsf.core;

/**
 * <p>All keys which the game accepts as input. If a key is not listed here it can not be 
 * used as an input to the game.</p>
 * 
 * @see Game#isPressed(GameKey)
 */
public enum GameKey {
	
	// The regular letter keys
	A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z,
	// The number keys both from the row at the top of the keyboard and from the numblock
	_0, _1, _2, _3, _4, _5, _6, _7, _8, _9, 
	// Control keys
	ESCAPE, SPACE, ENTER, CTRL, SHIFT, ALT, DELETE, BACKSPACE,
	// The arrow keys
	LEFT, RIGHT, UP, DOWN,
	// Mouse buttons
	MOUSE_LEFT, MOUSE_RIGHT, 
	;
	
}