import java.util.*;

public class DividedFourTimes {
    public static void main(String[] args) {
        int userNumber, divisor;

        Scanner input = new Scanner(System.in);

        System.out.println("Enter two numbers:");

        userNumber = input.nextInt();
        divisor = input.nextInt();

        int num1, num2, num3, num4;

        num1 = userNumber / divisor;

        num2 = userNumber / (divisor * divisor);

        num3 = userNumber / (divisor * divisor * divisor);

        num4 = userNumber / (divisor * divisor * divisor * divisor);

        System.out.println(num1 + " " + num2 + " " + num3 + " " + num4);


    }
}

