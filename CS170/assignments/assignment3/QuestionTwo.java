import java.util.*;

public class QuestionTwo {
    public static void main(String[] args) {

        double x,y,z;

        Scanner scan = new Scanner(System.in);

        System.out.println("Please enter three floating-point numbers:");
        x = scan.nextDouble();
        y = scan.nextDouble();
        z = scan.nextDouble();

        double xPowerY = Math.pow(x,y);

        double xPowerYZ = Math.pow(x,Math.pow(y,z));

        double xAbs = Math.abs(x);

        double squareRoot = Math.sqrt(Math.pow(x*y,z));

        System.out.println("x to the power of y = " + xPowerY);
        System.out.println("x to the power of (y to the power of z) = " + xPowerYZ);
        System.out.println("The absolute value of x = " + xAbs);
        System.out.println("The square root of ((x*y) to the power of z)= " + squareRoot);

    }
}
