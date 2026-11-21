package com.airline.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CombiAircraftTest {

    private static CombiAircraft create(double cargoDeck) {
        return new CombiAircraft("Boeing 737-700C", "UR-CMA", 6000, 2400,
                120, 1800, cargoDeck);
    }

    @Test
    void sumsBaggageAndCargoDeckCapacity() {
        assertEquals(13800.0, create(12000).getCargoCapacityKg(), 0.001);
    }

    @Test
    void keepsPassengerSeats() {
        assertEquals(120, create(12000).getPassengerCapacity());
    }

    @Test
    void hasCombiType() {
        assertEquals(AircraftType.COMBI, create(12000).getType());
    }

    @Test
    void isPassengerAircraft() {
        assertInstanceOf(PassengerAircraft.class, create(12000));
    }

    @Test
    void rejectsZeroCargoDeckCapacity() {
        assertThrows(IllegalArgumentException.class, () -> create(0));
    }
}