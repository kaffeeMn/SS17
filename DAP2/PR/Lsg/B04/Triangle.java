public class Triangle extends Simplex{

    public Triangle(Point... points){
        super(points);
    }
    public boolean validate(){
        if(this.minPoints == 3){ 
            for(Point p : super.points){
                if(p.dim() != 2){
                    return false;
                }
            }
            return !samePoints(3);
        }
        return false;
    }
    private boolean samePoints(int upTo){
        for(int i=0; i<upTo; ++i){
            for(int j=i+1; j<upTo; ++j){
                if(sameVals(this.points[i], this.points[j])){
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean sameVals(Point p1, Point p2){
        if(p1.dim() != p2.dim()) throw new IllegalArgumentException("Points don't share the same dimension");
        else{
            for(int i=0; i<p1.dim(); ++i){
                if(p1.get(i) != p2.get(i)){ 
                    return false;
                }
            }
            return true;
        }
    }
}
