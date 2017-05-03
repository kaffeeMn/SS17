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
            int key = arr[j];
            int i = j-1;
            while ((i>=0) && (arr[i]>key)){
                arr[i+1] = arr[i];
                --i;
            }
            arr[i+1] = key;
        }
        // end of the runtime, assertions will not be counted
        tEnd = System.currentTimeMillis();
        assert isSorted(arr) : "Feld NICHT sortiert.";
        printRunTime(tEnd-tStart);
    }
    private static boolean isSorted(int[] arr){
        // check for an sorted arra by comparing each field with its followup
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
        // coppying arr into tmpArr so arr can be rearanged lateron
        int tmpLeft = left;
        int tmpMid  = q+1;
        for(int i=left; i<=right; ++i){
            if(tmpLeft <= q && tmpMid <= right){
                if(arr[tmpLeft] < arr[tmpMid]){
                    tmpArr[i] = arr[tmpLeft++];
                }else{
                    tmpArr[i] = arr[tmpMid++];
                }
            }else if(tmpLeft <= q){
                tmpArr[i] = arr[tmpLeft++];
            }else{
                tmpArr[i] = arr[tmpMid++];
            }
        }
        for(int i=left; i<=right; ++i) arr[i] = tmpArr[i];
    }
    private static void printRunTime(long runtime){
        System.out.println("Feld sortiert.\nZeit benoetigt:\t"+runtime+"ms");
    }
    private static void printArr(int[] arr){
        int limit = arr.length;
        System.out.print("[");
        for(int i=0; i<limit-1; ++i) System.out.print(arr[i]+",");
        System.out.println(arr[limit-1]+"]");
    }
    private static int[] handleInput(String[] args, int limit, int range, int[] cand){
        switch(limit){
            case 1:
                try{
                    cand = randArr(range);
                    mergeSort(cand);
                }catch(Exception e){
                    System.out.println(e+"\n"+getManual());
                }
                break;
            case 2:
                try{
                    return handleDouble(args[1], range, cand);
                }catch(Exception e){
                    System.out.println(e+"\n"+getManual());
                }
                break;
            case 3:
                try{
                    return handleTriple(args[1], args[2], range, cand);
                }catch(Exception e){
                    System.out.println(e+"\n"+getManual());
                }
                break;
        }
        return cand;
    }
    private static int[] handleDouble(String param, int range, int[] cand){
        switch(param){
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
                System.out.println(getManual());
                break;
        }
        return cand;
    }
    private static int[] handleTriple(String method, String param, int range, int[] cand){
        switch(param){
            case "auf":
                switch(method){
                    case "insert":
                        cand = upArr(range);
                        insertionSort(cand);
                        break;
                    case "merge":
                        cand = upArr(range);
                        mergeSort(cand);
                        break;
                    default:
                        System.out.println(getManual());
                        break;
                }
                break;
            case "ab":
                switch(method){
                    case "insert":
                        cand = downArr(range);
                        insertionSort(cand);
                        break;
                    case "merge":
                        cand = downArr(range);
                        mergeSort(cand);
                        break;
                    default:
                        System.out.println(getManual());
                        break;
                }
                break;
            case "rand":
                switch(method){
                    case "insert":
                        cand = randArr(range);
                        insertionSort(cand);
                        break;
                    case "merge":
                        cand = randArr(range);
                        mergeSort(cand);
                        break;
                    default:
                        System.out.println(getManual());
                        break;
                }
                break;
            default:
                System.out.println(getManual());
                break;
        }
        return cand;
    }
    public static void main(String[] args){
        // checking for a maximum input of 3 parameter and a minum of 1
        int limit = args.length;
        assert(limit>0 && limit <4) : getManual();
        try{
            int range = Integer.parseInt(args[0]);
            int[] cand = new int[range];
            cand = handleInput(args, limit, range, cand);
            if(range <= 100){
                printArr(cand);
            }
        }catch(Exception e){
            System.out.println(e+"\n"+getManual());
        }
    }
}
