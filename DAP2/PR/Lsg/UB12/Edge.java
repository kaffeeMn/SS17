public class Edge{
    private final Node src;
    private final Node dst;
    
    public Edge(Node src, Node dst){
        this.src = src;
        this.dst = dst;
    }
    public Node src(){
        return this.src;
    }
    public Node dst(){
        return this.dst;
    }
    public String toString(){
        return String.format("%1$s,%2$s", src.getID(), dst.getID());
    }
}
