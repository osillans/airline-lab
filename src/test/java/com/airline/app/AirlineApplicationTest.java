package com.airline.app;

import static org.junit.jupiter.api.Assertions.*;

import com.airline.exception.DataLoadException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AirlineApplicationTest {

    @TempDir
    Path tempDir;

    private ByteArrayOutputStream buffer;
    private AirlineApplication application;

    @BeforeEach
    void setUp() {
        buffer = new ByteArrayOutputStream();
        application = new AirlineApplication(
                new PrintStream(buffer, true, StandardCharsets.UTF_8));
    }

    private Path writeConfig(Path fleetFile) throws IOException {
        Path config = tempDir.resolve("app.properties");
        Files.write(config, List.of(
                "airline.name=Test Air",
                "fleet.file=" + fleetFile.toString().replace('\\', '/'),
                "fuel.min=2000",
                "fuel.max=3000"));
        return config;
    }

    private Path writeFleet() throws IOException {
        Path fleet = tempDir.resolve("fleet.csv");
        Files.write(fleet, List.of(
                "type;model;registrationNumber;rangeKm;fuelKgPerHour;"
                        + "seats;baggageKg;cargoKg",
                "PASSENGER;Boeing 737-800;UR-PSA;5436;2500;189;3500;0",
                "CARGO;Antonov An-124;UR-CGB;4800;11000;0;0;120000"));
        return fleet;
    }

    @Test
    void printsReportForConfiguredFleet() throws IOException {
        Path config = writeConfig(writeFleet());

        application.run(config);

        String output = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Airline: Test Air"));
        assertTrue(output.contains("Fleet size: 2"));
        assertTrue(output.contains("Total passenger capacity: 189 seats"));
        assertTrue(output.contains("Total cargo capacity: 123500.0 kg"));
        assertFalse(output.substring(output.indexOf("Aircraft with fuel"))
                .contains("UR-CGB"));
    }

    @Test
    void failsWhenConfigFileIsMissing() {
        Path missing = tempDir.resolve("missing.properties");

        assertThrows(DataLoadException.class,
                () -> application.run(missing));
    }

    @Test
    void failsWhenFleetFileIsMissing() throws IOException {
        Path config = writeConfig(tempDir.resolve("missing.csv"));

        assertThrows(DataLoadException.class, () -> application.run(config));
    }
}