package com.airline.model;

public class CargoAircraft extends Aircraft {

    private final double cargoCapacityKg;

    public CargoAircraft(String model, String registrationNumber,
            int rangeKm, double fuelConsumptionKgPerHour,
            double cargoCapacityKg) {
        super(model, registrationNumber, rangeKm, fuelConsumptionKgPerHour);
        requirePositive(cargoCapacityKg, "Cargo capacity");
        this.cargoCapacityKg = cargoCapacityKg;
    }

    @Override
    public AircraftType getType() {
        return AircraftType.CARGO;
    }

    @Override
    public int getPassengerCapacity() {
        return 0;
    }

    @Override
    public double getCargoCapacityKg() {
        return cargoCapacityKg;
    }
}