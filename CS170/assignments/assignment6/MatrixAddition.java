import java.util.*;

public class MatrixAddition {
    public static void main(String[] args) {
        int n, m;
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the number of rows:");
        n = scan.nextInt();
        System.out.println("Please enter the number of columns:");
        m = scan.nextInt();

        int[][] a = new int[n][m];
        int[][] b = new int[n][m];

        System.out.println("Please enter matrix A:");
        for (int i = 0; i<n; i++){
            for (int j = 0; j < m; j++){
                a[i][j] = scan.nextInt();
            }
        }

        System.out.println("Please enter matrix B:");
        for (int i = 0; i<n; i++){
            for (int j = 0; j < m; j++){
                b[i][j] = scan.nextInt();
            }
        }

        for (int i = 0; i<n; i++){
            for (int j = 0; j < m; j++){
                System.out.print(a[i][j] + b[i][j] + " ");
            }
            System.out.println();
        }


    }
}
