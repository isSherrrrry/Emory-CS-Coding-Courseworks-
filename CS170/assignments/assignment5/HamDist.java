import java.util.*;
public class HamDist {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Please enter the value for x:");
        int x = scan.nextInt();
        System.out.println("Please enter the value for y:");
        int y = scan.nextInt();

        int xor = x^y;

        int count = 0;

        while (xor!=0){
            xor = xor & (xor-1);
            count++;
        }

        System.out.print(count);
    }
}
