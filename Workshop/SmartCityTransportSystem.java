import java.util.*;
import java.util.function.*;
import java.util.stream.*;

// ✅ Functional Interface
@FunctionalInterface
interface FareCalculator {
    double calculateFare(double distance, double baseFare);
}

// ✅ Marker Interface
interface EmergencyService {}

// ✅ TransportService Interface
interface TransportService {
    String getServiceName();
    double getFare();
    String getRoute();
    boolean isPeakTime();

    // ✅ Default Method
    default void printServiceDetails() {
        System.out.println("🚗 Service: " + getServiceName() + ", Route: " + getRoute() + ", Fare: ₹" + getFare());
    }

    // ✅ Static Method in Interface
    static double calculateDistance(String from, String to) {
        // Mock distance (real impl would use maps or geo coords)
        return new Random().nextInt(10) + 1;
    }
}

// ✅ Bus Service
class BusService implements TransportService {
    private String route;
    private double fare;
    private boolean peak;

    public BusService(String route, double fare, boolean peak) {
        this.route = route;
        this.fare = fare;
        this.peak = peak;
    }

    public String getServiceName() { return "Bus"; }
    public double getFare() { return fare; }
    public String getRoute() { return route; }
    public boolean isPeakTime() { return peak; }
}

// ✅ Metro Service
class MetroService implements TransportService {
    private String route;
    private double fare;
    private boolean peak;

    public MetroService(String route, double fare, boolean peak) {
        this.route = route;
        this.fare = fare;
        this.peak = peak;
    }

    public String getServiceName() { return "Metro"; }
    public double getFare() { return fare; }
    public String getRoute() { return route; }
    public boolean isPeakTime() { return peak; }
}

// ✅ Taxi Service (Emergency)
class TaxiService implements TransportService, EmergencyService {
    private String route;
    private double fare;
    private boolean peak;

    public TaxiService(String route, double fare, boolean peak) {
        this.route = route;
        this.fare = fare;
        this.peak = peak;
    }

    public String getServiceName() { return "Taxi (Emergency)"; }
    public double getFare() { return fare; }
    public String getRoute() { return route; }
    public boolean isPeakTime() { return peak; }
}

// ✅ Passenger class
class Passenger {
    String name;
    String routeTaken;
    double farePaid;
    boolean peakTime;

    public Passenger(String name, String routeTaken, double farePaid, boolean peakTime) {
        this.name = name;
        this.routeTaken = routeTaken;
        this.farePaid = farePaid;
        this.peakTime = peakTime;
    }

    public String getRouteTaken() { return routeTaken; }
    public double getFarePaid() { return farePaid; }
    public boolean isPeakTime() { return peakTime; }

    @Override
    public String toString() {
        return name + " | ₹" + farePaid + " | " + routeTaken + " | " + (peakTime ? "Peak" : "Off-Peak");
    }
}

// ✅ Main App Class
public class SmartCityTransportSystem {
    public static void main(String[] args) {
        // 🔧 Sample Transport Services
        List<TransportService> services = Arrays.asList(
                new BusService("Route A", 20, true),
                new MetroService("Route B", 15, false),
                new TaxiService("Route C", 100, true),
                new BusService("Route D", 25, false)
        );

        // ✅ Method Reference: Print all services
        System.out.println("🔄 Available Services:");
        services.forEach(TransportService::printServiceDetails);

        // ✅ Lambda: Sort by Fare, Filter Peak Time
        List<TransportService> filtered = services.stream()
                .filter(s -> s.isPeakTime())
                .sorted(Comparator.comparingDouble(TransportService::getFare))
                .collect(Collectors.toList());

        System.out.println("\n🔥 Peak Time Services (Sorted by Fare):");
        filtered.forEach(s -> System.out.println(s.getServiceName() + " - ₹" + s.getFare()));

        // ✅ Fare Calculator with Lambda
        FareCalculator calculator = (dist, baseFare) -> baseFare + (dist * 2.5);

        double distance = TransportService.calculateDistance("Point A", "Point B");
        double fare = calculator.calculateFare(distance, 10);
        System.out.println("\n🧮 Calculated Fare (Distance: " + distance + "km): ₹" + fare);

        // ✅ Passengers booking trips
        List<Passenger> passengers = Arrays.asList(
                new Passenger("Alice", "Route A", 20, true),
                new Passenger("Bob", "Route B", 15, false),
                new Passenger("Charlie", "Route C", 100, true),
                new Passenger("David", "Route A", 20, false),
                new Passenger("Eva", "Route C", 100, true)
        );

        // ✅ Stream + Collector: Group by Route
        System.out.println("\n📊 Grouped by Route:");
        Map<String, List<Passenger>> grouped = passengers.stream()
                .collect(Collectors.groupingBy(Passenger::getRouteTaken));
        grouped.forEach((route, list) -> {
            System.out.println("🔁 " + route + ": " + list.size() + " passengers");
        });

        // ✅ Partitioning by Peak Time
        System.out.println("\n⏰ Peak vs Off-Peak:");
        Map<Boolean, List<Passenger>> partitioned = passengers.stream()
                .collect(Collectors.partitioningBy(Passenger::isPeakTime));
        System.out.println("Peak Time: " + partitioned.get(true).size());
        System.out.println("Off-Peak Time: " + partitioned.get(false).size());

        // ✅ Revenue Summary
        DoubleSummaryStatistics stats = passengers.stream()
                .collect(Collectors.summarizingDouble(Passenger::getFarePaid));
        System.out.println("\n💰 Revenue Summary:");
        System.out.println("Total: ₹" + stats.getSum());
        System.out.println("Average Fare: ₹" + stats.getAverage());
        System.out.println("Highest Fare: ₹" + stats.getMax());

        // ✅ Identify Emergency Services
        System.out.println("\n🚨 Emergency Services:");
        services.stream()
                .filter(s -> s instanceof EmergencyService)
                .forEach(s -> System.out.println(s.getServiceName() + " on " + s.getRoute()));
    }
}
