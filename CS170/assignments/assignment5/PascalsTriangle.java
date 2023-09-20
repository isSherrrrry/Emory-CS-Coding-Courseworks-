import java.util.*;

public class PascalsTriangle {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int usern = scan.nextInt();
        int n = usern + 1;

        int[][] triangle = new int[1000][1000];

        for (int i = 1; i<n; i++){
            triangle[i][1] = 1;
            triangle[i][i] = 1;
        }

        for (int i = 3; i<n; i++){
            for (int j = 2; j<i; j++){
                triangle[i][j] = triangle[i-1][j-1] + triangle[i-1][j];
            }
        }

        for (int i=1; i<n; i++){
            for (int j=1; j<=i;j++){
                System.out.print(triangle[i][j] + " ");
            }
            System.out.println();
        }



    }
}
