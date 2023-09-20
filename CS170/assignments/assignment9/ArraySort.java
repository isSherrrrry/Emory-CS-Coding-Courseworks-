import java.util.*;

public class ArraySort {
    private ArrayList<Integer> list = new ArrayList<>();

    ArraySort(){
    }

    ArraySort(int[] array){
        for (int i : array)
        {
            list.add(i);
        }
    }

    public int search(int target){
        if (list.size() <= 0) return -1;
        boolean contain = list.contains(target);
        if (contain) return Collections.frequency(list, target);
        else return -1;
    }

    public int maximum(){
        if (list.size() <= 0) return -1;
        else return Collections.max(list);
    }
    public int minimum(){
        if (list.size() <= 0) return -1;
        else return Collections.min(list);
    }

    public int getElement(int index){
        try {
            return list.get(index);
        } catch (Exception e) {
            System.out.println("IndexOutOfBoundsException");
            return Integer.MIN_VALUE;
        }

    }

    public void setElement(int index, int value){
        try {
            list.set(index,value);
        } catch (Exception e) {
            System.out.println("IndexOutOfBoundsException");
        }
    }

    public void addElement(int value){
        list.add(value);
    }

    public void sort(){
        Collections.sort(list);
    }

    public void printArray(){
        System.out.println("Array is:");
        for (Integer i : list) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    public void delete(int value){
        list.removeIf( n -> (n == value));
    }

}
