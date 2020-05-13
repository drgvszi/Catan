package test;

import catan.API.response.Code;
import catan.game.board.Board;
import catan.game.enumeration.Development;
import catan.game.enumeration.Port;
import catan.game.enumeration.Resource;
import catan.game.game.BaseGame;
import catan.game.game.Game;
import catan.game.player.Player;
import catan.game.rule.Component;
import catan.game.rule.Cost;
import javafx.util.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Game game;

    public GameTest() {
        game = new BaseGame();
        game.addPlayer("Ana", new Player("Ana", game));
        game.addPlayer("Maria", new Player("Maria", game));
        game.addNextPlayer("Ana");
        game.addNextPlayer("Maria");
        game.startGame();
    }

    @DisplayName("Check build road")
    @Test
    public void buildRoad() {
        assertEquals(game.buildRoad(5, 6), Code.InvalidRoadPosition);
        game.buildSettlement(15);
        assertNull(game.buildRoad(15, 16));
        assertEquals(game.buildRoad(15, 16), Code.RoadAlreadyExistent);
        assertEquals(game.buildRoad(17, 10), Code.InvalidRoadPosition);
        assertNull(game.buildRoad(16, 17));
    }

    @DisplayName("Check build settlement")
    @Test
    public void buildSettlement() {
        assertNull(game.buildSettlement(15));
        assertEquals(game.buildSettlement(60), Code.InvalidSettlementPosition);
        assertEquals(game.buildSettlement(15), Code.IntersectionAlreadyOccupied);
        assertEquals(game.buildSettlement(16), Code.DistanceRuleViolated);
    }

    @DisplayName("Check discard resources")
    @Test
    public void discardResources() {
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.addResource(Resource.Brick, 5);
        Map<Resource, Integer> resources = new HashMap<>();
        resources.put(Resource.Brick, 5);
        game.discardResources(currentPlayer.getId(), resources);
        assertFalse(game.getCurrentPlayer().hasResource(Resource.Brick, 5));
        assertEquals(game.discardResources(currentPlayer.getId(), resources), Code.PlayerNotEnoughBrick);
    }

    @DisplayName("Check buy development")
    @Test
    public void buyDevelopment() {
        Player player = game.getPlayer("Ana");
        player.addResource(Resource.Wool, Cost.DEVELOPMENT_WOOL);
        player.addResource(Resource.Grain, Cost.DEVELOPMENT_GRAIN);
        player.addResource(Resource.Ore, Cost.DEVELOPMENT_ORE);

        assertNull(game.buyDevelopment());
        assertEquals(game.buyDevelopment(), Code.PlayerNotEnoughWool);
    }

    @DisplayName("Check buy Road")
    @Test
    public void buyRoad() {
        game.buildSettlement(15);
        game.buildRoad(15, 14);

        Player player = game.getPlayer("Ana");
        player.addResource(Resource.Lumber, Cost.ROAD_LUMBER);
        player.addResource(Resource.Brick, Cost.ROAD_BRICK);

        assertNull(game.buyRoad(15, 16));
        assertEquals(game.buyRoad(15, 16), Code.PlayerNotEnoughLumber);

        player.addResource(Resource.Lumber, Cost.ROAD_LUMBER);
        assertEquals(game.buyRoad(15, 16), Code.PlayerNotEnoughBrick);
    }

    @DisplayName("Check buy settlement")
    @Test
    public void buySettlement() {
        game.buildSettlement(17);
        game.buildRoad(16, 17);
        game.buildRoad(15, 16);
        game.buildRoad(15, 14);

        Player player = game.getPlayer("Ana");
        player.addResource(Resource.Lumber, Cost.SETTLEMENT_LUMBER);
        player.addResource(Resource.Wool, Cost.SETTLEMENT_WOOL);
        player.addResource(Resource.Grain, Cost.SETTLEMENT_GRAIN);
        player.addResource(Resource.Brick, Cost.SETTLEMENT_BRICK);

        assertNull(game.buySettlement(15));

        player.addResource(Resource.Lumber, Cost.SETTLEMENT_LUMBER);
        player.addResource(Resource.Wool, Cost.SETTLEMENT_WOOL);
        player.addResource(Resource.Grain, Cost.SETTLEMENT_GRAIN);
        player.addResource(Resource.Brick, Cost.SETTLEMENT_BRICK);

        assertEquals(game.buySettlement(18), Code.NotConnectsToRoad);
    }

    @DisplayName("Check buy city")
    @Test
    public void buyCity() {
        game.buildSettlement(15);
        game.buildRoad(15, 16);

        Player player = game.getPlayer("Ana");
        player.addResource(Resource.Grain, Cost.CITY_GRAIN);
        player.addResource(Resource.Ore, Cost.CITY_ORE);

        assertNull(game.buyCity(15));

        assertEquals(game.buyCity(15), Code.InvalidRequest);
        assertEquals(game.buyCity(17), Code.InvalidRequest);

        game.buildSettlement(17);
        assertEquals(game.buyCity(17), Code.PlayerNotEnoughGrain);

        player.addResource(Resource.Grain, 3);
        assertEquals(game.buyCity(17), Code.PlayerNotEnoughOre);

        player.addResource(Resource.Ore, 3);
        assertEquals(game.buyCity(17), Code.NotConnectsToRoad);

        game.buildRoad(16, 17);
        assertNull(game.buyCity(17));
    }

    @DisplayName("Check player trade")
    @Test
    public void playerTrade() {
        game.getCurrentPlayer().addResource(Resource.Lumber, 1);
        Map<Resource, Integer> offer = new HashMap<>();
        offer.put(Resource.Lumber, 1);
        Map<Resource, Integer> request = new HashMap<>();
        request.put(Resource.Grain, 1);

        assertEquals(game.wantToTrade("Maria"), Code.NoTradeAvailable);
        game.playerTrade(offer, request);
        assertEquals(game.wantToTrade("Maria"), Code.PlayerNotEnoughGrain);

        game.getPlayer("Maria").addResource(Resource.Grain, 1);
        assertNull(game.wantToTrade("Maria"));

        assertEquals(game.wantToTrade("Maria"), Code.AlreadyInTrade);

        assertEquals(game.selectPartner("Elena"), Code.NotInTrade);
        assertNull(game.selectPartner("Maria"));
    }

    @DisplayName("Check bank trade")
    @Test
    public void bankTrade() {
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.addResource(Resource.Lumber, 3);

        assertEquals(game.bankTrade("lumber", "desert"), Code.InvalidRequest);
        assertEquals(game.bankTrade("desert", "grain"), Code.InvalidRequest);

        assertEquals(game.bankTrade("lumber", "grain"), Code.PlayerNotEnoughLumber);
        currentPlayer.addResource(Resource.Lumber, 1);
        assertNull(game.bankTrade("lumber", "grain"));
    }

    @DisplayName("Check port trade")
    @Test
    public void portTrade() {
        assertEquals(game.portTrade(19, "lumber", "wool"), Code.InvalidRequest);
        int port = game.getBoard().getPorts().indexOf(Port.ThreeForOne);
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.addResource(Resource.Lumber, 3);

        assertNull(game.portTrade(port, "lumber", "grain"));
        assertEquals(game.portTrade(port, "lumber", "grain"), Code.PlayerNotEnoughLumber);

        port = game.getBoard().getPorts().indexOf(Port.Lumber);
        currentPlayer.addResource(Resource.Lumber, 2);
        assertNull(game.portTrade(port, "lumber", "grain"));
    }

    @DisplayName("Check use development")
    @Test
    public void useDevelopment() {
        assertEquals(game.useDevelopment(Development.knight), Code.PlayerNoKnight);
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.addDevelopment(Development.knight);
        assertNull(game.useDevelopment(Development.knight));
        assertFalse(currentPlayer.hasDevelopment(Development.knight));
    }

    @DisplayName("Check Monopoly")
    @Test
    public void takeResourceFromAll() {
        assertEquals(game.takeResourceFromAll("desert").getKey(), Code.InvalidRequest);
        Map<String, Object> result = new HashMap<>();
        int index = 0;
        for (Player player : game.getPlayersOrder()) {
            if (!player.equals(game.getCurrentPlayer())) {
                player.addResource(Resource.Brick, 2);
                int resourcesNumber = player.getResourcesNumber(Resource.Brick);
                player.removeResource(Resource.Brick, resourcesNumber);
                game.getCurrentPlayer().addResource(Resource.Brick, resourcesNumber);
                result.put("player_" + index, player.getId());
                result.put("resources_" + index, resourcesNumber);
                ++index;
            }
        }
        for (Player player : game.getPlayersOrder()) {
            player.addResource(Resource.Brick, 2);
        }
        assertEquals(new Pair<Code, Map<String, Object>>(null, result), game.takeResourceFromAll("brick"));
    }

    @DisplayName("Check YearOfPlenty")
    @Test
    public void takeTwoResources() {
        assertEquals(game.takeTwoResources("desert", "desert"), Code.InvalidRequest);
        assertEquals(game.takeTwoResources("brick", "desert"), Code.InvalidRequest);
        assertEquals(game.takeTwoResources("desert", "lumber"), Code.InvalidRequest);

        assertNull(game.takeTwoResources("brick", "lumber"));

        game.getBank().removeResource(Resource.Brick, game.getBank().getResourcesNumber(Resource.Brick));
        assertEquals(Code.BankNoBrick, game.takeTwoResources("brick", "lumber"));

        assertNull(game.takeTwoResources("lumber", "lumber"));
    }

    @DisplayName("Check move robber")
    @Test
    public void moveRobber() {
        int tile = game.getBoard().getRobberPosition().getId();
        assertEquals(game.moveRobber(tile), Code.SameTile);

        assertNull(game.moveRobber((tile + 1) % Component.TILES));
        assertEquals(game.getBoard().getRobberPosition().getId(), (tile + 1) % Component.TILES);
    }

    @DisplayName("Check steal resource")
    @Test
    public void stealResource() {
        Board board = game.getBoard();
        board.setRobberPosition(board.getTile(0));
        Player currentPlayer = game.getCurrentPlayer();
        Player anotherPlayer = game.getPlayersOrder().get(game.getPlayersNumber() - 1);
        anotherPlayer.addSettlement(board.getIntersection(0));
        assertEquals(game.stealResource(anotherPlayer.getId()).getKey(), Code.PlayerNoResource);

        anotherPlayer.addResource(Resource.Lumber);
        assertEquals(game.stealResource(anotherPlayer.getId()).getValue(), Resource.Lumber);
        assertFalse(anotherPlayer.hasResource(Resource.Lumber));
        assertTrue(currentPlayer.hasResource(Resource.Lumber));

        assertEquals(game.stealResource(anotherPlayer.getId()).getKey(), Code.PlayerNoResource);
        assertFalse(currentPlayer.hasResource(Resource.Lumber, 2));

        board.setRobberPosition(board.getTile(1));
        assertEquals(game.stealResource(anotherPlayer.getId()).getKey(), Code.InvalidRequest);
    }
}
