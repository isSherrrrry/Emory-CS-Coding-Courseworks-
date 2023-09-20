import java.util.*;

public class MatrixMultiplication {
    public static void main(String[] args) {
        int ra, rb, ca, cb;

        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the number of rows for the first matrix:");
        ra = scan.nextInt();
        System.out.println("Please enter the number of columns for the first matrix:");
        ca = scan.nextInt();

        System.out.println("Please enter the number of rows for the second matrix:");
        rb = scan.nextInt();
        System.out.println("Please enter the number of columns for the second matrix:");
        cb = scan.nextInt();

        int[][] a = new int[ra][ca];
        int[][] b = new int[rb][cb];

        int[][] ans = new int[ra][cb];

        if (ca != rb){
            System.out.println("invalid");
            return;
        }

        System.out.println("Please enter matrix A:");
        for (int i = 0; i<ra; i++){
            for (int j = 0; j < ca; j++){
                a[i][j] = scan.nextInt();
            }
        }

        System.out.println("Please enter matrix B:");
        for (int i = 0; i<rb; i++){
            for (int j = 0; j < cb; j++){
                b[i][j] = scan.nextInt();
            }
        }

        for (int i = 0; i < ra; i++){
            for (int j = 0; j < cb; j++){
                for (int k = 0; k < ca; k++){
                    ans[i][j] = ans[i][j] + a[i][k] * b[k][j];
                }
            }
        }

        for (int i = 0; i<ra; i++){
            for (int j = 0; j < cb; j++){
                System.out.print(ans[i][j] + " ");
            }
            System.out.println();
        }


    }
}
