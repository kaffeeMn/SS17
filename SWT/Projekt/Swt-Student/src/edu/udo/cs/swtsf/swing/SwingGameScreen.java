package edu.udo.cs.swtsf.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;

public abstract class SwingGameScreen {
	
	private SwtGameMain main;
	private JComponent jComp;
	
	public abstract void initialize();
	
	public abstract void terminate();
	
	public abstract void pause();
	
	public abstract void unpause();
	
	protected void setJComponent(JComponent asJComponent) {
		jComp = asJComponent;
	}
	
	public JComponent asJComponent() {
		return jComp;
	}
	
	void setSwtGameMain(SwtGameMain swtGameMain) {
		main = swtGameMain;
	}
	
	public SwtGameMain getSwtGameMain() {
		return main;
	}
	
	public static GridBagLayout createGridBagLayout(int cols, int rows) {
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[cols];
		gbl.rowHeights = new int[rows];
		gbl.columnWeights = new double[cols];
		gbl.rowWeights = new double[rows];
//		Arrays.fill(gbl.columnWeights, 0.0);
//		Arrays.fill(gbl.rowWeights, 0.0);
		return gbl;
	}
	
	public static GridBagConstraints createGridBagConstraint(int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = x;
		gbc.gridy = y;
		return gbc;
	}
	
}