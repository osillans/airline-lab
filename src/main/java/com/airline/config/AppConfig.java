package com.airline.config;

import com.airline.exception.DataLoadException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class AppConfig {

    private static final String AIRLINE_NAME_KEY = "airline.name";
    private static final String FLEET_FILE_KEY = "fleet.file";
    private static final String FUEL_MIN_KEY = "fuel.min";
    private static final String FUEL_MAX_KEY = "fuel.max";

    private final String airlineName;
    private final Path fleetFile;
    private final double minFuelKgPerHour;
    private final double maxFuelKgPerHour;

    private AppConfig(String airlineName, Path fleetFile,
            double minFuelKgPerHour, double maxFuelKgPerHour) {
        this.airlineName = airlineName;
        this.fleetFile = fleetFile;
        this.minFuelKgPerHour = minFuelKgPerHour;
        this.maxFuelKgPerHour = maxFuelKgPerHour;
    }

    public static AppConfig load(Path configFile) {
        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(configFile)) {
            properties.load(reader);
        } catch (IOException e) {
            throw new DataLoadException(
                    "Cannot read config file: " + configFile, e);
        }
        String name = requireProperty(properties, AIRLINE_NAME_KEY);
        Path fleetFile = Path.of(requireProperty(properties, FLEET_FILE_KEY));
        double minFuel = requireNumber(properties, FUEL_MIN_KEY);
        double maxFuel = requireNumber(properties, FUEL_MAX_KEY);
        if (minFuel > maxFuel) {
            throw new DataLoadException(
                    FUEL_MIN_KEY + " must not exceed " + FUEL_MAX_KEY);
        }
        return new AppConfig(name, fleetFile, minFuel, maxFuel);
    }

    public String getAirlineName() {
        return airlineName;
    }

    public Path getFleetFile() {
        return fleetFile;
    }

    public double getMinFuelKgPerHour() {
        return minFuelKgPerHour;
    }

    public double getMaxFuelKgPerHour() {
        return maxFuelKgPerHour;
    }

    private static String requireProperty(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new DataLoadException("Missing property: " + key);
        }
        return value.trim();
    }

    private static double requireNumber(Properties properties, String key) {
        String value = requireProperty(properties, key);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new DataLoadException(
                    "Property " + key + " must be a number: " + value, e);
        }
    }
}