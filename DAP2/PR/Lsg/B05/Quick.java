public class Quick extends Algorithm{
    
    public Quick(int range, int length){
        super(range, length, "Quick-Sort");
    }
    @Override
    public int[] algorithm(int[] arr){
        quickSort(arr, 0, arr.length-1);
        return arr;
    }
    private void quickSort(int[] arr, int left, int right){
        if(left<right){
            int i = left;
            int j = right;
            int pivot = arr[(left+right)/2];
            int tmp;
            while(i<=j){
                while(arr[i] < pivot){
                    ++i;
                }
                while(arr[j] > pivot){
                    --j;
                }
                if(i<=j){
                    tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                    ++i;
                    --j;
                }
            }
            quickSort(arr, left, j);
            quickSort(arr, i, right);
        }
    }
}
