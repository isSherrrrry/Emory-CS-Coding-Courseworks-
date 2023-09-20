import java.util.*;

public class Zodiac {
    public static void main(String[] args) {
        System.out.println("Please enter a year:");
        Scanner scan = new Scanner(System.in);
        int year = scan.nextInt();

        int yearAnimal = year % 12;

        switch (yearAnimal) {
            case 0 -> System.out.println("monkey");
            case 1 -> System.out.println("rooster");
            case 2 -> System.out.println("dog");
            case 3 -> System.out.println("pig");
            case 4 -> System.out.println("rat");
            case 5 -> System.out.println("ox");
            case 6 -> System.out.println("tiger");
            case 7 -> System.out.println("rabbit");
            case 8 -> System.out.println("dragon");
            case 9 -> System.out.println("snake");
            case 10 -> System.out.println("horse");
            case 11 -> System.out.println("sheep");
            default -> System.out.println("invalid");
        }
    }
}
