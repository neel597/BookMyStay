// File: UseCase10BookingCancellation.java

import java.util.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String roomType;
    private String guestName;
    private boolean cancelled;

    public Reservation(String reservationId, String roomType, String guestName) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.guestName = guestName;
        this.cancelled = false;
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

    public boolean isCancelled() {
        return cancelled;
    }

    public void markCancelled() {
        this.cancelled = true;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                ", RoomType: " + roomType +
                ", Guest: " + guestName +
                ", Status: " + (cancelled ? "Cancelled" : "Active");
    }
}

// Booking Cancellation Service
public class BookMyStayApp {

    private Map<String, Integer> inventory = new HashMap<>();
    private Map<String, Reservation> reservations = new HashMap<>();
    private Stack<String> rollbackStack = new Stack<>();

    public BookMyStayApp() {
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);
        inventory.put("Standard", 3);
    }

    // Confirm reservation
    public Reservation confirmReservation(String roomType, String guestName) {
        if (!inventory.containsKey(roomType) || inventory.get(roomType) <= 0) {
            System.out.println("Booking Failed: No " + roomType + " rooms available.");
            return null;
        }

        String reservationId = generateUniqueReservationId(roomType);
        Reservation reservation = new Reservation(reservationId, roomType, guestName);

        inventory.put(roomType, inventory.get(roomType) - 1);
        reservations.put(reservationId, reservation);

        System.out.println("Booking Confirmed: " + reservation);
        return reservation;
    }

    // Cancel reservation
    public void cancelReservation(String reservationId) {
        if (!reservations.containsKey(reservationId)) {
            System.out.println("Cancellation Failed: Reservation " + reservationId + " does not exist.");
            return;
        }

        Reservation reservation = reservations.get(reservationId);
        if (reservation.isCancelled()) {
            System.out.println("Cancellation Failed: Reservation " + reservationId + " is already cancelled.");
            return;
        }

        // Rollback logic
        rollbackStack.push(reservationId);
        reservation.markCancelled();
        inventory.put(reservation.getRoomType(), inventory.get(reservation.getRoomType()) + 1);

        System.out.println("Cancellation Successful: " + reservation);
        System.out.println("Rolled back room ID: " + rollbackStack.peek());
    }

    // Generate unique reservation ID
    private String generateUniqueReservationId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 6);
    }

    // Display inventory
    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
        }
    }

    // Display all reservations
    public void displayReservations() {
        System.out.println("\n--- Reservations ---");
        for (Reservation r : reservations.values()) {
            System.out.println(r);
        }
    }

    // Main method
    public static void main(String[] args) {
        BookMyStayApp service = new BookMyStayApp();

        // Confirm bookings
        Reservation r1 = service.confirmReservation("Deluxe", "Alice");
        Reservation r2 = service.confirmReservation("Suite", "Bob");

        // Attempt cancellations
        if (r1 != null) service.cancelReservation(r1.getReservationId());
        if (r2 != null) service.cancelReservation(r2.getReservationId());

        // Invalid cancellation
        service.cancelReservation("INVALID-ID");

        // Double cancellation attempt
        if (r1 != null) service.cancelReservation(r1.getReservationId());

        // Display final state
        service.displayInventory();
        service.displayReservations();
    }
}