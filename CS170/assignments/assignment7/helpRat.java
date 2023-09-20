import java.util.Scanner;

public class helpRat {
    static int[] dx = {0,-1,0,1};
    static int[] dy = {-1,0,1,0};
    static int k = 0;
    static int n;
    static int[][] maze = new int[50][50];
    static int[][] record = new int[50][50];
    static int[][] steps = new int[2501][2];
    static boolean isSolution = false;

    public static void main(String[] args) {
        System.out.println("Dimensions:");
        Scanner scan = new Scanner(System.in);
        n = scan.nextInt();
        for(int i=1;i<=n;i++){
            for(int j=1;j<=n;j++) {
                maze[i][j] = scan.nextInt();
            }
        }
        helpRat(1,1);
        if (!isSolution) System.out.println("there is no such path");
    }

    public static void helpRat(int x, int y){
        if (x == n && y == n && !isSolution){
            road();
            return;
        }
        for (int i = 0; i<4; i++){
            if (maze[x+dx[i]][y+dy[i]] == 1 && record[x+dx[i]][y+dy[i]] == 0){
                record[x][y] = 1;
                steps[k][0] = x;
                steps[k][1] = y;
                k++;
                helpRat(x+dx[i], y+dy[i]);
                record[x][y] = 0;
                k--;
            }
        }
    }

    public static void road(){
        if (!isSolution){
            isSolution = true;
        }
        for (int i = 0; i<k; i++){
            System.out.print("(" + steps[i][0] + ", " + steps[i][1] + ") ");
        }
        System.out.println("(" + n + ", " + n + ") ");

    }
}
