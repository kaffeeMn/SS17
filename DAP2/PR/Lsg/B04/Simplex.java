public abstract class Simplex implements Distance{
    private int minPoints;
    private Point[] points;

    public Simplex(Point... points){
        if(points.length != 0){
            int i = 0;
            for(Point p : points){
                this.points[i] = p;
                ++i;
            }
            this.minPoints = i+1;
        }else{
            throw new IllegalArgumentException("No arguments passed.");
        }
    }
    public abstract boolean validate();
    public double perimeter(){
        double abs = 0.0;
        for(int i=0; i<this.points.length; ++i){
            for(int j=i-1; j>0; --j){
                abs += distance(this.points[i], this.points[j]);
            }
            for(int j=i+1; j<this.points.length; ++j){
                abs += distance(this.points[i], this.points[j]);
            }
        }
        return abs;
    }
    public static void main(String[] args){}
}
