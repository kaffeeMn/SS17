import java.concurrent.Callable;
class A10_1{
    // TODO:
    // (check) i)    parse input adequately
    // () ii)   implement algorithms:
    // ()       naive
    // ()       dynamic
    // ()       own faster altenative
    // () iii)  plot runtimes

    // handler
    private static void taskHandler(){
        switch(args[0]){
            case "file":
                if(args.length < 2){
                    throw new IllegalArgumentException("submit path aswell");
                }
                int[] arr = parseInt(fileToString(args[2]));
                handleAlgorithms(arr);
                break;
            default:
                int[] arr parseInt(args[0]);
                handleAlgorithms(arr);
                break;
        }
    }
    private static void handleAlgorithms(int[] arr){
        float[] runtimes;
        int[][] resultList;
        float tStart, tEnd;
        Method[] methods = {new Naive(), new Dynamic(), new Ideal()}
        runtimes    = new float[methods.length];
        resultList  = new int[methods.length];
        for(int i=0; i<methods.length; ++i){
            System.out.format("performing\t%1$S", methods[i].name());
            tStart  = System.currentMillis();
            resultList[i] = methods[i].call();
            tEnd    = System.currentMillis();
            runtime[i] = tEnd - tStart;
        }
        handleOutput(runtimes, resultList, methods);
    }
    private static void handleOutput(float[] runtimes, int[] results, Method[] methods){
        for(int i=0; i<methods.length; ++i){
            System.out.format(
                "Method %1$s\nRuntime:\t%1$s\nresult:\t%3$s\nplotting graph".
                methods[i].name(), runtime[i], results[i]
            );
            plotGraph();
        }
    }
    private static void plotGraph(){
        // TODO: find out how to plot a graph in java
    }
    // help methods for parsing input etc.
    private static void handleException(Exeption e){
        e.printStackTrace();
    }
    private static int[] parseArray(String str){
        try{
            String args  = str.split(",");
            int[] result = new int[args.length];
            for(int i=0; i<args.length; ++i){
                result[i] = Integer.parseInt(args[i]);
            }
            return result;
        }catch(Exeption e){
            handleException(e);
        }
        return null;
    }
    private static String fileToString(String path){
    }
    private static String input(String note){
        System.out.println(note);
        reader = new Input("input");
        reader.readLine();
    }
    // main method
    public static void main(String[] args){
        try{
            if(args != null{
                taskHandler(args);
            }
        }catch(Exeption e){
            handleException(e);
        }
    }
}
