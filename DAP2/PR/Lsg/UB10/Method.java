import java.util.concurrent.Callable;

class Method implements Callable<int[]>{
    private     final String    name;
    protected   final int[]     array;

    public Method(String name, int[] arr){
        this.name = name;
        this.array = arr;
    }
    public String name(){
        return this.name;
    }
    @Override
    public int[] call(){
        return null;
    }
}
