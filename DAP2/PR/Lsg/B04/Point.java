public class Point{
    private int d;
    private double[] dimensions;

    public Point(int dimension, double[] dimensions){
        if(dimension > 0 && dimensions.length > 0){
            this.d = dimension;
            this.dimensions = dimensions;
        }else{
            throw new IllegalArgumentException();
        }
    }
    public static void main(String[] args){
        //
    }
}
