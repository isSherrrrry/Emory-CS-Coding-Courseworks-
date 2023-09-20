import java.util.Random;
import java.util.Scanner;

public class checkTTT {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Please enter the dimension:");
        int n = scan.nextInt();

        Random randomGenerator = new Random();

        char[][] game = new char[n][n];

        for (int i = 0; i<n; i++){
            for (int j = 0; j<n; j++){
                int randomInt = randomGenerator.nextInt(100);
                if(randomInt%2 == 0){
                    game[i][j] = 'O';
                }
                else {
                    game[i][j] = 'X';
                }
            }
        }

        int count = 0;

        for (int i = 0; i<n; i++){
            for (int j = 0; j<n; j++) {
                if (game[i][j] == 'X') count++;
            }
        }

        char replace, toReplace;

        int whichLoop = n*n-2*count;
        if (whichLoop < 0){
            toReplace = 'X';
            replace = 'O';
        }
        else{
            toReplace = 'O';
            replace = 'X';
        }

        int numLoop = Math.abs((n*n-count) - count) / 2;

        int[] positionXA = new int[n*n];
        int[] positionYA = new int[n*n];
        int positionCountA = 0;

        int[] positionXB = new int[n*n];
        int[] positionYB = new int[n*n];
        int positionCountB = 0;

        boolean shouldChange = true;

        if ((n*n/2)%2 == 0){
            if (count == n*n/2){
                shouldChange = false;
            }
        }
        else {
            if ((count == n*n/2-1) || (count == n*n/2+1)){
                shouldChange = false;
            }
        }

        if (shouldChange){
            for (int i = 0; i<n; i++) {
                for (int j = 0; j < n; j++) {
                    if (game[i][j] == toReplace){
                        positionXA[positionCountA] = i;
                        positionYA[positionCountA] = j;
                        positionCountA++;
                    }
                }
            }
            for (int i = 0; i<n; i++) {
                for (int j = 0; j < n; j++) {
                    if (game[i][j] == replace){
                        positionXB[positionCountB] = i;
                        positionYB[positionCountB] = j;
                        positionCountB++;
                    }
                }
            }
            int loopCount = 0;
            while(numLoop != 0){
                game[positionXA[loopCount]][positionYA[loopCount]] =  game[positionXB[loopCount]][positionYB[loopCount]];
                loopCount++;
                numLoop--;
            }


            for (int i = 0; i<n; i++){
                for (int j = 0; j<n; j++){
                    System.out.print(game[i][j] + " ");
                }
                System.out.println();
            }

            System.out.println(checkTTT(game));
        }
    }

    public static boolean checkTTT(char[][] result){
        int numRowCol = result.length;
        boolean checkX = checkRow(result, 'O', numRowCol) || checkCol(result, 'O', numRowCol) || checkDia(result, 'O', numRowCol) || checkAntidia(result, 'O', numRowCol);
        boolean checkO = checkRow(result, 'X', numRowCol) || checkCol(result, 'X', numRowCol) || checkDia(result, 'X', numRowCol) || checkAntidia(result, 'X', numRowCol);
        if (checkO ^ checkX) return true;
        return false;
    }

    public static boolean checkRow(char[][] input, char inputCha, int n){
        int count;
        for (int i = 0; i<n; i++){
            count = 0;
            for (int j = 0; j<n; j++){
                if (input[i][j] == inputCha) count++;
                if (count == n) return true;
            }
        }
        return false;
    }

    public static boolean checkCol(char[][] input, char inputCha, int n){
        int count;
        for (int i = 0; i<n; i++){
            count = 0;
            for (int j = 0; j<n; j++){
                if (input[j][i] == inputCha) count++;
                if (count == n) return true;
            }
        }
        return false;
    }

    public static boolean checkDia(char[][] input, char inputCha, int n){
        for (int i = 0; i<n; i++){
            if(input[i][i] != inputCha) break;
            if(i == n-1) return true;
        }
        return false;
    }

    public static boolean checkAntidia(char[][] input, char inputCha, int n){
        for (int i = 0; i<n; i++){
            if(input[i][n-1-i] != inputCha) break;
            if(i == n-1) return true;
        }
        return false;
    }
}
