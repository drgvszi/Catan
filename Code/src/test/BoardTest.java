package test;

import catan.game.board.Board;
import catan.game.board.Tile;
import catan.game.enumeration.Port;
import catan.game.enumeration.Resource;
import catan.game.rule.Component;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BoardTest {
    private Board board;

    public BoardTest() {
        board = new Board();
    }

    @DisplayName("Check create board")
    @Test
    public void createBoard() {
        assertEquals(board.getTiles().size(), Component.TILES);
        assertEquals(board.getIntersections().size(), Component.INTERSECTIONS);
        assertEquals(board.getPorts().size(), Component.INTERSECTIONS);
        assertEquals(board.getRoads().size(), 0);
    }

    @DisplayName("Check intersection graph")
    @Test
    public void checkIntersectionGraph() {
        List<Integer> adjacentIntersections = board.getIntersectionGraph().getAdjacentIntersections(0);
        assertEquals(adjacentIntersections.size(), 3);
        assertEquals(adjacentIntersections.get(0), 1);
        assertEquals(adjacentIntersections.get(1), 5);
        assertEquals(adjacentIntersections.get(2), 21);

        adjacentIntersections = board.getIntersectionGraph().getAdjacentIntersections(24);
        assertEquals(adjacentIntersections.size(), 3);
        assertEquals(adjacentIntersections.get(0), 7);
        assertEquals(adjacentIntersections.get(1), 25);
        assertEquals(adjacentIntersections.get(2), 53);
    }

    @DisplayName("Check tile graph")
    @Test
    public void checkTileGraph() {
        List<Integer> adjacentTiles = board.getAdjacentTiles(0);
        assertEquals(adjacentTiles.size(), 6);
        for (int tile = 1; tile <= 6; ++tile) {
            assertEquals(adjacentTiles.get(tile - 1), tile);
        }

        adjacentTiles = board.getAdjacentTiles(18);
        assertEquals(adjacentTiles.size(), 4);
        assertEquals(adjacentTiles.get(0), 1);
        assertEquals(adjacentTiles.get(1), 6);
        assertEquals(adjacentTiles.get(2), 7);
        assertEquals(adjacentTiles.get(3), 17);

        adjacentTiles = board.getAdjacentTiles(13);
        assertEquals(adjacentTiles.size(), 3);
        assertEquals(adjacentTiles.get(0), 4);
        assertEquals(adjacentTiles.get(1), 12);
        assertEquals(adjacentTiles.get(2), 14);
    }

    @DisplayName("Check adjacent intersections to tiles")
    @Test
    public void checkAdjacentIntersectionsToTiles() {
        List<Integer> adjacentIntersections = board.getAdjacentIntersections(0);
        assertEquals(adjacentIntersections.size(), 6);
        for (int intersection = 0; intersection <= 5; ++intersection) {
            assertEquals(adjacentIntersections.get(intersection), intersection);
        }

        adjacentIntersections = board.getAdjacentIntersections(9);
        adjacentIntersections.sort(Comparator.comparing(Integer::valueOf));
        assertEquals(adjacentIntersections.size(), 6);
        assertEquals(adjacentIntersections.get(0), 10);
        assertEquals(adjacentIntersections.get(1), 11);
        assertEquals(adjacentIntersections.get(2), 29);
        assertEquals(adjacentIntersections.get(3), 30);
        assertEquals(adjacentIntersections.get(4), 31);
        assertEquals(adjacentIntersections.get(5), 32);
    }

    @DisplayName("Check adjacent tiles to intersections")
    @Test
    public void checkAdjacentTilesToIntersections() {
        List<Integer> adjacentTiles = board.getAdjacentTilesToIntersection(0);
        assertEquals(adjacentTiles.size(), 3);
        assertEquals(adjacentTiles.get(0), 0);
        assertEquals(adjacentTiles.get(1), 5);
        assertEquals(adjacentTiles.get(2), 6);

        adjacentTiles = board.getAdjacentTilesToIntersection(42);
        assertEquals(adjacentTiles.size(), 2);
        assertEquals(adjacentTiles.get(0), 13);
        assertEquals(adjacentTiles.get(1), 14);
    }

    @DisplayName("Check tile values")
    @Test
    public void checkTileValues() {
        List<Tile> tiles = board.getTiles();
        List<Integer> values = new ArrayList<>();
        for (Tile tile : tiles) {
            values.add(tile.getNumber());
        }
        values.sort(Comparator.comparing(Integer::valueOf));
        List<Integer> valuesToVerify = Arrays.asList(0, 2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12);
        assertEquals(values, valuesToVerify);

        for (Tile tile : tiles) {
            if (tile.getNumber() == 6) {
                for (int adjacentTile : board.getAdjacentTiles(tile.getId())) {
                    assertNotEquals(tiles.get(adjacentTile).getNumber(), 8);
                }
            }
            if (tile.getNumber() == 8) {
                for (int adjacentTile : board.getAdjacentTiles(tile.getId())) {
                    assertNotEquals(tiles.get(adjacentTile).getNumber(), 6);
                }
            }
        }
    }

    @DisplayName("Check ports")
    @Test
    public void checkPorts() {
        List<Port> ports = board.getPorts();
        Map<Port, Integer> portsNumber = new HashMap<>();
        portsNumber.put(Port.ThreeForOne, 0);
        portsNumber.put(Port.Lumber, 0);
        portsNumber.put(Port.Wool, 0);
        portsNumber.put(Port.Grain, 0);
        portsNumber.put(Port.Brick, 0);
        portsNumber.put(Port.Ore, 0);
        for (Port port : ports) {
            if (port != Port.None) {
                portsNumber.put(port, portsNumber.get(port) + 1);
            }
        }
        assertEquals(portsNumber.get(Port.ThreeForOne), 8);
        assertEquals(portsNumber.get(Port.Lumber), 2);
        assertEquals(portsNumber.get(Port.Wool), 2);
        assertEquals(portsNumber.get(Port.Grain), 2);
        assertEquals(portsNumber.get(Port.Brick), 2);
        assertEquals(portsNumber.get(Port.Ore), 2);

        assertNotEquals(ports.get(26), Port.None);
        assertNotEquals(ports.get(27), Port.None);
        assertEquals(ports.get(26), ports.get(27));

        assertNotEquals(ports.get(29), Port.None);
        assertNotEquals(ports.get(30), Port.None);
        assertEquals(ports.get(29), ports.get(30));

        assertNotEquals(ports.get(33), Port.None);
        assertNotEquals(ports.get(34), Port.None);
        assertEquals(ports.get(33), ports.get(34));

        assertNotEquals(ports.get(36), Port.None);
        assertNotEquals(ports.get(37), Port.None);
        assertEquals(ports.get(36), ports.get(37));

        assertNotEquals(ports.get(40), Port.None);
        assertNotEquals(ports.get(41), Port.None);
        assertEquals(ports.get(40), ports.get(41));

        assertNotEquals(ports.get(43), Port.None);
        assertNotEquals(ports.get(44), Port.None);
        assertEquals(ports.get(43), ports.get(44));

        assertNotEquals(ports.get(46), Port.None);
        assertNotEquals(ports.get(47), Port.None);
        assertEquals(ports.get(46), ports.get(47));

        assertNotEquals(ports.get(49), Port.None);
        assertNotEquals(ports.get(50), Port.None);
        assertEquals(ports.get(49), ports.get(50));

        assertNotEquals(ports.get(52), Port.None);
        assertNotEquals(ports.get(53), Port.None);
        assertEquals(ports.get(52), ports.get(53));
    }

    @DisplayName("Check robber position")
    @Test
    public void checkRobberPosition() {
        assertEquals(board.getRobberPosition().getResource(), Resource.desert);
    }
}
