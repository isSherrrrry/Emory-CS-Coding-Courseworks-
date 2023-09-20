import java.util.*;

public class ReplaceChar {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        System.out.println("Enter a string:");
        String line = scan.nextLine();

        System.out.println("Enter the index:");
        int index = scan.nextInt();

        System.out.println("Enter the character:");
        String charString = scan.next();

        char aChar = charString.charAt(0);

        String newString = line.substring(0,index) + aChar + line.substring(index+1);

        System.out.println(newString);


    }
}
