import java.util.*;

public class LotteryNumber {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter a two-digit number here: ");
        int userNum = scan.nextInt();

        if (userNum > 99 || userNum <0){
            System.out.println("Input Error");
        }

        else{
            int num1 = userNum % 10;
            int num2 = userNum / 10;

            Random randNum = new Random();

            int winningNum = randNum.nextInt(100);

            int winningNum1 = winningNum % 10;
            int winningNum2 = winningNum / 10;

            boolean match1, match2, match3;

            boolean match11, match12, match21, match22;

            match11 = (num1 == winningNum1);
            match12 = (num1 == winningNum2);
            match21 = (num2 == winningNum1);
            match22 = (num2 == winningNum2);

            match1 = (match11 && match22);

            match2 = (match12 && match21);

            match3 = (match11 || match12 || match21 || match22);

            System.out.println("The winning numer is: " + winningNum);

            if (match1){
                System.out.println("Your award is $10,000.");
            }
            else if (match2){
                System.out.println("Your award is $3,000.");
            }
            else if (match3){
                System.out.println("Your award is $1,000.");
            }
            else{
                System.out.println("You did not win this lottery.");
            }

        }

    }
}
