package com.airline.app;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MainTest {

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @TempDir
    Path tempDir;

    private ByteArrayOutputStream out;
    private ByteArrayOutputStream err;

    @BeforeEach
    void redirectStreams() {
        out = new ByteArrayOutputStream();
        err = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(err, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void printsReportForConfigGivenAsArgument() throws IOException {
        Path fleet = tempDir.resolve("fleet.csv");
        Files.write(fleet, List.of(
                "type;model;registrationNumber;rangeKm;fuelKgPerHour;"
                        + "seats;baggageKg;cargoKg",
                "PASSENGER;Boeing 737-800;UR-PSA;5436;2500;189;3500;0"));
        Path config = tempDir.resolve("app.properties");
        Files.write(config, List.of(
                "airline.name=Main Air",
                "fleet.file=" + fleet.toString().replace('\\', '/'),
                "fuel.min=1000",
                "fuel.max=3000"));

        Main.main(new String[] {config.toString()});

        assertTrue(out.toString(StandardCharsets.UTF_8)
                .contains("Airline: Main Air"));
        assertEquals("", err.toString(StandardCharsets.UTF_8));
    }

    @Test
    void printsErrorWhenConfigIsMissing() {
        String missing = tempDir.resolve("missing.properties").toString();

        Main.main(new String[] {missing});

        assertTrue(err.toString(StandardCharsets.UTF_8).startsWith("Error:"));
        assertEquals("", out.toString(StandardCharsets.UTF_8));
    }

    @Test
    void printsErrorForInvalidFuelRange() throws IOException {
        Path fleet = tempDir.resolve("fleet.csv");
        Files.write(fleet, List.of("header"));
        Path config = tempDir.resolve("app.properties");
        Files.write(config, List.of(
                "airline.name=Main Air",
                "fleet.file=" + fleet.toString().replace('\\', '/'),
                "fuel.min=3000",
                "fuel.max=1000"));

        Main.main(new String[] {config.toString()});

        assertTrue(err.toString(StandardCharsets.UTF_8).startsWith("Error:"));
    }

    @Test
    void hasPrivateConstructor() throws ReflectiveOperationException {
        Constructor<Main> constructor = Main.class.getDeclaredConstructor();

        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        assertInstanceOf(Main.class, constructor.newInstance());
    }

    @Test
    void usesDefaultConfigWhenNoArguments() {
        Main.main(new String[0]);

        assertTrue(out.toString(StandardCharsets.UTF_8).contains("Airline:"));
    }
}