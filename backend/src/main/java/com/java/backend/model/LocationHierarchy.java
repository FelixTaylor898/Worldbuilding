package com.java.backend.model;

import jakarta.persistence.*;


@Entity
public class LocationHierarchy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "child_location_id", referencedColumnName = "locationId")
    private Location childLocation;

    @ManyToOne
    @JoinColumn(name = "parent_location_id", referencedColumnName = "locationId")
    private Location parentLocation;

    // Getters and Setters
    public Location getChildLocation() {
        return childLocation;
    }

    public void setChildLocation(Location childLocation) {
        this.childLocation = childLocation;
    }

    public Location getParentLocation() {
        return parentLocation;
    }

    public void setParentLocation(Location parentLocation) {
        this.parentLocation = parentLocation;
    }
}