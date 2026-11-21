package com.airline.io;

import com.airline.model.Aircraft;
import java.util.List;

public interface FleetRepository {

    List<Aircraft> loadAircraft();
}