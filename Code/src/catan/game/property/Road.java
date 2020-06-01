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
        } else {
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

    public int getCommonIntersection(int startId, int endId) {
        if (start.getId() == startId && end.getId() == endId) {
            return -1;
        }
        if (start.getId() == startId || start.getId() == endId) {
            return startId;
        }
        if (end.getId() == startId || end.getId() == endId) {
            return endId;
        }
        return -1;
    }

    public boolean connectsToRoad(int start, int end) {

        return getCommonIntersection(start, end) != -1;
    }

    public boolean connectsToRoad(int intersection) {
        return start.getId() == intersection || end.getId() == intersection;
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
