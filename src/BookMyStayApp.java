import java.io.*;
import java.util.*;

// Reservation class (Serializable for persistence)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reservationId;
    private String roomType;
    private String guestName;

    public Reservation(String reservationId, String roomType, String guestName) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.guestName = guestName;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                ", RoomType: " + roomType +
                ", Guest: " + guestName;
    }
}

// Persistence Service
class PersistenceService {
    private static final String FILE_NAME = "bookingData.ser";

    // Save state to file
    public static void saveState(Map<String, Integer> inventory, List<Reservation> history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state from file
    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadState() {
        Map<String, Object> state = new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Map<String, Integer> inventory = (Map<String, Integer>) ois.readObject();
            List<Reservation> history = (List<Reservation>) ois.readObject();
            state.put("inventory", inventory);
            state.put("history", history);
            System.out.println("System state loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("No saved state found. Starting fresh.");
            state.put("inventory", new HashMap<String, Integer>());
            state.put("history", new ArrayList<Reservation>());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state: " + e.getMessage());
            state.put("inventory", new HashMap<String, Integer>());
            state.put("history", new ArrayList<Reservation>());
        }
        return state;
    }
}

public class BookMyStayApp {
    private Map<String, Integer> inventory;
    private List<Reservation> bookingHistory;

    public BookMyStayApp(Map<String, Integer> inventory, List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }

    // Confirm reservation
    public void confirmReservation(String roomType, String guestName) {
        if (!inventory.containsKey(roomType) || inventory.get(roomType) <= 0) {
            System.out.println("Booking Failed: No " + roomType + " rooms available.");
            return;
        }

        String reservationId = roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 6);
        Reservation reservation = new Reservation(reservationId, roomType, guestName);

        inventory.put(roomType, inventory.get(roomType) - 1);
        bookingHistory.add(reservation);

        System.out.println("Booking Confirmed: " + reservation);
    }

    // Display inventory
    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
        }
    }

    // Display booking history
    public void displayHistory() {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : bookingHistory) {
            System.out.println(r);
        }
    }

    // Main method
    public static void main(String[] args) {
        // Load state
        Map<String, Object> state = PersistenceService.loadState();
        Map<String, Integer> inventory = (Map<String, Integer>) state.get("inventory");
        List<Reservation> history = (List<Reservation>) state.get("history");

        // Initialize inventory if empty
        if (inventory.isEmpty()) {
            inventory.put("Deluxe", 2);
            inventory.put("Suite", 1);
            inventory.put("Standard", 3);
        }

        BookMyStayApp system = new BookMyStayApp(inventory, history);

        // Simulate bookings
        system.confirmReservation("Deluxe", "Alice");
        system.confirmReservation("Suite", "Bob");

        // Display state
        system.displayInventory();
        system.displayHistory();

        // Save state before shutdown
        PersistenceService.saveState(inventory, history);
    }
}
