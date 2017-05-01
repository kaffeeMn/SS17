public class EUCLID{
    private static int euclid(int a, int b){
        return (b==0) ? a : euclid(b,(a%b));
    }
    public static void main(String[] args){
        if(args.length != 2){
            throw new IllegalArgumentException("Submit 2 INTEGER!");
        }else{
            int a = Integer.parseInt(args[0]);
            int b = Integer.parseInt(args[1]);
            System.out.println(euclid(a,b));
        }
    }
}
