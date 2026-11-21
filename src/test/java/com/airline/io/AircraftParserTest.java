package com.airline.io;

import static org.junit.jupiter.api.Assertions.*;

import com.airline.exception.DataLoadException;
import com.airline.model.Aircraft;
import com.airline.model.AircraftType;
import org.junit.jupiter.api.Test;

class AircraftParserTest {

    private final AircraftParser parser = new AircraftParser();

    @Test
    void parsesPassengerAircraft() {
        Aircraft aircraft = parser.parse(
                "PASSENGER;Boeing 737-800;UR-PSA;5436;2500;189;3500;0");

        assertEquals(AircraftType.PASSENGER, aircraft.getType());
        assertEquals("Boeing 737-800", aircraft.getModel());
        assertEquals("UR-PSA", aircraft.getRegistrationNumber());
        assertEquals(5436, aircraft.getRangeKm());
        assertEquals(2500.0, aircraft.getFuelConsumptionKgPerHour(), 0.001);
        assertEquals(189, aircraft.getPassengerCapacity());
        assertEquals(3500.0, aircraft.getCargoCapacityKg(), 0.001);
    }

    @Test
    void parsesCargoAircraft() {
        Aircraft aircraft = parser.parse(
                "CARGO;Antonov An-124;UR-CGB;4800;11000;0;0;120000");

        assertEquals(AircraftType.CARGO, aircraft.getType());
        assertEquals(0, aircraft.getPassengerCapacity());
        assertEquals(120000.0, aircraft.getCargoCapacityKg(), 0.001);
    }

    @Test
    void parsesCombiAircraft() {
        Aircraft aircraft = parser.parse(
                "COMBI;Boeing 737-700C;UR-CMA;6000;2400;120;1800;12000");

        assertEquals(AircraftType.COMBI, aircraft.getType());
        assertEquals(120, aircraft.getPassengerCapacity());
        assertEquals(13800.0, aircraft.getCargoCapacityKg(), 0.001);
    }

    @Test
    void ignoresCaseOfTypeAndSpacesAroundValues() {
        Aircraft aircraft = parser.parse(
                " passenger ; ATR 72 ; UR-PSC ; 1528 ; 650 ; 70 ; 1200 ; 0");

        assertEquals(AircraftType.PASSENGER, aircraft.getType());
        assertEquals("ATR 72", aircraft.getModel());
        assertEquals(1528, aircraft.getRangeKm());
    }

    @Test
    void rejectsWrongNumberOfFields() {
        DataLoadException exception = assertThrows(DataLoadException.class,
                () -> parser.parse("PASSENGER;Boeing 737-800;UR-PSA"));

        assertTrue(exception.getMessage().contains("8 fields"));
    }

    @Test
    void rejectsUnknownType() {
        DataLoadException exception = assertThrows(DataLoadException.class,
                () -> parser.parse("HELICOPTER;Mi-8;UR-HEL;500;600;20;0;0"));

        assertTrue(exception.getMessage().contains("HELICOPTER"));
    }

    @Test
    void rejectsNonNumericValue() {
        assertThrows(DataLoadException.class, () -> parser.parse(
                "PASSENGER;Boeing 737-800;UR-PSA;far;2500;189;3500;0"));
    }

    @Test
    void rejectsInvalidAircraftData() {
        DataLoadException exception = assertThrows(DataLoadException.class,
                () -> parser.parse(
                        "PASSENGER;Boeing 737-800;UR-PSA;5436;2500;0;3500;0"));

        assertTrue(exception.getMessage().contains("Seats"));
    }
}