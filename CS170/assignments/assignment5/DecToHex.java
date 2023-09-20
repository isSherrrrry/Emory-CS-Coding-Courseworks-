import java.util.*;

public class DecToHex {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        int num = scan.nextInt();
        int base = 16;

        String[] list ={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};

        String[] finalAns = new String[10000];
        int modAns;
        int count = 0;

        while (num>=base){
            modAns = num % base;
            num = num / base;
            finalAns[count] = list[modAns];
            count++;
        }

        if (num % base == 0){
            System.out.print(list[base-1]);
            for (int i = count-1; i>=0; i--) {
                System.out.print(finalAns[i]);
            }
        }

        else {
            System.out.print(list[num]);
            for (int i = count-1; i>=0; i--) {
                System.out.print(finalAns[i]);
            }

        }

    }
}
