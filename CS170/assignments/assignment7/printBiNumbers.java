import java.util.*;
public class printBiNumbers {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int numBit = scan.nextInt();
        printBiNumbers(numBit);

    }
    public static void printBiNumbers(int n){
        String result = "";
        isPrint1(result, 0, 0, n);
    }

    public static void isPrint1(String result, int zero, int one, int length){
        if (one<zero) return;
        if (length == 0){
            System.out.println(result);
            return;
        }
        isPrint1(result + "1", zero, one+1, length-1);
        isPrint1(result + "0", zero+1, one, length-1);
    }
}
