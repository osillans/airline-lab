package com.airline.config;

import static org.junit.jupiter.api.Assertions.*;

import com.airline.exception.DataLoadException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AppConfigTest {

    @TempDir
    Path tempDir;

    private Path writeConfig(String... lines) throws IOException {
        Path file = tempDir.resolve("app.properties");
        Files.write(file, List.of(lines));
        return file;
    }

    @Test
    void loadsAllValues() throws IOException {
        Path file = writeConfig(
                "airline.name=Test Air",
                "fleet.file=data/fleet.csv",
                "fuel.min=1500",
                "fuel.max=3000.5");

        AppConfig config = AppConfig.load(file);

        assertEquals("Test Air", config.getAirlineName());
        assertEquals(Path.of("data/fleet.csv"), config.getFleetFile());
        assertEquals(1500.0, config.getMinFuelKgPerHour(), 0.001);
        assertEquals(3000.5, config.getMaxFuelKgPerHour(), 0.001);
    }

    @Test
    void failsWhenFileDoesNotExist() {
        Path missing = tempDir.resolve("missing.properties");

        DataLoadException exception = assertThrows(DataLoadException.class,
                () -> AppConfig.load(missing));

        assertInstanceOf(IOException.class, exception.getCause());
    }

    @Test
    void failsWhenPropertyIsMissing() throws IOException {
        Path file = writeConfig(
                "airline.name=Test Air",
                "fleet.file=data/fleet.csv",
                "fuel.min=1500");

        DataLoadException exception = assertThrows(DataLoadException.class,
                () -> AppConfig.load(file));

        assertTrue(exception.getMessage().contains("fuel.max"));
    }

    @Test
    void failsWhenPropertyIsBlank() throws IOException {
        Path file = writeConfig(
                "airline.name=   ",
                "fleet.file=data/fleet.csv",
                "fuel.min=1500",
                "fuel.max=3000");

        DataLoadException exception = assertThrows(DataLoadException.class,
                () -> AppConfig.load(file));

        assertTrue(exception.getMessage().contains("airline.name"));
    }

    @Test
    void failsWhenMinimumFuelExceedsMaximum() throws IOException {
        Path file = writeConfig(
                "airline.name=Test Air",
                "fleet.file=data/fleet.csv",
                "fuel.min=3000",
                "fuel.max=1000");

        DataLoadException exception = assertThrows(DataLoadException.class,
                () -> AppConfig.load(file));

        assertTrue(exception.getMessage().contains("must not exceed"));
    }

    @Test
    void failsWhenFuelValueIsNotANumber() throws IOException {
        Path file = writeConfig(
                "airline.name=Test Air",
                "fleet.file=data/fleet.csv",
                "fuel.min=a lot",
                "fuel.max=3000");

        DataLoadException exception = assertThrows(DataLoadException.class,
                () -> AppConfig.load(file));

        assertTrue(exception.getMessage().contains("fuel.min"));
    }
}