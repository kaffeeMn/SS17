public class Point{
    private int d;
    private double[] dimensions;

    public Point(double... values){
        if(values != null){
            int i = 0;
            for(double v :values){
                this.dimensions[i] = v;
                ++i;
            }
            this.d = ++i;
        }else{
            throw new IllegalArgumentException("No arguments passed.");
        }
    }
    public int dim(){
        return this.d;
    }
    public double get(int i){
        if(i < this.d-1){
            return this.dimensions[i];
        }else{
            throw new IllegalArgumentException("Indice too large"); 
        }
    }
    public static void main(String[] args){
        //
    }
}
