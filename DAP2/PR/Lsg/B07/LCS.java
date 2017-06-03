import java.util.Random;

public class LCS{

    private static int[][] algorithm(String str1, String str2){
        int len1 = str1.length();
        int len2 = str2.length();
        int[][] table = new int[len1+1][len2+1];
        for(int i=0; i<len1 + 1; ++i) table[i][0] = 0;
        for(int i=0; i<len2 + 1; ++i) table[0][i] = 0;
        for(int i=0; i<len1; ++i){
            for(int j=0; j<len2; ++j){
                laenge(str1, str2, table, i, j);
            }
        }
        return table;
    }
    private static void laenge(String str1, String str2, int[][] table, int i, int j){
        if(str1.charAt(i) == str2.charAt(j)){
            table[i+1][j+1] = table[i][j] + 1;
        }else{
            if(table[i][j+1] >= table[i+1][j]){
                table[i+1][j+1] = table[i][j+1];
            }else{
                table[i+1][j+1] = table[i+1][j];
            }
        }
    }
    private static void task(String str1, String str2){
        float tStart, tEnd;
        System.out.println("Grahic output:");
        System.out.format("Input:\t%1$s\t%2$s\n", str1, str2);
        // calling the gcc for a cleaner runtime
        int[][] table;
        System.gc();
        tStart  = System.currentTimeMillis();
        table = algorithm(str1, str2);
        tEnd    = System.currentTimeMillis();
        System.out.format("Output:\n%1$s\n", parseStr(table, str1, str2));
        System.out.format("It took %1$s ms\n", tEnd - tStart);
    }
    private static String parseStr(int[][] table, String str1, String str2){
        String result = "\t";
        for(int i=0; i<table.length - 1; ++i){
            result += String.format("\t%1$s", str1.charAt(i));
        }
        for(int i=0; i<table.length; ++i){
            result += "\n";
            for(int j=0; j<table[0].length; ++j){
                if(j==0 && i>0){
                    result += String.format("%1$s", str2.charAt(i-1));
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
            int n = Integer.parseInt(args[0]);
            Random randFac = new Random();
            String rStr1 = randStr(n, randFac);
            String rStr2 = randStr(n, randFac);
            randFac = null;
            task(rStr1, rStr2);
        }catch(Exception e){
            e.printStackTrace();
            System.out.format("Exception with the Message '%1$s' has occured.", e.getMessage());
        }
    }
}
