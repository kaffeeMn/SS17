import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class EditDistance{
    
    // A1
    private static int[][] distance(String str1, String str2){
        int len1 = str1.length()+1;
        int len2 = str2.length()+1;
        int[][] matrix = initMatrix(len1, len2);
        for(int i=1; i<len1; ++i){
            for(int j=1; j<len2; ++j){
                matrix[i][j] = evaluate(
                    str1.charAt(i-1), str2.charAt(j-1), matrix[i-1][j-1], matrix[i-1][j], matrix[i][j-1]
                );
            }
        }
        return matrix;
    }
    private static int[][] initMatrix(int x, int y){
        int[][] result = new int[x][y];
        for(int i=0; i<x; ++i) result[i][0] = i;
        for(int i=0; i<y; ++i) result[0][i] = i;
        return result;
    }
    private static int evaluate(char a, char b, int... vals){
        return (a == b) ? vals[0] : min(vals)+1;
    }
    private static int min(int[] vals){
        int result = vals[0]; 
        for(int i=1; i<vals.length; ++i){
            if(result>vals[i]){
                result = vals[i];
            }
        }
        return result;
    }
    // A2
    private static void printOperations(int[][] matrix, String str1, String str2){
        System.out.println("received matrix");
        for(int i=0; i<str1.length()+1; ++i){
            for(int j=0; j<str2.length()+1; ++j){
                System.out.print("\t" + matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println(str1 + "\t" + str2);
        System.out.format(
            "backtraced operations:\n%1$S", backtrace(matrix, str1, str2)
        );
    }
    private static String backtrace(int[][] matrix, String str1, String str2){
        String result= String.format(
            "Loesung fuer %1$s --> %2$s mit Gesamtkosten %3$s\n",
            str1, str2, matrix[str1.length()+1][str2.length()+1]
        );
        for(int i=0; i<result.length(); ++i){
            result += "=";
        }
        String backtraces = "";
        for(String line : backtraceList(matrix, str1, str2)){
            backtraces = line + "\n" + backtraces;
        }
        return result + "\n" + backtraces;
    }
    private static LinkedList<String> backtraceList(int[][] matrix, String str1, String str2){
        int len1 = str1.length()+1;
        int len2 = str2.length()+1;
        LinkedList<String> result = new LinkedList<String>();
        int stepCount = (len1 > len2) ? len1 : len2;
        for(int i=len1; i>0; --i){
            for(int j=len2; j>0; --j){
                result.add(stepToStr(stepCount--, i, j, matrix));
            }
        }
        return result;
    }
    private static String stepToStr(int count, int i, int j, int[][] matrix){
        String result = String.format("%1$s) Kosten: ", count);
        int val = matrix[i][j]
            if(val == matrix[i-1][j-1]){
                result += "Behalte";
            }
            if(val == matrix[i-1][j-1] + 1){
                result += "Ersetze";
            }
            if(val == matrix[i-1][j] + 1){
                result += "Loesche";
            }
            if(val == matrix[i][j-1] + 1){
                result += "Fuege";
            }
            return result + "--> ";
    }
    // parsing output
    private static void outputHandler(int[][] matrix, String[] input, int flag){
        if(flag == 0){
            System.out.format(
                "Input:\t%1$s, %2$s\nOperations needed:\t%3$d\n",
                input[0], input[1], matrix[input[0].length()][input[1].length()]
            );
        }else{
            printOperations(matrix, input[0], input[1]);
        }
    }
    private static String[] fileToStrings(String path) throws Exception{
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String file = new String(bytes, StandardCharsets.UTF_8);
        String[] result = file.split("\n");
        if(result.length > 2)
            throw new IllegalArgumentException("wrong number of arguments");
        return result;
    }
    public static int input(String note){
        System.out.println(note);
        try{
            String input = new BufferedReader(new InputStreamReader(System.in)).readLine();
            if(input != null && input.charAt(0) == '0'){
                return 0;
            }
            return 1;
        }catch(IOException ioe){
            ioe.printStackTrace();
            System.out.format("Exception Message:\t", ioe.getMessage());
        }
        return 1;
    }
    public static void main(String[] args){
        try{
            int flag = input("0 = no operations will be displayed.");
            switch(args.length){
                case 1:
                    String[] fileLines = fileToStrings(args[0]);
                    outputHandler(distance(fileLines[0], fileLines[1]), fileLines, flag);
                     break;
                case 2:
                    outputHandler(distance(args[0], args[1]), args, flag);
                     break;
                default:
                     throw new IllegalArgumentException("wrong number of arguments");
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.format("Exception Message:\t", e.getMessage());
        }
    }
}
