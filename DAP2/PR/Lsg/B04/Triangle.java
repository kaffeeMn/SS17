public class Triangle extends Simplex{
    private int minPoints;
    private Point[] points;

    public Triangle(Point... points){
        super(points);
    }
    public boolean validate(){
        if(this.minPoints == 3){ 
            for(Point p : points){
                if(p.dim() != 2) return false;
            }
            return (sameVals(2)) ? false : true;
        }
        return false;
    }
    private boolean samePoints(int dim){
        // checks whether points contains a point with the same values
        // up to the dimension dim
        for(int i=0; i<this.minPoints; ++i){
            for(int j=i-1; j>=0; ++j){
                if(sameVals(this.points[i], this.points[j])) return true;
            }
            for(int j=i; j<this.minPoints; ++j){
                if(sameVals(this.points[i], this.points[j])) return true;
            }
        }
        return false;
    }
    private boolean sameVals(Point p1, Point p2){
        if(p1.dim() != p2.dim()) throw new IllegalArgumentException("Points don't share the same dimension");
        else{
            for(int i=0; i<p1.dim(); ++i){
                if(p1.get(i) != p2.get(i)) return false;
            }
            return true;
        }
    }
}
