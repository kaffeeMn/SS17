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
            return true;
        }
        return false;
    }
}
