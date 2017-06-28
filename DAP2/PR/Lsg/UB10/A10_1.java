class A10_1{
    // TODO:
    // (check) i)    parse input adequately
    // () ii)   implement algorithms:
    // ()       naive
    // ()       dynamic
    // ()       own faster altenative

    // handler
    private static void taskHandler(String[] args){
        int[] arr;
        switch(args[0]){
            case "file":
                if(args.length < 2){
                    throw new IllegalArgumentException("submit path aswell");
                }
                arr = parseInts(fileToString(args[2]));
                handleAlgorithms(arr);
                break;
            default:
                arr =  parseInts(args[0]);
                handleAlgorithms(arr);
                break;
        }
    }
    private static void handleAlgorithms(int[] arr){
        float tStart, tEnd;
        Method[] methods = {new Naive("naive", arr), new Dynamic("dynamic", arr)};
        float[] runtimes    = new float[methods.length];
        int[][] resultList  = new int[methods.length][arr.length];
        for(int i=0; i<methods.length; ++i){
            System.out.format("performing\t%1$S", methods[i].name());
            tStart  = System.currentTimeMillis();
            resultList[i] = methods[i].call();
            tEnd        = System.currentTimeMillis();
            runtimes[i] = tEnd - tStart;
        }
        handleOutput(runtimes, resultList, methods);
    }
    private static void handleOutput(float[] runtimes, int[][] results, Method[] methods){
        for(int i=0; i<methods.length; ++i){
            System.out.format(
                "Method %1$s\nRuntime:\t%1$s\nresult:\t%3$s\nplotting graph",
                methods[i].name(), runtimes[i], results[i]
            );
        }
    }
    // help methods for parsing input etc.
    private static void handleException(Exception e){
        e.printStackTrace();
    }
    private static int[] parseInts(String str){
        try{
            String[] args  = str.split(",");
            int[] result = new int[args.length];
            for(int i=0; i<args.length; ++i){
                result[i] = Integer.parseInt(args[i]);
            }
            return result;
        }catch(Exception e){
            handleException(e);
        }
        return null;
    }
    private static String fileToString(String path){
        return "hi";
    }
    private static String input(String note){
        System.out.println(note);
        Input reader = new Input("input", System.in);
        return reader.readLine();
    }
    // main method
    public static void main(String[] args){
        try{
            if(args != null){
                taskHandler(args);
            }
        }catch(Exception e){
            handleException(e);
        }
    }
}
