package com.java.backend.domain;

import com.java.backend.data.LocationHierarchyRepository;
import com.java.backend.data.LocationRepository;
import com.java.backend.model.AppUser;
import com.java.backend.model.Location;
import com.java.backend.model.LocationHierarchy;
import com.java.backend.model.World;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Location addLocation(Location location) {
        location.setCreatedAt(LocalDateTime.now());
        return locationRepository.save(location);
    }

    public void deleteLocation(Long locationId) {
        locationRepository.deleteById(locationId);
    }

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Location findById(Long id) {
        Optional<Location> op = locationRepository.findById(id);
        return op.orElse(null);
    }

    public List<Location> findByWorld(World world) {
        return locationRepository.findByWorld(world);
    }

    public Location updateLocation(Long locationId, Location updatedLocation) {
        Optional<Location> op = locationRepository.findById(locationId);
        if (op.isPresent()) {
            Location existing = op.get();
            if (!updatedLocation.getDescription().isEmpty()) existing.setDescription(updatedLocation.getDescription());
            if (!updatedLocation.getName().isEmpty()) existing.setName(updatedLocation.getName());
            return locationRepository.save(existing);
        }
        return null;
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
