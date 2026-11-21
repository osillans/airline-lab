package com.airline.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.airline.exception.DataLoadException;
import com.airline.io.FleetRepository;
import com.airline.model.Aircraft;
import com.airline.model.Airline;
import com.airline.model.CargoAircraft;
import com.airline.model.PassengerAircraft;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AirlineServiceTest {

    @Mock
    private FleetRepository repository;

    private AirlineService service;

    @BeforeEach
    void setUp() {
        service = new AirlineService(repository);
    }

    @Test
    void createsAirlineWithAircraftFromRepository() {
        Aircraft first = new PassengerAircraft("Boeing 737-800", "UR-PSA",
                5436, 2500, 189, 3500);
        Aircraft second = new CargoAircraft("Antonov An-124", "UR-CGB",
                4800, 11000, 120000);
        when(repository.loadAircraft()).thenReturn(List.of(first, second));

        Airline airline = service.createAirline("Test Air");

        assertEquals("Test Air", airline.getName());
        assertEquals(List.of(first, second), airline.getFleet());
        verify(repository).loadAircraft();
    }

    @Test
    void createsEmptyAirlineWhenRepositoryIsEmpty() {
        when(repository.loadAircraft()).thenReturn(List.of());

        Airline airline = service.createAirline("Test Air");

        assertEquals(0, airline.getFleetSize());
    }

    @Test
    void rejectsDuplicateAircraftFromRepository() {
        Aircraft first = new PassengerAircraft("Boeing 737-800", "UR-PSA",
                5436, 2500, 189, 3500);
        Aircraft second = new CargoAircraft("Other", "UR-PSA", 1000, 100, 10);
        when(repository.loadAircraft()).thenReturn(List.of(first, second));

        assertThrows(IllegalArgumentException.class,
                () -> service.createAirline("Test Air"));
    }

    @Test
    void propagatesRepositoryFailure() {
        when(repository.loadAircraft())
                .thenThrow(new DataLoadException("Cannot read fleet"));

        assertThrows(DataLoadException.class,
                () -> service.createAirline("Test Air"));
    }

    @Test
    void doesNotLoadAircraftForInvalidAirlineName() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createAirline(" "));

        verifyNoInteractions(repository);
    }
}