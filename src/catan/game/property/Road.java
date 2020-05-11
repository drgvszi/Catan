package catan.game.property;

import java.util.Objects;

public class Road extends Property {
    private Intersection start;
    private Intersection end;

    public Road(Intersection start, Intersection end) {
        super();
        if (start.getId() < end.getId()) {
            this.start = start;
            this.end = end;
        }
        else {
            this.start = end;
            this.end = start;
        }
    }

    public Intersection getStart() {
        return start;
    }

    public void setStart(Intersection start) {
        this.start = start;
    }

    public Intersection getEnd() {
        return end;
    }

    public void setEnd(Intersection end) {
        this.end = end;
    }

    public Intersection getCommonIntersection(Intersection start, Intersection end) {
        if (this.start == start && this.end == end) {
            return null;
        }
        if (this.start.equals(start) || this.start.equals(end)) {
            return this.start;
        }
        if (this.end.equals(start) || this.end.equals(end)) {
            return this.end;
        }
        return null;
    }

    public boolean connectsToRoad(Intersection start, Intersection end) {
        return getCommonIntersection(start, end) != null;
    }

    public boolean connectsToRoad(Intersection intersection) {
        return start.equals(intersection) || end.equals(intersection);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Road)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        Road road = (Road) object;
        return Objects.equals(start, road.getStart()) &&
                Objects.equals(end, road.getEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), start, end);
    }

    @Override
    public String toString() {
        return "Road{" +
                "owner=" + owner +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
