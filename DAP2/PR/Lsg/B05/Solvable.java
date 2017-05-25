public interface Solvable{
    // getter needed
    public int[] getInitialArr();
    public int[] getSolvedArr();
    public float getRunTime();
    public String getCorrectOutput();
    public String getName();
    // default methods
    default public int[] algorithm(int[] arr){return arr;}
    default public boolean isSolved(int[] arr){
        for(int i=0; i<arr.length-1; ++i){
            if(arr[i] > arr[i+1]){
                return false;
            }
        }
        return true;
    }
}
