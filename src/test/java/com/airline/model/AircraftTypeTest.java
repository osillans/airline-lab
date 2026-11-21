package com.airline.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AircraftTypeTest {

    @Test
    void definesThreeTypes() {
        assertEquals(3, AircraftType.values().length);
    }

    @Test
    void findsTypeByName() {
        assertEquals(AircraftType.PASSENGER,
                AircraftType.valueOf("PASSENGER"));
        assertEquals(AircraftType.CARGO, AircraftType.valueOf("CARGO"));
        assertEquals(AircraftType.COMBI, AircraftType.valueOf("COMBI"));
    }

    @Test
    void rejectsUnknownName() {
        assertThrows(IllegalArgumentException.class,
                () -> AircraftType.valueOf("HELICOPTER"));
    }
}