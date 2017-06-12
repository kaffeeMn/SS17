package edu.udo.cs.swtsf.swing;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import edu.udo.cs.swtsf.core.GameKey;
import edu.udo.cs.swtsf.core.player.MovePlayerAction;
import edu.udo.cs.swtsf.core.player.ShootLaserAction;

public class SwingKeyBindingsScreen extends SwingGameScreen {
	
	public static final String KEY_BINDINGS_FILE_NAME = "KeyBindings.ser";
	private static final List<KeyBinding> ALL_INPUTS = new ArrayList<>();
	static {
		ALL_INPUTS.add(new KeyBinding(MovePlayerAction.INPUT_MOVE_FORWARD, "Forward", 
				k -> MovePlayerAction.INPUT_MOVE_FORWARD = k));
		ALL_INPUTS.add(new KeyBinding(MovePlayerAction.INPUT_SLOW_DOWN, "Slow Down", 
				k -> MovePlayerAction.INPUT_SLOW_DOWN = k));
		ALL_INPUTS.add(new KeyBinding(MovePlayerAction.INPUT_TURN_LEFT, "Turn Left", 
				k -> MovePlayerAction.INPUT_TURN_LEFT = k));
		ALL_INPUTS.add(new KeyBinding(MovePlayerAction.INPUT_TURN_RIGHT, "Turn Right", 
				k -> MovePlayerAction.INPUT_TURN_RIGHT = k));
		ALL_INPUTS.add(new KeyBinding(ShootLaserAction.INPUT_SHOOT_LASER, "Shoot Laser", 
				k -> ShootLaserAction.INPUT_SHOOT_LASER = k));
	}
	
	private final Map<KeyBinding, SelectKeyBox> inputToBoxMap = new HashMap<>();
	private final BackgroundPanel bodyPanel;
	
	public SwingKeyBindingsScreen() {
		bodyPanel = new BackgroundPanel(createGridBagLayout(4, ALL_INPUTS.size()));
		
		JButton btnBack = new JButton("Back to the Titlescreen");
		bodyPanel.add(btnBack, createGridBagConstraint(1, 1));
		
		JButton btnReset = new JButton("Reset Default Keys");
		bodyPanel.add(btnReset, createGridBagConstraint(1, 1));
		
		for (int i = 0; i < ALL_INPUTS.size(); i++) {
			KeyBinding input = ALL_INPUTS.get(i);
			int y = i + 2;
			
			JLabel lblName = new JLabel(input.name);
			lblName.setForeground(Color.WHITE);
			bodyPanel.add(lblName, createGridBagConstraint(1, y));
			
			SelectKeyBox keyBox = new SelectKeyBox(input);
			bodyPanel.add(keyBox, createGridBagConstraint(2, y));
			
			inputToBoxMap.put(input, keyBox);
		}
		
		setJComponent(bodyPanel);
		
		btnBack.addActionListener((e) 
				-> getSwtGameMain().setScreen(new SwingTitleScreen()));
		btnReset.addActionListener((e) 
				-> resetDefaultKeys());
	}
	
	private void resetDefaultKeys() {
		for (KeyBinding input : ALL_INPUTS) {
			inputToBoxMap.get(input).resetToDefaultKey();
		}
	}
	
	public void initialize() {
		bodyPanel.setSwtGameMain(getSwtGameMain());
	}
	
	public static void loadKeyBindingMap(Component host) {
		File keyBindingFile = new File(KEY_BINDINGS_FILE_NAME);
		if (!keyBindingFile.exists()) {
			JOptionPane.showMessageDialog(host, 
					"There is no Key-Bindings file. An empty file has been created.", 
					"Info", 
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		try (ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream(keyBindingFile))) 
		{
			Object loadedObj = ois.readObject();
			
			@SuppressWarnings("unchecked")
			Map<String, Integer> keyBindingMap = (Map<String, Integer>) loadedObj;
			GameKey[] allKeys = GameKey.values();
			for (KeyBinding input : ALL_INPUTS) {
				Integer keyID = keyBindingMap.get(input.name);
				if (keyID == null || keyID < 0 || keyID >= allKeys.length) {
					continue;
				}
				input.currentKey = allKeys[keyID.intValue()];
				input.set();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(host, 
					"Failed to load Key-Bindings!\nError Message: "+e.getMessage(), 
					"Error", 
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void terminate() {
		saveKeyBindingMap();
		bodyPanel.setSwtGameMain(null);
	}
	
	private void saveKeyBindingMap() {
		Map<String, Integer> keyBindingMap = new HashMap<>();
		for (KeyBinding input : ALL_INPUTS) {
			input.set();
			keyBindingMap.put(input.name, input.currentKey.ordinal());
		}
		
		File keyBindingFile = new File(KEY_BINDINGS_FILE_NAME);
		
		try (ObjectOutputStream ois = new ObjectOutputStream(
				new FileOutputStream(keyBindingFile))) 
		{
			ois.writeObject(keyBindingMap);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(asJComponent(), 
					"Failed to save Key-Bindings!\nError Message: "+e.getMessage(), 
					"Error", 
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void pause() {
	}
	
	public void unpause() {
	}
	
	private static class SelectKeyBox extends JComboBox<GameKey> {
		private static final long serialVersionUID = 1L;
		
		private final KeyBinding input;
		
		public SelectKeyBox(KeyBinding input) {
			this.input = input;
			setModel(new DefaultComboBoxModel<>(GameKey.values()));
			setSelectedItem(input.currentKey);
			
			addItemListener((e) -> selectInputKey());
		}
		
		private void selectInputKey() {
			if (getSelectedItem() instanceof GameKey) {
				input.currentKey = (GameKey) getSelectedItem();
				input.set();
			}
		}
		
		private void setToKey(GameKey key) {
			input.currentKey = key;
			setSelectedItem(input.currentKey);
			input.set();
		}
		
		private void resetToDefaultKey() {
			setToKey(input.defaultKey);
		}
		
	}
	
	public static class KeyBinding implements Serializable {
		private static final long serialVersionUID = 2951928269456721314L;
		
		final GameKey defaultKey;
		final String name;
		final Consumer<GameKey> setter;
		GameKey currentKey;
		
		public KeyBinding(GameKey key, String name, Consumer<GameKey> setter) {
			this.defaultKey = key;
			this.currentKey = defaultKey;
			this.setter = setter;
			this.name = name;
		}
		
		public void set() {
			setter.accept(currentKey);
		}
	}
}