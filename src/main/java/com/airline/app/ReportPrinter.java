package com.airline.app;

import com.airline.model.Aircraft;
import com.airline.model.Airline;
import java.io.PrintStream;
import java.util.List;
import java.util.Locale;

public class ReportPrinter {

    private final PrintStream out;

    public ReportPrinter(PrintStream out) {
        this.out = out;
    }

    public void print(Airline airline, double minFuelKgPerHour,
            double maxFuelKgPerHour) {
        out.println("Airline: " + airline.getName());
        out.println("Fleet size: " + airline.getFleetSize());
        out.println("Total passenger capacity: "
                + airline.getTotalPassengerCapacity() + " seats");
        out.printf(Locale.ROOT, "Total cargo capacity: %.1f kg%n",
                airline.getTotalCargoCapacityKg());
        out.println();
        out.println("Fleet sorted by range:");
        printAircraft(airline.getFleetSortedByRange());
        out.println();
        out.printf(Locale.ROOT,
                "Aircraft with fuel consumption from %.1f to %.1f kg/h:%n",
                minFuelKgPerHour, maxFuelKgPerHour);
        List<Aircraft> found = airline.findAircraftByFuelConsumption(
                minFuelKgPerHour, maxFuelKgPerHour);
        if (found.isEmpty()) {
            out.println("  No matching aircraft");
        } else {
            printAircraft(found);
        }
    }

    private void printAircraft(List<Aircraft> aircraft) {
        for (Aircraft item : aircraft) {
            out.println("  " + item);
        }
    }
}