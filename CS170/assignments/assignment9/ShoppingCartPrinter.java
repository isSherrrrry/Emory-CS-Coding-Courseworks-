import java.util.Scanner;

public class ShoppingCartPrinter {
    public static void main(String[] args) {
        ItemToPurchase itemInfo1 = new ItemToPurchase();
        ItemToPurchase itemInfo2 = new ItemToPurchase();

        Scanner scnr = new Scanner(System.in);

        System.out.println("Item 1");
        System.out.println("Enter the item name:");
        itemInfo1.setName(scnr.nextLine());

        System.out.println("Enter the item price:");
        itemInfo1.setPrice(scnr.nextInt());

        System.out.println("Enter the item quantity:");
        itemInfo1.setQuantity(scnr.nextInt());
        System.out.println();

        System.out.println("Item 2");
        itemInfo2.setName(scnr.nextLine());
        System.out.println("Enter the item name:");
        itemInfo2.setName(scnr.nextLine());

        System.out.println("Enter the item price:");
        itemInfo2.setPrice(scnr.nextInt());

        System.out.println("Enter the item quantity:");
        itemInfo2.setQuantity(scnr.nextInt());
        System.out.println();

        System.out.println("TOTAL COST");
        System.out.println(itemInfo1.getName() + " " + itemInfo1.getQuantity() + " @ $" + itemInfo1.getPrice() + " = $" + (itemInfo1.getPrice() * itemInfo1.getQuantity()));
        System.out.println(itemInfo2.getName() + " " + itemInfo2.getQuantity() + " @ $" + itemInfo2.getPrice() + " = $" + (itemInfo2.getPrice() * itemInfo2.getQuantity()));
        System.out.println("Total: $" + ((itemInfo1.getPrice() * itemInfo1.getQuantity()) + (itemInfo2.getPrice() * itemInfo2.getQuantity())));
    }
}
