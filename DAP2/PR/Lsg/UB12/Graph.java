import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class Graph{
    private enum Color{
        WHITE, GRAY, BLACK
    }
    private ArrayList<Node> nodeList;
    private final int MAX_INT = Integer.MAX_VALUE;
    
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
    private int[] getAllIds(){
        try{
            LinkedList<Integer> result = new LinkedList<Integer>();
            for(Node node : nodeList){
                appendIds(result, node);
            }
            return toInts( (Integer[]) result.toArray());
        }catch(Exception e){
            switch(e.hashCode()){}
        }
        return null;
    }
    private int[] toInts(Integer[] vals){
        int[] result = new int[vals.length];
        for(int i=0; i<vals.length; ++i){
            result[i] = vals[i].intValue();
        }
        return result;
    }
    private void appendIds(LinkedList<Integer> list, Node node){
        list.add(node.getID());
        for(Edge e : node.getAdjList()){
            Node src = e.src();
            Node dst = e.dst();
            appendIds(list, src);
            appendIds(list, dst);
        }
    }
    private int max(int... vals){
        if(vals.length == 0){
            throw new IllegalArgumentException("submitted empty list");
        }
        int result = vals[0];
        for(int i=1; i<vals.length; ++i){
            if(result < vals[i]){
                result = vals[i];
            }
        }
        return result;
    }
    public int[] bfs(int id){
        try{
            // init objects first
            ArrayList<Color>       colors = new ArrayList<Color>();
            PriorityQueue<Integer> queue  = new PriorityQueue<Integer>();
            int[] ids   = getAllIds();
            for(int i : ids){
                queue.add(i);
            }
            int   maxId = max(ids);
            int[]  distance = new int[maxId];
            Node[] fathers  = new Node[maxId];
            for(int i=0; i<maxId; ++i){
                colors.add(Color.WHITE);
                distance[i] = MAX_INT;
                fathers[i]  = null;
            }
            distance[id] = 0;
            colors.set(id, Color.GRAY);
            // actual algorithm
            int candId;
            int[] vals = new int[2];
            while(queue.peek() != null){
                // dequeue
                candId = queue.poll();
                for(Edge edge : nodeList.get(candId).getAdjList()){
                    vals[0] = edge.src().getID();
                    vals[1] = edge.dst().getID();
                    for(int val : vals){
                        if(colors.get(val) == Color.WHITE){
                            colors.set(val, Color.GRAY);
                            distance[val] = distance[candId] + 1;
                            fathers[val] = getNode(candId);
                            // enqueue
                            queue.add(val);
                        }
                    }
                }
                colors.set(candId, Color.BLACK);
            }
            return distance;
        }catch(Exception e){
            switch(e.hashCode()){
                //
            }
        }
        return null;
    }
}
