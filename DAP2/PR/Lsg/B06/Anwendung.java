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
    public static LinkedList<ArrayList<Interval>> pathsToArrayListsInt(String[] args){
        LinkedList<ArrayList<Interval>> aList = new LinkedList<ArrayList<Interval>>();
        ArrayList<Interval> tmp;
        Interval iv = new Interval(0,0);
        for(int j=0; j<args.length; ++j){
            try{
                tmp = new ArrayList<Interval>();
                int[][] ranges = txtToInts(args[j]);
                System.out.format("Es wurden %1$d Zeilen mit folgendem Inhalt gelesen:\n[", ranges.length);
                for(int i=0; i<ranges.length; ++i){
                    iv = new Interval(ranges[i][0], ranges[i][1]);
                    tmp.add(iv);
                    if(i < ranges.length-1){
                        System.out.print(iv.toString() + ",");
                    }
                }
                System.out.println(iv.toString() + "]");
                Collections.sort(tmp);
                System.out.println("Sortiert:");
                printArrayListInt(tmp);
                aList.add(tmp);
            }catch(Exception e){
                handle(e);
            }
        }
        return aList;
    }
    public static LinkedList<ArrayList<Job>> pathsToArrayListsLate(String[] args){
        LinkedList<ArrayList<Job>> aList = new LinkedList<ArrayList<Job>>();
        ArrayList<Job> tmp;
        Job iv = new Job(0,0);
        for(int j=0; j<args.length; ++j){
            try{
                tmp = new ArrayList<Job>();
                int[][] ranges = txtToInts(args[j]);
                System.out.format("Es wurden %1$d Zeilen mit folgendem Inhalt gelesen:\n[", ranges.length);
                for(int i=0; i<ranges.length; ++i){
                    iv = new Job(ranges[i][0], ranges[i][1]);
                    tmp.add(iv);
                    if(i < ranges.length-1){
                        System.out.print(iv.toString() + ",");
                    }
                }
                System.out.println(iv.toString() + "]");
                Collections.sort(tmp);
                System.out.println("Sortiert:");
                printArrayListLate(tmp);
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
        ArrayList<Interval> result = new ArrayList<Interval>();
        result.add(intervals.get(0));
        for(int i=1; i<intervals.size(); ++i){
            if(intervals.get(i).getStart() >= intervals.get(j).getEnd()){
                result.add(intervals.get(i));
                j = i;
            }
        }
        return result;
    }
    public static int maxLate(ArrayList<Job> jobs){
        int max = 0;
        int tmp;
        for(Job j : jobs){
            tmp = (j.getEnd() - j.getStart());
            if(tmp > max){
                max = tmp;
            }
        }
        return max;
    }
    public static int[] latenessScheduling(ArrayList<Job> intervals){
        int z = 0;
        int[] result = new int[intervals.size()];
        for(int i=0; i<intervals.size(); ++i){
            result[i] = z;
            z += intervals.get(i).getStart();
        }
        return result;
    }
    public static void printArrayListInt(ArrayList<Interval> arr){
        String result = "";
        for(IvObj iv : arr){
            result += iv.toString() + "\n";
        }
        System.out.print(result);
    }
    public static void printArrayListLate(ArrayList<Job> arr){
        String result = "";
        for(Job j : arr){
            result += j.toString() + "\n";
        }
        System.out.print(result);
        System.out.println(maxLate(arr) + " units too late.");
    }
    public static void handleInterval(String[] paths){
        LinkedList<ArrayList<Interval>> aList = pathsToArrayListsInt(paths);
        int i = 0;
        for(ArrayList<Interval> arr : aList){
            System.out.println("Bearbeite: " + paths[i]);
            printArrayListInt(intervalScheduling(arr));
            ++i;
        }
    }
    public static void printArray(int[] arr){
        String result = "[" + arr[0];
        for(int i=1; i<arr.length; ++i){
            result += ", " + arr[i];
        }
        System.out.println(result + "]");
    }
    public static void handleLateness(String[] paths){
        LinkedList<ArrayList<Job>> aList = pathsToArrayListsLate(paths);
        int i = 0;
        for(ArrayList<Job> arr : aList){
            System.out.println("Bearbeite: " + paths[i]);
            printArray(latenessScheduling(arr));
            ++i;
        }
    }
    public static void main(String[] args){
        String[] paths = new String[args.length - 1];
        for(int i=0; i<paths.length; ++i){
            paths[i] = args[i+1];
        }
        switch(args[0]){
            case "Interval":
                handleInterval(paths);
                break;
            case "Lateness":
                handleLateness(paths);
                break;
            default:
                break;
        }
    }
}
