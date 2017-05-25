public class QuickSort{

    private static void getResults(Algorithm a){
        System.out.format(
            "Algorithm:%1$s\nInitial array:\t%2$s\nSolved array:\t%3$s\nRuntime:\t%4$f\nCorrectness:\t%5\n"
            a.getName(), a.getInitialArr(), a.getSolvedArr(), a.getRunTime(), a.getCorrectOutput()
        );
    }
    private static void runAlgorithms(int range, int length, String[] algoList){
        for(String s : algoList){
            switch(s){
                case "quick":
                    getResults(new Quick(range, length));
                    break;
                case "merge":
                    getResults(range, length);
                    break;
                case "bubble":
                    getResults(range, length);
                    break;
                default:
                    System.out.format("Unknown algorithm:\t%1$s.\n", s);
                    break;
            }
        }
    }
    private static void task(String arg1, String arg2){
        try{
            int range length;
            range   = Integer.parseInt(args[0]);
            length  = Integer.parseInt(args[1]);
            algoList = {"quick", "merge", "bubble"};
            runAlgorithms(range, length, algoList);
        }catch(Exception e){
            handleException(e);
            requestInput("Incorrect Input.\nPlease try again.");
        }
    }
    public static void requestInput(String message){
        try{
            System.out.println(message);
            BufferedReader reader = new BufferedReader(InputStreamReader(System.in));
            String[] input = reader.readLine().split(" ");
            task(input[0], input[1]);
        }catch(Exception e){
            handleException(e);
            readLine("Incorrect Input.\nPlease try again.");
        }
    }
    private static void handleException(Exception e){
        e.printStackTrace();
        System.out.format("Something went wrong:\t%1$s\n", e,getMessage());
    }
    public static void main(String[] args){
        if(args.length < 2){
             requestInput("Not enough Input submitted.\n please pass an Integer for the length and another for the range.");
        }else{
            task(args[0], args[1]);
        }
    }
}
