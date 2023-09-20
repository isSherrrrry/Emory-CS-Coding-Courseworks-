import java.util.*;

public class GCD {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Please enter two positive integers:");
        int a = scan.nextInt();
        int b = scan.nextInt();

        System.out.print(getGCD(a,b));

    }
    static int getGCD(int x, int y){
        int max = Math.max(x,y);
        int min = Math.min(x,y);

        if (max % min == 0){
            return min;
        }
        else {
            return getGCD(min, max%min);
        }
    }
}
