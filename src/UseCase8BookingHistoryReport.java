// File: UseCase9ErrorHandlingValidation.java

import java.util.*;

// Custom exception for invalid booking scenarios
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String reservationId;
    private String roomType;
    private String guestName;

    public Reservation(String reservationId, String roomType, String guestName) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.guestName = guestName;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                ", RoomType: " + roomType +
                ", Guest: " + guestName;
    }
}

// Booking service with validation
class BookMyStayApp {

    private Map<String, Integer> inventory = new HashMap<>();
    private Set<String> allocatedIds = new HashSet<>();

    public BookMyStayApp() {
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);
        inventory.put("Standard", 3);
    }

    // Validate input before processing
    private void validateBookingInput(String roomType, String guestName) throws InvalidBookingException {
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
        if (inventory.get(roomType) <= 0) {
            throw new InvalidBookingException("No " + roomType + " rooms available.");
        }
    }

    // Confirm reservation
    public Reservation confirmReservation(String roomType, String guestName) {
        try {
            validateBookingInput(roomType, guestName);

            String reservationId = generateUniqueReservationId(roomType);
            Reservation reservation = new Reservation(reservationId, roomType, guestName);

            // Update inventory atomically
            inventory.put(roomType, inventory.get(roomType) - 1);
            allocatedIds.add(reservationId);

            System.out.println("Booking Confirmed: " + reservation);
            return reservation;

        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
            return null;
        }
    }

    // Generate unique reservation ID
    private String generateUniqueReservationId(String roomType) {
        String id;
        do {
            id = roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 6);
        } while (allocatedIds.contains(id));
        return id;
    }

    // Display inventory status
    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
        }
    }

    // Main method
    public static void main(String[] args) {
        BookMyStayApp service = new BookMyStayApp();

        // Valid bookings
        service.confirmReservation("Deluxe", "Alice");
        service.confirmReservation("Suite", "Bob");

        // Invalid scenarios
        service.confirmReservation("Penthouse", "Charlie"); // invalid room type
        service.confirmReservation("Deluxe", "");           // empty guest name
        service.confirmReservation("Suite", "David");       // no Suite rooms left

        // Display final inventory
        service.displayInventory();
    }
}