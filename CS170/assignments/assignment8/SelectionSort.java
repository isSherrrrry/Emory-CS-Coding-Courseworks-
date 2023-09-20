public class SelectionSort {
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
        int[] a = {4, 9, 7, 4, 1, 2, 2, 3, 7, 4, 5, 8, 7, 6};
        sort(a, 0, a.length);
        for (int i = 0; i<a.length;i++){
            System.out.print(a[i] + " ");
        }
    }
}
