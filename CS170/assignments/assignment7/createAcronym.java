import java.util.*;

public class createAcronym {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        createAcronym(input);
    }
    public static void createAcronym(String userPhrase){
        int count = userPhrase.length() - userPhrase.replaceAll(" ", "").length();
        int index = userPhrase.indexOf(' ');
        if (Character.isUpperCase(userPhrase.charAt(0)) && index !=0) System.out.print(userPhrase.charAt(0));
        userPhrase = userPhrase.substring(index+1);
        if (count == 0 || index == 0) return;
        createAcronym(userPhrase);
    }
}
