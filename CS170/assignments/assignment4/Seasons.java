import java.util.*;

public class Seasons {
    public static void main(String[] args) {
        System.out.println("Please enter the date:");
        Scanner scan = new Scanner(System.in);

        String month = scan.next();
        int day = scan.nextInt();

        String[] monthList = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        boolean isMonth = false;
        int i = 0;
        int monthNum = 0;

        while (!isMonth && i<=11){
            if (month.equals(monthList[i])){
                monthNum = i + 1;
                isMonth = true;
            }
            else {
                i++;
            }
        }

        if (isMonth && (day > 0)){
            int maxDays = switch (monthNum) {
                case 2 -> 28;
                case 4, 6, 9, 11 -> 30;
                default -> 31;
            };

            if (day <= maxDays){
                int dateNum = monthNum * 100 + day;
                if((dateNum>=320) && (dateNum<=620)){
                    System.out.println("spring");
                }
                else if((dateNum>=621) && (dateNum<=921)){
                    System.out.println("summer");
                }
                else if((dateNum>=922) && (dateNum<=1220)){
                    System.out.println("autumn");
                }
                else {
                    System.out.println("winter");
                }
            }

            else{
                System.out.println("invalid");
            }

        }

        else{
            System.out.println("invalid");
        }
    }
}
