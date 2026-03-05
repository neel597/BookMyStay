/**
 * UseCase3InventorySetup.java
 *
 * This class demonstrates centralized room inventory management
 * using a HashMap for the Hotel Booking Management System.
 * It replaces scattered availability variables with a single source of truth.
 *
 * @author YourName
 * @version 3.1
 */

import java.util.HashMap;
import java.util.Map;

// Inventory class encapsulating room availability logic
class RoomInventory {
    private Map<String, Integer> inventory;

    // Constructor initializes inventory with room types and counts
    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Method to get availability of a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update availability (e.g., after booking or cancellation)
    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        } else {
            System.out.println("Room type not found in inventory.");
        }
    }

    // Method to display current inventory state
    public void displayInventory() {
        System.out.println("=== Current Room Inventory ===");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " - Available: " + entry.getValue());
        }
    }
}

// Application entry point
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System!");
        System.out.println("Application: Book My Stay");
        System.out.println("Version: 3.1\n");

        // Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory state
        inventory.displayInventory();

        // Example update: booking reduces availability
        System.out.println("\nBooking one Single Room...");
        int currentSingle = inventory.getAvailability("Single Room");
        inventory.updateAvailability("Single Room", currentSingle - 1);

        // Display updated inventory state
        inventory.displayInventory();

        // Program terminates after showing inventory updates
    }
}