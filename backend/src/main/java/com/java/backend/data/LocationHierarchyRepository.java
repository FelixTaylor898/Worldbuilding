package com.java.backend.data;

import com.java.backend.model.LocationHierarchy;
import com.java.backend.model.LocationHierarchyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationHierarchyRepository extends JpaRepository<LocationHierarchy, LocationHierarchyId> {

    List<LocationHierarchy> findByParentLocationId(Long parentLocationId);
    List<LocationHierarchy> findByChildLocationId(Long parentLocationId);
}