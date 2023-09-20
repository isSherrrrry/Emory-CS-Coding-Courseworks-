import java.util.*;

public class ProductAndAverage {
    public static void main(String[] args) {
        int num1, num2, num3;

        Scanner input = new Scanner(System.in);

        System.out.println("Enter three numbers:");

        num1 = input.nextInt();
        num2 = input.nextInt();
        num3 = input.nextInt();

        int product = num1 * num2 * num3;

        double average;

        average = (num1 + num2 + num3) / 3.0;

        System.out.print(product + " ");
        System.out.printf("%.2f", average);

    }
}
