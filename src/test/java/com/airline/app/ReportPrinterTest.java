package com.airline.app;

import static org.junit.jupiter.api.Assertions.*;

import com.airline.model.Airline;
import com.airline.model.CargoAircraft;
import com.airline.model.PassengerAircraft;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportPrinterTest {

    private ByteArrayOutputStream buffer;
    private ReportPrinter printer;
    private Airline airline;

    @BeforeEach
    void setUp() {
        buffer = new ByteArrayOutputStream();
        printer = new ReportPrinter(
                new PrintStream(buffer, true, StandardCharsets.UTF_8));
        airline = new Airline("Test Air");
        airline.addAircraft(new PassengerAircraft("Boeing 737-800",
                "UR-PSA", 5436, 2500, 189, 3500));
        airline.addAircraft(new PassengerAircraft("ATR 72-600",
                "UR-PSC", 1528, 650, 70, 1200));
        airline.addAircraft(new CargoAircraft("Antonov An-124",
                "UR-CGB", 4800, 11000, 120000));
    }

    private String output() {
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void printsAirlineSummary() {
        printer.print(airline, 2000, 3000);

        assertTrue(output().contains("Airline: Test Air"));
        assertTrue(output().contains("Fleet size: 3"));
        assertTrue(output().contains("Total passenger capacity: 259 seats"));
        assertTrue(output().contains("Total cargo capacity: 124700.0 kg"));
    }

    @Test
    void printsFleetSortedByRange() {
        printer.print(airline, 2000, 3000);

        int atr = output().indexOf("UR-PSC");
        int antonov = output().indexOf("UR-CGB");
        int boeing = output().indexOf("UR-PSA");
        assertTrue(atr < antonov);
        assertTrue(antonov < boeing);
    }

    @Test
    void printsOnlyAircraftInsideFuelRange() {
        printer.print(airline, 2000, 3000);

        String section = output().substring(
                output().indexOf("Aircraft with fuel consumption"));
        assertTrue(section.contains("from 2000.0 to 3000.0 kg/h"));
        assertTrue(section.contains("UR-PSA"));
        assertFalse(section.contains("UR-PSC"));
        assertFalse(section.contains("UR-CGB"));
    }

    @Test
    void printsMessageWhenNothingMatchesFuelRange() {
        printer.print(airline, 3000, 5000);

        assertTrue(output().contains("No matching aircraft"));
    }
}