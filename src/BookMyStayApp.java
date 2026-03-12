// File: UseCase11ConcurrentBookingSimulation.java

import java.util.*;

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

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                ", RoomType: " + roomType +
                ", Guest: " + guestName;
    }
}

// Concurrent Booking Processor
class BookingProcessor implements Runnable {
    private Queue<String> bookingQueue;
    private Map<String, Integer> inventory;
    private Set<String> allocatedIds;
    private String guestName;

    public BookingProcessor(Queue<String> bookingQueue, Map<String, Integer> inventory,
                            Set<String> allocatedIds, String guestName) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocatedIds = allocatedIds;
        this.guestName = guestName;
    }

    @Override
    public void run() {
        while (true) {
            String roomType;
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                roomType = bookingQueue.poll();
            }

            processBooking(roomType, guestName);
        }
    }

    private void processBooking(String roomType, String guestName) {
        synchronized (inventory) {
            if (!inventory.containsKey(roomType) || inventory.get(roomType) <= 0) {
                System.out.println("Booking Failed for " + guestName + ": No " + roomType + " rooms available.");
                return;
            }

            String reservationId = generateUniqueReservationId(roomType);
            allocatedIds.add(reservationId);
            inventory.put(roomType, inventory.get(roomType) - 1);

            Reservation reservation = new Reservation(reservationId, roomType, guestName);
            System.out.println("Booking Confirmed: " + reservation);
        }
    }

    private String generateUniqueReservationId(String roomType) {
        String id;
        do {
            id = roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 6);
        } while (allocatedIds.contains(id));
        return id;
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        Queue<String> bookingQueue = new LinkedList<>();
        Map<String, Integer> inventory = new HashMap<>();
        Set<String> allocatedIds = new HashSet<>();

        // Initialize inventory
        inventory.put("Deluxe", 3);
        inventory.put("Suite", 2);
        inventory.put("Standard", 4);

        // Add booking requests
        bookingQueue.offer("Deluxe");
        bookingQueue.offer("Suite");
        bookingQueue.offer("Standard");
        bookingQueue.offer("Deluxe");
        bookingQueue.offer("Suite");
        bookingQueue.offer("Standard");
        bookingQueue.offer("Deluxe");

        // Create multiple threads simulating different guests
        Thread guest1 = new Thread(new BookingProcessor(bookingQueue, inventory, allocatedIds, "Alice"));
        Thread guest2 = new Thread(new BookingProcessor(bookingQueue, inventory, allocatedIds, "Bob"));
        Thread guest3 = new Thread(new BookingProcessor(bookingQueue, inventory, allocatedIds, "Charlie"));

        // Start threads
        guest1.start();
        guest2.start();
        guest3.start();

        // Wait for threads to finish
        try {
            guest1.join();
            guest2.join();
            guest3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Display final inventory
        System.out.println("\n--- Final Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
        }
    }
}