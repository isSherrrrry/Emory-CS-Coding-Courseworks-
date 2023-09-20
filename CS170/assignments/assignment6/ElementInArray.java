import java.util.*;
public class ElementInArray {
    public static void main(String[] args) {
        final double[] myList = {0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377};
        Scanner scan = new Scanner(System.in);

        System.out.println("Please enter your number:");
        double e = scan.nextDouble();

        boolean isE = false;

        for (int i = 0; i< myList.length; i++){
            if (Math.abs(e - myList[i]) < 0.0001) {
                isE = true;
            }
        }

        if (isE){
            System.out.print("It is in the array");
        }
        else{
            System.out.print("It is not in the array");
        }
    }
}
