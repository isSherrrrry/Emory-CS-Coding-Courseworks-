import java.util.*;

public class WallPaint {
    public static void main(String[] args) {
        double height,width;

        Scanner scan = new Scanner(System.in);

        System.out.println("Enter wall height (feet):");
        height = scan.nextDouble();

        System.out.println("Enter wall width (feet):");
        width = scan.nextDouble();

        double area = width * height;

        double gallon = area / 350;

        double can = Math.ceil(gallon);

        System.out.println("Wall area: " + area + " square feet");
        System.out.println("Paint needed: " + gallon + " gallons");
        System.out.println("Cans needed: " + (int)can + " can(s)");

    }
}
