public class Quick extends Algorithm{
    
    public Quick(int range, int length){
        super(range, length, "Quick-Sort");
    }
    @Override
    public int[] algorithm(int[] arr){
        quickSort(arr, 0, arr.length-1);
        return arr;
    }
    // Runtime:
    // worst case:
    // O(n^2), since we end up looping twice if the pivot is set badly
    //
    // usually it should be:
    // O(n*log(n))
    //
    // idealy runs through a sorted array:
    // O(n)
    private void quickSort(int[] arr, int left, int right){
        if(left<right){
            // initializing counter variables that max out at left and right
            int i = left;
            int j = right;
            // the pivot element will be the element at the very mid
            int pivot = arr[(left+right)/2];
            // declaring tmp. That way it won't have to be generated in each loop
            int tmp;
            // this should be O(n)
            while(i<=j){
                // the idea is to look out for elements, that are (according to the  pivot element)
                // misplaced in the array
                while(arr[i] < pivot){
                    ++i;
                }
                while(arr[j] > pivot){
                    --j;
                }
                // in case the array has not been run through already
                // nor have i and j met already, we change elements 
                if(i<=j){
                    tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                    ++i;
                    --j;
                }
            }
            // now we recursively sort what remains left and right to the pivot element
            // this should be O(log(n)), but might end up as O(n)
            quickSort(arr, left, j);
            quickSort(arr, i, right);
        }
    }
}
