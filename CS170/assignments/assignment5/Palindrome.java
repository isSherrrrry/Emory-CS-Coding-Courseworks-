import java.util.*;

public class Palindrome {
    public static void main(String[] args) {
        System.out.println("Please enter a word:");
        Scanner scan = new Scanner(System.in);

        String word = scan.next();

        int count = 0;
        int loopTime = 0;

        for(int i = 0; i<=word.length()/2; i++){
            loopTime++;
            if (word.charAt(i) == word.charAt(word.length()-1-i)){
                count++;
            }
            else break;
        }

        if (loopTime==count){
            System.out.print(word + " is a palindrome");
        }

        else {
            System.out.print(word + " is not a palindrome");
        }
    }
}
