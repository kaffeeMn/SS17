package exercise.lasers;

import edu.udo.cs.swtsf.core.player.Laser;
import edu.udo.cs.swtsf.core.player.LaserUpgrade;


public class LaserUpgradeSizeAndSpeed implements LaserUpgrade{
    private Laser decorated = null;

    public void setDecorated(Laser laser){
        decorated = laser;
    }

    public Laser getDecorated(){
        return decorated;
    }

    public int getBulletSpeed(){
		if (getDecorated() == null) {
			throw new IllegalStateException("getDecorated() == null");
		}
        int tmpBulletSpeed = getDecorated().getBulletSpeed() - 1;
        if(tmpBulletSpeed < 1){
            return 1;
        }
        return tmpBulletSpeed;
    }

    public int getBulletSize(){
		if (getDecorated() == null) {
			throw new IllegalStateException("getDecorated() == null");
		}
        int tmpSize = getDecorated().getBulletSize() + 2;
        if(tmpSize > 32){
            return 32;
        }
        return tmpSize;
    }

}
