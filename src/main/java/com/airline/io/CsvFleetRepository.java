package com.airline.io;

import com.airline.exception.DataLoadException;
import com.airline.model.Aircraft;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvFleetRepository implements FleetRepository {

    private static final int HEADER_LINES = 1;

    private final Path file;
    private final AircraftParser parser = new AircraftParser();

    public CsvFleetRepository(Path file) {
        this.file = file;
    }

    @Override
    public List<Aircraft> loadAircraft() {
        List<String> lines = readLines();
        List<Aircraft> aircraft = new ArrayList<>();
        for (int i = HEADER_LINES; i < lines.size(); i++) {
            if (!lines.get(i).isBlank()) {
                aircraft.add(parseLine(lines.get(i), i + 1));
            }
        }
        return aircraft;
    }

    private List<String> readLines() {
        try {
            return Files.readAllLines(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new DataLoadException("Cannot read fleet file: " + file, e);
        }
    }

    private Aircraft parseLine(String line, int lineNumber) {
        try {
            return parser.parse(line);
        } catch (DataLoadException e) {
            throw new DataLoadException(
                    "Line " + lineNumber + ": " + e.getMessage(), e);
        }
    }
}