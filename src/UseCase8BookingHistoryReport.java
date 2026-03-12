import java.util.*;

// Simple Reservation class to hold booking details
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

// Booking History stores confirmed reservations
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Added to history: " + reservation);
    }

    public List<Reservation> getHistory() {
        return history;
    }
}

// Reporting service generates summaries
class BookingReportService {
    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    // Display all reservations
    public void displayAllReservations() {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : bookingHistory.getHistory()) {
            System.out.println(r);
        }
    }

    // Generate summary by room type
    public void generateRoomTypeSummary() {
        Map<String, Integer> summary = new HashMap<>();
        for (Reservation r : bookingHistory.getHistory()) {
            summary.put(r.getRoomType(), summary.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("\n--- Room Type Summary ---");
        for (Map.Entry<String, Integer> entry : summary.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " bookings");
        }
    }
}

class BookMyStayApp {
    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService(history);

        // Simulate confirmed reservations
        Reservation r1 = new Reservation("DL-123ABC", "Deluxe", "Alice");
        Reservation r2 = new Reservation("ST-456XYZ", "Standard", "Bob");
        Reservation r3 = new Reservation("SU-789LMN", "Suite", "Charlie");
        Reservation r4 = new Reservation("DL-321PQR", "Deluxe", "David");

        // Add to history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);
        history.addReservation(r4);

        // Generate reports
        reportService.displayAllReservations();
        reportService.generateRoomTypeSummary();
    }
}