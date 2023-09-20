import java.util.Scanner;

public class searchMinIndex {
    public static int searchMinIndex(int[] A, int m) {
        if (A[A.length-1] < m || A[0] > m) return -1;
        int low = 0;
        int up = A.length - 1;
        while (low < up) {
            int mid = low + (up - low) / 2;
            if (A[mid] < m) {
                low = mid + 1;
            } else up = mid;
        }
        if (A[low] == m) return low;
        return -1;
    }

    public static void main(String[] args) {
        System.out.println("Array size:");
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        int[] a = new int[n];
        System.out.println("Sorted array:");
        for (int i = 0; i<n; i++){
            a[i] = scan.nextInt();
        }
        System.out.println("Element finding:");
        int m = scan.nextInt();

        System.out.print(searchMinIndex(a,m));


    }
}
