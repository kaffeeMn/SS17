import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class Graph{
    ArrayList<Node> nodeList;
    
    public Graph(){
        this.nodeList = new ArrayList<Node>();
    }
    public boolean contains(int id){
        for(Node node : this.nodeList){
            if(id == node.getID()){
                return true;
            }
        }
        return false;
    }
    public void addNode(int id){
        if(! contains(id)){
            Node newNode = new Node(id);
            this.nodeList.add(newNode);
        }
    }
    public Node getNode(int id){
        for(Node node : this.nodeList){
            if(node.getID() == id){
                return node;
            }
        }
        return null;
    }
    public void addEdge(Node src, Node dst){
        for(Node node1 : this.nodeList){
            if(node1.getID() == src.getID()){
                for(Node node2 : this.nodeList){
                    if(node2.getID() == dst.getID()){
                        node1.addEdge(node2);
                        break;
                    }
                }
                break;
            }
        }
    }
    public static Graph fromFile(String path){
        try{
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            String file = new String(bytes, StandardCharsets.UTF_8);
            String[] lines = file.split("\n");
            Graph result = new Graph();
            String[] raw;
            int id1;
            int id2;
            Node node1;
            Node node2;
            for(String line : lines){
                try{
                    raw = line.split(",");
                    id1 = Integer.parseInt(raw[0]);
                    id2 = Integer.parseInt(raw[1]);
                    result.addNode(id1);
                    result.addNode(id2);
                    node1 = result.getNode(id1);
                    node2 = result.getNode(id2);
                    result.addEdge(node1, node2);
                }catch(NullPointerException ne){
                    ne.printStackTrace();
                    System.out.format("Exception with message: %1$s\n", ne.getMessage());
                }
            }
            return result;
        }catch(IOException ioe){
            ioe.printStackTrace();
            System.out.format("Exception with message: %1$s\n", ioe.getMessage());
        }
        return null;
    }
    @Override
    public String toString(){
        String str = "";
        for(Node node : this.nodeList){
            str += String.format("%1$s\n", node.toString());
        }
        return str;
    }
}
