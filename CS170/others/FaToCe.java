import java.util.Scanner;

public class FaToCe{
	public static void main(String[] args) {
		double fa, ca;

		Scanner scan = new Scanner(System.in);
		System.out.println("Temperature in Fahrenheit:");

        fa = scan.nextDouble();

        ca = (fa - 32) * 5/9;

        System.out.println("Temperature in Celsius:" + ca); 
	}
}