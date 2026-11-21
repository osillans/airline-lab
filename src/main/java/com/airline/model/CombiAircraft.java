package com.airline.model;

public class CombiAircraft extends PassengerAircraft {

    private final double cargoDeckCapacityKg;

    public CombiAircraft(String model, String registrationNumber,
            int rangeKm, double fuelConsumptionKgPerHour, int seats,
            double baggageCapacityKg, double cargoDeckCapacityKg) {
        super(model, registrationNumber, rangeKm, fuelConsumptionKgPerHour,
                seats, baggageCapacityKg);
        requirePositive(cargoDeckCapacityKg, "Cargo deck capacity");
        this.cargoDeckCapacityKg = cargoDeckCapacityKg;
    }

    @Override
    public AircraftType getType() {
        return AircraftType.COMBI;
    }

    @Override
    public double getCargoCapacityKg() {
        return super.getCargoCapacityKg() + cargoDeckCapacityKg;
    }
}