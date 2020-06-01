package test;

import catan.game.enumeration.Development;
import catan.game.enumeration.Resource;
import catan.game.game.BaseGame;
import catan.game.game.Game;
import catan.game.player.Player;
import catan.game.property.Intersection;
import catan.game.property.Road;
import catan.game.rule.Cost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Game game;
    private Player player;

    public PlayerTest() {
        game = new BaseGame();
        player = new Player("Gelu", game);
    }

    @DisplayName("Check create player")
    @Test
    public void createPlayer() {
        //region Cards

        Map<Resource, Integer> resources = player.getResources();
        for (Map.Entry<Resource, Integer> resource : resources.entrySet()) {
            assertEquals(resource.getValue(), 0);
        }

        Map<Development, Integer> developments = player.getDevelopments();
        for (Map.Entry<Development, Integer> development : developments.entrySet()) {
            assertEquals(development.getValue(), 0);
        }

        //endregion

        //region Properties

        assertEquals(player.getRoadsNumber(), 0);
        assertEquals(player.getSettlementsNumber(), 0);
        assertEquals(player.getCitiesNumber(), 0);

        //endregion

        //region Bonus

        assertEquals(player.getUsedKnights(), 0);
        assertEquals(player.getRoadsToBuild(), 0);

        assertFalse(player.hasLongestRoad());
        assertFalse(player.hasLargestArmy());

        assertEquals(player.getPublicVictoryPoints(), 0);
        assertEquals(player.getHiddenVictoryPoints(), 0);

        //endregion
    }

    @DisplayName("Check road connects other road")
    @Test
    void roadConnectsToRoad() {
        player.addRoad(new Road(new Intersection(3), new Intersection(4)));
        assertNull(player.connectsToRoad(4, 5));
        assertNotNull(player.connectsToRoad(5, 6));
    }

    @DisplayName("Checks intersection connect to road")
    @Test
    void intersectionConnectsToRoad() {
        player.addRoad(new Road(new Intersection(3), new Intersection(4)));
        assertNull(player.connectsToRoad(3));
        assertNotNull(player.connectsToRoad(10));
    }

    @DisplayName("Check buy development")
    @Test
    void buyDevelopment() {
        player.addResource(Resource.grain, Cost.DEVELOPMENT_GRAIN);
        assertEquals(player.getResourcesNumber(Resource.grain), Cost.DEVELOPMENT_GRAIN);
        assertNotNull(player.canBuyDevelopment());

        player.addResource(Resource.wool, Cost.DEVELOPMENT_WOOL);
        assertEquals(player.getResourcesNumber(Resource.wool), Cost.DEVELOPMENT_WOOL);
        assertNotNull(player.canBuyDevelopment());

        player.addResource(Resource.ore, Cost.DEVELOPMENT_ORE);
        assertEquals(player.getResourcesNumber(Resource.ore), Cost.DEVELOPMENT_ORE);
        assertNull(player.canBuyDevelopment());
        assertNull(player.buyDevelopment(Development.victoryPoint));

        assertEquals(player.getHiddenVictoryPoints(), 1);
    }

    @DisplayName("Check buy road")
    @Test
    void buyRoad() {
        player.addRoad(new Road(new Intersection(3), new Intersection(4)));
        assertNotNull(player.canBuyRoad(4, 5));

        player.addResource(Resource.lumber, Cost.ROAD_LUMBER);
        assertEquals(player.getResourcesNumber(Resource.lumber), Cost.ROAD_LUMBER);
        assertNotNull(player.canBuyRoad(4, 5));

        player.addResource(Resource.brick, Cost.ROAD_BRICK);
        assertEquals(player.getResourcesNumber(Resource.brick), Cost.ROAD_BRICK);
        assertNull(player.canBuyRoad(4, 5));
        assertNull(player.buyRoad());
    }

    @DisplayName("Check build road")
    @Test
    void buildRoad() {
        Road road = new Road(new Intersection(3), new Intersection(4));
        player.buildRoad(road);
        assertEquals(road.getOwner(), player);
        assertEquals(player.getRoads().get(player.getRoadsNumber() - 1), road);
    }

    @DisplayName("Check buy settlement")
    @Test
    void buySettlement() {
        player.addSettlement(new Intersection(10));
        assertNotNull(player.canBuySettlement(11));

        player.addRoad(new Road(new Intersection(10), new Intersection(11)));

        player.addResource(Resource.lumber, Cost.SETTLEMENT_LUMBER);
        assertEquals(player.getResourcesNumber(Resource.lumber), Cost.SETTLEMENT_LUMBER);
        assertNotNull(player.canBuySettlement(11));

        player.addResource(Resource.wool, Cost.SETTLEMENT_WOOL);
        assertEquals(player.getResourcesNumber(Resource.wool), Cost.SETTLEMENT_WOOL);
        assertNotNull(player.canBuySettlement(11));

        player.addResource(Resource.grain, Cost.SETTLEMENT_GRAIN);
        assertEquals(player.getResourcesNumber(Resource.grain), Cost.SETTLEMENT_GRAIN);
        assertNotNull(player.canBuySettlement(11));

        player.addResource(Resource.brick, Cost.SETTLEMENT_BRICK);
        assertEquals(player.getResourcesNumber(Resource.brick), Cost.SETTLEMENT_BRICK);
        assertNull(player.canBuySettlement(11));
        assertNull(player.buySettlement());
    }

    @DisplayName("Check build settlement")
    @Test
    void buildSettlement() {
        Intersection settlement = new Intersection(10);
        player.buildSettlement(settlement);
        assertEquals(settlement.getOwner(), player);
        assertEquals(player.getSettlements().get(player.getSettlementsNumber() - 1), settlement);
    }

    @DisplayName("Check can buy city")
    @Test
    void canBuyCity() {
        player.addCity(new Intersection(10));
        assertNotNull(player.canBuyCity(11));

        player.addSettlement(new Intersection(11));
        assertNotNull(player.canBuyCity(11));

        player.addResource(Resource.grain, Cost.CITY_GRAIN);
        assertEquals(player.getResourcesNumber(Resource.grain), Cost.CITY_GRAIN);
        assertNotNull(player.canBuyCity(11));

        player.addResource(Resource.ore, Cost.CITY_ORE);
        assertEquals(player.getResourcesNumber(Resource.ore), Cost.CITY_ORE);
        assertNotNull(player.canBuyCity(11));

        player.addRoad(new Road(new Intersection(10), new Intersection(11)));
        assertNull(player.canBuyCity(11));
        assertNull(player.buyCity());
    }

    @DisplayName("Check build city")
    @Test
    void buildCity() {
        Intersection city = new Intersection(10);
        player.buildCity(city);
        assertEquals(city.getOwner(), player);
        assertEquals(player.getCities().get(player.getCitiesNumber() - 1), city);
    }
}
