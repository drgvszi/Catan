package catan.game.board;

import catan.game.rule.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileGraph {
    private List<List<Integer>> rings;
    private boolean[][] adjacencyMatrix;
    private List<List<Integer>> adjacencyLists;

    public TileGraph() {
        rings = new ArrayList<>(Component.RINGS);
        adjacencyMatrix = new boolean[Component.TILES][Component.TILES];
        adjacencyLists = new ArrayList<>(Component.TILES);
        setIndexes();
        createAdjacencyMatrix();
        createAdjacencyLists();
        printAdjacencyLists();
    }

    public List<Integer> getRing(Integer ring) {
        return rings.get(ring);
    }

    public List<Integer> getAdjacentTiles(Integer tile) {
        return adjacencyLists.get(tile);
    }

    public boolean areAdjacent(int source, int target) {
        return adjacencyMatrix[source][target];
    }

    private void setIndexes() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(0);
        rings.add(list1);
        List<Integer> list2 = new ArrayList<>();
        for (int i = 1; i <= 6; ++i) {
            list2.add(i);
        }
        rings.add(list2);
        List<Integer> list3 = new ArrayList<>();
        for (int i = 7; i <= 18; ++i) {
            list3.add(i);
        }
        rings.add(list3);
    }

    private void createAdjacencyMatrix() {
        for (int i = 1; i <= 6; ++i) {
            adjacencyMatrix[0][i] = true;
            adjacencyMatrix[i][0] = true;
        }

        int ring = 1;
        int ringIndex = 0;
        for (int boardIndex = 1; boardIndex < Component.TILES; boardIndex++) {
            if (ringIndex >= ring * 6) {
                ringIndex = 0;
                ring++;
            }

            markPreviousAdjacency(ring, ringIndex, boardIndex);
            markNextAdjacency(ring, ringIndex, boardIndex);

            int adjacentRing = ring + 1;
            if (adjacentRing < Component.RINGS) {
                int adjacentIndex = (ringIndex + 1) * adjacentRing - 2;
                int neighbor = rings.get(adjacentRing).get(adjacentIndex);
                adjacencyMatrix[boardIndex][neighbor] = true;
                adjacencyMatrix[neighbor][boardIndex] = true;

                markNextAdjacency(adjacentRing, adjacentIndex, boardIndex);
                markPreviousAdjacency(adjacentRing, adjacentIndex, boardIndex);
            }
            ringIndex++;
        }
    }

    private void markPreviousAdjacency(int ring, int ringIndex, int boardIndex) {
        int neighbor;
        if (ringIndex == 0) {
            neighbor = rings.get(ring).get(rings.get(ring).size() - 1);
        } else {
            neighbor = rings.get(ring).get(ringIndex - 1);
        }
        adjacencyMatrix[boardIndex][neighbor] = true;
        adjacencyMatrix[neighbor][boardIndex] = true;
    }

    private void markNextAdjacency(int ring, int index, int i) {
        int neighbor;
        if (index == rings.get(ring).size() - 1) {
            neighbor = rings.get(ring).get(0);
        } else {
            neighbor = rings.get(ring).get(index + 1);
        }
        adjacencyMatrix[i][neighbor] = true;
        adjacencyMatrix[neighbor][i] = true;
    }

    private void createAdjacencyLists() {
        for (int i = 0; i < Component.TILES; i++) {
            List<Integer> neighbors = new ArrayList<>();
            for (int j = 0; j < Component.TILES; j++)
                if (adjacencyMatrix[i][j])
                    neighbors.add(j);
            adjacencyLists.add(neighbors);
        }
    }

    public void printAdjacencyLists() {
        try {
            FileWriter fileWriter = new FileWriter("resources/TilesAdjacency.txt");
            for (int i = 0; i < Component.TILES; i++) {
                fileWriter.write(i + " : ");
                for (int j = 0; j < adjacencyLists.get(i).size(); j++)
                    fileWriter.write(adjacencyLists.get(i).get(j) + " ");
                fileWriter.write('\n');
            }
            fileWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
