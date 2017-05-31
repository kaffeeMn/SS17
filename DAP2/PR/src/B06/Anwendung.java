import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class Anwendung{
    
    public static Interval[] algo(Interval[] iv){
        return iv;
    }
    public static void task(String args) throws Exception{
        String[] tmp = args.split("\n");
        Interval[] result = new Interval[tmp.length];
        for(int i=0; i<tmp.length; ++i){
            String[] cand = tmp[i].split(",", 2);
            result[i] = new Interval(Integer.parseInt(cand[0]), Integer.parseInt(cand[1]));
        }
        for(Interval s : result) System.out.println(s.toString());
    }
    public static String parseFile(String path) throws Exception{
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }
    public static void main(String[] args){
        for(String s : args){
            try{
                task(parseFile(s));
            }catch(Exception e){
                ExceptionHandler.handleException(e, "Something went wrong:");
            }
        }
    }
}
