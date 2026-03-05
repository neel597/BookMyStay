import java.util.HashMap;
import java.util.Map;

// Abstract Room class
abstract class Room {
    private String roomType;
    private int numberOfBeds;
    private double pricePerNight;

    public Room(String roomType, int numberOfBeds, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Price per Night: $" + pricePerNight);
    }
}

// Concrete room classes
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 100.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 180.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 300.0);
    }
}

// Inventory class
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // Example: Suite fully booked
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

// Search Service (read-only access)
class RoomSearchService {
    private RoomInventory inventory;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void searchAvailableRooms(Room[] rooms) {
        System.out.println("=== Available Rooms ===");
        for (Room room : rooms) {
            int availability = inventory.getAvailability(room.getRoomType());
            if (availability > 0) {
                room.displayRoomDetails();
                System.out.println("Available: " + availability + "\n");
            }
        }
    }
}

// Application entry point
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System!");
        System.out.println("Application: Book My Stay");
        System.out.println("Version: 4.1\n");

        // Initialize inventory and rooms
        RoomInventory inventory = new RoomInventory();
        Room[] rooms = { new SingleRoom(), new DoubleRoom(), new SuiteRoom() };

        // Initialize search service
        RoomSearchService searchService = new RoomSearchService(inventory);

        // Perform search (read-only)
        searchService.searchAvailableRooms(rooms);

        // Program terminates after displaying available rooms
    }
}