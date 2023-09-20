import java.util.Scanner;

public class sum {
    public static void result(int[] a, int start, int end, int n){
        for (int i = start; i<end; i++){
            int sum = a[i] + a[end];
            if (sum == n){
                System.out.print(i + ", " + end);
                return;
            }
            if (sum>n) result(a,i,end-1,n);
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
        System.out.println("n:");
        int num = scan.nextInt();
        result(a, 0,n-1, num);
    }
}
