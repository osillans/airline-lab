package com.airline.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AircraftTest {

    private static PassengerAircraft create(String model,
            String registration, int range, double fuel) {
        return new PassengerAircraft(model, registration, range, fuel,
                100, 1000);
    }

    @Test
    void storesCommonParameters() {
        Aircraft aircraft = create("Boeing 737-800", "UR-PSA", 5436, 2500);

        assertEquals("Boeing 737-800", aircraft.getModel());
        assertEquals("UR-PSA", aircraft.getRegistrationNumber());
        assertEquals(5436, aircraft.getRangeKm());
        assertEquals(2500.0, aircraft.getFuelConsumptionKgPerHour(), 0.001);
    }

    @Test
    void trimsModelAndRegistrationNumber() {
        Aircraft aircraft = create("  ATR 72  ", " UR-PSC ", 1528, 650);

        assertEquals("ATR 72", aircraft.getModel());
        assertEquals("UR-PSC", aircraft.getRegistrationNumber());
    }

    @Test
    void rejectsNullModel() {
        assertThrows(IllegalArgumentException.class,
                () -> create(null, "UR-PSA", 5436, 2500));
    }

    @Test
    void rejectsBlankModel() {
        assertThrows(IllegalArgumentException.class,
                () -> create("   ", "UR-PSA", 5436, 2500));
    }

    @Test
    void rejectsBlankRegistrationNumber() {
        assertThrows(IllegalArgumentException.class,
                () -> create("Boeing 737-800", "", 5436, 2500));
    }

    @Test
    void rejectsZeroRange() {
        assertThrows(IllegalArgumentException.class,
                () -> create("Boeing 737-800", "UR-PSA", 0, 2500));
    }

    @Test
    void rejectsNegativeRange() {
        assertThrows(IllegalArgumentException.class,
                () -> create("Boeing 737-800", "UR-PSA", -10, 2500));
    }

    @Test
    void rejectsZeroFuelConsumption() {
        assertThrows(IllegalArgumentException.class,
                () -> create("Boeing 737-800", "UR-PSA", 5436, 0));
    }

    @Test
    void rejectsNotANumberFuelConsumption() {
        assertThrows(IllegalArgumentException.class,
                () -> create("Boeing 737-800", "UR-PSA", 5436, Double.NaN));
    }

    @Test
    void equalsUsesRegistrationNumber() {
        Aircraft first = create("Boeing 737-800", "UR-PSA", 5436, 2500);
        Aircraft second = create("Other model", "UR-PSA", 1000, 100);

        assertTrue(first.equals(first));
        assertTrue(first.equals(second));
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void differentRegistrationNumbersAreNotEqual() {
        Aircraft first = create("Boeing 737-800", "UR-PSA", 5436, 2500);
        Aircraft second = create("Boeing 737-800", "UR-PSB", 5436, 2500);

        assertNotEquals(first, second);
    }

    @Test
    void isNotEqualToNullOrOtherClass() {
        Aircraft aircraft = create("Boeing 737-800", "UR-PSA", 5436, 2500);

        assertFalse(aircraft.equals(null));
        assertFalse(aircraft.equals("UR-PSA"));
    }

    @Test
    void toStringContainsMainParameters() {
        Aircraft aircraft = create("Boeing 737-800", "UR-PSA", 5436, 2500);

        String text = aircraft.toString();

        assertTrue(text.contains("PASSENGER"));
        assertTrue(text.contains("Boeing 737-800"));
        assertTrue(text.contains("[UR-PSA]"));
        assertTrue(text.contains("range=5436 km"));
        assertTrue(text.contains("fuel=2500.0 kg/h"));
        assertTrue(text.contains("seats=100"));
        assertTrue(text.contains("cargo=1000.0 kg"));
    }
}