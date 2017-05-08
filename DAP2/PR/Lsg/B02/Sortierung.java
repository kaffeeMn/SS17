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
        for(int j=1; j<arr.length; ++j){
            // taking the value from the element at j
            int key = arr[j];
            int i = j-1;
            while ((i>=0) && (arr[i]>key)){
                // runtime Sum t_k
                // for each element at an lower indice than j, 
                // we set its followup to its current value, as
                // long as it's greater
                arr[i+1] = arr[i];
                --i;
            }
            // at the end we still need to insert key at the position 
            // where it fits in since its greater or equals the element 
            // at i
            arr[i+1] = key;
        }
        // end of the runtime, assertions will not be counted
        tEnd = System.currentTimeMillis();
        assert isSorted(arr) : "Feld NICHT sortiert.";
        printRunTime(tEnd-tStart);
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
    // methods to initialize the arrays, not much to say here
    private static int[] upArr(int range){
        int[] result = new int[range];
        for(int i=0; i<range; ++i) result[i] = i;
        return result;
    }
    private static int[] downArr(int range){
        int[] result = new int[range];
        for(int i=range-1; i>0; --i)  result[range-(i+1)] = i;
        return result;
    }
    private static int[] randArr(int range){
        java.util.Random numberGenerator = new java.util.Random();
        int[] result = new int[range];
        for(int i=0; i<range; ++i) result[i] = numberGenerator.nextInt();
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
        printRunTime(tEnd - tStart);
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
        int tmpLeft = left;
        int tmpMid  = q+1;
        for(int i=left; i<=right; ++i){
            // we assume tmpLeft and tmpMid are within their range
            if(tmpLeft <= q && tmpMid <= right){
                // comparing values, like in any sortalgorithm
                if(arr[tmpLeft] < arr[tmpMid]){
                    tmpArr[i] = arr[tmpLeft++];
                }else{
                    tmpArr[i] = arr[tmpMid++];
                }
            // otherwise the array is filled with the remaining elements
            }else if(tmpLeft <= q){
                tmpArr[i] = arr[tmpLeft++];
            }else{
                tmpArr[i] = arr[tmpMid++];
            }
        }
        // we still need to rewrite arr with the sorted tmpArray
        for(int i=left; i<=right; ++i) arr[i] = tmpArr[i];
    }
    private static void printRunTime(long runtime){
        // printing the sucsessfull runtime, not much to say here
        System.out.println("Feld sortiert.\nZeit benoetigt:\t"+runtime+"ms");
    }
    private static void printArr(int[] arr){
        // printing the array, not much to say here
        int limit = arr.length;
        System.out.print("[");
        for(int i=0; i<limit-1; ++i) System.out.print(arr[i]+",");
        System.out.println(arr[limit-1]+"]");
    }
    private static int[] handleInput(String[] args, int limit, int range, int[] cand){
        /* @param args: original input
         * @param limit: length of original input
         * @param range: first number of input, defines the range of the array
         * @param cand: empty array with the range of (param) range
         * */
        // method to parse general input
        switch(limit){
            // depending on how many args are given, different operations will apply
            case 1:
                // in case of just one argument, we default to mergeSort and randArr
                try{
                    cand = randArr(range);
                    mergeSort(cand);
                }catch(Exception e){
                    System.out.println(e+"\n"+getManual());
                }
                break;
            case 2:
                // in case of two arguments, we have to make further checks
                try{
                    return handleDouble(args[1], range, cand);
                }catch(Exception e){
                    System.out.println(e+"\n"+getManual());
                }
                break;
            case 3:
                // in case of three arguments, we have to make further checks
                try{
                    return handleTriple(args[1], args[2], range, cand);
                }catch(Exception e){
                    System.out.println(e+"\n"+getManual());
                }
                break;
        }
        // we will have to return cand, hence there are complications between switch
        // cases and void.
        return cand;
    }
    private static int[] handleDouble(String param, int range, int[] cand)throws Exception{
        /* @param range: first number of input, defines the range of the array
         * @param cand: empty array with the range of (param) range
         * @param param: userinput(will be parsed)
         * */
        switch(param){
            // param can be either a key to the initialzation of the array (default rand)
            // or the sortalgorithm (defaults merge)
            case "auf":
                cand = upArr(range);
                mergeSort(cand);
                break;
            case "ab":
                cand = downArr(range);
                mergeSort(cand);
                break;
            case "rand":
                cand = randArr(range);
                mergeSort(cand);
                break;
            case "merge":
                cand = randArr(range);
                mergeSort(cand);
                break;
            case "insert":
                cand = randArr(range);
                insertionSort(cand);
                break;
            default:
                // in case someone submitted some nonesense
                System.out.println(getManual());
                break;
        }
        // we will have to return cand, hence there are complications between switch
        // cases and void.
        return cand;
    }
    private static int[] handleTriple(String method, String param, int range, int[] cand)throws Exception{
        /* @param range: first number of input, defines the range of the array
         * @param cand: empty array with the range of (param) range
         * @param param: userinput(will be parsed)
         * @param method: userinput(will be parsed)
         * */
        switch(param){
            // unlike in handleDouble the key arguments for initialization(param) and the
            // searchalgorithm (method) are now seperated
            // we start of with parsing param and look for the method afterwards
            case "auf":
                cand = upArr(range);
                switch(method){
                    case "insert":
                        insertionSort(cand);
                        break;
                    case "merge":
                        mergeSort(cand);
                        break;
                    default:
                        System.out.println(getManual());
                        break;
                }
                break;
            case "ab":
                cand = downArr(range);
                switch(method){
                    case "insert":
                        insertionSort(cand);
                        break;
                    case "merge":
                        mergeSort(cand);
                        break;
                    default:
                        System.out.println(getManual());
                        break;
                }
                break;
            case "rand":
                cand = randArr(range);
                switch(method){
                    case "insert":
                        insertionSort(cand);
                        break;
                    case "merge":
                        mergeSort(cand);
                        break;
                    default:
                        System.out.println(getManual());
                        break;
                }
                break;
            default:
                // in case someone submitted some nonesense
                System.out.println(getManual());
                break;
        }
        // we will have to return cand, hence there are complications between switch
        // cases and void.
        return cand;
    }
    public static void main(String[] args){
        // checking for a maximum input of 3 parameter and a minum of 1
        int limit = args.length;
        assert(limit>0 && limit <4) : getManual();
        try{
            int range = Integer.parseInt(args[0]);
            int[] cand = new int[range];
            // retrieving the sorted array by the handleInput method,
            // which parses the input in args[1], args[3]
            // and calls the mthods in demand
            cand = handleInput(args, limit, range, cand);
            if(range <= 100){
                printArr(cand);
            }
        }catch(Exception e){
            System.out.println(e+"\n"+getManual());
        }
    }
}
