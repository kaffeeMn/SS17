import java.nio.file.Files;
import java.nio.file.Paths;

public class Anwendung{

    public static int[][] txtToInts(String path){
        byte[] bytes = Files.readAllBytes(Paths.get(path))
        String lines = new String(bytes);
        lines = lines.split("\n");
        int[][] result = new int[lines.length][2];
        String[] tmp = new String[2];
        for(int i=0; i<lines.length; ++i){
            tmp = lines[i].split(",", 2);
            result[i][0] = Integer.parseint(tmp[0]);
            result[i][0] = Integer.parseInt(tmp[1]);
        }
        return result;
    }
    public static ArrayList<Interval> pathsToArrayList(){
        for(String path : args){
            int[][] ranges = txtToInts(path);
            Interval[] ivList = new Interval[ranges.length];
            for(int i=0; i<ranges.length; ++i){
                ivList[i] = new Interval(ranges[i][0], ranges[i][1]);
            }
        }
        return ArrayList<Interval>(ivList);
    }
    public static ArrayList<Interval> intervalScheduling(ArrayList<Interval>){}
    public static void main(String[] args){
        //
    }
}
