package com.java.backend.domain;

import com.java.backend.data.LocationHierarchyRepository;
import com.java.backend.data.LocationRepository;
import com.java.backend.model.Location;
import com.java.backend.model.LocationHierarchy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {

    @Autowired
    private final LocationRepository locationRepository;

    @Autowired
    private final LocationHierarchyRepository locationHierarchyRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository, LocationHierarchyRepository locationHierarchyRepository) {
        this.locationRepository = locationRepository;
        this.locationHierarchyRepository = locationHierarchyRepository;
    }

    public Location attachChildLocationsToParent(Long parentLocationId) {
        Location parentLocation = locationRepository.findById(parentLocationId).orElse(null);
        if (parentLocation != null) {
            List<LocationHierarchy> childLocations = locationHierarchyRepository.findByParentLocationId(parentLocationId);
            List<Location> childLocationList = new ArrayList<>();
            for (LocationHierarchy child : childLocations) {
                childLocationList.add(child.getChildLocation());
            }
            parentLocation.setChildLocations(childLocationList);
        }
        return parentLocation;
    }
}
