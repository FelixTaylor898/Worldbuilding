package com.java.backend.model;

import java.io.Serializable;
import java.util.Objects;

public class LocationHierarchyId implements Serializable {

    private Long parentLocationId;
    private Long childLocationId;

    // Default constructor, getters, setters, hashCode, and equals methods

    public LocationHierarchyId() {
    }

    public LocationHierarchyId(Long parentLocationId, Long childLocationId) {
        this.parentLocationId = parentLocationId;
        this.childLocationId = childLocationId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationHierarchyId that = (LocationHierarchyId) o;
        return Objects.equals(parentLocationId, that.parentLocationId) &&
                Objects.equals(childLocationId, that.childLocationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentLocationId, childLocationId);
    }
}