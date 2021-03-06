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
    private HashSet<Point>      pointsSet;
    private HashSet<Tupel>      allPoints;
    private LinkedList<Tupel>   hull;
    //private Set<Set<Point>>     powSet;


    public ConvexHull(Point[] points){
        initSet(points);
        //Set<Point> dummy = this.pointsSet;
        //this.powSet      = initPowSet(dummy);
        initHull();
    }
    public ConvexHull(Point[] points, int i){
        //Set<Point> dummy = this.pointsSet;
        //this.powSet      = initPowSet(dummy);
        initPointSet(points);
        System.out.println("These are the points:");
        for(Point p : this.pointsSet){
            p.print();
        }
        Point p1, p2, p3;
        Tupel t1, t2, t3;
        p1 = new Point(10.0,10.0);
        p2 = new Point(10.0,100.0);
        p3 = new Point(100.0,10.0);
        t1 = new Tupel(p1,p2);
        t2 = new Tupel(p2,p3);
        t3 = new Tupel(p3,p1);
        HashSet<Tupel> set = new HashSet<Tupel>();
        set.add(t1);
        set.add(t2);
        set.add(t3);
        this.allPoints = set;
        System.out.println("These are the tuples:");
        for(Tupel t : this.allPoints){
            t.printAll();
            System.out.println("---------------------");
        }
        hullBySort();
        System.out.println("result ");
        for(Point p : this.pointsSet){
            p.print();
        }
    }
    public void hullBySort(){
        boolean valid;
        HashSet<Point> list = new HashSet<Point>();
        for(Point p : this.pointsSet){
            valid = true;
            for(Tupel t : this.allPoints){
                if(leftToLine(p, t)){
                    valid &= false;
                }
            }
            if(valid){
                list.add(p);
            }
        }
        this.pointsSet = list;
    }
    public void initAllPoints(Point[] points){
        HashSet<Tupel> set2 = new HashSet<Tupel>();
        for(int i=0; i<points.length; ++i){
            for(int j=i+1; j<points.length; ++j){
                set2.add(new Tupel(points[i], points[j]));
                set2.add(new Tupel(points[j], points[i]));
            }
        }
        System.out.println("\n");
        this.allPoints = set2;
    }
    private void initPointSet(Point[] points){
        HashSet<Point> set1 = new HashSet<Point>();
        for(int i=0; i<points.length; ++i){
            set1.add(points[i]);
        }
        this.pointsSet = set1;
    }
    private void initSet(Point[] points){
        HashSet<Point> set1 = new HashSet<Point>();
        HashSet<Tupel> set2 = new HashSet<Tupel>();
        for(int i=0; i<points.length; ++i){
            set1.add(points[i]);
            for(int j=i+1; j<points.length; ++j){
                set2.add(new Tupel(points[i], points[j]));
                set2.add(new Tupel(points[j], points[i]));
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
        double x0, x1, x2, y0, y1, y2;
        Point a = t.getA();
        Point b = t.getB();
        x0 = a.get(0);
        y0 = a.get(1);
        x1 = b.get(0);
        y1 = b.get(1);
        x2 = p.get(0);
        y2 = p.get(1);
        return ((x1 - x0)*(y2 - y0)) - ((x2 - x0)*(y1 - y0));
    }
    public static void print(LinkedList<Tupel> list){
        for(Tupel cand : list){
            cand.printAll();
            System.out.println("--------------");
        }
    }
    public void printAll(){
        for(Tupel cand : this.hull){
            cand.printAll();
            System.out.println("--------------");
        }
    }
    //public static LinkedList<Tupel> simpleConvex(Point[] param){
    //    HashSet<Point> set = new HashSet<Point>();
    //    for(int i=0; i<param.length; ++i){
    //        for(int j=i+1; j<param.length; ++j){
    //            set.add(new Tupel(param[i], param[j]));
    //            set.add(new Tupel(param[j], param[i]));
    //        }
    //    }
    //    boolean valid;
    //    LinkedList<Tupel> list = new LinkedList<Tupel>();
    //    for(Tupel t : set){
    //        valid = true;
    //        for(Point p : param){
    //            if(leftToLine(p, t)){
    //                valid &= false;
    //            }
    //        }
    //        if(valid){
    //            list.add(t);
    //        }
    //    }
    //    return list;
    //}
    //public static LinkedList<Tupel> simpleConvex(Point[] param, LinkedList<Tupel> corners){
    //    boolean valid;
    //    LinkedList<Tupel> list = new LinkedList<Tupel>();
    //    for(Tupel t : corners){
    //        valid = true;
    //        for(Point p : param){
    //            if(leftToLine(p, t)){
    //                valid &= false;
    //            }
    //        }
    //        if(valid){
    //            list.add(t);
    //        }
    //    }
    //    return list;
    //}
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
