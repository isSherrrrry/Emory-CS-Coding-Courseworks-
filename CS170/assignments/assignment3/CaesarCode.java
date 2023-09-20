import java.util.*;
import java.lang.*;

public class CaesarCode {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter a string consisting of ONE mix-case letter:");
        String beforeEncrypt = scan.nextLine();

        System.out.println("Enter the value for n:");
        int n = scan.nextInt();

        if (n < 0){
            n = n + 26;
        }

        int lengthStr = beforeEncrypt.length();

        String CaesarCode = "";

        for (int i = 0; i <= lengthStr - 1; i++){
            char letter = beforeEncrypt.charAt(i);

            if (letter >= 'a' && letter <= 'z'){

                letter = (char)(letter + n);

                if (letter > 'z'){
                    letter = (char)(letter - 26);
                }

                letter = Character.toUpperCase(letter);

                CaesarCode += letter;

            }

            else if (letter >= 'A' && letter <= 'Z'){

                letter = (char)(letter + n);

                if (letter > 'Z'){
                    letter = (char)(letter - 26);
                }

                CaesarCode += letter;

            }

            else{
                CaesarCode += letter;
            }
        }

        System.out.println("ciphertext = " + CaesarCode);


    }
}
