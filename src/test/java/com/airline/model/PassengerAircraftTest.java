package com.airline.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PassengerAircraftTest {

    private static PassengerAircraft create(int seats, double baggage) {
        return new PassengerAircraft("Boeing 737-800", "UR-PSA", 5436, 2500,
                seats, baggage);
    }

    @Test
    void reportsSeatsAsPassengerCapacity() {
        PassengerAircraft aircraft = create(189, 3500);

        assertEquals(189, aircraft.getPassengerCapacity());
    }

    @Test
    void reportsBaggageCapacityAsCargoCapacity() {
        PassengerAircraft aircraft = create(189, 3500);

        assertEquals(3500.0, aircraft.getCargoCapacityKg(), 0.001);
    }

    @Test
    void hasPassengerType() {
        assertEquals(AircraftType.PASSENGER, create(189, 3500).getType());
    }

    @Test
    void acceptsZeroBaggageCapacity() {
        assertEquals(0.0, create(189, 0).getCargoCapacityKg(), 0.001);
    }

    @Test
    void rejectsZeroSeats() {
        assertThrows(IllegalArgumentException.class, () -> create(0, 3500));
    }

    @Test
    void rejectsNegativeBaggageCapacity() {
        assertThrows(IllegalArgumentException.class, () -> create(189, -1));
    }
}