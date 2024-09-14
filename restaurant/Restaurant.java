package restaurant;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private final List<MenuItem> menu = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();

    public Restaurant() {
        // Initialize menu with some items
        menu.add(new MenuItem(1, "Burger", 5.99));
        menu.add(new MenuItem(2, "Pizza", 8.99));
        menu.add(new MenuItem(3, "Pasta", 7.49));
    }

    public void viewMenu() {
        System.out.println("Menu:");
        for (MenuItem item : menu) {
            System.out.println(item);
        }
    }

    public Order createOrder() {
        Order order = new Order();
        orders.add(order);
        return order;
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public double calculateTotalWithGST(Order order) {
        double total = order.getItems().stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
        return total * 1.18; // Adding 18% GST
    }
}