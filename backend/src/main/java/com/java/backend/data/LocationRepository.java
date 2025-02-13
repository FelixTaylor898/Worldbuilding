package com.java.backend.data;

import com.java.backend.model.Location;
import com.java.backend.model.World;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    // Find Locations by World ID
    List<Location> findByWorld(World world);

    // Find Locations by Name
    List<Location> findByName(String name);
}
