package com.airline.model;

public class PassengerAircraft extends Aircraft {

    private final int seats;
    private final double baggageCapacityKg;

    public PassengerAircraft(String model, String registrationNumber,
            int rangeKm, double fuelConsumptionKgPerHour, int seats,
            double baggageCapacityKg) {
        super(model, registrationNumber, rangeKm, fuelConsumptionKgPerHour);
        requirePositive(seats, "Seats");
        requireNonNegative(baggageCapacityKg, "Baggage capacity");
        this.seats = seats;
        this.baggageCapacityKg = baggageCapacityKg;
    }

    @Override
    public AircraftType getType() {
        return AircraftType.PASSENGER;
    }

    @Override
    public int getPassengerCapacity() {
        return seats;
    }

    @Override
    public double getCargoCapacityKg() {
        return baggageCapacityKg;
    }
}