import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class EditDistance{
    
    //TODO: print substeps
    private static int distance(String str1, String str2){
        int len1 = str1.length()+1;
        int len2 = str2.length()+1;
        int[][] matrix = initMatrix(len1, len2);
        for(int i=1; i<len1; ++i){
            for(int j=1; j<len2; ++j){
                matrix[i][j] = evaluate(matrix[i-1][j-1], str1.charAt(i-1), str2.charAt(j-1));
            }
        }
        return matrix[len1-1][len2-1];
    }
    private static int[][] initMatrix(int x, int y){
        int[][] result = new int[x][y];
        for(int i=0; i<x; ++i) result[i][0] = i;
        for(int i=0; i<y; ++i) result[0][i] = i;
        return result;
    }
    private static int evaluate(int preVal, char a, char b){
        return (a == b) ? preVal : preVal+1;
    }
    private static void outputHandler(int n, String[] input){
        System.out.format("Input:\t%1$s, %2$s\nOperations:\t%3$d\n",input[0], input[1], n);
    }
    private static String[] fileToStrings(String path) throws Exception{
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String file = new String(bytes, StandardCharsets.UTF_8);
        String[] result = file.split("\n");
        if(result.length > 2)
            throw new IllegalArgumentException("wrong number of arguments");
        return result;
    }
    public static void main(String[] args){
        try{
            switch(args.length){
                case 1:
                    String[] fileLines = fileToStrings(args[0]);
                    outputHandler(distance(fileLines[0], fileLines[1]), fileLines);
                     break;
                case 2:
                    outputHandler(distance(args[0], args[1]), args);
                     break;
                default:
                     throw new IllegalArgumentException("wrong number of arguments");
            }
        }catch(Exception e){
        }
    }
}
