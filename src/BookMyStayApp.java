// File: UseCase7AddOnServiceSelection.java

import java.util.*;

class Service {
    private String name;
    private double cost;

    public Service(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return name + " (₹" + cost + ")";
    }
}

public class BookMyStayApp {

    // Reservation ID -> List of Services
    private Map<String, List<Service>> reservationServices = new HashMap<>();

    // Attach services to a reservation
    public void addServicesToReservation(String reservationId, List<Service> services) {
        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).addAll(services);
        System.out.println("Services added to Reservation " + reservationId + ": " + services);
    }

    // Calculate total additional cost for a reservation
    public double calculateAdditionalCost(String reservationId) {
        List<Service> services = reservationServices.getOrDefault(reservationId, Collections.emptyList());
        double total = 0;
        for (Service s : services) {
            total += s.getCost();
        }
        return total;
    }

    // Display all services for a reservation
    public void displayReservationServices(String reservationId) {
        List<Service> services = reservationServices.getOrDefault(reservationId, Collections.emptyList());
        System.out.println("Reservation " + reservationId + " has services: " + services);
        System.out.println("Total Additional Cost: ₹" + calculateAdditionalCost(reservationId));
    }

    // Main method
    public static void main(String[] args) {
        BookMyStayApp manager = new BookMyStayApp();

        // Example reservation IDs (from Use Case 6)
        String reservation1 = "DL-123ABC";
        String reservation2 = "ST-456XYZ";

        // Define services
        Service breakfast = new Service("Breakfast", 500);
        Service airportPickup = new Service("Airport Pickup", 1200);
        Service spa = new Service("Spa Access", 1500);

        // Add services to reservations
        manager.addServicesToReservation(reservation1, Arrays.asList(breakfast, spa));
        manager.addServicesToReservation(reservation2, Arrays.asList(airportPickup));

        // Display services and costs
        manager.displayReservationServices(reservation1);
        manager.displayReservationServices(reservation2);
    }
}