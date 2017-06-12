package edu.udo.cs.swtsf.swing;

import java.awt.EventQueue;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EnumSet;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.GameKey;
import edu.udo.cs.swtsf.core.GameObserver;
import edu.udo.cs.swtsf.swing.SwingHighScoreScreen.HighScore;
import edu.udo.cs.swtsf.swing.game.SwingPainter;
import edu.udo.cs.swtsf.view.GameInitializer;
import exercise.starfighter.SwtStarFighter;

public class SwingPlayScreen extends SwingGameScreen {
	
	private static final int TIMER_UPDATE_DELAY_MS = 25;
	
	private final Set<GameKey> keysPressed = EnumSet.noneOf(GameKey.class);
	private final Set<GameKey> keysReleased = EnumSet.noneOf(GameKey.class);
	private final SwingPainter painter;
	private final Timer updateTimer;
	
	private final Game game;
	private Timer gameEndTimer;
	private long lastFrameTimeStamp;
	
	public SwingPlayScreen() {
		painter = new SwingPainter(this);
		painter.getAsJComponent().setFocusable(true);
		painter.getAsJComponent().addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				SwingPlayScreen.this.onKeyPressed(e);
			}
			public void keyReleased(KeyEvent e) {
				SwingPlayScreen.this.onKeyReleased(e);
			}
		});
		painter.getAsJComponent().addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				onMousePressed(e);
			}
			public void mouseReleased(MouseEvent e) {
				onMouseReleased(e);
			}
		});
		
		updateTimer = new Timer(TIMER_UPDATE_DELAY_MS, 
				(e) -> onTimerTick());
		updateTimer.setRepeats(true);
		updateTimer.start();
		
		GameInitializer initializer = new SwtStarFighter();
		initializer.beforeGameStart(painter);
		game = new Game(painter.getPlayerFactory());
		game.addObserver(new GameObserver() {
			public void onEntityAdded(Game game, Entity entity) {
				painter.addSpriteFor(entity);
			}
			public void onEntityRemoved(Game game, Entity entity) {
				painter.removeSpriteOf(entity);
			}
		});
		game.getAllEntities().forEach(e -> painter.addSpriteFor(e));
//		game.forEachEntity((entity) -> painter.addSpriteFor(entity));
		initializer.atGameStart(painter, game);
		
		painter.setPlayer(game.getPlayer());
		game.addObserver(new GameObserver() {
			public void onEntityRemoved(Game game, Entity entity) {
				if (entity == game.getPlayer()) {
					onPlayerDeath();
				}
			}
		});
		
		EventQueue.invokeLater(() -> painter.getAsJComponent().requestFocusInWindow());
		
		setJComponent(painter.getAsJComponent());
		
		lastFrameTimeStamp = System.currentTimeMillis();
	}
	
	private void onPlayerDeath() {
		gameEndTimer = new Timer(5000, (e) -> goToHighScoreScreen());
		gameEndTimer.setRepeats(false);
		gameEndTimer.start();
	}
	
	private void goToHighScoreScreen() {
		gameEndTimer = null;
		int score = game.getPlayer().getScore();
		HighScore highScore = new HighScore("", score);
		getSwtGameMain().setScreen(
				new SwingHighScoreScreen(highScore));
	}
	
	public Game getGame() {
		return game;
	}
	
	public void initialize() {
		updateTimer.start();
	}
	
	public void terminate() {
		updateTimer.stop();
		if (gameEndTimer != null) {
			gameEndTimer.stop();
		}
	}
	
	public void pause() {
		updateTimer.stop();
	}
	
	public void unpause() {
		updateTimer.start();
	}
	
	private void onTimerTick() {
		for (GameKey key : keysPressed) {
			game.bufferKey(key);
		}
		calculateMousePosition();
		
		keysPressed.removeAll(keysReleased);
		keysReleased.clear();
		
		game.update();
		painter.panCameraToEntity(painter.getPlayer());
		
		long previousTimeStamp = lastFrameTimeStamp;
		lastFrameTimeStamp = System.currentTimeMillis();
		long timeSince = lastFrameTimeStamp - previousTimeStamp;
		
		painter.updateAnimations(timeSince);
	}
	
	private void calculateMousePosition() {
		double cameraX = painter.getCameraCenterX();
		double cameraY = painter.getCameraCenterY();
		
		Point mousePos = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(mousePos, painter.getAsJComponent());
		game.setMousePosition(mousePos.x - cameraX, mousePos.y - cameraY);
	}
	
	private void onClickRestart() {
		int choice = JOptionPane.showConfirmDialog(
				painter.getAsJComponent(), 
				"Do you want to restart the game?", 
				"Restart", JOptionPane.YES_NO_OPTION);
		
		if (choice == JOptionPane.YES_OPTION) {
			getSwtGameMain().setScreen(new SwingPlayScreen());
		}
	}
	
	private void onMousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			keysPressed.add(GameKey.MOUSE_LEFT);
		} else if (SwingUtilities.isRightMouseButton(e)) {
			keysPressed.add(GameKey.MOUSE_RIGHT);
		}
	}
	
	private void onMouseReleased(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			keysReleased.add(GameKey.MOUSE_LEFT);
		} else if (SwingUtilities.isRightMouseButton(e)) {
			keysReleased.add(GameKey.MOUSE_RIGHT);
		}
	}
	
	private void onKeyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_F12) {
			onClickRestart();
			return;
		}
		GameKey gameInput = forAwtKey(e.getKeyCode());
		if (gameInput != null) {
			keysPressed.add(gameInput);
		}
	}
	
	private void onKeyReleased(KeyEvent e) {
		GameKey gameInput = forAwtKey(e.getKeyCode());
		if (gameInput != null) {
			keysReleased.add(gameInput);
		}
	}
	
	private static GameKey forAwtKey(int awtKeyCode) {
		switch (awtKeyCode) {
			case KeyEvent.VK_A: return GameKey.A;
			case KeyEvent.VK_B: return GameKey.B;
			case KeyEvent.VK_C: return GameKey.C;
			case KeyEvent.VK_D: return GameKey.D;
			case KeyEvent.VK_E: return GameKey.E;
			case KeyEvent.VK_F: return GameKey.F;
			case KeyEvent.VK_G: return GameKey.G;
			case KeyEvent.VK_H: return GameKey.H;
			case KeyEvent.VK_I: return GameKey.I;
			case KeyEvent.VK_J: return GameKey.J;
			case KeyEvent.VK_K: return GameKey.K;
			case KeyEvent.VK_L: return GameKey.L;
			case KeyEvent.VK_M: return GameKey.M;
			case KeyEvent.VK_N: return GameKey.N;
			case KeyEvent.VK_O: return GameKey.O;
			case KeyEvent.VK_P: return GameKey.P;
			case KeyEvent.VK_Q: return GameKey.Q;
			case KeyEvent.VK_R: return GameKey.R;
			case KeyEvent.VK_S: return GameKey.S;
			case KeyEvent.VK_T: return GameKey.T;
			case KeyEvent.VK_U: return GameKey.U;
			case KeyEvent.VK_V: return GameKey.V;
			case KeyEvent.VK_W: return GameKey.W;
			case KeyEvent.VK_X: return GameKey.X;
			case KeyEvent.VK_Y: return GameKey.Y;
			case KeyEvent.VK_Z: return GameKey.Z;
			case KeyEvent.VK_0: return GameKey._0;
			case KeyEvent.VK_1: return GameKey._1;
			case KeyEvent.VK_2: return GameKey._2;
			case KeyEvent.VK_3: return GameKey._3;
			case KeyEvent.VK_4: return GameKey._4;
			case KeyEvent.VK_5: return GameKey._5;
			case KeyEvent.VK_6: return GameKey._6;
			case KeyEvent.VK_7: return GameKey._7;
			case KeyEvent.VK_8: return GameKey._8;
			case KeyEvent.VK_9: return GameKey._9;
			case KeyEvent.VK_ESCAPE: return GameKey.ESCAPE;
			case KeyEvent.VK_SPACE: return GameKey.SPACE;
			case KeyEvent.VK_ENTER: return GameKey.ENTER;
			case KeyEvent.VK_LEFT: return GameKey.LEFT;
			case KeyEvent.VK_RIGHT: return GameKey.RIGHT;
			case KeyEvent.VK_UP: return GameKey.UP;
			case KeyEvent.VK_DOWN: return GameKey.DOWN;
			case KeyEvent.VK_CONTROL: return GameKey.CTRL;
			case KeyEvent.VK_SHIFT: return GameKey.SHIFT;
			case KeyEvent.VK_ALT: return GameKey.ALT;
			case KeyEvent.VK_DELETE: return GameKey.DELETE;
			case KeyEvent.VK_BACK_SPACE: return GameKey.BACKSPACE;
		}
		return null;
	}
	
}