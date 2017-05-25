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
        float tStart, tEnd;
        // messuring runTime
        tStart = System.currentTimeMillis();
        // solved array via the the algorithm
        this.solvedArr  = algorithm(rA.getIntArr());
        tEnd = System.currentTimeMillis();
        this.runTime = tEnd - tStart;
        // checking for correctness
        this.correct = isSolved(this.solvedArr);
    }
    public Algorithm(int range, int length, String name){
        // optional initialization with name
        this(range, length);
        this.name = name;
    }
    // getter requested from interface
    @Override
    public int[] getInitialArr(){return this.initialArr;}
    @Override
    public int[] getSolvedArr(){return this.solvedArr;}
    @Override
    public float getRunTime(){return this.runTime;}
    @Override
    public String getCorrectOutput(){return (this.correct) ? "correct" : "uncorrect";}
    @Override
    public String getName(){return this.name;}
}
