package edu.udo.cs.swtsf.swing;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SwingHighScoreScreen extends SwingGameScreen {
	
	public static final Color BACKGROUND_A = new Color(0.8f, 0.8f, 0.8f);
	public static final Color BACKGROUND_B = new Color(0.6f, 0.6f, 0.6f);
	public static final String HIGH_SCORE_FILE_NAME = "HighScores.ser";
	
	private final List<HighScore> highScores = new ArrayList<>();
	private final BackgroundPanel scorePanel;
	private HighScore inputHighScore = null;
	
	public SwingHighScoreScreen(HighScore inputHighScore) {
		this.inputHighScore = inputHighScore;
		/*
		 * if inputHighScore is null we are only viewing the highscores.
		 * if inputHighScore is not null we are writing a new highscore.
		 */
		if (inputHighScore != null) {
			highScores.add(inputHighScore);
		}
		
		scorePanel = new BackgroundPanel(createGridBagLayout(1, 2));
		JScrollPane bodyPanel = new JScrollPane(scorePanel);
		bodyPanel.setBorder(null);
		
		JButton btnBack = new JButton("Back to Title");
		scorePanel.add(btnBack, createGridBagConstraint(0, 0));
		
		btnBack.addActionListener((e) 
				-> getSwtGameMain().setScreen(new SwingTitleScreen()));
		
		setJComponent(bodyPanel);
	}
	
	protected void generateRandomHighScores() {
		String[] names = {"Bob", "Bob the Great", "Bob is awesome", "Bow before Bob the allmighty"};
		for (String n : names) {
			highScores.add(new HighScore(n, (int) (Math.random() * 1234)));
		}
	}
	
	protected void writeHighScores() {
		File highScoreFile = new File(HIGH_SCORE_FILE_NAME);
		
		try (ObjectOutputStream ois = new ObjectOutputStream(
				new FileOutputStream(highScoreFile))) 
		{
			for (HighScore hs : highScores) {
				ois.writeObject(hs);
			}
			ois.writeObject("EOF");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(scorePanel, 
					"Failed to save High-Score list!\nError Message: "+e.getMessage(), 
					"Error", 
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	protected void loadHighScores() {
		File highScoreFile = new File(HIGH_SCORE_FILE_NAME);
		if (!highScoreFile.exists()) {
			JOptionPane.showMessageDialog(scorePanel, 
					"There is no High-Score file. An empty file has been created.", 
					"Info", 
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		try (ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream(highScoreFile))) 
		{
			while (true) {
				Object loadedObj = ois.readObject();
				
				if (loadedObj instanceof HighScore) {
					HighScore hs = (HighScore) loadedObj;
					highScores.add(hs);
				} else {
					break;
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(scorePanel, 
					"Failed to load High-Score list!\nError Message: "+e.getMessage(), 
					"Error", 
					JOptionPane.ERROR_MESSAGE);
		}
		Collections.sort(highScores);
	}
	
	protected void showHighScores() {
		for (int i = 0; i < highScores.size(); i++) {
			HighScore hs = highScores.get(i);
			
			JComponent hsJComp;
			if (hs == inputHighScore) {
				InputHighScorePanel hsPnl = new InputHighScorePanel(hs, i);
				hsJComp = hsPnl.jPanel;
			} else {
				ReadOnlyHighScorePanel hsPnl = new ReadOnlyHighScorePanel(hs, i);
				hsJComp = hsPnl.jPanel;
			}
			
			int y = i + 1;
			scorePanel.add(hsJComp, createGridBagConstraint(0, y));
		}
		
		scorePanel.revalidate();
		scorePanel.repaint();
	}
	
	public void initialize() {
		loadHighScores();
		showHighScores();
		scorePanel.setSwtGameMain(getSwtGameMain());
	}
	
	public void terminate() {
		writeHighScores();
		scorePanel.setSwtGameMain(null);
	}
	
	public void pause() {
	}
	
	public void unpause() {
	}
	
	public static class InputHighScorePanel {
		
		public static final String INITIAL_TEXT = ">> Please Enter Your Name <<";
		
		final JPanel jPanel;
		boolean wasChanged;
		
		public InputHighScorePanel(HighScore hs, int index) {
			Color backGroundColor;
			if (index % 2 == 0) {
				backGroundColor = BACKGROUND_A;
			} else {
				backGroundColor = BACKGROUND_B;
			}
			
			jPanel = new JPanel(SwingGameScreen.createGridBagLayout(2, 1));
			jPanel.setBackground(backGroundColor);
			
			JTextField txtFieldName = new JTextField(INITIAL_TEXT);
			txtFieldName.setBorder(null);
			txtFieldName.setBackground(backGroundColor);
			jPanel.add(txtFieldName, SwingGameScreen.createGridBagConstraint(0, 0));
			
			JLabel lblScore = new JLabel(Integer.toString(hs.score));
			jPanel.add(lblScore, SwingGameScreen.createGridBagConstraint(1, 0));
			
			txtFieldName.addActionListener((e) 
					-> hs.name = txtFieldName.getText());
			txtFieldName.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					hs.name = txtFieldName.getText();
				}
			});
			txtFieldName.getDocument().addDocumentListener(new DocumentListener() {
				public void removeUpdate(DocumentEvent e) {
					onChange(txtFieldName);
				}
				public void insertUpdate(DocumentEvent e) {
					onChange(txtFieldName);
				}
				public void changedUpdate(DocumentEvent e) {
					onChange(txtFieldName);
				}
			});
			
			EventQueue.invokeLater(() -> txtFieldName.requestFocusInWindow());
		}
		
		void onChange(JTextField txtFieldName) {
			if (!wasChanged) {
				EventQueue.invokeLater(() -> {
					String text = txtFieldName.getText().replace(INITIAL_TEXT, "");
					txtFieldName.setText(text);
					wasChanged = true;
				});
			}
		}
		
	}
	
	public static class ReadOnlyHighScorePanel {
		
		final JPanel jPanel;
		
		public ReadOnlyHighScorePanel(HighScore hs, int index) {
			Color backGroundColor;
			if (index % 2 == 0) {
				backGroundColor = BACKGROUND_A;
			} else {
				backGroundColor = BACKGROUND_B;
			}
			
			jPanel = new JPanel(SwingGameScreen.createGridBagLayout(2, 1));
			jPanel.setBackground(backGroundColor);
			
			JLabel lblName = new JLabel(hs.name);
			jPanel.add(lblName, createGridBagConstraint(0, 0));
			
			JLabel lblScore = new JLabel(Integer.toString(hs.score));
			jPanel.add(lblScore, createGridBagConstraint(1, 0));
		}
		
	}
	
	public static class HighScore implements Serializable, Comparable<HighScore> {
		private static final long serialVersionUID = 1L;
		
		public String name = "";
		public int score = 0;
		
		public HighScore(String name, int score) {
			this.name = name;
			this.score = score;
		}
		
		public int compareTo(HighScore other) {
			return Integer.compare(other.score, score);
		}

		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("[");
			builder.append(name);
			builder.append(" = ");
			builder.append(score);
			builder.append("]");
			return builder.toString();
		}
	}
	
}