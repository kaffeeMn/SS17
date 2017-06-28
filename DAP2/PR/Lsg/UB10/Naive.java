public class Naive extends Method{
    
    public Naive(String name, int[] arr){
        super(name, arr);
    }
    @Override
    public int[] call(){
        int[] arr = this.array;
        return arr;
    }
}
