public class EuclidDistance implements Distance{
    public EuclidDistance(){}

    public double distance(Point p1, Point p2){
        if(p1.dim() != p2.dim()) throw new IllegalArgumentException("Points don't share the same dimension");
        else{
            double result = 0.0;
            for(int i=0; i<p1.dim(); ++i){
                result += Math.pow(p1.get(i) - p2.get(i), 2);
            }
            System.out.println("distance:" + Math.sqrt(result));
            return Math.sqrt(result);
        }
    }
}
