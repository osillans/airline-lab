package com.airline.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Airline {

    private final String name;
    private final List<Aircraft> fleet = new ArrayList<>();

    public Airline(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Airline name must not be empty");
        }
        this.name = name.trim();
    }

    public String getName() {
        return name;
    }

    public void addAircraft(Aircraft aircraft) {
        if (aircraft == null) {
            throw new IllegalArgumentException("Aircraft must not be null");
        }
        if (fleet.contains(aircraft)) {
            throw new IllegalArgumentException(
                    "Aircraft is already in the fleet: "
                            + aircraft.getRegistrationNumber());
        }
        fleet.add(aircraft);
    }

    public List<Aircraft> getFleet() {
        return Collections.unmodifiableList(fleet);
    }

    public int getFleetSize() {
        return fleet.size();
    }

    public int getTotalPassengerCapacity() {
        int total = 0;
        for (Aircraft aircraft : fleet) {
            total += aircraft.getPassengerCapacity();
        }
        return total;
    }

    public double getTotalCargoCapacityKg() {
        double total = 0;
        for (Aircraft aircraft : fleet) {
            total += aircraft.getCargoCapacityKg();
        }
        return total;
    }
}