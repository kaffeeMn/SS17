public abstract class Simplex{
    protected int minPoints;
    protected Point[] points;

    public Simplex(Point... points){
        if(points.length != 0){
            this.points = new Point[points.length];
            int i = 0;
            for(Point p : points){
                this.points[i] = p;
                ++i;
            }
            this.minPoints = i;
        }else{
            throw new IllegalArgumentException("No arguments passed.");
        }
    }
    public abstract boolean validate();
    public double perimeter(){
        double abs = 0.0;
        for(int i=0; i<this.points.length; ++i){
            EuclidDistance eDist = new EuclidDistance();
            for(int j=i+1; j<this.points.length; ++j){
                abs += eDist.distance(this.points[i], this.points[j]);
            }
        }
        return abs;
    }
    public static void main(String[] args){}
}
