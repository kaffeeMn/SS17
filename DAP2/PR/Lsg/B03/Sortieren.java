// TODO: System.gc() verwenden ...
public class Sortieren{
    /* Aufgabe 1
     * */
    private static void bubbleSort(int[] arr){
        // the idea is to always shift the minum value to the
        // left of the array
        int n = arr.length;
        for(int i=0; i<n; ++i){
            // we run throgh the array and sort n sublists:
            // each sublist has its minimum value at its head
            // since we are incrementing by 1 we get a sorted
            // list in the end.
            for(int j=n-1; j>=i+1; --j){
                // comparing values and swapping them if neccessary
                if(arr[j-1]>arr[j]){
                    int tmp = arr[j];
                    arr[j] = arr[j-1];
                    arr[j-1] = tmp;
                }
            }
        }
    }
    private static int[] downArr(int range){
        // initializes an descending array
        int[] result = new int[range];
        for(int i=range-1; i>0; --i)  result[range-(i+1)] = i;
        return result;
    }
    /* Aufgabe 2
     * */
    private static void handler(float time, int[] arr, int tmpLen){
        long tStart, tEnd;
        // beginning of the runTime
        tStart = System.currentTimeMillis();
        bubbleSort(arr);
        // end of runTime
        tEnd = System.currentTimeMillis();
        // dividing by 1000, since the input will be parsed in seconds
        float runTime = (tEnd-tStart) / 1000;
        if(runTime < time){
            // the fieldlength will be needed when the recursion ends(binary search)
            tmpLen = arr.length;
            arr = downArr(tmpLen*2);
            // getting some information of what the algorithm is calculating
            System.out.println("Feldgroesse: " + tmpLen);
            handler(time, arr, tmpLen);
        }else{
            // in case it took longer, than the time variable
            // a binar search will be started with the two last fieldlengths
            System.out.println("\nen tering binary search");
            // binary search will be started at half the size of both field lengths
            binarySearch(tmpLen, (tmpLen+arr.length)/2, arr.length, arr, time);
        }
    }
    public static void binarySearch(int bottom, int mid, int right, int[] arr, float wanted){
        // we could technically get binary search working without the mid parameter, but 
        // it looks cleaner that way
        long tStart, tEnd;
        arr = downArr(mid);
        // beginning of the runTime
        tStart = System.currentTimeMillis();
        bubbleSort(arr);
        // end of runTime
        tEnd = System.currentTimeMillis();
        float runTime = (tEnd-tStart)/1000;
        // getting some information of what the algorithm is calculating
        System.out.println("Feldgroesse: "+mid+"\nZeit: "+runTime+"s");
        if(!(Math.abs(runTime-wanted) <= 0.1f)){
            // If the runtime took at least the same time as wanted, we can
            // safely set mid as our new right.
            // Otherwise we can safely set mid as our new bottom. 
            if(wanted <= runTime) 
                binarySearch(bottom, (bottom+mid)/2, mid, arr, wanted);
            else
                binarySearch(mid, (mid+right)/2 , right, arr, wanted);
        }else{
            System.out.println("\ndone");
            System.out.println("======================================================================");
        }
    }
//  printing the array if we want to
//    public static void printArr(int[] arr){
//        for(int i=0; i<arr.length; ++i){
//            System.out.print(" "+arr[i]);
//        }
//        System.out.println();
//    }
    public static void main(String[] args){
        int limit = args.length;
        if(limit!=1){    
            System.out.println("too many arguments");
        }else{
            try{
                // parsing the input to a floating point number and initialzing an array of the length 1000
                float time = Float.parseFloat(args[0]);
                if(time >= 0.0f){
                    int[] arr = downArr(1000);
                    // informing the user about the state of our programm
                    System.out.println("======================================================================");
                    System.out.println("initial time: " + time + "s\n\nentering presearch");
                    handler(time, arr, arr.length);
                }else{
                    System.out.println("Don't expect me to solve time travel.");
                }
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("EXCEPTION: " + e.getMessage());
            }
        }
    }
}
