public class Algorithm implements Solvable{
    /* class to be extended for each algorithm
     * to initialize a new algorithm 'public int[] algorithm(int[] arr)' has to be overriden
     * all other methods are provided by either the interface, or this class
     * */
    private final int[]     initialArr;
    private final int[]     solvedArr;
    private final float     runTime;
    private final boolean   correct;
    private String    name;

    public Algorithm(int range, int length){
        RandArr rA = new RandArr(range, length);
        // initial random array
        this.initialArr = rA.getIntArr();
        int[] copy = makeCopy(this.initialArr);
        float tStart, tEnd;
        // messuring runTime TODO: for some reason currentTimeMillis() always returns the some number
        tStart = System.currentTimeMillis();
        System.out.println(tStart);
        // solved array via the the algorithm
        this.solvedArr  = algorithm(copy);
        tEnd = System.currentTimeMillis();
        System.out.println(tEnd);
        this.runTime = tEnd - tStart;
        // checking for correctness
        this.correct = isSolved(this.solvedArr);
    }
    public Algorithm(int range, int length, String name){
        // optional initialization with name
        this(range, length);
        this.name = name;
    }
    private static int[] makeCopy(int[] arr){
        // apperently Objects can work like pointers ...
        // ugly but neccessary
        int[] copy = new int[arr.length];
        for(int i=0; i<arr.length; ++i)    copy[i] = arr[i];
        return copy;
    }
    public static String arrToString(int[] arr){
        // returns the last 100 elements
        if(arr != null){
            if(arr.length > 100){
                String s = "[" + arr[0];
                for(int i=arr.length-101; i<arr.length; ++i)    s += ", " + arr[i];
                return s + "]";
            }else{
                String s = "[" + arr[0];
                for(int i=0; i<arr.length; ++i)    s += ", " + arr[i];
                return s + "]";
            }
        }
        return "[]";
    }
    // getter requested from interface
    @Override
    public String getStrInitialArr(){return arrToString(this.initialArr);}
    @Override
    public String getStrSolvedArr(){return arrToString(this.solvedArr);}
    @Override
    public float getRunTime(){return this.runTime;}
    @Override
    public String getCorrectOutput(){return (this.correct) ? "correct" : "uncorrect";}
    @Override
    public String getName(){return this.name;}
}
