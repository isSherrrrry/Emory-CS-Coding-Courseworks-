import java.util.*;

public class LeapYear {
    public static void main(String[] args) {
        System.out.println("Please enter a year:");
        Scanner scan = new Scanner(System.in);
        int year = scan.nextInt();

        System.out.println(((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))? year + " is a leap year.":year + " is not a leap year.");


    }
}
