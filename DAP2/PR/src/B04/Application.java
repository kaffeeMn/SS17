public class Application{

    private static void printPerimeter(Triangle triangle){
        System.out.println(
            "======================================================================"
            + "\nSeitenlänge des Dreiecks: " + triangle.perimeter()
            + "\n======================================================================"
        );
    }
    private static Triangle randTriangle(){
        Point[] randPoints = recFillPoints(new Point[3], 0, 3);
        return new Triangle(randPoints);
    }
    public static Point[] recFillPoints(Point[] points, int idx, int quant){
        if(idx == 0){ 
            points[0] = new Point(genRandDouble(), genRandDouble());
            return recFillPoints(points, 1, quant);
        }else if(idx < quant){
            Point tmpPoint = new Point(genRandDouble(), genRandDouble());
            boolean valid = true;
            for(int i=0; i<idx; ++i){
                if(Triangle.sameVals(tmpPoint, points[i])){
                    valid &= false;
                }
            }
            if(valid){
                points[idx++] = tmpPoint;
                return recFillPoints(points, idx, quant);
            }else{
                return recFillPoints(points, idx, quant);
            }
        }
        return points;
    }
    private static double genRandDouble(){
        java.util.Random numberGenerator = new java.util.Random();
        double vz = 1;
        if(numberGenerator.nextBoolean()) vz = -1;
        return vz * numberGenerator.nextDouble() * 100;
    }
    public static void main(String[] args){
        try{
            int argsLen = args.length;
            double[] params = new double[argsLen];
            Point[] points = new Point[argsLen/2];
            int j = 0;
            for(int i=0; i<argsLen; ++i){
                params[i] = Double.parseDouble(args[i]);
                if(i%2 != 0){
                    double[] tmpParams = {params[i-1], params[i]};
                    points[j] = new Point(tmpParams);
                    ++j;
                }
            }
            if(args.length == 6){
                //for(Point p : points){
                //    p.print();
                //}
                Triangle triangle = new Triangle(points);
                if(triangle.validate()){
                    printPerimeter(triangle);
                }else{
                    throw new IllegalArgumentException("You did not submit a valid triangle.");
                }
            }else{
                System.out.println("Not 6 coordinates submitted. Generating random triangle.");
                Triangle triangle = randTriangle();
                printPerimeter(triangle);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("EXCEPTION: " + e.getMessage());
        }
    }
}
