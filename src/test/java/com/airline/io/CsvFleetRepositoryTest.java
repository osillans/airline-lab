package com.airline.io;

import static org.junit.jupiter.api.Assertions.*;

import com.airline.exception.DataLoadException;
import com.airline.model.Aircraft;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CsvFleetRepositoryTest {

    private static final String HEADER =
            "type;model;registrationNumber;rangeKm;fuelKgPerHour;"
                    + "seats;baggageKg;cargoKg";

    @TempDir
    Path tempDir;

    private Path writeFile(String... lines) throws IOException {
        Path file = tempDir.resolve("fleet.csv");
        Files.write(file, List.of(lines));
        return file;
    }

    @Test
    void loadsAircraftSkippingHeaderAndBlankLines() throws IOException {
        Path file = writeFile(HEADER,
                "PASSENGER;Boeing 737-800;UR-PSA;5436;2500;189;3500;0",
                "",
                "CARGO;Antonov An-124;UR-CGB;4800;11000;0;0;120000");

        List<Aircraft> aircraft = new CsvFleetRepository(file).loadAircraft();

        assertEquals(2, aircraft.size());
        assertEquals("UR-PSA", aircraft.get(0).getRegistrationNumber());
        assertEquals("UR-CGB", aircraft.get(1).getRegistrationNumber());
    }

    @Test
    void emptyFileGivesEmptyList() throws IOException {
        Path file = writeFile();

        assertTrue(new CsvFleetRepository(file).loadAircraft().isEmpty());
    }

    @Test
    void reportsLineNumberOfInvalidLine() throws IOException {
        Path file = writeFile(HEADER,
                "PASSENGER;Boeing 737-800;UR-PSA;5436;2500;189;3500;0",
                "PASSENGER;broken line");
        CsvFleetRepository repository = new CsvFleetRepository(file);

        DataLoadException exception = assertThrows(DataLoadException.class,
                repository::loadAircraft);

        assertTrue(exception.getMessage().startsWith("Line 3:"));
    }

    @Test
    void failsWhenFileDoesNotExist() {
        CsvFleetRepository repository =
                new CsvFleetRepository(tempDir.resolve("missing.csv"));

        DataLoadException exception = assertThrows(DataLoadException.class,
                repository::loadAircraft);

        assertInstanceOf(IOException.class, exception.getCause());
    }
}