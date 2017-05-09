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
//    private static boolean isSorted(int[] arr){
//        // check for an sorted array by comparing each field with its followup
//        int limit = arr.length;
//        assert (limit > 0) : "The array, needs to consist of at least 1 INT.";
//        for(int i = 0; i < limit-1; ++i){
//            if(arr[i] > arr[i+1]) return false;
//        }
//        return true;
//    }
    private static void downArr(int range, int[] result){
        // initializes an descending array
        result = new int[range];
        for(int i=range-1; i>0; --i)  result[range-(i+1)] = i;
    }
    /* Aufgabe 2
     * */
    private static void handler(float time, int[] arr, tmpLen){
        float tStart, tEnd, runTime;
        // beginning of the runTime
        tStart = System.currentTimeMillis();
        bubbleSort(arr);
        // end of runTime
        tEnd = System.currentTimeMillis();
        if(tEnd-tStart < time){
            tmpLen = arr.length;
            downArr(tmplen*2, arr);
            handler(time, arr, tmpArr);
        }else{
            binarySearch(tmpLen, time, arr.length);
        }
    }
    public void binarySearch(float bottom , float wanted, float right){
        float tStart, tEnd, runTime;
        int[] arr;
        downArr(right, arr);
        // beginning of the runTime
        tStart = System.currentTimeMillis();
        bubbleSort(arr);
        // end of runTime
        tEnd = System.currentTimeMillis();
        runTime = tEnd-tStart;
        // Zwischenergebnis ausgeben lassen
        System.out.println("Feldgroesse: "+right+"\nZeit: "+runTime);
        if!((Math.abs(runTime-wanted) <= 0.1f)){
            float q = (bottom+right)/2;
            downArr(q, arr);
            // beginning of the runTime
            tStart = System.currentTimeMillis();
            bubbleSort(arr);
            // end of runTime
            tEnd = System.currentTimeMillis();
            runTime = tEnd-tStart;
            if(wanted <= runTime) 
                binarySearch(bottom, wanted, q);
            else
                binarySearch(bottom, ++q, right);
        }else{
            System.out.println("done");
        }
    }
    public static void main(String[] args){
        int limit = args.length;
        if(limit!=1)    System.out.println(getManual());
        else{
            try{
                float time = Float.parseFloat(args[0]);
                int[] arr;
                downArr(ARRNNUM, arr);
                handler(time, arr, arr.length);
            }catch(Exception e){
                e.printStacktrace();
                System.out.println("EXCEPTION: " + e.getMessage());
            }
        }
    }
}
