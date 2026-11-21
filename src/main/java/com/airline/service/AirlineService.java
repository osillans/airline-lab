package com.airline.service;

import com.airline.io.FleetRepository;
import com.airline.model.Aircraft;
import com.airline.model.Airline;

public class AirlineService {

    private final FleetRepository repository;

    public AirlineService(FleetRepository repository) {
        this.repository = repository;
    }

    public Airline createAirline(String name) {
        Airline airline = new Airline(name);
        for (Aircraft aircraft : repository.loadAircraft()) {
            airline.addAircraft(aircraft);
        }
        return airline;
    }
}