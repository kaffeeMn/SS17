package exercise.lasers;

import edu.udo.cs.swtsf.core.player.Laser;
import edu.udo.cs.swtsf.core.player.LaserUpgrade;

/*
 * Aufgabe:
 * Schreibe eine Klasse LaserUpgradeDamage, welche das interface LaserUpgrade implementiert.
 * Eine Instanz von LaserUpgradeDamage soll den Schaden des Lasers vom Spieler um jeweils 
 * einen Punkt erhöhen, wenn es verwendet wird.
 * 
 * Integrieren sie ihre Klasse auf geeignete Art und Weise in das Spiel.
 */
public class LaserUpgradeDamage implements LaserUpgrade {
	
	private Laser decorated = null;
	
	public void setDecorated(Laser laser) {
		decorated = laser;
	}
	
	public Laser getDecorated() {
		return decorated;
	}
	
	public int getDamage() {
		return getDecorated().getDamage() + 1;
	}
	
}