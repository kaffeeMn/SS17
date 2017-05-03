import java.lang.StringBuilder;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Objects;

public class Sortierung{
    /* Aufgabe 1
     * */
    private static void insertionSort(int[] arr){
        long tStart, tEnd, msecs;
        // beginning of the runtime
        tStart = System.currentTimeMillis();
        int limit = arr.length;
        for(int i=2; i< limit-1; ++i){
            int key = arr[i];
            int j = i-1;
            while ((j>0) && (arr[j]>key)){
                arr[i+1] = arr[i];
                --i;
            }
            arr[i+1] = key;
        }
        // end of the runtime, assertions will not be counted
        tEnd = System.currentTimeMillis();
        assert isSorted(arr) : "Feld NICHT sortiert.";
        msecs = tEnd - tStart;
        System.out.println("Feld sortiert.\nZeit benoetigt:\t"+msecs);
    }
    private static boolean isSorted(int[] arr){
        // check for an sorted arra by comparing each field with its followup
        int limit = arr.length;
        assert (limit > 0) : "The array, needs to consist of at least 1 INT.";
        for(int i = 0; i < limit-1; ++i)    if(arr[i] > arr[i+1]) return false;
        return true;
    }
    private static String getManual(){
        // this method comes in handy, when it comes to exceptions
        try(BufferedReader reader = new BufferedReader(new FileReader("manual.txt"))){
            // we initialize a buffered reader, aswell as a stringbuilder to parse
            // out .txt document into a single String.
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while(line != null){
                builder.append(line);
                builder.append(System.lineSeparator());
                line = reader.readLine();
            }
            // the toString() method does the rest for us
            return builder.toString();
        }catch(Exception e){
            System.out.println(e);
        }
        // return empty String as dummy
        return "";
    }
    private static int[] upArr(int range){
        int[] result = new int[range];
        for(int i=0; i<range; ++i) result[i] = i;
        return result;
    }
    private static int[] downArr(int range){
        int[] result = new int[range];
        for(int i=range; i>0; --i)  result[i-1] = i;
        return result;
    }
    private static int[] randArr(int range){
        java.util.Random numberGenerator = new java.util.Random();
        int[] result = new int[range];
        System.out.println("there");
        for(int i=0; i<range; ++i) result[i] = numberGenerator.nextInt();
        System.out.println("there");
        return result;
    }
    /* Aufgabe 2
     * */
    private static void mergeSort(int[] arr){
        long tStart, tEnd, msecs; 
        // beginning of the runtime
        tStart = System.currentTimeMillis();
        int[] tmpArr = new int[arr.length];
        mergeSort(arr, tmpArr, 0, arr.length-1);
        // end of the runtime, assertions will not be counted
        tEnd = System.currentTimeMillis();
        assert isSorted(arr) : "Feld NICHT sortiert.";
        msecs = tEnd - tStart;
        System.out.println("Feld sortiert.\nZeit benoetigt:\t"+msecs);
    }
    private static void mergeSort(int[] arr, int[] tmpArr, int left, int right){
        // method like it's been defined in the exercise
        if(left<right){
            int q = (left+right) / 2;
            mergeSort(arr, tmpArr, left, q);
            mergeSort(arr, tmpArr, q+1, right);
            merge(arr, tmpArr, left, q, right);
        }
    }
    private static void merge(int[] arr, int[] tmpArr, int left, int q, int right){
        // coppying arr into tmpArr so arr can be rearanged lateron
        for(int i=left; i<=right; ++i)   tmpArr[i] = arr[i];
        // initializing some acsess variables for tmpArr
        int tmpRight = right;
        int tmpLeft = left;
        for(int i=left; i<=right; ++i){
            // the idea is to check relations of the two outmost fields in the
            // array and in-/decrease the acsess variables, to make sure no field
            // is looked at twice
            if(tmpArr[tmpLeft] <= tmpArr[tmpRight]){
                arr[i] = tmpArr[tmpLeft];
                ++tmpLeft;
            }else{
                arr[i] = tmpArr[tmpRight];
                --tmpRight;
            }
        }
    }
    public static void main(String[] args){
        // checking for a maximum input of 3 parameter and a minum of 1
        int limit = args.length;
        assert(limit>0 && limit <4) : getManual();
        if(limit == 1){
            // defaults to auf
            try{
                int range = Integer.parseInt(args[0]);
                insertionSort(upArr(range));
            }catch(Exception e){
                System.out.println(e+"\n"+getManual());
            }
        }else if(limit == 2){
            // defaults to insertionSort (to handle part 1 of the exercise)
            try{
                String param = args[1];
                int range = Integer.parseInt(args[0]);
                if(Objects.equals(param, "auf")){
                    insertionSort(upArr(range));
                }else if(Objects.equals(param, "ab")){
                    insertionSort(downArr(range));
                }else if(Objects.equals(param, "rand")){
                    insertionSort(randArr(range));
                }else{
                    System.out.println(getManual());
                }
            }catch(Exception e){
                System.out.println(e+"\n"+getManual());
            }
        }else if(limit == 3){
            // no defaults anymore
            try{
                String param = args[2];
                String method = args[1];
                int range = Integer.parseInt(args[0]);
                if(Objects.equals(param, "auf")){
                    if(Objects.equals(method, "insert")){
                        insertionSort(upArr(range));
                    }else if(Objects.equals(method, "merge")){
                        mergeSort(upArr(range));
                    }else{
                        System.out.println(getManual());
                    }
                }else if(Objects.equals(param, "ab")){
                    if(Objects.equals(method, "insert")){
                        insertionSort(downArr(range));
                    }else if(Objects.equals(method, "merge")){
                        mergeSort(downArr(range));
                    }else{
                        System.out.println(getManual());
                    }
                }else if(Objects.equals(param, "rand")){
                    if(Objects.equals(method, "insert")){
                        System.out.println("there");
                        insertionSort(randArr(range));
                        System.out.println("there");
                    }else if(Objects.equals(method, "merge")){
                        mergeSort(downArr(range));
                    }else{
                        System.out.println(getManual());
                    }
                }else{
                    System.out.println(getManual());
                }
            }catch(Exception e){
                System.out.println(e+"\n"+getManual());
            }
        }
    }
}
