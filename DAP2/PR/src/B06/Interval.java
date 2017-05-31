public class Interval{
    private final int start, end;
    
    public Interval(int start, int end){
        this.start = start;
        this.end = end;
    }
    // getter
    public int getStart(){return this.start;}
    public int getEnd(){return this.end;}
    public String toString(){return String.format("[%1$d,%2$d]", this.start, this.end);}
}
