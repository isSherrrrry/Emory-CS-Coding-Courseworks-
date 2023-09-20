import java.util.*;
import java.lang.*;
import java.io.*;

public class CeaserCodes {
    public static void main(String[] args) throws IOException {

        String pathname = "/Users/liuyijun/Desktop/example-1.txt";
        String pathname2 = "/Users/liuyijun/Desktop/cipher.txt";
        String beforeEncrypt = "";

        try{
            File myfile = new File(pathname);
            Scanner file = new Scanner(myfile);
            while (file.hasNextLine()){
                beforeEncrypt = file.nextLine();
            }
            file.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the value for n:");
        int n = scan.nextInt();

        String oriN = "" + n;

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

        File write = new File(pathname2);
        write.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(pathname2));

        out.write(oriN);
        out.write("\r\n");
        out.write(CaesarCode);
        out.flush();
        out.close();

    }
}
