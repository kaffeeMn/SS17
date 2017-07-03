public class Heap{
    private int[]   heap;
    private int     heapSize;
    private final int INT_MIN = -2147483648;

    public Heap(int heapSize){
        this.heap     = new int[heapSize];
        for(int i=0; i<heapSize; ++i){
            this.heap[i] = -1;
        }
        this.heapSize = heapSize;
    }
    public int left(int index){
        return 2*(index+1) -1;
    }
    public int right(int index){
        return 2*(index+1);
    }
    public int parent(int index){
        return index/2;
    }
    public void heapify(int index){
        int[] vals = extractAndDelete(index);
        for(int val : vals){
            insert(val, index);
        }
    }
    private int[] extractAndDelete(int index){
        int length = countLength(index);
        int[] tmp  = new int[length];
        collect(tmp, index, 0);
        return tmp;
    }
    private int countLength(int index){
        int cand  = this.heap[index];
        int left  = this.heap[left(index)];
        int right = this.heap[right(index)];
        int i = 0;
        if(cand != INT_MIN){
            ++i;
            i += countLength(left(index));
            i += countLength(right(index));
        }
        return i;
    }
    private void collect(int[] tmp, int index, int i){
        int cand = this.heap[index];
        if(cand != INT_MIN){
            tmp[i] = cand;
            collect(tmp, left(index), ++i);
            collect(tmp, right(index), ++i);
        }
    }
    public void insert(int val, int idx){
        int cand = this.heap[idx];
        while(cand != INT_MIN){
            if(cand > val){
                idx  = left(idx);
                cand = this.heap[idx];
            }else if(cand < val){
                idx  = right(idx);
                cand = this.heap[idx];
            }else{
                return;
            }
        }
        this.heap[idx] = val;
    }
}
