import java.util.Random;
public class Parcour{
    
    private static boolean[] generateParcours(int length, int hurdles){
        Random randFac   = new Random();
        boolean[] result = new boolean[length];
        for(int i=0; i<length; ++i){
            result[i] = true;
        }
        int hurdleCount = 0;
        int pos;
        while(hurdleCount < hurdles){
            pos = (int) (randFac.nextDouble() * length);
            if(!result[pos]){
                result[pos] = false;
                ++hurdleCount;
            }
        }
        return result;
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
                int n = Integer.parseInt(args[0]);
                int k = Integer.parseInt(args[1]);
                int r = Integer.parseInt(args[2]);
                boolean[] parcour = generateParcours(n, k);
                MinStepCount minSteps = new MinStepCount(parcour, r);
                minSteps.sequenceOfSteps(0);
            }catch(Exception e){
                System.out.println("Failed to parse Integers");
                e.printStackTrace();
            }
        }
    }
}
