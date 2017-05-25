public class Bubble extends Algorithm{
    
    public Bubble(int range, int length){
        super(range, length, "Bubble-Sort")
    }
    @Override
    public int[] algorithm(int[] arr){
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
        return arr;
    }
}
