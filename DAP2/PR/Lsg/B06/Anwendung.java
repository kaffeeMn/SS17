import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;

public class Anwendung{

    public static int[][] txtToInts(String path) throws Exception{
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String file = new String(bytes, StandardCharsets.UTF_8);
        String[] lines = file.split("\n");
        int[][] result = new int[lines.length][2];
        String[] tmp = new String[2];
        for(int i=0; i<lines.length; ++i){
            tmp = lines[i].split(",", 2);
            result[i][0] = Integer.parseInt(tmp[0]);
            result[i][0] = Integer.parseInt(tmp[1]);
        }
        return result;
    }
    public static LinkedList<ArrayList<Interval>> pathsToArrayLists(String[] args){
        Interval[] ivList;
        LinkedList<ArrayList<Interval>> aList = new LinkedList<ArrayList<Interval>>();
        ArrayList<Interval> tmp;
        for(int j=0; j<args.length; ++j){
            try{
                tmp = new ArrayList<Interval>();
                int[][] ranges = txtToInts(args[j]);
                System.out.format("Es wurden %1$d Zeilen mit folgendem Inhalt gelesen:\n[", ranges.length);
                for(int i=0; i<ranges.length; ++i){
                    iv = new Interval(ranges[i][0], ranges[i][1])
                    tmp.add(iv);
                    if(i < arr.length-1){
                        System.out.print(iv.toString() + ",");
                    }
                }
                System.out.println(iv.toString() + "]");
                Collections.sort(tmp);
                System.out.println("Sortier:");
                printArrayList(tmp);
                aList.add(tmp);
            }catch(Exception e){
                handle(e);
            }
        }
        System.out.
        return aList;
    }
    public static void handle(Exception e){
        e.printStackTrace();
        System.out.println(e.getMessage());
    }
    public static ArrayList<Interval> intervalScheduling(ArrayList<Interval> intervals){
        int j = 0;
        for(int i=0; i<intervals.size(); ++i){
            //
        }
        return intervals;
    }
    public static ArrayList<Interval> latenessScheduling(ArrayList<Interval> intervals){
        return intervals;
    }
    public static void printArrayList(ArrayList<Interval> arr, String kind){
        String result = "";
        for(Interval iv : arr){
            result += iv.toString() + "\n";
        }
        System.out.print(result);
    }
    public static void main(String[] args){
        String[] paths = new String[args.length - 1];
        for(int i=0; i<paths.length; ++i){
            paths[i] = args[i+1];
        }
        LinkedList<ArrayList<Interval>> aList = pathsToArrayLists(paths);
        switch(args[0]){
            case "Interval":
                for(ArrayList<Interval> arr : aList){
                    printArrayList(intervalScheduling(arr), "iv");
                }
                break;
            case "Lateness":
                for(ArrayList<Interval> arr : aList){
                    printArrayList(latenessScheduling(arr), "lt");
                }
                break;
            default:
                break;
        }
    }
}
