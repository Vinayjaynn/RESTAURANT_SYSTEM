package restaurant;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Restaurant Management System!");

        while (true) {
            System.out.println("Please choose an option:");
            System.out.println("1. Order");
            System.out.println("2. Get Bill");
            System.out.println("3. Exit");

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    handleOrder(restaurant, scanner);
                    break;
                case 2:
                    handleBill(restaurant);
                    break;
                case 3:
                    System.out.println("Thank you for visiting! Have a great day!");
                    scanner.close();
                    return; // Exit the program
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleOrder(Restaurant restaurant, Scanner scanner) {
        Order order = restaurant.createOrder();

        while (true) {
            restaurant.viewMenu();
            System.out.println("Place your order. Enter menu item ID and quantity:");

            int menuItemId = 0;
            int quantity = 0;

            try {
                System.out.print("Enter menu item ID: ");
                menuItemId = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                MenuItem item = findMenuItemById(restaurant.getMenu(), menuItemId);

                if (item == null) {
                    System.out.println("Menu item not found. Please try again.");
                    continue;
                }

                System.out.print("Enter quantity: ");
                quantity = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (quantity > 0) {
                    order.addItem(new OrderItem(item, quantity));
                    System.out.println("Added " + quantity + " x " + item.getName() + " to your order.");
                } else {
                    System.out.println("Quantity must be greater than 0. Please try again.");
                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter numbers for menu item ID and quantity.");
                scanner.next(); // Clear the invalid input
                continue;
            }

            System.out.println("Would you like to:");
            System.out.println("1. Place another Order");
            System.out.println("2. Get the Bill");
            System.out.println("3. Exit");

            int nextChoice = getUserChoice(scanner);

            switch (nextChoice) {
                case 1:
                    // Continue ordering
                    break;
                case 2:
                    handleBill(restaurant);
                    return; // Return to main menu after getting the bill
                case 3:
                    System.out.println("Thank you for visiting! Have a great day!");
                    System.exit(0); // Exit the program
                default:
                    System.out.println("Invalid choice. Returning to order menu.");
                    break;
            }
        }
    }

    private static void handleBill(Restaurant restaurant) {
        if (restaurant.getOrders().isEmpty()) {
            System.out.println("No orders have been placed.");
        } else {
            Order lastOrder = restaurant.getOrders().get(restaurant.getOrders().size() - 1);
            double totalWithoutGST = lastOrder.getItems().stream()
                    .mapToDouble(item -> item.getQuantity() * item.getItem().getPrice())
                    .sum();
            double gst = totalWithoutGST * 0.18; // 18% GST
            double totalWithGST = totalWithoutGST + gst;

            // Display detailed bill
            System.out.println("Detailed Bill:");
            System.out.printf("%-10s %-20s %-10s %-10s%n", "ID", "Item", "Quantity", "Subtotal");
            System.out.println("--------------------------------------");
            for (OrderItem orderItem : lastOrder.getItems()) {
                MenuItem item = orderItem.getItem();
                int quantity = orderItem.getQuantity();
                double itemTotal = quantity * item.getPrice();
                System.out.printf("%-10d %-20s %-10d $%.2f%n",
                        item.getId(),
                        item.getName(),
                        quantity,
                        itemTotal);
            }
            System.out.println("--------------------------------------");
            System.out.printf("%-30s $%.2f%n", "Total (excluding GST)", totalWithoutGST);
            System.out.printf("%-30s $%.2f%n", "GST (18%)", gst);
            System.out.printf("%-30s $%.2f%n", "Total Payable Amount", totalWithGST);
            System.out.println("Thank you for dining with us!");

            // Clear orders after billing
            restaurant.getOrders().clear();
        }
    }

    private static MenuItem findMenuItemById(List<MenuItem> menu, int id) {
        return menu.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private static int getUserChoice(Scanner scanner) {
        try {
            System.out.print("Enter your choice: ");
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // Clear the invalid input
            return -1; // Return an invalid choice
        }
    }

    // private static int getMenuItemId(Scanner scanner) {
    // System.out.print("Enter menu item ID: ");
    // return getUserChoice(scanner);
    // }

    // private static int getQuantity(Scanner scanner) {
    // System.out.print("Enter quantity: ");
    // return getUserChoice(scanner);
    // }
}