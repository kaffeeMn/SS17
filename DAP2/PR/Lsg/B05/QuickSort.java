import java.io.*;

public class QuickSort{

    private static void getResults(Algorithm a){
        System.out.format(
            "Algorithm:\t%1$s\nInitial array:\t%2$s\nSolved array:\t%3$s\nRuntime:\t%4$f ms\nCorrectness:\t%5$s\n\n"
            , a.getName(), a.getStrInitialArr(), a.getStrSolvedArr(), a.getRunTime(), a.getCorrectOutput()
        );
    }
    private static void runAlgorithms(int range, int length, String[] algoList){
        for(String s : algoList){
            switch(s){
                case "quick":
                    getResults(new Quick(range, length));
                    break;
                case "merge":
                    getResults(new Merge(range, length));
                    break;
                case "bubble":
                    getResults(new Quick(range, length));
                    break;
                default:
                    System.out.format("Unknown algorithm:\t%1$s.\n", s);
                    break;
            }
        }
    }
    private static void task(String arg1, String arg2){
        try{
            int range, length;
            range   = Integer.parseInt(arg1);
            length  = Integer.parseInt(arg2);
            String[] algoList = {"quick", "merge", "bubble"};
            runAlgorithms(range, length, algoList);
        }catch(Exception e){
            handleException(e);
            requestInput("Incorrect Input.\nPlease try again.");
        }
    }
    public static void requestInput(String message){
        try{
            System.out.println(message);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String[] input = reader.readLine().split(" ", 2);
            task(input[0], input[1]);
        }catch(Exception e){
            handleException(e);
            requestInput("Incorrect Input.\nPlease try again.");
        }
    }
    public static void handleException(Exception e){
        e.printStackTrace();
        System.out.format("Something went wrong:\t%1$s\n", e.getMessage());
    }
    public static void main(String[] args){
        if(args.length < 2){
             requestInput("Not enough Input submitted.\nplease pass an Integer for the length and another for the range.");
        }else{
            task(args[0], args[1]);
        }
    }
}
