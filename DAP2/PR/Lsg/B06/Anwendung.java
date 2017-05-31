import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;

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
                for(int i=0; i<ranges.length; ++i){
                    tmp.add(new Interval(ranges[i][0], ranges[i][1]));
                }
                aList.add(tmp);
            }catch(Exception e){
                handle(e);
            }
        }
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
    public static void printArrayList(ArrayList<Interval> arr){
        String result = "";
        for(Interval iv : arr){
            result += iv.toString() + "\n";
        }
        System.out.print(result);
    }
    public static void main(String[] args){
        LinkedList<ArrayList<Interval>> aList = pathsToArrayLists(args);
        for(ArrayList<Interval> arr : aList){
            printArrayList(intervalScheduling(arr));
        }
    }
}
