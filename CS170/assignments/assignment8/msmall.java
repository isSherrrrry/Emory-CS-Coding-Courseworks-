import java.util.Scanner;

public class msmall {
    public static int msmall(int[] A, int m){
        sort(A, 0, A.length);
        return A[m-1];
    }

    public static void sort(int[] a, int start, int end){
        int small = start;
        int temp;
        if (start<end-1){
            for (int i = start+1; i<end; i++){
                if (a[i] < a[small]) small = i;
            }
            temp = a[small];
            a[small] = a[start];
            a[start] = temp;
            sort(a,start+1,end);
        }
    }
    public static void main(String[] args) {
        System.out.println("Array size:");
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        int[] a = new int[n];
        System.out.println("Array:");
        for (int i = 0; i<n; i++){
            a[i] = scan.nextInt();
        }
        System.out.println("m:");
        int m = scan.nextInt();

        if (m<=n) System.out.print(msmall(a,m));
        else System.out.print("invalid");

    }
}
