public class Muenzen{
    
    private static void change(int value, int[] pieces){
        int[] result = new int[pieces.length];
        int counter;
        for(int i=0; i<pieces.length; ++i){
            if(value>=pieces[i]){
                counter = value / pieces[i];
                result[i] = counter;
                value -= counter * pieces[i];
            }else{
                result[i] = 0;
            }
        }
        System.out.println(Algorithm.arrToString(result));
    }
    private static void printEuroArr(int value){
        int[] euros = {200,100,50,20,10,5,2,1};
        change(value, euros);
    }
    private static void printAlternativeArr(int value){
        int[] alternative = {200,100,50,20,10,5,4,2,1};
        change(value, alternative);
    }
    private static String results(int[] arr){
        if(arr != null){
            String s = "[" + arr[0];
            for(int i=1; i<arr.length; ++i)    s += ", " + arr[i];
            return s + "]";
        }
        return "[]";
    }
    private static void task(String arg0, String arg1){
        try{
            int value = Integer.parseInt(arg1);
            switch(arg0){
                case "Euro":
                    printEuroArr(value);
                    break;
                case "Alternative":
                    printAlternativeArr(value);
                    break;
                default:
                    break;
            }
        }catch(Exception e){
            QuickSort.handleException(e);
        }
    }
    public static void main(String[] args){
        if(args.length < 2){
        }else{
            task(args[0], args[1]);
        }
    }
}
