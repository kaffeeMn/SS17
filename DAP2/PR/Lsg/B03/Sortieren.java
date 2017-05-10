// TODO: System.gc() verwenden ...
public class Sortieren{
    /* Aufgabe 1
     * */
    private static void bubbleSort(int[] arr){
        int n = arr.length;
        for(int i=0; i<n; ++i){
            for(int j=n-1; j>=i+1; --j){
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
        float runTime = (tEnd-tStart) / 1000;
        if(runTime < time){
            tmpLen = arr.length;
            arr = downArr(tmpLen*2);
            System.out.println("Feldgroesse: " + tmpLen);
            handler(time, arr, tmpLen);
        }else{
            System.out.println("\nentering binary search");
            binarySearch(tmpLen, (tmpLen+arr.length)/2, arr.length, arr, time);
        }
    }
    public static void binarySearch(int bottom, int mid, int right, int[] arr, float wanted){
        long tStart, tEnd;
        arr = downArr(mid);
        // beginning of the runTime
        tStart = System.currentTimeMillis();
        bubbleSort(arr);
        // end of runTime
        tEnd = System.currentTimeMillis();
        float runTime = (tEnd-tStart)/1000;
        // Zwischenergebnis ausgeben lassen
        System.out.println("Feldgroesse: "+mid+"\nZeit: "+runTime+"s");
        if(!(Math.abs(runTime-wanted) <= 0.1f)){
            if(wanted <= runTime) 
                binarySearch(bottom, (bottom+mid)/2, mid, arr, wanted);
            else
                binarySearch(mid, (mid+right)/2 , right, arr, wanted);
        }else{
            System.out.println("\ndone");
            System.out.println("======================================================================");
        }
    }
    public static void printArr(int[] arr){
        for(int i=0; i<arr.length; ++i){
            System.out.print(" "+arr[i]);
        }
        System.out.println();
    }
    public static void main(String[] args){
        int limit = args.length;
        if(limit!=1){    
            System.out.println("too many arguments");
        }else{
            try{
                float time = Float.parseFloat(args[0]);
                int[] arr = downArr(1000);
                System.out.println("======================================================================");
                System.out.println("initial time: " + time + "s\n\nentering presearch");
                handler(time, arr, arr.length);
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("EXCEPTION: " + e.getMessage());
            }
        }
    }
}

