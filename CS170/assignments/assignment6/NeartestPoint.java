import java.util.*;

public class NeartestPoint {
    public static void main(String[] args) {
        double[] pointX = new double[10];
        double[] pointY = new double[10];

        final int invalid = -999999;

        Scanner scan = new Scanner(System.in);

        System.out.println("Enter ten points:");

        for (int i = 0; i<10; i++){
            pointX[i] = scan.nextDouble();
            pointY[i] = scan.nextDouble();
        }


        double totalDis;
        double minDis = Math.sqrt(Math.pow((pointX[0] - pointX[1]),2) + Math.pow(pointY[0] - pointY[1],2));

        double minX1 = pointX[0];
        double minX2 = pointX[1];
        double minY1 = pointY[0];
        double minY2 = pointY[1];


        for (int i = 0; i<10; i++){
            for (int j = i+1; j<10; j++){
                double disX = Math.abs(pointX[i] - pointX[j]);
                double disY = Math.abs(pointY[i] - pointY[j]);
                totalDis = Math.sqrt(Math.pow(disX,2) + Math.pow(disY,2));
                if (totalDis < minDis){
                    minDis = totalDis;
                    minX1 = pointX[i];
                    minX2 = pointX[j];
                    minY1 = pointY[i];
                    minY2 = pointY[j];
                }
            }
        }

        System.out.println("(" + minX1 + ", " + minY1 + ")" + " and (" + minX2 + ", " + minY2 + ") are nearest to each other");


    }
}
