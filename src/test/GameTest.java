package test;

import catan.API.response.Code;
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

    @DisplayName("Check buy development")
    @Test
    public void buyDevelopment() {
        Player player = game.getPlayer("Ana");
        player.addResource(Resource.wool, Cost.DEVELOPMENT_WOOL);
        player.addResource(Resource.grain, Cost.DEVELOPMENT_GRAIN);
        player.addResource(Resource.ore, Cost.DEVELOPMENT_ORE);

        assertNull(game.buyDevelopment());
        assertEquals(game.buyDevelopment(), Code.PlayerNotEnoughWool);
    }

    @DisplayName("Check buy Road")
    @Test
    public void buyRoad() {
        game.buildSettlement(15);
        game.buildRoad(15, 14);

        Player player = game.getPlayer("Ana");
        player.addResource(Resource.lumber, Cost.ROAD_LUMBER);
        player.addResource(Resource.brick, Cost.ROAD_BRICK);

        assertNull(game.buyRoad(15, 16));
        assertEquals(game.buyRoad(15, 16), Code.PlayerNotEnoughLumber);

        player.addResource(Resource.lumber, Cost.ROAD_LUMBER);
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
        player.addResource(Resource.lumber, Cost.SETTLEMENT_LUMBER);
        player.addResource(Resource.wool, Cost.SETTLEMENT_WOOL);
        player.addResource(Resource.grain, Cost.SETTLEMENT_GRAIN);
        player.addResource(Resource.brick, Cost.SETTLEMENT_BRICK);

        assertNull(game.buySettlement(15));

        player.addResource(Resource.lumber, Cost.SETTLEMENT_LUMBER);
        player.addResource(Resource.wool, Cost.SETTLEMENT_WOOL);
        player.addResource(Resource.grain, Cost.SETTLEMENT_GRAIN);
        player.addResource(Resource.brick, Cost.SETTLEMENT_BRICK);

        assertEquals(game.buySettlement(18), Code.NotConnectsToRoad);
    }

    @DisplayName("Check buy city")
    @Test
    public void buyCity() {
        game.buildSettlement(15);
        game.buildRoad(15, 16);

        Player player = game.getPlayer("Ana");
        player.addResource(Resource.grain, Cost.CITY_GRAIN);
        player.addResource(Resource.ore, Cost.CITY_ORE);

        assertNull(game.buyCity(15));

        assertEquals(game.buyCity(15), Code.InvalidRequest);
        assertEquals(game.buyCity(17), Code.InvalidRequest);

        game.buildSettlement(17);
        assertEquals(game.buyCity(17), Code.PlayerNotEnoughGrain);

        player.addResource(Resource.grain, 3);
        assertEquals(game.buyCity(17), Code.PlayerNotEnoughOre);

        player.addResource(Resource.ore, 3);
        assertEquals(game.buyCity(17), Code.NotConnectsToRoad);

        game.buildRoad(16, 17);
        assertNull(game.buyCity(17));
    }

    @DisplayName("Check use development")
    @Test
    public void useDevelopment() {
        assertEquals(Code.PlayerNoKnight, game.useDevelopment(Development.knight));
        game.getCurrentPlayer().addDevelopment(Development.knight);
        assertNull(game.useDevelopment(Development.knight));
        assertFalse(game.getCurrentPlayer().hasDevelopment(Development.knight));
    }

    @DisplayName("Check Monopoly")
    @Test
    public void takeResourceFromAll() {
        assertEquals(Code.InvalidRequest, game.takeResourceFromAll("desert").getKey());
        Map<String, Object> expectedResult = new HashMap<>();
        int index = 0;
        for (Player player : game.getPlayerOrder()) {
            if (!player.equals(game.getCurrentPlayer())) {
                player.addResource(Resource.brick, 2);
                int resourcesNumber = player.getResourcesNumber(Resource.brick);
                player.removeResource(Resource.brick, resourcesNumber);
                game.getCurrentPlayer().addResource(Resource.brick, resourcesNumber);
                expectedResult.put("player_" + index, player.getId());
                expectedResult.put("resources_" + index, resourcesNumber);
                ++index;
            }
        }
        for (Player player : game.getPlayerOrder()) {
            player.addResource(Resource.brick, 2);
        }
        assertEquals(new Pair<Code, Map<String, Object>>(null, expectedResult), game.takeResourceFromAll("brick"));
    }

    @DisplayName("Check YearOfPlenty")
    @Test
    public void takeTwoResources() {
        assertEquals(Code.InvalidRequest, game.takeTwoResources("testStr1", "testStr2"));
        assertEquals(Code.InvalidRequest, game.takeTwoResources("brick", "testStr2"));
        assertEquals(Code.InvalidRequest, game.takeTwoResources("testStr1", "lumber"));

        assertNull(game.takeTwoResources("brick", "lumber"));
        game.getBank().removeResource(Resource.brick, game.getBank().getResourcesNumber(Resource.brick));

        assertEquals(Code.BankNoBrick, game.takeTwoResources("brick", "lumber"));
        assertEquals(Code.BankNoBrick, game.takeTwoResources("lumber", "brick"));
        assertNull(game.takeTwoResources("lumber", "lumber"));
    }

    @DisplayName("Check discard resources")
    @Test
    public void discardResources() {
        game.getCurrentPlayer().addResource(Resource.brick, 5);
        Map<Resource, Integer> resources = new HashMap<>();
        resources.put(Resource.brick, 5);
        game.discardResources(game.getCurrentPlayer().getId(), resources);
        assertFalse(game.getCurrentPlayer().hasResource());
        assertEquals(Code.PlayerNotEnoughBrick, game.discardResources(game.getCurrentPlayer().getId(), resources));
    }

    @DisplayName("Check player trade")
    @Test
    public void playerTrade() {
        game.getCurrentPlayer().addResource(Resource.lumber, 1);
        Map<Resource, Integer> offer = new HashMap<>();
        offer.put(Resource.lumber, 1);
        Map<Resource, Integer> request = new HashMap<>();
        request.put(Resource.grain, 1);

        assertEquals(game.wantToTrade("Maria"), Code.NoTradeAvailable);
        game.playerTrade(offer, request);
        assertEquals(game.wantToTrade("Maria"), Code.PlayerNotEnoughGrain);

        game.getPlayer("Maria").addResource(Resource.grain, 1);
        assertNull(game.wantToTrade("Maria"));

        assertEquals(game.wantToTrade("Maria"), Code.AlreadyInTrade);

        assertEquals(game.selectPartner("Elena"), Code.NotInTrade);
        assertNull(game.selectPartner("Maria"));
    }

    @DisplayName("Check bank trade")
    @Test
    public void bankTrade() {
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.addResource(Resource.lumber, 3);

        assertEquals(game.bankTrade("lumber", "desert"), Code.InvalidRequest);
        assertEquals(game.bankTrade("desert", "grain"), Code.InvalidRequest);

        assertEquals(game.bankTrade("lumber", "grain"), Code.PlayerNotEnoughLumber);
        currentPlayer.addResource(Resource.lumber, 1);
        assertNull(game.bankTrade("lumber", "grain"));
    }

    @DisplayName("Check port trade")
    @Test
    public void portTrade() {
        assertEquals(game.portTrade(19, "lumber", "wool"), Code.InvalidRequest);
        int port = game.getBoard().getPorts().indexOf(Port.ThreeForOne);
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.addResource(Resource.lumber, 3);

        assertNull(game.portTrade(port, "lumber", "grain"));
        assertEquals(game.portTrade(port, "lumber", "grain"), Code.PlayerNotEnoughLumber);

        port = game.getBoard().getPorts().indexOf(Port.Lumber);
        currentPlayer.addResource(Resource.lumber, 2);
        assertNull(game.portTrade(port, "lumber", "grain"));
    }

    @DisplayName("Check move robber")
    @Test
    public void moveRobber() {
        int tile = game.getBoard().getRobberPosition().getId();
        assertEquals(game.moveRobber(tile), Code.SameTile);

        assertNull(game.moveRobber((tile + 1) % Component.TILES));
        assertEquals(game.getBoard().getRobberPosition().getId(), (tile + 1) % Component.TILES);
    }

    @DisplayName("Steal resource")
    @Test
    public void stealResource() {
        Player currentPlayer = game.getCurrentPlayer();
        Player anotherPlayer = game.getPlayerOrder().get(game.getPlayersNumber() - 1);
        assertNull(game.stealResource(anotherPlayer.getId()));

        anotherPlayer.addResource(Resource.lumber);
        assertEquals(game.stealResource(anotherPlayer.getId()), Resource.lumber);
        assertFalse(anotherPlayer.hasResource(Resource.lumber));
        assertTrue(currentPlayer.hasResource(Resource.lumber));

        assertNull(game.stealResource(anotherPlayer.getId()));
        assertFalse(currentPlayer.hasResource(Resource.lumber, 2));
    }
}
