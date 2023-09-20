import java.util.*;

public class findTwinPrimes {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int num = scan.nextInt();
        findTwinPrimes(num);
    }

    public static boolean isPrime (int x){
        if (x<2) return false;
        if (x==2) return true;
        for (int i = 2; i <= Math.sqrt(x) + 1;i++){
            if (x%i == 0) return false;
        }
        return true;
    }

    public static void findTwinPrimes(int n){
        if (n == 1) return;
        if (isPrime(n) && isPrime(n-2)){
            System.out.println(n + " " + (n-2));
        }
        findTwinPrimes(n-1);
    }
}
