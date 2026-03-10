import java.util.*;

public class ParkingLotSystem {

    // Parking spot status
    enum Status {
        EMPTY,
        OCCUPIED,
        DELETED
    }

    // Vehicle entry record
    static class ParkingSpot {
        String licensePlate;
        long entryTime;
        Status status;

        ParkingSpot() {
            status = Status.EMPTY;
        }
    }

    private ParkingSpot[] table;
    private int capacity;
    private int occupied = 0;
    private int totalProbes = 0;
    private int totalParks = 0;

    public ParkingLotSystem(int capacity) {
        this.capacity = capacity;
        table = new ParkingSpot[capacity];

        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

    // Park vehicle using linear probing
    public void parkVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (table[index].status == Status.OCCUPIED) {
            index = (index + 1) % capacity;
            probes++;
        }

        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = Status.OCCUPIED;

        occupied++;
        totalProbes += probes;
        totalParks++;

        System.out.println("parkVehicle(\"" + licensePlate + "\") → Assigned spot #" +
                index + " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String licensePlate) {

        int index = hash(licensePlate);

        int probes = 0;

        while (table[index].status != Status.EMPTY) {

            if (table[index].status == Status.OCCUPIED &&
                    table[index].licensePlate.equals(licensePlate)) {

                long durationMs = System.currentTimeMillis() - table[index].entryTime;

                double hours = durationMs / (1000.0 * 60 * 60);

                double fee = hours * 5; // $5 per hour

                table[index].status = Status.DELETED;

                occupied--;

                System.out.println("exitVehicle(\"" + licensePlate + "\") → Spot #" +
                        index + " freed, Duration: " +
                        String.format("%.2f", hours) +
                        "h, Fee: $" + String.format("%.2f", fee));

                return;
            }

            index = (index + 1) % capacity;
            probes++;

            if (probes >= capacity) break;
        }

        System.out.println("Vehicle not found.");
    }

    // Find nearest spot to entrance
    public void findNearestAvailableSpot() {

        for (int i = 0; i < capacity; i++) {
            if (table[i].status != Status.OCCUPIED) {
                System.out.println("Nearest available spot → #" + i);
                return;
            }
        }

        System.out.println("Parking lot full.");
    }

    // Generate statistics
    public void getStatistics() {

        double occupancyRate = (occupied * 100.0) / capacity;

        double avgProbes = totalParks == 0 ? 0 : (double) totalProbes / totalParks;

        System.out.println("\nParking Statistics:");
        System.out.println("Occupancy: " + String.format("%.1f", occupancyRate) + "%");
        System.out.println("Avg Probes: " + String.format("%.2f", avgProbes));
        System.out.println("Total Vehicles Parked: " + totalParks);
    }

    public static void main(String[] args) throws Exception {

        ParkingLotSystem parking = new ParkingLotSystem(500);

        parking.parkVehicle("ABC-1234");
        parking.parkVehicle("ABC-1235");
        parking.parkVehicle("XYZ-9999");

        parking.findNearestAvailableSpot();

        Thread.sleep(2000);

        parking.exitVehicle("ABC-1234");

        parking.getStatistics();
    }
}
