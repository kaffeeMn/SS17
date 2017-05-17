import java.util.HashSet;
import java.util.Set;
import java.util.LinkedList;

public class ConvexHull{
    protected class Tupel{
        private final Point a;
        private final Point b;
        
        public Tupel(Point a, Point b){
            this.a = a;
            this.b = b;
        }
        public Point getA(){return a;}
        public Point getB(){return b;}
        private void printAll(){ a.print(); b.print();}
    }
    private Set<Point>          pointsSet;
    private Set<Tupel>          allPoints;
    private LinkedList<Tupel>   hull;
    //private Set<Set<Point>>                 powSet;


    public ConvexHull(Point[] points){
        initSet(points);
        //Set<Point> dummy = this.pointsSet;
        //this.powSet      = initPowSet(dummy);
        initHull();
    }
    private void initSet(Point[] points){
        Set<Point> set1 = new HashSet<Point>();
        Set<Tupel> set2 = new HashSet<Tupel>();
        for(int i=0; i<points.length; ++i){
            set1.add(points[i]);
            for(int j=i+1; j<points.length; ++j){
                set2.add(new Tupel(points[i], points[j]));
            }
        }
        this.pointsSet = set1;
        this.allPoints = set2;
    }
    private void initHull(){
        boolean valid;
        LinkedList<Tupel> list = new LinkedList<Tupel>();
        for(Tupel t : this.allPoints){
            valid = true;
            for(Point p : this.pointsSet){
                if(leftToLine(p, t)){
                    valid &= false;
                }
            }
            if(valid){
                list.add(t);
            }
        }
        this.hull = list;
    }
    private boolean leftToLine(Point p, Tupel t){
        return cross(p, t) > 0;
    }
    private double cross(Point p, Tupel t){
        Point a = t.getA();
        Point b = t.getB();
        return ((b.get(0) - a.get(0))*(p.get(1) - a.get(1)) - (b.get(1) - a.get(1))*(p.get(0) - a.get(0)));
    }
    public void printAll(){
        for(Tupel cand : this.hull){
            cand.printAll();
        }
    }
    //private Set<Set<Point>> initPowSet(Set<Point> tmpSet){
    //    Set<Set<Point>> result = new Set<Set<Point>>();
    //    if(tmpSet.isEmpty()){
    //        result.add(new Set<Point>);
    //        return result;
    //    }
    //    ArrayList<Point>    list = new ArrayList<Point>(tmpSet);
    //    Point               head = list.get(0);
    //    Set<Point>          tail = new Set<T>(list.subList(1, list.size());
    //    for(Set<Point> set : powerSet(tail)){
    //        Set<Point> newSet = new Set<T>();
    //        newSet.add(head);
    //        newSet.addAll(set);
    //        result.add(newSet);
    //        result.add(set);
    //    }
    //    return result;
    //}
}
