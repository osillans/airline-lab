package com.airline.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CargoAircraftTest {

    private static CargoAircraft create(double cargo) {
        return new CargoAircraft("Antonov An-124", "UR-CGB", 4800, 11000,
                cargo);
    }

    @Test
    void reportsCargoCapacity() {
        assertEquals(120000.0, create(120000).getCargoCapacityKg(), 0.001);
    }

    @Test
    void hasNoPassengerSeats() {
        assertEquals(0, create(120000).getPassengerCapacity());
    }

    @Test
    void hasCargoType() {
        assertEquals(AircraftType.CARGO, create(120000).getType());
    }

    @Test
    void rejectsZeroCargoCapacity() {
        assertThrows(IllegalArgumentException.class, () -> create(0));
    }
}