package com.airline.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AirlineTest {

    private Airline airline;
    private Aircraft boeing;
    private Aircraft atr;
    private Aircraft antonov;

    @BeforeEach
    void setUp() {
        boeing = new PassengerAircraft("Boeing 737-800", "UR-PSA", 5436,
                2500, 189, 3500);
        atr = new PassengerAircraft("ATR 72-600", "UR-PSC", 1528,
                650, 70, 1200);
        antonov = new CargoAircraft("Antonov An-124", "UR-CGB", 4800,
                11000, 120000);
        airline = new Airline("Test Air");
        airline.addAircraft(boeing);
        airline.addAircraft(atr);
        airline.addAircraft(antonov);
    }

    @Test
    void storesTrimmedName() {
        assertEquals("Test Air", new Airline("  Test Air ").getName());
    }

    @Test
    void rejectsNullName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Airline(null));
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Airline("  "));
    }

    @Test
    void keepsAircraftInInsertionOrder() {
        assertEquals(List.of(boeing, atr, antonov), airline.getFleet());
        assertEquals(3, airline.getFleetSize());
    }

    @Test
    void rejectsNullAircraft() {
        assertThrows(IllegalArgumentException.class,
                () -> airline.addAircraft(null));
    }

    @Test
    void rejectsDuplicateAircraft() {
        Aircraft duplicate = new CargoAircraft("Other", "UR-PSA", 1000,
                100, 10);

        assertThrows(IllegalArgumentException.class,
                () -> airline.addAircraft(duplicate));
        assertEquals(3, airline.getFleetSize());
    }

    @Test
    void fleetViewCannotBeModified() {
        List<Aircraft> fleet = airline.getFleet();

        assertThrows(UnsupportedOperationException.class,
                () -> fleet.add(boeing));
    }

    @Test
    void sumsPassengerCapacity() {
        assertEquals(259, airline.getTotalPassengerCapacity());
    }

    @Test
    void sumsCargoCapacity() {
        assertEquals(124700.0, airline.getTotalCargoCapacityKg(), 0.001);
    }

    @Test
    void emptyAirlineHasZeroCapacity() {
        Airline empty = new Airline("Empty Air");

        assertEquals(0, empty.getTotalPassengerCapacity());
        assertEquals(0.0, empty.getTotalCargoCapacityKg(), 0.001);
    }

    @Test
    void sortsFleetByRangeAscending() {
        assertEquals(List.of(atr, antonov, boeing),
                airline.getFleetSortedByRange());
    }

    @Test
    void sortingDoesNotChangeOriginalOrder() {
        airline.getFleetSortedByRange();

        assertEquals(List.of(boeing, atr, antonov), airline.getFleet());
    }

    @Test
    void sortingEmptyFleetGivesEmptyList() {
        assertTrue(new Airline("Empty Air").getFleetSortedByRange()
                .isEmpty());
    }

    @Test
    void findsAircraftInsideFuelRange() {
        assertEquals(List.of(boeing),
                airline.findAircraftByFuelConsumption(2000, 3000));
    }

    @Test
    void fuelRangeBoundsAreInclusive() {
        assertEquals(List.of(boeing, atr),
                airline.findAircraftByFuelConsumption(650, 2500));
    }

    @Test
    void returnsEmptyListWhenNoAircraftMatches() {
        assertTrue(airline.findAircraftByFuelConsumption(3000, 5000)
                .isEmpty());
    }

    @Test
    void rejectsInvertedFuelRange() {
        assertThrows(IllegalArgumentException.class,
                () -> airline.findAircraftByFuelConsumption(3000, 2000));
    }
}