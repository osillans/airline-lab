package com.airline.io;

import static org.junit.jupiter.api.Assertions.*;

import com.airline.model.Aircraft;
import com.airline.model.CargoAircraft;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FleetRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void canBeImplementedByAnyAircraftSource() {
        Aircraft aircraft = new CargoAircraft("Antonov An-124", "UR-CGB",
                4800, 11000, 120000);
        FleetRepository repository = () -> List.of(aircraft);

        assertEquals(List.of(aircraft), repository.loadAircraft());
    }

    @Test
    void csvRepositoryIsFleetRepository() throws IOException {
        Path file = tempDir.resolve("fleet.csv");
        Files.write(file, List.of("header",
                "CARGO;Antonov An-124;UR-CGB;4800;11000;0;0;120000"));

        FleetRepository repository = new CsvFleetRepository(file);

        assertEquals(1, repository.loadAircraft().size());
    }
}