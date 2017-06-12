package edu.udo.cs.swtsf.core.player;

import java.util.ArrayList;
import java.util.List;

import edu.udo.cs.swtsf.core.Bullet;
import edu.udo.cs.swtsf.core.Entity;

/**
 * <p>A simple {@link Laser} implementation which can be used as an atom in the 
 * Decorator-Pattern. The {@link BasicLaser} has final attributes for each property 
 * which can only be set in the {@link BasicLaser#BasicLaser(int, int, int, double, int) 
 * constructor}.</p>
 */
public class BasicLaser implements Laser {
	
	private final int damage;
	private final int lifeTime;
	private final int coolDownTime;
	private final int bulletSize;
	private final double bulletSpeed;
	
	public BasicLaser() {
		this(1, 100, 8, 6, 10);
	}
	
	public BasicLaser(int damage, int bulletLifeTime, int bulletSize, double bulletSpeed, int coolDownTime) {
		this.damage = damage;
		this.lifeTime = bulletLifeTime;
		this.bulletSize = bulletSize;
		this.bulletSpeed = bulletSpeed;
		this.coolDownTime = coolDownTime;
	}
	
	public Laser getDecorated() {
		return null;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public int getBulletLifeTime() {
		return lifeTime;
	}
	
	public int getBulletSize() {
		return bulletSize;
	}
	
	public double getBulletSpeed() {
		return bulletSpeed;
	}
	
	public int getCooldownTime() {
		return coolDownTime;
	}
	
	public void initializeBullets(List<Bullet> bullets) {
	}
	
	public List<Bullet> createBullets(Entity src) {
		List<Bullet> list = new ArrayList<>(1);
		list.add(new BasicBullet(src));
		return list;
	}
	
}