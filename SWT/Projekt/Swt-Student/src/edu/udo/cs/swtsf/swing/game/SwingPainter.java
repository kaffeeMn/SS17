package edu.udo.cs.swtsf.swing.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.JPanel;

import edu.udo.cs.swtsf.core.Entity;
import edu.udo.cs.swtsf.core.Game;
import edu.udo.cs.swtsf.core.player.Player;
import edu.udo.cs.swtsf.swing.SwingPlayScreen;
import edu.udo.cs.swtsf.util.BufferedGroup;
import edu.udo.cs.swtsf.util.Group;
import edu.udo.cs.swtsf.view.GraphicalElement;
import edu.udo.cs.swtsf.view.GraphicalElementFactory;
import edu.udo.cs.swtsf.view.HudElement;
import edu.udo.cs.swtsf.view.HudElementOrientation;
import edu.udo.cs.swtsf.view.RgbColor;
import edu.udo.cs.swtsf.view.Sprite;
import edu.udo.cs.swtsf.view.SpriteSet;
import edu.udo.cs.swtsf.view.ViewManager;

public class SwingPainter extends ViewManager {
	
	public static final String IMAGE_PATH_PREFIX = "images/";
	public static final String IMAGE_PATH_SUFFIX = ".png";
	
	public static final String IMAGE_PATH_LIFE_METER = "PlayerLife";
	
	public static final Font HUD_FONT = new Font("Arial", Font.BOLD, 20);
	public static final Font SHOW_TEXT_FONT = new Font("Arial", Font.BOLD, 32);
	public static final int SHOW_TEXT_FLASH_TIMER = 200;
	
	private static final RenderingHints RENDERING_HINTS;
	static {
		RENDERING_HINTS = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		RENDERING_HINTS.put(
				RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		RENDERING_HINTS.put(
				RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	}
	
	private final JPanel jPanel = new JPanel() {
		private static final long serialVersionUID = 1L;
		public void paintComponent(Graphics g) {
			SwingPainter.this.paintAll((Graphics2D) g);
		}
	};
	private final Set<HudElement>						errorHudElems = new HashSet<>();
	private final Map<Object, SwingGraphicalElement>	spriteMap = new HashMap<>();
	private final Group<GraphicalElement>				updateSpriteList = new BufferedGroup<>();
	private final SwingSpriteSet						rootSpriteSet = new SwingSpriteSet();
	private final SwingPlayScreen screen;
	
	private boolean hudPaintingHasErrors = false;
	
	private Player player;
	
	private String showText;
	private Color showTextColor;
	private int showTextTimeLeft;
	private int showTextFlashTimer;
	
	public SwingPainter(SwingPlayScreen playScreen) {
		screen = playScreen;
		rootSpriteSet.setViewManager(this);
	}
	
	public JComponent getAsJComponent() {
		return jPanel;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Supplier<? extends Player> getPlayerFactory() {
		return playerFactory;
	}
	
	protected Game getGame() {
		return screen.getGame();
	}
	
	public void updateAnimations(long timeSince) {
		updateSpriteList.forEach((elem) 
				-> elem.getAnimator().updateAnimation(elem, timeSince));
		
		updateShowText(timeSince);
		repaint();
	}
	
	void addToAnimatedSpritesList(GraphicalElement elem) {
		if (updateSpriteList.contains(elem)) {
			throw new IllegalStateException("updateSpriteList.contains("+elem+") == true");
		}
		updateSpriteList.add(elem);
	}
	
	void removeFromAnimatedSpritesList(GraphicalElement elem) {
		updateSpriteList.remove(elem);
	}
	
	private SwingGraphicalElement createSpriteFor(Entity entity) {
		GraphicalElementFactory factory = 
				getFactoryForClass(entity.getClass());
		if (factory == null) {
			return new SwingEntitySprite(entity);
		}
		GraphicalElement graphicalElement = factory.createForEntity(this, entity);
		if (graphicalElement instanceof SwingGraphicalElement) {
			return (SwingGraphicalElement) graphicalElement;
		}
		return null;
	}
	
	public void addSpriteFor(Entity entity) {
		if (spriteMap.containsKey(entity)) {
			throw new IllegalArgumentException(
					"spriteMap.containsKey("+entity+") == true");
		}
		SwingGraphicalElement sprite = createSpriteFor(entity);
		if (sprite == null) {
//			throw new IllegalStateException(
//					"createSpriteFor("+entity+") == null");
			return;
		}
		spriteMap.put(entity, sprite);
		sprite.setModelObject(entity);
		
		addSprite((GraphicalElement) sprite);
	}
	
	public void addSpriteInternal(GraphicalElement sprite) {
		SwingGraphicalElement swingSprite = (SwingGraphicalElement) sprite;
		swingSprite.setParent(rootSpriteSet);
		
//		if (sprite.getAnimator() != null) {
//			addToAnimatedSpritesList(sprite);
//		}
	}
	
	public void removeSpriteOf(Entity entity) {
		SwingGraphicalElement sprite = spriteMap.remove(entity);
		if (sprite == null) {
//			System.out.println("NoSpriteFor: "+entity);
			return;
		}
		GraphicalElement elem = (GraphicalElement) sprite;
		if (elem.isRemoveWithEntity()) {
			removeSprite(elem);
		}
	}
	
	public void removeSpriteInternal(GraphicalElement sprite) {
		SwingGraphicalElement swingSprite = (SwingGraphicalElement) sprite;
		Object modelObj = swingSprite.getModelObject();
		SwingGraphicalElement modelObjSprite = spriteMap.get(modelObj);
		if (modelObjSprite != null && modelObjSprite != sprite) {
			throw new IllegalArgumentException("spriteMap.get(sprite.getModelObject()) != sprite");
		}
		spriteMap.remove(modelObj);
		sprite.setParent(null);
		swingSprite.setModelObject(null);
		
//		removeFromAnimatedSpritesList(sprite);
	}
	
	public void repaint() {
		jPanel.repaint();
	}
	
	public void setCameraCenter(double offsetX, double offsetY) {
		double translateX = getCameraWidth() / 2 - offsetX;
		double translateY = getCameraHeight() / 2 - offsetY;
		rootSpriteSet.setTranslation(translateX, translateY);
	}
	
	public double getCameraCenterX() {
		return rootSpriteSet.getTranslateX();
	}
	
	public double getCameraCenterY() {
		return rootSpriteSet.getTranslateY();
	}
	
	public double getCameraWidth() {
		return jPanel.getWidth();
	}
	
	public double getCameraHeight() {
		return jPanel.getHeight();
	}
	
	public Sprite newSprite() {
		return new SwingSprite();
	}
	
	public SpriteSet newSpriteSet() {
		return new SwingSpriteSet();
	}
	
	public Sprite newEntitySprite(Entity entity) {
		return new SwingEntitySprite(entity);
	}
	
	public SpriteSet newEntitySpriteSet(Entity entity) {
		return new SwingEntitySpriteSet(entity);
	}
	
	private void paintAll(Graphics2D graphics) {
		if (graphics == null) {
			throw new IllegalArgumentException("graphics == null");
		}
		paintBackgroundImage(graphics);
		
		paintHUD(graphics);
		
		graphics.setRenderingHints(RENDERING_HINTS);
		
		rootSpriteSet.paint(graphics);
	}
	
	private void paintBackgroundImage(Graphics2D g) {
		String imgPath = getBackgroundImagePath();
		if (imgPath == null) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, jPanel.getWidth(), jPanel.getHeight());
			return;
		}
		try {
			int totalW = jPanel.getWidth();
			int totalH = jPanel.getHeight();
			
			BufferedImage bgImg = fetchImage(getBackgroundImagePath());
			int imgW = bgImg.getWidth();
			int imgH = bgImg.getHeight();
			
			int cameraOffsetX = ((int) getCameraCenterX()) % imgW;
			int cameraOffsetY = ((int) getCameraCenterY()) % imgH;
			int tileShiftX = cameraOffsetX < 0 ? 0 : -1;
			int tileShiftY = cameraOffsetY < 0 ? 0 : -1;
			
			int tilesInW = (int) Math.ceil(totalW / (double) imgW) + 1;
			int tilesInH = (int) Math.ceil(totalH / (double) imgH) + 1;
			
			for (int ty = 0; ty < tilesInH; ty++) {
				for (int tx = 0; tx < tilesInW; tx++) {
					int tileX = (tx + tileShiftX) * imgW + cameraOffsetX;
					int tileY = (ty + tileShiftY) * imgH + cameraOffsetY;
					g.drawImage(bgImg, tileX, tileY, null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void paintHUD(Graphics2D graphics) {
		if (hudPaintingHasErrors) {
			return;
		}
		try {
			int hudTopX = 0;
			int hudBottomX = 0;
			int hudBottomY = (int) getCameraHeight();
			for (HudElement element : hudList) {
				HudElementOrientation ori = element.getOrientation();
				if (ori == HudElementOrientation.TOP) {
					hudTopX = drawHudElement(graphics, hudTopX, 0, element) + 4;
				} else if (ori == HudElementOrientation.BOTTOM) {
					int elementY = hudBottomY - element.getImageCutoutHeight();
					hudBottomX = drawHudElement(graphics, hudBottomX, elementY, element) + 4;
				} else {
					throw new IllegalStateException("element.getOrientation() == "+ori);
				}
			}
			
			drawShowText(graphics);
		} catch (Exception e) {
			e.printStackTrace();
			hudPaintingHasErrors = true;
		}
	}
	
	public void removeHudElement(HudElement element) {
		super.removeHudElement(element);
		errorHudElems.remove(element);
	}
	
	private int drawHudElement(Graphics2D g, int x, int y, 
			HudElement element) throws IOException 
	{
		int cutoutW = element.getImageCutoutWidth();
		int cutoutH = element.getImageCutoutHeight();
		int x2 = x + cutoutW;
		int y2 = y + cutoutH;
		if (!errorHudElems.contains(element)) {
			int cutoutX = element.getImageCutoutX();
			int cutoutY = element.getImageCutoutY();
			int cutoutX2 = cutoutX + cutoutW;
			int cutoutY2 = cutoutY + cutoutH;
			try {
				BufferedImage img = fetchImage(element.getImagePath());
				g.drawImage(img, x, y, x2, y2, cutoutX, cutoutY, cutoutX2, cutoutY2, null);
			} catch (Exception e) {
				e.printStackTrace();
				errorHudElems.add(element);
			}
		}
		
		int txtX = x2;
		int txtY = y;
		String valueText = element.getText();
		g.setColor(Color.WHITE);
		g.setFont(HUD_FONT);
//		int txtLength = drawStringCentered(g, valueText, txtX, txtY, cutoutW, cutoutH);
		Rectangle2D txtRect = drawStringTopLeft(g, valueText, txtX, txtY);
		
		return x2 + (int) (txtRect.getWidth());
	}
	
	private Rectangle2D drawStringTopLeft(Graphics2D g, String text, int x, int y) {
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D rect = fm.getStringBounds(text, g);
		int textX = x;
		int textY = y + fm.getHeight() - fm.getDescent();
		g.drawString(text, textX, textY);
		return rect;
	}
	
	private Rectangle2D drawStringCentered(Graphics2D g, String text, int x, int y, int width, int height) {
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D rect = fm.getStringBounds(text, g);
		int textW = (int) rect.getWidth();
		int textH = (int) rect.getHeight();
		int textX = x + width / 2 - textW / 2;
		int textY = y + height / 2 - textH / 2;
		g.drawString(text, textX, textY);
		return rect;
	}
	
	private void drawShowText(Graphics2D graphics) {
		if (showTextTimeLeft > 0 && showTextFlashTimer > 0) {
			graphics.setFont(SHOW_TEXT_FONT);
			graphics.setColor(showTextColor);
			
//			int screenW = (int) getCameraWidth();
//			int screenH = (int) getCameraHeight();
//			
//			FontMetrics fm = graphics.getFontMetrics();
//			Rectangle2D rect = fm.getStringBounds(showText, graphics);
//			int textW = (int) rect.getWidth();
//			int textH = (int) rect.getHeight();
//			while (textW > screenW) {
//				SHOW_TEXT_FONT = SHOW_TEXT_FONT.deriveFont(SHOW_TEXT_FONT.getSize2D() - 2);
//				graphics.setFont(SHOW_TEXT_FONT);
//				fm = graphics.getFontMetrics();
//				rect = fm.getStringBounds(showText, graphics);
//				textW = (int) rect.getWidth();
//				textH = (int) rect.getHeight();
//			}
//			int textX = screenW / 2 - textW / 2;
//			int textY = screenH / 2 - textH / 2;
//			graphics.drawString(showText, textX, textY);
			
			drawStringCentered(graphics, showText, 
					0, 0, (int) getCameraWidth(), (int) getCameraHeight());
		}
	}
	
	private void updateShowText(long timeSince) {
		if (showTextTimeLeft > 0) {
			if (showTextFlashTimer > 0) {
				showTextFlashTimer -= timeSince;
				if (showTextFlashTimer <= 0) {
					showTextFlashTimer = -SHOW_TEXT_FLASH_TIMER;
				}
			} else {
				showTextFlashTimer += timeSince;
				if (showTextFlashTimer > 0) {
					showTextFlashTimer = SHOW_TEXT_FLASH_TIMER * 2;
				}
			}
			showTextTimeLeft -= timeSince;
		}
	}
	
	public void showText(String text, int timeInSeconds, RgbColor color) {
		if (text == null) {
			throw new IllegalArgumentException("text == null");
		}
		if (color == null) {
			throw new IllegalArgumentException("color == null");
		}
		if (timeInSeconds < 1) {
			throw new IllegalArgumentException("timeInSeconds < 1");
		}
		showText = text;
		showTextColor = new Color(color.getRed(), color.getGreen(), color.getBlue());
		showTextTimeLeft = timeInSeconds * 1000;
		showTextFlashTimer = SHOW_TEXT_FLASH_TIMER;
	}
	
	public BufferedImage fetchImage(String imagePath) throws IOException {
		return screen.getSwtGameMain().getImageCache().fetchImage(imagePath);
	}
	
}