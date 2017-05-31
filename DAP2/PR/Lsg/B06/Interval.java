public class Interval{
    private final int start;
    private final int end;

    public Interval(int start, int end){
        this.start = start;
        this.end = end;
    }
    public String toString(){
        return String.format("[%1$d,%2$d]", this.start, this.end);
    }
}
