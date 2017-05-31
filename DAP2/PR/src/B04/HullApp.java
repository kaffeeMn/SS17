public class HullApp{

    public static void random(){
        System.out.println("here");
        Point[] points = new Point[100];
        points = Application.recFillPoints(points, 0, 100);
        ConvexHull hull = new ConvexHull(points);
        hull.printAll();
    }
    public static void triangle(){
        Point[] points = new Point[1000];
        points = Application.recFillPoints(points, 0, 1000);
        ConvexHull hull = new ConvexHull(points, 1);
    }
    public static void main(String[] args){
        //simple example:
        //Point p1, p2, p3, p4, p5;
        //p1 = new Point(0,0);
        //p2 = new Point(2,0);
        //p3 = new Point(1,1);
        //p4 = new Point(0.5,0.5);
        //p5 = new Point(0,2);
        //Point[] pList = {p1, p2, p3, p4, p5};
        ////for(Point p : pList) System.out.println(p);
        //ConvexHull hull = new ConvexHull(pList);
        //System.out.println("result:");
        //hull.printAll();
        if(args.length > 0){
            switch(args[0]){
                case "random":
                    random();
                case "triangle":
                    triangle();
            }
        }
    }
}
