package edu.udo.cs.swtsf.swing;

import javax.swing.JButton;
import javax.swing.JPanel;

public class SwingTitleScreen extends SwingGameScreen {
	
	private final BackgroundPanel bodyPanel;
	
	public SwingTitleScreen() {
		bodyPanel = new BackgroundPanel();
		bodyPanel.setLayout(createGridBagLayout(3, 3));
		
		JPanel buttonPanel = new JPanel(createGridBagLayout(1, 1));
		buttonPanel.setBorder(null);
		buttonPanel.setOpaque(false);
		bodyPanel.add(buttonPanel, createGridBagConstraint(1, 1));
		
		JButton btnStartGame = new JButton("Start New Game");
		buttonPanel.add(btnStartGame, createGridBagConstraint(0, 0));
		
		JButton btnHighScores = new JButton("Show High Scores");
		buttonPanel.add(btnHighScores, createGridBagConstraint(0, 1));
		
		JButton btnKeyBindings = new JButton("Change Game Keys");
		buttonPanel.add(btnKeyBindings, createGridBagConstraint(0, 2));
		
		JButton btnQuit = new JButton("Quit");
		buttonPanel.add(btnQuit, createGridBagConstraint(0, 3));
		
		btnStartGame.addActionListener((e) 
				-> getSwtGameMain().setScreen(new SwingPlayScreen()));
		btnHighScores.addActionListener((e) 
				-> getSwtGameMain().setScreen(new SwingHighScoreScreen(null)));
		btnKeyBindings.addActionListener((e) 
				-> getSwtGameMain().setScreen(new SwingKeyBindingsScreen()));
		btnQuit.addActionListener((e) 
				-> getSwtGameMain().setScreen(null));
		
		setJComponent(bodyPanel);
	}
	
	public void initialize() {
		bodyPanel.setSwtGameMain(getSwtGameMain());
	}
	
	public void terminate() {
		bodyPanel.setSwtGameMain(null);
	}
	
	public void pause() {
	}
	
	public void unpause() {
	}
	
}