package catan.game.board;

import catan.game.enumeration.Resource;

import java.util.List;

public class Tile {
    private int id;
    private Resource resource;
    private int number;
    private List<Tile> neighbours;

    Tile(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Tile> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Tile> neighbours) {
        this.neighbours = neighbours;
    }

    public void addNeighbour(Tile neighbour) {
        this.neighbours.add(neighbour);
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "id=" + id +
                ", resource=" + resource +
                ", number=" + number +
                ", neighbours=" + neighbours +
                '}';
    }
}
