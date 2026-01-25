package com.camlong.homnayangi.application.location;

import com.camlong.homnayangi.application.constants.District;
import com.camlong.homnayangi.domain.models.Location;

import java.util.List;
import java.util.Optional;

public interface LocationRepository {
    Optional<Location> findLocationById(Long id);

    List<Location> findAllLocationsByDistrictAndNameContains(District district, String name);

    Boolean saveLocation(Location location);

    Boolean saveAllLocations(List<Location> locations);
}
