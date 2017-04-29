import java.lang.StringBuilder;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Objects;

public class Sortierung{
    /* Aufgabe 1
     * */
    private static void insertionSort(int[] arr){
        int limit = arr.length;
        assert (limit > 0) : "The array, needs to consist of at least 1 INT.";
        for(int i=2; i< limit-1; ++i){
            int key = arr[i];
            int j = i-1;
            while ((j>0) && (arr[j]>key)){
                arr[i+1] = arr[i];
                --i;
            }
            arr[i+1] = key;
        }
        assert isSorted(arr) : "Feld NICHT sortiert.";
        System.out.println("Feld sortiert.");
    }
    private static boolean isSorted(int[] arr){
        int limit = arr.length;
        assert (limit > 0) : "The array, needs to consist of at least 1 INT.";
        for(int i = 0; i < limit-1; ++i)
            if(arr[i] > arr[i+1]) return false;
        return true;
    }
    private static String getManual(){
        try(BufferedReader reader = new BufferedReader(new FileReader("manual.txt"))){
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while(line != null){
                builder.append(line);
                builder.append(System.lineSeparator());
                line = reader.readLine();
            }
            return builder.toString();
        }catch(Exception e){
            System.out.println(e);
        }
        // return empty String as dummy
        return "";
    }
    private static int[] upArr(int range){
        int[] result = new int[range];
        for(int i=1; i<=range; ++i){
            result[i] = i;
        }
        return result;
    }
    private static int[] downArr(int range){
        int[] result = new int[range];
        for(int i=range; i>0; --i){
            result[i] = i;
        }
        return result;
    }
    private static int[] randArr(int range){
        java.util.Random numberGenerator = new java.util.Random();
        int[] result = new int[range];
        for(int i : result){
            i = numberGenerator.nextInt();
        }
        return result;
    }
    /* Aufgabe 2
     * */
    private static void mergeSort(int[] arr){
        int[] tmpArr = new int[arr.length];
        mergeSort(arr, tmpArr, 0, arr.length-1);
        assert isSorted(arr);
    }
    private static void mergeSort(int[] arr, int[] tmpArr, int left, int right){
        if(left<right){
            int q = (left+right) / 2;
            mergeSort(arr, tmpArr, left, q);
            mergeSort(arr, tmpArr, q, right);
            merge(arr, tmpArr, left, q, right);
        }
    }
    private static void merge(int[] arr, int[] tmpArr, int left, int q, int right){
        for(int i=left; i<q; ++i)   tmpArr[i] = arr[i];
        int max = left+q+right;
        for(int i=q; i<=right; ++i) tmpArr[max-i] = arr[i];
        int tmpLeft = left;
        int tmpRight = right;
        for(int i=left; i<right; ++i){
            if(tmpArr[tmpLeft] < tmpArr[tmpRight]){
                arr[i] = tmpArr[tmpLeft];
                --tmpLeft;
            }else{
                arr[i] = tmpArr[tmpRight];
                --tmpRight;
            }
        }
    }
    public static void main(String[] args){
        // checking for a maximum input of 3 parameter and a minum of 1
        int limit = args.length ;
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
            // defaults to insertionSort
            try{
                String param = args[1];
                int range = Integer.parseInt(args[0]);
                if(Objects.equals(param, "auf")){
                    insertionSort(upArr(range));
                }
                if(Objects.equals(param, "ab")){
                    insertionSort(downArr(range));
                }
                if(Objects.equals(param, "rand")){
                    insertionSort(randArr(range));
                }
            }catch(Exception e){
                System.out.println(e+"\n"+getManual());
            }
        }else if(limit == 3){
            try{
                String param = args[2];
                String method = args[1];
                int range = Integer.parseInt(args[0]);
                if(Objects.equals(param, "auf")){
                    if(Objects.equals(method, "insert")){
                        insertionSort(upArr(range));
                    }else if(Objects.equals(method, "insert")){
                        mergeSort(upArr(range));
                    }
                }
                if(Objects.equals(param, "ab")){
                    if(Objects.equals(method, "insert")){
                        insertionSort(downArr(range));
                    }else if(Objects.equals(method, "insert")){
                        mergeSort(downArr(range));
                    }
                }
                if(Objects.equals(param, "rand")){
                    if(Objects.equals(method, "insert")){
                        insertionSort(randArr(range));
                    }else if(Objects.equals(method, "insert")){
                        mergeSort(downArr(range));
                    }
                }
            }catch(Exception e){
                System.out.println(e+"\n"+getManual());
            }
        }
    }
}
