package catan.game.property;

import catan.game.enumeration.Building;

import java.util.Objects;

public class Intersection extends Property {
    private int id;
    private Building building;

    public Intersection(int id) {
        super();
        this.id = id;
        this.building = Building.None;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Intersection)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        Intersection intersection = (Intersection) object;
        return id == intersection.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public String toString() {
        return "Building{" +
                "owner=" + owner +
                ", id=" + id +
                ", buildingType=" + building +
                '}';
    }
}
