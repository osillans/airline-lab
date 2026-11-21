package com.airline.io;

import com.airline.exception.DataLoadException;
import com.airline.model.Aircraft;
import com.airline.model.AircraftType;
import com.airline.model.CargoAircraft;
import com.airline.model.CombiAircraft;
import com.airline.model.PassengerAircraft;
import java.util.Locale;

public class AircraftParser {

    private static final String SEPARATOR = ";";
    private static final int FIELD_COUNT = 8;

    public Aircraft parse(String line) {
        String[] fields = line.split(SEPARATOR, -1);
        if (fields.length != FIELD_COUNT) {
            throw new DataLoadException("Expected " + FIELD_COUNT
                    + " fields but found " + fields.length + ": " + line);
        }
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }
        AircraftType type = parseType(fields[0]);
        try {
            return createAircraft(type, fields);
        } catch (IllegalArgumentException e) {
            throw new DataLoadException(
                    "Invalid aircraft data: " + e.getMessage(), e);
        }
    }

    private AircraftType parseType(String text) {
        try {
            return AircraftType.valueOf(text.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new DataLoadException("Unknown aircraft type: " + text, e);
        }
    }

    private Aircraft createAircraft(AircraftType type, String[] fields) {
        String model = fields[1];
        String registrationNumber = fields[2];
        int rangeKm = Integer.parseInt(fields[3]);
        double fuelKgPerHour = Double.parseDouble(fields[4]);
        int seats = Integer.parseInt(fields[5]);
        double baggageKg = Double.parseDouble(fields[6]);
        double cargoKg = Double.parseDouble(fields[7]);
        return switch (type) {
            case PASSENGER -> new PassengerAircraft(model,
                    registrationNumber, rangeKm, fuelKgPerHour, seats,
                    baggageKg);
            case CARGO -> new CargoAircraft(model, registrationNumber,
                    rangeKm, fuelKgPerHour, cargoKg);
            case COMBI -> new CombiAircraft(model, registrationNumber,
                    rangeKm, fuelKgPerHour, seats, baggageKg, cargoKg);
        };
    }
}