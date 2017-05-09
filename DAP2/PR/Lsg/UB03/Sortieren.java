// TODO: System.gc() verwenden ...
public class Sortieren{
    // hardcoded constant of elements the array should be initialized with
    private final int ARRNNUM = 1000;

    /* Aufgabe 1
     * */
    private static void bubbleSort(int[] arr){
        int n = arr.length;
        for(int i=0: i<n; ++i){
            for(int j=n; j>i; --j){
                if(arr[j-1]>arr[j]){
                    int tmp = arr[j];
                    arr[j] = arr[j-1];
                    arr[j-1] = tmp;
                }
            }
        }
    }
    private static void printRunTime(long runTime, int[] arr){
        // printing the runTime, not much to say here
        if(isSorted(arr))   System.out.println("Feld sortiert.\nZeit benoetigt:\t"+runTime+"ms");
        else                System.out.println("Feld NICHT sortiert.\nZeit benoetigt:\t"+runTime+"ms");
    }
    private static boolean isSorted(int[] arr){
        // check for an sorted array by comparing each field with its followup
        int limit = arr.length;
        assert (limit > 0) : "The array, needs to consist of at least 1 INT.";
        for(int i = 0; i < limit-1; ++i){
            if(arr[i] > arr[i+1]) return false;
        }
        return true;
    }
    private static int[] downArr(int range){
        // initializes an descending array
        int[] result = new int[range];
        for(int i=range-1; i>0; --i)  result[range-(i+1)] = i;
        return result;
    }
    /* Aufgabe 2
     * */
    private static void handler(float time, int[] arr, int[]tmpArr){
        float tStart, tEnd, runTime;
        // beginning of the runTime
        tStart = System.currentTimeMillis();
        bubbleSort(arr);
        // end of runTime
        tEnd = System.currentTimeMillis();
        runTime = tEnd-tStart;
        if(Math.abs(time-runTime) <= 0.1f){
            tmpArr = new int[arr.length];
            tmpArr = arr;
            arr = new int[arr.length*2];
            arr = downArr(arr.length);
            handler(time, arr, tmpArr);
        }else{
            //TODO: implement ninary search
            binarySearch(time, arr, tmpArr);
        }
    }
    public void binarySearch(float time, int[]arr, int[]tmpArr){
        //
    }
    public static void main(String[] args){
        int limit = args.length;
        if(limit!=1)    System.out.println(getManual());
        else{
            try{
                float time = Float.parseFloat(args[0]);
                int[] arr =  downArr(ARRNNUM);
                int[] tmpArr = arr;
                handler(time, arr, tmpArr);
            }catch(Exception e){
                e.printStacktrace()
                System.out.println("EXCEPTION: " + e.getMessage())
            }
        }
    }
}
