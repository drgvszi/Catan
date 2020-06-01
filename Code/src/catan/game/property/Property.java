package catan.game.property;

import catan.game.player.Player;

import java.util.Objects;

public abstract class Property {
    protected Player owner;

    public Property() {
        owner = null;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean hasOwner() {
        return owner != null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Property)) {
            return false;
        }
        Property property = (Property) object;
        return Objects.equals(owner, property.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner);
    }

    @Override
    public String toString() {
        return "Property{" +
                "owner=" + owner +
                '}';
    }
}
