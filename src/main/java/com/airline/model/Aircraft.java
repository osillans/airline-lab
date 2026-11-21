package com.airline.model;

import java.util.Locale;

public abstract class Aircraft {

    private final String model;
    private final String registrationNumber;
    private final int rangeKm;
    private final double fuelConsumptionKgPerHour;

    protected Aircraft(String model, String registrationNumber,
            int rangeKm, double fuelConsumptionKgPerHour) {
        requirePositive(rangeKm, "Range");
        requirePositive(fuelConsumptionKgPerHour, "Fuel consumption");
        this.model = requireText(model, "Model");
        this.registrationNumber =
                requireText(registrationNumber, "Registration number");
        this.rangeKm = rangeKm;
        this.fuelConsumptionKgPerHour = fuelConsumptionKgPerHour;
    }

    public String getModel() {
        return model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public int getRangeKm() {
        return rangeKm;
    }

    public double getFuelConsumptionKgPerHour() {
        return fuelConsumptionKgPerHour;
    }

    public abstract AircraftType getType();

    public abstract int getPassengerCapacity();

    public abstract double getCargoCapacityKg();

    protected static void requirePositive(double value, String fieldName) {
        if (!Double.isFinite(value) || value <= 0) {
            throw new IllegalArgumentException(
                    fieldName + " must be positive: " + value);
        }
    }

    protected static void requireNonNegative(double value, String fieldName) {
        if (!Double.isFinite(value) || value < 0) {
            throw new IllegalArgumentException(
                    fieldName + " must not be negative: " + value);
        }
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " must not be empty");
        }
        return value.trim();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Aircraft aircraft)) {
            return false;
        }
        return registrationNumber.equals(aircraft.registrationNumber);
    }

    @Override
    public int hashCode() {
        return registrationNumber.hashCode();
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "%s %s [%s] range=%d km, fuel=%.1f kg/h, seats=%d, "
                        + "cargo=%.1f kg",
                getType(), model, registrationNumber, rangeKm,
                fuelConsumptionKgPerHour, getPassengerCapacity(),
                getCargoCapacityKg());
    }
}