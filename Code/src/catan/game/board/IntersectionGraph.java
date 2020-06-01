package catan.game.board;

import catan.game.rule.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IntersectionGraph {
    private List<List<Integer>> rings;
    private boolean[][] adjacencyMatrix;
    private List<List<Integer>> adjacencyLists;

    IntersectionGraph() {
        rings = new ArrayList<>(Component.RINGS);
        adjacencyMatrix = new boolean[Component.INTERSECTIONS][Component.INTERSECTIONS];
        adjacencyLists = new ArrayList<>(Component.INTERSECTIONS);
        setIndexes();
        createAdjacencyMatrix();
        createAdjacencyLists();
        // printAdjacencyLists();
    }

    public List<Integer> getRing(Integer ring) {
        return rings.get(ring);
    }

    public List<Integer> getAdjacentIntersections(int intersection) {
        return adjacencyLists.get(intersection);
    }

    public boolean areAdjacent(int source, int target) {
        return adjacencyMatrix[source][target];
    }

    private void setIndexes() {
        List<Integer> list1 = new ArrayList<>(6);
        List<Integer> list2 = new ArrayList<>(18);
        List<Integer> list3 = new ArrayList<>(30);
        for (int i = 0; i < 6; i++)
            list1.add(i);
        for (int i = 6; i < 24; i++)
            list2.add(i);
        for (int i = 24; i < Component.INTERSECTIONS; i++)
            list3.add(i);
        rings.add(list1);
        rings.add(list2);
        rings.add(list3);
    }

    private void createAdjacencyMatrix() {
        // for-ul este pentru iterarea prin fiecare inel
        for (int i = 0; i < 3; i++) {
            // extragem fiecare inel din lista
            List<Integer> ring = rings.get(i);
            int ringSize = ring.size();
            int difference = -1;
            int wait = 0;
            // variabila care indica daca inelul curent mai are un inel ca vecin
            boolean hasNextLink = true;
            // parcurgem inelul actual
            for (int j = 1; j <= ringSize; j++) {
                List<Integer> followingRing;
                int followingRingSize;
                // ultimul inel se invecineaza cu marea
                if (i + 1 < 3) {
                    followingRing = rings.get(i + 1);
                    followingRingSize = followingRing.size();
                } else {
                    followingRing = null;
                    followingRingSize = 0;
                }
                int index = ring.get(j % ringSize);
                int neighbourIndex = ring.get((j + 1) % ringSize);
                // ne ocupam de adiacenta celor de pe acelasi inel
                adjacencyMatrix[index][neighbourIndex] = true;
                adjacencyMatrix[neighbourIndex][index] = true;
                // urmeaza adiacenta cu intersectia de pe celalalt inel
                if (hasNextLink) {
                    int index1;
                    if (j + difference < followingRingSize) {
                        assert followingRing != null;
                        index1 = followingRing.get(j + difference);
                        adjacencyMatrix[index][index1] = true;
                        adjacencyMatrix[index1][index] = true;
                    }
                    if (wait == 0) {
                        wait = i;
                        difference = difference + 2;
                    } else {
                        wait = wait - 1;
                        hasNextLink = false;
                    }
                } else {
                    hasNextLink = true;
                }
            }
        }
    }

    private void createAdjacencyLists() {
        for (int i = 0; i < Component.INTERSECTIONS; i++) {
            List<Integer> neighbors = new ArrayList<>();
            for (int j = 0; j < Component.INTERSECTIONS; j++)
                if (adjacencyMatrix[i][j])
                    neighbors.add(j);
            adjacencyLists.add(neighbors);
        }
    }

    public void printAdjacencyLists() {
        try {
            FileWriter fileWriter = new FileWriter("resources/IntersectionsAdjacency.txt");
            for (int i = 0; i < Component.INTERSECTIONS; i++) {
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
