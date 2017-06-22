import java.util.Random;
import java.lang.Math;

public class Rucksack{
   
    private static int[][] algorithm(Ware[] wares, int g, int p){
        int[][] result = new int[wares.length+1][g];
        int count = 1;
        for(int i=0; i<g; ++i){
            result[0][i] = 2*p*count++;
        }
        for(int i=1; i<result.length; ++i){
            for(int j=0; j<g; ++j){
                if(wares[i-1].getWeight() > j){
                    result[i][j] = result[i-1][j];
                }else{
                    result[i][j] = min(
                        result[i-1][j], 
                        wares[i-1].getValue() + result[i-1][j-wares[i-1].getWeight()]
                    );
                }
            }
        }
        return result;
    }
    private static int min(int... vals){
        int result = vals[0];
        for(int i=1; i<vals.length; ++i){
            if(vals[i-1] > vals[i]){
                result = vals[i];
            }
        }
        return result;
    }
    private static Ware[] initWare(int p, int n){
        java.util.Random rand = new java.util.Random();
        Ware[] result = new Ware[n];
        for(int i=0; i<result.length; ++i){
            result[i] = new Ware(
                rand.nextInt((int)Math.round(p*1.2-p*0.8)) + (int)Math.round(p*0.8),
                rand.nextInt(900) + 100
            );
        }
        return result;
    }
    private static void task(int n, int p, int g){
        Ware[] wares = initWare(p, n);
        float tStart, tEnd;
        tStart  = System.currentTimeMillis();
        int[][] matrix = algorithm(wares, g, p);
        tEnd    = System.currentTimeMillis();
        System.out.format("took %1$s ms", tEnd - tStart);
        printMatrix(matrix);
    }
    private static void printMatrix(int[][] matrix){
        //String result = "";
        //for(int i=0; i<matrix.length; ++i){
        //    for(int j=0; j<matrix[i].length; ++j){
        //        result += ", " + matrix[i][j];
        //    }
        //    result += "\n";
        //}
        System.out.format(
            /*"Resulting matrix:\n%1$s\n\n*/"With result: %1$s\n",
            /*result,*/ matrix[matrix.length-1][matrix[0].length-1]
        );
    }
    public static void main(String[] args){
        if(args == null){
            System.out.println("please enter a value for p");
        }else{
            try{
                int n = Integer.parseInt(args[0]);
                int p = Integer.parseInt(args[1]); 
                int g = Integer.parseInt(args[2]); 
                task(n, p, g);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
