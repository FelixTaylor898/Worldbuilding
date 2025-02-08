package com.java.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "location_hierarchy")
@IdClass(LocationHierarchyId.class) // Composite key
public class LocationHierarchy {

    // Do not annotate these fields with @Id
    @ManyToOne
    @JoinColumn(name = "parent_location_id", insertable = false, updatable = false)
    private Location parentLocation;

    @ManyToOne
    @JoinColumn(name = "child_location_id", insertable = false, updatable = false)
    private Location childLocation;

    // Use @Id for the fields matching the composite key
    @Id
    @Column(name = "parent_location_id", insertable = false, updatable = false)
    private Long parentLocationId;

    @Id
    @Column(name = "child_location_id", insertable = false, updatable = false)
    private Long childLocationId;

    // Getters and Setters
    public Location getParentLocation() {
        return parentLocation;
    }

    public void setParentLocation(Location parentLocation) {
        this.parentLocation = parentLocation;
    }

    public Location getChildLocation() {
        return childLocation;
    }

    public void setChildLocation(Location childLocation) {
        this.childLocation = childLocation;
    }

    public Long getParentLocationId() {
        return parentLocationId;
    }

    public void setParentLocationId(Long parentLocationId) {
        this.parentLocationId = parentLocationId;
    }

    public Long getChildLocationId() {
        return childLocationId;
    }

    public void setChildLocationId(Long childLocationId) {
        this.childLocationId = childLocationId;
    }
}