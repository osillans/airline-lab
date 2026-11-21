package com.airline.app;

import com.airline.config.AppConfig;
import com.airline.io.CsvFleetRepository;
import com.airline.io.FleetRepository;
import com.airline.model.Airline;
import com.airline.service.AirlineService;
import java.io.PrintStream;
import java.nio.file.Path;

public class AirlineApplication {

    private final PrintStream out;

    public AirlineApplication(PrintStream out) {
        this.out = out;
    }

    public void run(Path configFile) {
        AppConfig config = AppConfig.load(configFile);
        FleetRepository repository =
                new CsvFleetRepository(config.getFleetFile());
        AirlineService service = new AirlineService(repository);
        Airline airline = service.createAirline(config.getAirlineName());
        new ReportPrinter(out).print(airline,
                config.getMinFuelKgPerHour(), config.getMaxFuelKgPerHour());
    }
}