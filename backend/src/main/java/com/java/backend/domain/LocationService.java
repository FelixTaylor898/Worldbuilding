package com.java.backend.domain;

import com.java.backend.data.LocationRepository;
import com.java.backend.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location attachChildLocationsToParent(Long parentLocationId) {
        Location parentLocation = locationRepository.findById(parentLocationId).orElse(null);
        if (parentLocation != null) {
            List<Location> childLocations = locationRepository.findByParentLocationId(parentLocationId);
            parentLocation.setChildLocations(childLocations);
        }
        return parentLocation;
    }
}
