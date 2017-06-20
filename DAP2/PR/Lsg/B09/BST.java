import java.io.*;

public class BST <K extends Comparable<K>>{
    private K   knot;
    private BST left;
    private BST right;

    public BST(K... keys){
        if(keys != null){
            for(int i=0; i<keys.length; ++i){
                insert(keys[i]);
            }
        }else{
            throw new NullPointerException("You submitted an empty list.");
        }
    }
    public void insert(K key){
        if(this.knot==null){
            this.knot = key;
        }else{
            if(this.knot.compareTo(key)>0){
                if(this.left==null){
                    this.left = new BST(key);
                }else{
                    this.left.insert(key);
                }
            }else if(this.knot.compareTo(key)<0){
                if(this.right==null){
                    this.right = new BST(key);
                }else{
                    this.right.insert(key);
                }
            }
        }
    }
    public K getKnot(){return this.knot;}
    public BST<K> getLeft(){return this.left;}
    public BST<K> getRight(){return this.right;}
    public void printAllOrders(){
        System.out.println("\nEach {} is a subtree, empty leaves will be null.");
        Object[] elements = {this.knot, this.left, this.right};
        System.out.println("Traversing in in-order:\n" + getOrder(elements,   1,0,2));
        System.out.println("Traversing in pre-order:\n" + getOrder(elements,  0,1,2));
        System.out.println("Traversing in post-order:\n" + getOrder(elements, 1,2,0));
    }
    private String getOrder(Object[] elements, int... orderList){
        String[] strL = new String[3];
        for(int i=0; i<3; ++i){
            Object cand = elements[orderList[i]];
            if(cand instanceof BST){
                // case of Tree
                BST tree = (BST) cand;
                Object[] candElements = {tree.getKnot(), tree.getLeft(), tree.getRight()};
                strL[i] = tree.getOrder(candElements, orderList);
            }else if(cand == null){
                // case of null
                strL[i] = "null";
            }else{
                // case of key
                strL[i] = cand.toString();
            }
        }
        return String.format(
            "{%1$s, %2$s, %3$s}",
            strL[0], strL[1], strL[2] 
        );
    }
    private static Object parseList(String str, int flag){
        String[] strL = str.split(",");
        switch(flag){
            case 0:
                Integer[] ints = new Integer[strL.length];
                for(int i=0; i<ints.length; ++i){
                    ints[i] = Integer.parseInt(strL[i]);
                }
                return ints;
            case 1:
                Character[] chars = new Character[strL.length];
                for(int i=0; i<chars.length; ++i){
                    chars[i] = strL[i].charAt(0);
                }
                return chars;
            default:
                return strL;
        }
    }
    private static Object input(String note){
        String input = input2(note);
        switch(input){
            case "int":
                return 1;
            case "char":
                return 'c';
            default:
                return "s";
        }
    }
    private static String input2(String note){
        System.out.println(note);
        try{
            return new BufferedReader(new InputStreamReader(System.in)).readLine();
        }catch(IOException ioe){
            ioe.printStackTrace();
            System.out.format("Exception Message:\t", ioe.getMessage());
        }
        return "";
    }
    public static void main(String[] args){
        Object input = input("Choose your datatype for the Tree (int | char) (default String)");
        String note = "Enter your list, split elements by ','.";
        try{
            BST tree;
            if(input instanceof Integer){
                Integer[] list = (Integer[]) parseList(input2(note), 0);
                tree = new BST<Integer>(list);
            }
            else if(input instanceof Character){
                Character[] list = (Character[]) parseList(input2(note), 1);
                tree = new BST<Character>(list);
            }else{
                String[] list = (String[]) parseList(input2(note), 2);
                tree = new BST<String>(list);
            }
            tree.printAllOrders();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
