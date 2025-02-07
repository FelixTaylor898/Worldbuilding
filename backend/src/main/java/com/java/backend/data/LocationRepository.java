package com.java.backend.data;

import com.java.backend.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    // Find Location by User ID
    Optional<Location> findByUserId(Long userId);

    // Find Location by World ID
    Optional<Location> findByWorldId(Long worldId);

    // Find Location by Name
    Optional<Location> findByName(String name);

    List<Location> findByParentLocationId(Long parentLocationId);
}