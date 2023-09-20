import java.util.*;

public class MathHelp {
    public static void main(String[] args) {
        int number1, number2;

        Random randNum = new Random();
        number1 = randNum.nextInt(10);
        number2 = randNum.nextInt(10);

        int maxNum = Math.max(number1,number2);
        int minNum = Math.min(number1,number2);

        System.out.println("What is " + maxNum + " - " + minNum + " ?");

        Scanner scan = new Scanner(System.in);
        int studAns = scan.nextInt();

        int correctAns = maxNum - minNum;

        System.out.println((studAns == correctAns)? "Correct":"Incorrect");

    }
}
