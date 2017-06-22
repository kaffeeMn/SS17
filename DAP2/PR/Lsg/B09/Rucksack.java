public class Rucksack{
   
    private static int[][] algorithm(Ware[] wares, int g){
        int len = wares.length;
        int[][] result = new int[len][g];
        for(int i=0; i<g; ++i){
            result[0][i] = 0;
        }
        for(int i=0; i<len; ++i){
            for(int j=0; j<g; ++j){
                result[i][j] = evaluate(i, j, result, g);
            }
        }
        return result;
    }
    private static int evaluate(int i, int j, int[][] matrix, int g){
        //
        return 0;
    }
    private static Ware[] initWare(int p){
        //
        Ware w = new Ware(0,0);
        Ware[] l = {w};
        return l;
    }
    private static void task(int n, int p, int g){
        Ware[] wares = initWare(p);
        float tStart, tEnd;
        tStart  = System.currentTimeMillis();
        int[][] matrix = algorithm(wares, g);
        tEnd    = System.currentTimeMillis();
        System.out.format("took %1$s ms", tEnd - tStart);
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
