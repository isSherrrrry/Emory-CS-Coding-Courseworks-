import java.util.*;

public class GrayRemover {
    public static void main(String[] args) {
        System.out.println("Please enter the rgb values:");
        Scanner scan = new Scanner(System.in);
        int r = scan.nextInt();
        int g = scan.nextInt();
        int b = scan.nextInt();

        if ((r<0) || (r>255) || (g<0) || (g>255) || (b<0) || (b>255)){
            System.out.println("invalid input");
        }
        else {
            int min = Math.min(Math.min(r,g),b);
            System.out.println((r-min) + " " + (g-min) + " " + (b-min));
        }

    }
}
