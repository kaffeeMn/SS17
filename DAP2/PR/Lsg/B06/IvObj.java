public class IvObj implements Comparable<IvObj>{
    private final int start;
    private final int end;

    public IvObj(int start, int end){
        this.start = start;
        this.end = end;
    }
    public int getStart(){return this.start;}
    public int getEnd(){return this.end;}
    public String toString(){
        return String.format("[%1$d,%2$d]", this.start, this.end);
    }
    @Override
    public int  compareTo(IvObj other){
        return this.end - other.getEnd();
    }
}
