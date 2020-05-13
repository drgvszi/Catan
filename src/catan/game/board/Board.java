package catan.game.board;

import catan.game.enumeration.Port;
import catan.game.enumeration.Resource;
import catan.game.property.Intersection;
import catan.game.property.Road;
import catan.game.rule.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Board {
    private List<Tile> tiles;
    private List<List<Tile>> numberedTiles;
    private List<Intersection> intersections;
    private List<Port> ports;
    private List<Road> roads;
    private TileGraph tileGraph;
    private IntersectionGraph intersectionGraph;
    private List<List<Integer>> adjacentIntersectionsToTiles;
    private List<List<Integer>> adjacentTilesToIntersections;
    private Tile robberPosition;

    public Board() {
        tiles = new ArrayList<>();
        for (int i = 0; i < Component.TILES; i++) {
            tiles.add(new Tile(i));
        }
        numberedTiles = new ArrayList<>();
        intersections = new ArrayList<>();
        ports = new ArrayList<>();
        for (int index = 0; index < Component.INTERSECTIONS; ++index) {
            intersections.add(new Intersection(index));
            ports.add(Port.None);
        }
        roads = new ArrayList<>();
        tileGraph = new TileGraph();
        intersectionGraph = new IntersectionGraph();
        adjacentIntersectionsToTiles = new ArrayList<>();
        adjacentTilesToIntersections = new ArrayList<>();
        robberPosition = null;
        generateRandomTiles();
        createMapping();
        generatePorts();
        // printAdjacentIntersectionsToTiles();
        // printAdjacentTilesToIntersections();
    }

    //region Getters

    public List<Tile> getTiles() {
        return tiles;
    }

    public Tile getTile(int tile) {
        return tiles.get(tile);
    }

    public List<List<Tile>> getNumberedTiles() {
        return numberedTiles;
    }

    public List<Tile> getTilesFromNumber(int i) {
        return numberedTiles.get(i);
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public Intersection getIntersection(int intersection) {
        return intersections.get(intersection);
    }

    public List<Port> getPorts() {
        return ports;
    }

    public Port getPort(int port) {
        return ports.get(port);
    }

    public List<Road> getRoads() {
        return roads;
    }

    public TileGraph getTileGraph() {
        return tileGraph;
    }

    public List<Integer> getAdjacentTiles(int tile) {
        return tileGraph.getAdjacentTiles(tile);
    }

    public IntersectionGraph getIntersectionGraph() {
        return intersectionGraph;
    }

    public List<Intersection> getAdjacentIntersections(Intersection intersection) {
        List<Intersection> adjacentIntersections = new ArrayList<>();
        for (Intersection intersection1 :
                intersections) {
            if (intersectionGraph.getAdjacentIntersections(intersection.getId()).contains(intersection1.getId()))
                adjacentIntersections.add(intersection1);
        }
        return adjacentIntersections;
    }

    public List<List<Integer>> getAdjacentIntersectionsToTiles() {
        return adjacentIntersectionsToTiles;
    }

    public List<Integer> getAdjacentIntersections(int tile) {
        return adjacentIntersectionsToTiles.get(tile);
    }

    public List<Intersection> getAdjacentIntersections(Tile tile) {
        List<Intersection> adjacentIntersections = new ArrayList<>();
        List<Integer> intersectionsId = adjacentIntersectionsToTiles.get(tile.getId());
        for (Integer intersectionId : intersectionsId) {
            adjacentIntersections.add(intersections.get(intersectionId));
        }
        return adjacentIntersections;
    }

    public List<List<Integer>> getAdjacentTilesToIntersections() {
        return adjacentTilesToIntersections;
    }

    public List<Integer> getAdjacentTilesToIntersection(int intersection) {
        return adjacentTilesToIntersections.get(intersection);
    }

    public Tile getRobberPosition() {
        return robberPosition;
    }

    public List<Map<String, Object>> getBoardArguments() {
        List<Map<String, Object>> tilesInformation = new ArrayList<>();
        for (Tile tile : tiles) {
            Map<String, Object> tileInformation = new HashMap<>();
            tileInformation.put("resource", tile.getResource());
            tileInformation.put("number", tile.getNumber());
            tilesInformation.add(tileInformation);
        }
        return tilesInformation;
    }

    public String getBoardJson() {
        List<Pair<Resource, Integer>> tilesInformation = new ArrayList<>();
        for (Tile tile : tiles) {
            tilesInformation.add(new Pair<>(tile.getResource(), tile.getNumber()));
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String boardJSON = objectMapper.writeValueAsString(tilesInformation);
            return boardJSON.replaceAll("key", "resource")
                    .replaceAll("value", "number");
        } catch (JsonProcessingException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public String getPortsJson() {
        try {
            return new ObjectMapper().writeValueAsString(ports);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    //endregion

    //region Setters

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    public void setNumberedTiles(List<List<Tile>> numberedTiles) {
        this.numberedTiles = numberedTiles;
    }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
    }

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    public void setRoads(List<Road> roads) {
        this.roads = roads;
    }

    public void setTileGraph(TileGraph tileGraph) {
        this.tileGraph = tileGraph;
    }

    public void setIntersectionGraph(IntersectionGraph intersectionGraph) {
        this.intersectionGraph = intersectionGraph;
    }

    public void setAdjacentIntersectionsToTiles(List<List<Integer>> adjacentIntersectionsToTiles) {
        this.adjacentIntersectionsToTiles = adjacentIntersectionsToTiles;
    }

    public void setAdjacentTilesToIntersections(List<List<Integer>> adjacentTilesToIntersections) {
        this.adjacentTilesToIntersections = adjacentTilesToIntersections;
    }

    public void setRobberPosition(Tile robberPosition) {
        this.robberPosition = robberPosition;
    }

    //endregion

    //region Road

    public void addRoad(Road road) {
        roads.add(road);
    }

    public boolean hasRoad(int start, int end) {
        for (Road road : roads) {
            if (road.getStart().getId() == start && road.getEnd().getId() == end) {
                return true;
            }
            if (road.getStart().getId() == end && road.getEnd().getId() == start) {
                return true;
            }
        }
        return false;
    }

    //endregion

    //region Generate Map

    public void generateRandomTiles() {
        generateResources();
        generateNumbers();
    }

    private void generateResources() {
        List<Resource> resources = new ArrayList<>();

        for (int i = 0; i < Component.FIELD_TILES; i++)
            resources.add(Resource.grain);
        for (int i = 0; i < Component.FOREST_TILES; i++)
            resources.add(Resource.lumber);
        for (int i = 0; i < Component.PASTURE_TILES; i++)
            resources.add(Resource.wool);
        for (int i = 0; i < Component.MOUNTAINS_TILES; i++)
            resources.add(Resource.ore);
        for (int i = 0; i < Component.HILLS_TILES; i++)
            resources.add(Resource.brick);
        for (int i = 0; i < Component.DESERT_TILES; i++)
            resources.add(Resource.desert);
        Collections.shuffle(resources);

        int i = 0;
        for (Tile tile : tiles) {
            tile.setResource(resources.get(i++));
            if (tile.getResource() == Resource.desert) {
                setRobberPosition(tile);
            }
        }
    }

    private void generateNumbers() {
        boolean sixNearEight = true;
        while (sixNearEight) {
            sixNearEight = false;
            Integer[] numberArray = {2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12};
            List<Integer> numberList = Arrays.asList(numberArray);
            Collections.shuffle(numberList);
            int i = 0;
            for (Tile tile : tiles) {
                if (tile.getResource() != Resource.desert) {
                    tile.setNumber(numberList.get(i));
                    i++;
                } else {
                    tile.setNumber(0);
                }
            }
            for (Tile tile : tiles) {
                if (tile.getNumber() == 6 || tile.getNumber() == 8) {
                    List<Integer> neighbors = tileGraph.getAdjacentTiles(tile.getId());
                    for (Integer neighbor : neighbors) {
                        if (tile.getNumber() + tiles.get(neighbor).getNumber() == 14) {
                            sixNearEight = true;
                            break;
                        }
                    }
                }
            }
        }

        for (int i = 0; i <= 12; i++) {
            numberedTiles.add(new ArrayList<>());
        }
        for (Tile tile : tiles) {
            numberedTiles.get(tile.getNumber()).add(tile);
        }
    }

    private void createMapping() {
        for (int i = 0; i < Component.TILES; i++)
            adjacentIntersectionsToTiles.add(new ArrayList<>(6));

        for (int i = 0; i < Component.INTERSECTIONS; i++)
            adjacentTilesToIntersections.add(new ArrayList<>(3));

        for (int ring = 0; ring < 3; ring++) {
            List<Integer> tileRing = tileGraph.getRing(ring);
            int tileRingSize = tileRing.size();

            List<Integer> intersectionRing = intersectionGraph.getRing(ring);
            int intersectionRingSize = intersectionRing.size();

            List<Integer> beforeIntersectionRing;
            int previousIntersectionRingSize;
            if (ring > 0) {
                beforeIntersectionRing = intersectionGraph.getRing(ring - 1);
                previousIntersectionRingSize = beforeIntersectionRing.size();
            } else {
                beforeIntersectionRing = null;
                previousIntersectionRingSize = 0;
            }

            int iIndex1 = 0;
            int iIndex2 = 1;
            for (int tile = 0; tile < tileRingSize; tile++) {
                int iIndex3 = 3;
                int iIndex4 = iIndex3;
                if (ring != 0) {
                    boolean corner = ((tile % ring) == 0);
                    if (corner) {
                        iIndex3++;
                        iIndex4--;
                    }
                }

                for (int k = 0; k < iIndex3; k++) {
                    adjacentIntersectionsToTiles.get(tileRing.get(tile)).add(intersectionRing.get(iIndex1));
                    adjacentTilesToIntersections.get(intersectionRing.get(iIndex1)).add(tileRing.get(tile));
                    if (k + 1 < iIndex3) {
                        iIndex1 = (iIndex1 + 1) % intersectionRingSize;
                    }
                }

                if (beforeIntersectionRing != null) {
                    for (int k = 0; k < iIndex4; k++) {
                        adjacentIntersectionsToTiles.get(tileRing.get(tile)).add(beforeIntersectionRing.get(iIndex2));
                        adjacentTilesToIntersections.get(beforeIntersectionRing.get(iIndex2)).add(tileRing.get(tile));
                        if (k + 1 < iIndex4) {
                            iIndex2 = (iIndex2 + 1) % previousIntersectionRingSize;
                        }
                    }
                }
            }
        }

        adjacentIntersectionsToTiles.get(0).add(3);
        adjacentIntersectionsToTiles.get(0).add(4);
        adjacentIntersectionsToTiles.get(0).add(5);
        adjacentTilesToIntersections.get(3).add(0);
        Collections.sort(adjacentTilesToIntersections.get(3));
        adjacentTilesToIntersections.get(4).add(0);
        Collections.sort(adjacentTilesToIntersections.get(4));
        adjacentTilesToIntersections.get(5).add(0);
        Collections.sort(adjacentTilesToIntersections.get(5));
    }

    public void generatePorts() {
        int[] frequency = {4, 1, 1, 1, 1, 1};
        int counter = 0;
        int max = 5;
        int min = 0;
        int random;
        int sum;
        int addValue = 0;
        int index = 26;
        while (index < 54) {
            sum = 0;
            counter++;
            if (counter == 1 || counter == 3 || counter == 5 || counter == 6 || counter == 7 || counter == 8)
                addValue = 3;
            if (counter == 2 || counter == 4)
                addValue = 4;
            random = (int) (Math.random() * ((max - min) + 1)) + min;
            for (int value : frequency) sum += value;
            if (sum != 0) {
                while (frequency[random] <= 0) {
                    random = (int) (Math.random() * ((max - min) + 1)) + min;
                }
                ports.set(index, Port.values()[random]);
                int nextIndex = index + 1;
                ports.set(nextIndex, Port.values()[random]);
                frequency[random]--;
            }
            index += addValue;
        }
    }

    //endregion

    //region Print

    public void printAdjacentIntersectionsToTiles() {
        try {
            FileWriter fileWriter = new FileWriter("resources/AdjacentIntersectionsToTiles.txt");
            for (int i = 0; i < Component.TILES; i++) {
                fileWriter.write(i + " : ");
                for (int j = 0; j < adjacentIntersectionsToTiles.get(i).size(); j++)
                    fileWriter.write(adjacentIntersectionsToTiles.get(i).get(j) + " ");
                fileWriter.write('\n');
            }
            fileWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void printAdjacentTilesToIntersections() {
        try {
            FileWriter fileWriter = new FileWriter("resources/AdjacentTilesToIntersections.txt");
            for (int i = 0; i < Component.INTERSECTIONS; i++) {
                fileWriter.write(i + " : ");
                for (int j = 0; j < adjacentTilesToIntersections.get(i).size(); j++)
                    fileWriter.write(adjacentTilesToIntersections.get(i).get(j) + " ");
                fileWriter.write('\n');
            }
            fileWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    //endregion
}
