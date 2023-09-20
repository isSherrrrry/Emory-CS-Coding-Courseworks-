import java.util.*;

public class CartesianToPolar {
    public static void main(String[] args) {
        double x,y;

        Scanner scan = new Scanner(System.in);

        x = scan.nextDouble();
        y = scan.nextDouble();

        double r, theta;

        r = Math.sqrt(x * x + y * y);
        theta = Math.atan2(y,x);

        double degree = theta / 3.1415926 * 180;

        System.out.print("r = ");
        System.out.printf("%.2f", r);
        System.out.print(", θ = ");
        System.out.printf("%.2f", degree);
        System.out.println("°");
    }
}
