# Airline Lab

Console application that models an airline fleet (variant 9).

## Features

- Aircraft hierarchy: passenger, cargo and combi aircraft.
- Total passenger capacity and total cargo capacity of the fleet.
- Fleet sorting by flight range.
- Search for aircraft by a range of fuel consumption.

## Requirements

- JDK 21
- Maven 3.9 or newer

## Project structure

```
com.airline.model      Aircraft, PassengerAircraft, CargoAircraft,
                       CombiAircraft, AircraftType, Airline
com.airline.io         FleetRepository, CsvFleetRepository, AircraftParser
com.airline.config     AppConfig
com.airline.service    AirlineService
com.airline.exception  DataLoadException
com.airline.app        Main, AirlineApplication, ReportPrinter
```

## Configuration

Settings are stored in `config/app.properties`:

```
airline.name=SkyBridge Airlines
fleet.file=data/fleet.csv
fuel.min=2000
fuel.max=3000
```

`fuel.min` and `fuel.max` define the fuel consumption range (kg/h) used by
the aircraft search.

## Fleet file format

`data/fleet.csv` is a semicolon-separated file with a header line:

```
type;model;registrationNumber;rangeKm;fuelKgPerHour;seats;baggageKg;cargoKg
```

- `PASSENGER` uses `seats` and `baggageKg`.
- `CARGO` uses `cargoKg`.
- `COMBI` uses `seats`, `baggageKg` and `cargoKg`.

Unused numeric columns must still be present (use `0`).

## Build, test and run

```
mvn test
mvn compile
java -cp target/classes com.airline.app.Main
```

A path to another configuration file can be passed as the first argument.

## Test coverage

`mvn test` also generates a JaCoCo report in `target/site/jacoco/index.html`.