package com.airline.app;

import com.airline.exception.DataLoadException;
import java.nio.file.Path;

public class Main {

    private static final String DEFAULT_CONFIG = "config/app.properties";

    private Main() {
    }

    public static void main(String[] args) {
        Path configFile = Path.of(args.length > 0 ? args[0] : DEFAULT_CONFIG);
        AirlineApplication application = new AirlineApplication(System.out);
        try {
            application.run(configFile);
        } catch (DataLoadException | IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}