import java.util.Random;

public class RandArr{
    /* class with the same array as int[] and double[]
     * also grants easy access to random int and double
     * */
    private final int[]     intArr;
    private final double[]  doubleArr;
    private final Random    randomFactory;

    public RandArr(int range, int length){
        this.randomFactory = new Random();
        this.doubleArr  = initDoubleArr(range, length);
        this.intArr     = initIntArr();
    }
    private double[] initDoubleArr(int range, int length){
        double[] tmpArr = new double[length];
        for(int i=0; i<length; ++i)     tmpArr[i] = randDouble(range);
        return tmpArr;
    }
    private int[] initIntArr(){
        int[] tmpArr = new int[this.doubleArr.length];
        for(int i=0; i<this.doubleArr.length; ++i)     tmpArr[i] = (int) this.doubleArr[i];
        return tmpArr;
    }
    // getter
    public double[] getDoubleArr(){return this.doubleArr;}
    public int[] getIntArr(){return this.intArr;}
    public double randDouble(int range){
        int vz = (this.randomFactory.nextDouble() < 0.5) ? 1 : -1;
        return this.randomFactory.nextDouble() * vz * range;
    }
    public int randInt(int range){
        return (int) randDouble(range);
    }
}
