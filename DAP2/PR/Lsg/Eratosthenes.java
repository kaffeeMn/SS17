public class Eratosthenes{
    private static boolean isPrime(int n){
        if(n == 2)  return true;
        if(n < 2)   return false;
        int limit = (int) Math.round(Math.sqrt(n));
        for(int i = 2; i <= limit; ++i)
            if(n % i == 0)  return false;
        return true;
    }
    private static boolean[] sieve(int n){
        boolean[] result = new boolean[n-1];
        int pos = 0;
        for(int i = 2; i <= n; ++i){
            if(isPrime(i))  result[pos] = true;
            else            result[pos] = false;
            ++pos;
        }
        return result;
    }
    public static void main(String[] args){
        int n = Integer.parseInt(args[0]);
        boolean[] primeList = sieve(n);
        if(args.length > 1){
            String param = args[1];
            if(param.charAt(0) == '-' && param.charAt(1) == 'o'){
                int primeCount = 0;
                int nCount = 0;
                for(boolean bool : primeList){
                    if(bool){
                        System.out.print(nCount+"\t");
                        ++primeCount;
                    }
                    ++nCount;
                }
                System.out.println("\nTotal primes:\t"+primeCount);
            }else{
                throw new IllegalArgumentException("Unknown parameter:\t"+param);
            }
        }else{
            int primeCount = 0;
            int len = primeList.length;
            for(int i = 0; i < len; ++i)
                if(primeList[i]) ++primeCount;
            System.out.println("Total primes:\t"+primeCount);
        }
    }
}
