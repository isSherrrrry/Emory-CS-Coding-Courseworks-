import java.util.*;

public class CloserPoint {
    public static void main(String[] args) {

        double aX, aY, bX, bY;

        double rX, rY;

        Scanner scan = new Scanner(System.in);

        System.out.println("Enter the coordinates for point A:");
        aX = scan.nextDouble();
        aY = scan.nextDouble();

        System.out.println("Enter the coordinates for point B:");
        bX = scan.nextDouble();
        bY = scan.nextDouble();

        double rangeA, rangeB;
        double minX, minY;

        rangeA = Math.abs(aX-bX);
        rangeB = Math.abs(aY-bY);

        minX = Math.min(aX,bX);
        minY = Math.min(aY,bY);

        Random random = new Random();

        rX = random.nextDouble() * rangeA + minX;
        rY = random.nextDouble() * rangeB + minY;


        double disAR, disBR;

        disAR = Math.sqrt(Math.pow((aX-rX),2) + Math.pow((aY-rY),2));
        disBR = Math.sqrt(Math.pow((bX-rX),2) + Math.pow((bY-rY),2));

        String resultRX = String.format("%.1f",rX);
        String resultRY = String.format("%.1f",rY);

        System.out.println("The random points coordinates: x = " + resultRX + ", y = " + resultRY);

        if (disAR<disBR){
            System.out.println("The output is: x = " + aX + ", y = " + aY);
        }

        else if (disAR>disBR){
            System.out.println("The output is: x = " + bX + ", y = " + bY);
        }

        else{
            System.out.println("The output is: x = " + aX + ", y = " + aY);
            System.out.println("The output is: x = " + bX + ", y = " + bY);
        }



    }
}
