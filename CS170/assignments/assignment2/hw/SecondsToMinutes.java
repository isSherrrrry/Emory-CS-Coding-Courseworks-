import java.util.*;

public class SecondsToMinutes {
    public static void main(String[] args) {
        int seconds;

        Scanner input = new Scanner(System.in);

        System.out.println("Enter seconds:");
        seconds = input.nextInt();

        if (seconds < 0){
            System.out.println("invalid");
        }

        else {

            int min, remSecond;

            min = seconds / 60;

            remSecond = seconds % 60;

            if (min > 1 && remSecond > 1) {
                System.out.println(min + " minutes " + remSecond + " seconds");
            }
            if (min > 1 && remSecond <= 1) {
                System.out.println(min + " minutes " + remSecond + " second");
            }
            if (min <= 1 && remSecond > 1) {
                System.out.println(min + " minute " + remSecond + " seconds");
            }
            if (min <= 1 && remSecond <= 1) {
                System.out.println(min + " minute " + remSecond + " second");
            }
        }


    }
}
