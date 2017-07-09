import java.util.ArrayList;

public class Node{
    private final int id;
    private ArrayList<Edge> adjList;
    
    public Node(int id){
        this.adjList = new ArrayList<Edge>();
        this.id = id;
    }
    public int getID(){
        return this.id;
    }
    public ArrayList<Edge> getAdjList(){
        return this.adjList;
    }
    public void addAdjList(Edge e){
        this.adjList.add(e);
    }
    public void addEdge(Node dst){
        Edge newEdge = new Edge(this, dst);
        dst.addAdjList(newEdge);
        this.adjList.add(newEdge);
    }
    public boolean equals(Object other){
        if(other instanceof Node){
            Node cand = (Node) other;
            return cand.getID() == this.id;
        }
        return false;
    }
    public String toString(){
        String str = "";
        for(Edge edge: this.adjList){
            str += String.format("%1$s\n", edge.toString());
        }
        return str;
    }
}
