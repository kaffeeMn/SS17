import java.util.Random;

public class LCS{

    private static int[][] algorithm(String str1, String str2){
        int len1 = str1.length();
        int len2 = str2.length();
        // first column and row is needed for reference, hence len+1
        int[][] table = new int[len1+1][len2+1];
        // initializing a map/ table with zeros for the first column and row
        for(int i=0; i<len1 + 1; ++i) table[i][0] = 0;
        for(int i=0; i<len2 + 1; ++i) table[0][i] = 0;
        // initializing the table
        for(int i=0; i<len1; ++i){
            for(int j=0; j<len2; ++j){
                laenge(str1, str2, table, i, j);
            }
        }
        return table;
    }
    private static void laenge(String str1, String str2, int[][] table, int i, int j){
        // simple cases if chars are alike we inkrement, otherwise we choose the next biggest
        // prior value
        if(str1.charAt(i) == str2.charAt(j)){
            // the diagonal line is the exact string to string comparison, so multiples of a 
            // character will only be counted be the occurence of themself in the other string
            table[i+1][j+1] = table[i][j] + 1;
        }else{
            // left and right to the diagonal line are variations of the string to string comparision
            // their maximum will be counted, for it is now the longest subsequence up to i, j
            if(table[i][j+1] >= table[i+1][j]){
                table[i+1][j+1] = table[i][j+1];
            }else{
                table[i+1][j+1] = table[i+1][j];
            }
        }
    }
    private static void task(String str1, String str2){
        float tStart, tEnd;
        // printing input for reference
        System.out.println("Grahic output:");
        System.out.format("Input:\t%1$s\t%2$s\n", str1, str2);
        // calling the gcc for a cleaner runtime
        int[][] table;
        System.gc();
        tStart  = System.currentTimeMillis();
        table = algorithm(str1, str2);
        tEnd    = System.currentTimeMillis();
        String resString = tableToSol(table, str2);
        // printing output
        System.out.format("Output:\n%1$s\n", parseStr(table, str1, str2));
        System.out.format("It took %1$s ms\n", tEnd - tStart);
        System.out.println("Resulting string:\t" + resString);
    }
    private static String tableToSol(int[][] table, String str){
        String result= "";
        int counter = 0;
        for(int i=1; i<str.length()+1; ++i){
            if(table[i][i] > counter){
                result += str.charAt(i-1);
                ++counter;
            } 
        }
        return result;
    }
    private static String parseStr(int[][] table, String str1, String str2){
        // method to represent the table as a string 
        // tab at beginning due to the display of the letters first
        String result = "\t";
        // first line is the second string
        for(int i=0; i<table.length - 1; ++i){
            result += String.format("\t%1$s", str2.charAt(i));
        }
        for(int i=0; i<table.length; ++i){
            result += "\n";
            for(int j=0; j<table[0].length; ++j){
                if(j==0 && i>0){
                    // first column displays the first string vertically
                    result += String.format("%1$s", str1.charAt(i-1));
                }
                result += String.format("\t%1$d", table[i][j]);
            }
        }
        return result;
    }
    // method from sheet
    private static String randStr(int n, Random r){
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder res = new StringBuilder(n);
        while (0 < n--){
            res.append(alphabet.charAt(r.nextInt(alphabet.length())));
        }
        alphabet = null;
        return res.toString();
    }
    public static void main(String[] args){
        try{
            if(args == null) throw new IllegalArgumentException("Give me some input.");
            // generating random stringths of the length n
            int n = Integer.parseInt(args[0]);
            if(n<=0) throw new IllegalArgumentException("n needs to be larger than 0");
            Random randFac = new Random();
            String rStr1 = randStr(n, randFac);
            String rStr2 = randStr(n, randFac);
            // for the System.gc call
            randFac = null;
            // main task
            task(rStr1, rStr2);
        }catch(Exception e){
            e.printStackTrace();
            System.out.format("Exception with the Message '%1$s' has occured.", e.getMessage());
        }
    }
}
