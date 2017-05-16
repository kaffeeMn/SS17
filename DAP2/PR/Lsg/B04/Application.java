public class Applicacation{

    private static void printPerimeter(Triangle triangle){
        System.out.println(
            "======================================================================"
            + "\nSeitenlänge des Dreiecks: " + triangle.perimeter()
            + "\n======================================================================"
        );
    }
    private static Triangle randTriangle(){
        else vz = 1;
        Point[] randPoints = new Point[3];
        recFillPoints(ranPoints, 0, ranPoints.length-1);
        return Triangle(randPoints);
    }
    private static void recFillPoints(Point[] points, int idx, int quant){
        if(idx == 0){ 
            points[0] = Point(genRandDouble(), genRandDouble());
            recFillPoints(pointsm, 1, quant);
        }else if(idx <= quant){
            Point tmpPoint = Point(genRandDouble(), genRandDouble());
            boolean valid = true;
            for(int i=0; i<=idx){
                if(Traingle.sameVals(tmpPoint, points[i])) valid=false;
            }
            if(valid){
                point[idx++] = tmpPoint;
                recFillPoints(points, idx, quant);
            }else{
                recFillPoints(points, idx, quant);
            }
        }
    }
    private static double genRandDouble(){
        java.util.Random numberGenerator = new java.util.Random();
        double vz = 1;
        if(numberGenerator.nextBoolean()) vz = -1;
        return vz * numberGenerator.nextDouble() * 1000;
    }
    public static void main(String[] args){
        try{
            argsLen = args.length;
            double[] params = new double[argsLen]
            for(int i=0; i<argsLen; ++i){
                params[i] = Double.parseDouble(args[i]);
            }
            Triangle triangle;
            if(args.length == 6){
                triangle = Triangle(params);
                if(triangle.validate()){
                    printPerimeter(triangle);
                }else{
                    throw new IllegalArgumentException("You did not submit a valid triangle.");
                }
            }else{
                System.out.println("Not 6 coordinates submitted. Generating random triangle.");
                triangle = randTriangle();
                printPerimeter(triangle);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("EXCEPTION: " + e.getMessage());
        }
    }
}
