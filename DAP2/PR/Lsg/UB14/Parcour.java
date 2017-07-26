import java.util.Random;
public class Parcour{
    
    private static boolean[] generateParcours(int length, int hurdles){
        Random randFac   = new Random();
        boolean[] result = new boolean[n];
        int len = result.length;
        for(int i=0; i<len; ++i){
            result[i] = true;
        }
        int hurdleCount = 0;
        int pos;
        while(hurdleCount < hurdles){
            pos = (int) (randFac.nextDouble() * len);
            if(!result[pos]){
                result[pos] = false;
                ++hurdleCount;
            }
        }
    }
    private static void showParcour(boolean[] parcour){
        if(parcour.length <= 50){
            String representer = "";
            for(boolean b : parcour){
                if(b){
                    representer += "  ";
                }else{
                    representer += "XX";
                }
            }
            System.out.format("|%1$s|", representer);
        }else{
            System.out.println("Parcour is longer than 50");
        }
    }
    public static void main(String[] args){
        if(args.length != 3){
            throw new IllegalArgumentException("Bad input");
        }else{
            try{
                int n = Integer.parseint(args[0]);
                int k = Integer.parseint(args[1]);
                int r = Integer.parseint(args[2]);
            }catch(Exception e){
                System.out.pritnln("Failed to parse Integers");
                e.printStackTrace();
            }
            boolean[] parcour = generateParcours(n, k);
            MinSteoCount minSteps = new MinSteoCount(parcour, r);
            minSteps.sequenceOfSteps();
        }
    }
}
