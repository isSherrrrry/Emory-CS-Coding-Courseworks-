import java.util.Scanner;

public class printCombinationR {
    public static void main (String[] args) {
        System.out.println("Array size:");
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        int[] a = new int[n];
        System.out.println("Array:");
        for (int i = 0; i<n; i++){
            a[i] = scan.nextInt();
        }
        System.out.println("r:");
        int r = scan.nextInt();
        printCombination(a, r);
    }
    static void printCombination(int[] array, int r)
    {
        int n = array.length;
        int result[]=new int[r];
        combinationUtil(array, result, 0, n-1, 0, r);
    }
    static void combinationUtil(int[] array, int[] result, int start,
                                int end, int index, int r)
    {
        if (index == r)
        {
            for (int j=0; j<r; j++)
                System.out.print(result[j]+" ");
            System.out.println();
            return;
        }

        int dataLeft = r - index;

        for (int i=start; i<=end && end-i+1 >= dataLeft; i++)
        {
            result[index] = array[i];
            combinationUtil(array, result, i+1, end,index+1, r);
        }
    }



}
