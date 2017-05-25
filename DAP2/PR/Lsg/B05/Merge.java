public class Merge extends Algorithm{
    
    public Merge(int range, int length){
        super(range, length, "Merge-Sort");
    }
    @Override
    public int[] algorithm(int[] arr){
        int[] tmpArr = new int[arr.length];
        mergeSort(arr, tmpArr, 0, arr.length-1);
        return arr;
    }
    private void mergeSort(int[] arr, int[] tmpArr, int left, int right){
        // method like it's been defined in the exercise
        if(left<right){
            int q = (left+right) / 2;
            mergeSort(arr, tmpArr, left, q);
            mergeSort(arr, tmpArr, q+1, right);
            merge(arr, tmpArr, left, q, right);
        }
    }
    private void merge(int[] arr, int[] tmpArr, int left, int q, int right){
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
 
}
