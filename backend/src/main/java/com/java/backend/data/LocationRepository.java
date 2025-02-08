package com.java.backend.data;

import com.java.backend.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    // Find Locations by World ID
    @Query("SELECT l FROM Location l WHERE l.world.worldId = :worldId")
    List<Location> findByWorldId(@Param("worldId") Long worldId);

    // Find Locations by Name
    List<Location> findByName(String name);
}
