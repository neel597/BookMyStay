// File: UseCase6RoomAllocationService.java

import java.util.*;

public class BookMyStayApp {

    // Queue to simulate booking requests (FIFO)
    private Queue<String> bookingQueue = new LinkedList<>();

    // Inventory: room type -> available count
    private Map<String, Integer> inventory = new HashMap<>();

    // Allocated rooms: room type -> set of room IDs
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    // Constructor initializes inventory
    public BookMyStayApp() {
        inventory.put("Deluxe", 3);
        inventory.put("Suite", 2);
        inventory.put("Standard", 4);

        allocatedRooms.put("Deluxe", new HashSet<>());
        allocatedRooms.put("Suite", new HashSet<>());
        allocatedRooms.put("Standard", new HashSet<>());
    }

    // Add booking request to queue
    public void addBookingRequest(String roomType) {
        bookingQueue.offer(roomType);
    }

    // Process booking requests
    public void processBookings() {
        while (!bookingQueue.isEmpty()) {
            String roomType = bookingQueue.poll();
            confirmReservation(roomType);
        }
    }

    // Confirm reservation and allocate room
    private void confirmReservation(String roomType) {
        if (inventory.getOrDefault(roomType, 0) > 0) {
            String roomId = generateUniqueRoomId(roomType);
            allocatedRooms.get(roomType).add(roomId);
            inventory.put(roomType, inventory.get(roomType) - 1);

            System.out.println("Reservation Confirmed: " + roomType + " Room ID = " + roomId);
        } else {
            System.out.println("Reservation Failed: No " + roomType + " rooms available.");
        }
    }

    // Generate unique room ID
    private String generateUniqueRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 6);
        } while (allocatedRooms.get(roomType).contains(roomId));
        return roomId;
    }

    // Display current inventory and allocations
    public void displayStatus() {
        System.out.println("\n--- Current Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
        }

        System.out.println("\n--- Allocated Rooms ---");
        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    // Main method
    public static void main(String[] args) {
        BookMyStayApp service = new BookMyStayApp();

        // Add booking requests
        service.addBookingRequest("Deluxe");
        service.addBookingRequest("Suite");
        service.addBookingRequest("Deluxe");
        service.addBookingRequest("Standard");
        service.addBookingRequest("Suite");
        service.addBookingRequest("Suite"); // should fail if inventory exhausted

        // Process requests
        service.processBookings();

        // Display final status
        service.displayStatus();
    }
}