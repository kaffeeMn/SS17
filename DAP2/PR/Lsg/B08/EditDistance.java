import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.lang.Math;

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
            str1, str2, matrix[str1.length()][str2.length()]
        );
        String hLine = "";
        for(int i=0; i<result.length(); ++i){
            hLine += "=";
        }
        result += hLine + "\n";
        String backtraces = "";

        //Tuple<LinkedList<String>, LinkedList<Integer>, String> tup = backtraceList(matrix, str1, str2);
        //LinkedList<String> lines    = reverse(tup.getA());
        //LinkedList<Integer> flagList = reverse2(tup.getB());
        //int[] flag = new int[flagList.size()];
        //int count = 0;
        //String tmpStr1 = str1;
        //String tmpStr2 = str2;
        //int i=0;
        //for(int f : flagList){
        //    flag[i] = f; 
        //}
        //i=0;
        for(String line : backtraceList(matrix, str1, str2)){
            //str1 = new String(BuildUpTo(count++, str1, str2, flag[i++])); //line + "\n" + backtraces;
            backtraces += line + "\n" + backtraces;
        }
        return result + backtraces;
    }
    //private static LinkedList<Integer> reverse2(LinkedList<Integer> ll){
    //    LinkedList<Integer> result = new LinkedList<Integer>();
    //    Integer next = ll.poll();
    //    while(next != null){
    //        next = ll.poll();
    //        result.add(next.intValue());
    //    }
    //    return result;
    //}
    //private static LinkedList<String> reverse(LinkedList<String> ll){
    //    LinkedList<String> result = new LinkedList<String>();
    //    String next = ll.poll();
    //    while(next != null){
    //        next = ll.poll();
    //        result.add(next);
    //    }
    //    return result;
    //}
    private static LinkedList<String> backtraceList(int[][] matrix, String str1, String str2){
        int len1 = str1.length();
        int len2 = str2.length();
        LinkedList<String>  result      = new LinkedList<String>();
        LinkedList<Integer>     flagList    = new LinkedList<Integer>();
        int stepCount = (len1 > len2) ? len1 : len2;
        System.out.println(stepCount);
        Tuple<String, int[], Integer> tup;
        while(len1 != 0 || len2 != 0){
            tup = stepToTuple(stepCount--, len1, len2, matrix, str1, str2);
            result.add(tup.getA());
            len1 = tup.getB()[0];
            len2 = tup.getB()[1];
            flagList.add(tup.getC());
        }
        return result; 
    }
    private static Tuple<String, int[], Integer> stepToTuple(int count, int i, int j, int[][] matrix, String str1, String str2){
        int k = count;
        char[] cStr1 = str1.toCharArray();
        char[] cStr2 = str2.toCharArray();
        String result = String.format("%1$s) Kosten: ", count);
        int val = matrix[i][j];
        int[] arr = new int[2];
        int flag = 0;
        if(i>0 && val == matrix[i-1][j] + 1){
            result += String.format("%3$s Loesche %1$s an Position %2$s", cStr1[i-1], count/* (j-1)*/, 1);
            arr[0] = i-1;
            arr[1] = j;
            flag = 1;
        }else if(j>0 && val == matrix[i][j-1] + 1){
            result += String.format("%3$s Fuege %1$s an Position %2$s ein", cStr2[j-1], count/* (j-1)*/, 1);
            arr[0] = i;
            arr[1] = j-1;
            flag = 2;
        }else if(i>0 && j>0 && val == matrix[i-1][j-1] + 1){
            result += String.format("%3$s Ersetze %1$s mit %2$s an Position %4$s", cStr1[i-1], cStr2[j-1], 1, count);// (j-1));
            arr[0] = i-1;
            arr[1] = j-1;
        }else if(i>0 && j>0 && val == matrix[i-1][j-1]){
            result += String.format("%1$s Behalte %2$s an Position %3$s", 0, cStr1[i-1], count);// (j-1));
            arr[0] = i-1;
            arr[1] = j-1;
        }
        String changed = new String(takeTo(count/*j*/, cStr1, cStr2, flag));
        return new Tuple(result + " --> " + changed, arr, flag);
    }
    //private static char[] BuildUpTo(int n, String str1, String str2, int flag){
    //    char[] current  = str1.toCharArray();
    //    char[] target   = str2.toCharArray();
    //    int len = current.length;
    //    char[] result;
    //    switch(flag){
    //        case 1:
    //            // delete
    //            result = new char[len-1];
    //            for(int i=0; i<current.length; ++i){
    //                if(i!=n-1){
    //                    result[i] = current[i];
    //                }
    //            }
    //            break;
    //        case 2:
    //            // insert
    //            result = new char[len+1];
    //            for(int i=0; i<current.length; ++i){
    //                if(i==n){
    //                    continue;
    //                }else if(i<n-1){
    //                    result[i] = current[i];
    //                }else if(i==n-1){
    //                    result[i] = target[i];
    //                    result[i+1] = current[i];
    //                }else{
    //                    result[i+1] = current[i];
    //                }
    //            }
    //            break;
    //        default:
    //            result = new char[len];
    //            for(int i=0; i<current.length; ++i){
    //                if(i!=n-1){
    //                    result[i] = current[i];
    //                }else{
    //                    result[i] = target[i];
    //                }
    //            }
    //            break;
    //    }
    //    return result;
    //}
    private static char[] takeTo(int n, char[] arr1, char[] arr2, int flag){
        int len;
        char[] result = new char[Math.max(Math.max(arr1.length, arr2.length), n)];
        for(int i=0; i<n-1; ++i){
            if(arr2.length > i){
                result[i] = arr2[i];
            }
        }
        switch(flag){
            case 1:
                if(arr1.length > n){
                    for(int i=n; i<arr1.length; ++i){
                        result[i] = arr1[i];
                    }
                }
                break;
            case 2:
                result[n-1] = arr2[n-1];
                if(arr1.length > n-1){
                    int tmp = n-1;
                    for(int i=n; i<result.length; ++i){
                        if(i<arr1.length){
                            result[i] = arr1[tmp];
                            ++tmp;
                        }
                    }
                }
                break;
            default:
                result[n-1] = arr2[n-1];
                if(arr1.length > n){
                    for(int i=n; i<arr1.length; ++i){
                        result[i] = arr1[i];
                    }
                }
                break;
        }
       //switch(flag){
        //    case 1:
        //        len = (arr1.length > n) ? arrLen : n;
        //        result = new char[len-1];
        //        for(int i=0; i<len; ++i){
        //            if(i<n){
        //                result[i] = arr2[i];
        //            }else if(i>n){
        //                if(arr1.length > i){
        //                    result[i-1] = arr1[i];
        //                }
        //            }
        //        }
        //        break;
        //    case 2:
        //        len = (arr1.length > n) ? arrLen : n;
        //        result = new char[len+1];
        //        for(int i=0; i<result.length; ++i){
        //            if(i<n){
        //                result[i] = arr2[i];
        //            }else{
        //                if(arr1.length > i){
        //                    result[i] = arr1[i-1];
        //                }
        //            }
        //        }
        //        break;
        //    default:
        //        len = (arr1.length > n) ? arrLen : n;
        //        result = new char[len];
        //        for(int i=0; i<result.length; ++i){
        //            if(i<n){
        //                result[i] = arr2[i];
        //            }else{
        //                if(arr1.length > i){
        //                    result[i] = arr1[i];
        //                }
        //            }
        //        }
        //        break;
        //}
        return result;
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
            System.out.format("Exception Message:\t%1$s\n", e.getMessage());
        }
    }
}
