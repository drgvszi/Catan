package test;

import catan.game.bank.Bank;
import catan.game.enumeration.Development;
import catan.game.enumeration.Resource;
import catan.game.game.BaseGame;
import catan.game.game.Game;
import catan.game.player.Player;
import catan.game.rule.Component;
import catan.game.rule.Cost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    private Game game;
    private List<Player> players;
    private Bank bank;

    public BankTest() {
        game = new BaseGame();
        players = new ArrayList<>();
        players.add(new Player("Marian", game));
        players.add(new Player("Delia", game));
        players.add(new Player("Cristian", game));
        players.add(new Player("Simona", game));
        bank = new Bank(players);
    }

    @DisplayName("Check create bank")
    @Test
    public void createBank() {
        //region Cards

        for (Resource resource : Resource.values()) {
            if (resource != Resource.desert) {
                assertTrue(bank.hasResource(resource, Component.RESOURCES_BY_TYPE));
            }
        }

        assertTrue(bank.hasDevelopment(Development.knight, Component.KNIGHTS));
        assertTrue(bank.hasDevelopment(Development.monopoly, Component.MONOPOLIES));
        assertTrue(bank.hasDevelopment(Development.roadBuilding, Component.ROAD_BUILDINGS));
        assertTrue(bank.hasDevelopment(Development.victoryPoint, Component.VICTORY_POINTS));
        assertTrue(bank.hasDevelopment(Development.yearOfPlenty, Component.YEARS_OF_PLENTY));

        //endregion

        //region Properties

        for (Player player : players) {
            assertEquals(bank.getRoadsNumber(player), Component.ROADS);
            assertEquals(bank.getSettlementsNumber(player), Component.SETTLEMENTS);
            assertEquals(bank.getCitiesNumber(player), Component.CITIES);
        }

        // end region
    }

    @DisplayName("Check remove cards")
    @Test
    public void removeCards() {
        //region Resources

        assertNull(bank.removeResource(Resource.lumber));
        assertNull(bank.removeResource(Resource.wool));
        assertNull(bank.removeResource(Resource.grain));
        assertNull(bank.removeResource(Resource.brick));
        assertNull(bank.removeResource(Resource.ore));

        for (Resource resource : Resource.values()) {
            if (resource != Resource.desert) {
                assertFalse(bank.hasResource(resource, Component.RESOURCES_BY_TYPE));
            }
        }

        //endregion

        //region Developments

        assertNull(bank.removeDevelopment(Development.knight));
        assertNull(bank.removeDevelopment(Development.monopoly));
        assertNull(bank.removeDevelopment(Development.roadBuilding));
        assertNull(bank.removeDevelopment(Development.victoryPoint));
        assertNull(bank.removeDevelopment(Development.yearOfPlenty));

        assertFalse(bank.hasDevelopment(Development.knight, Component.KNIGHTS));
        assertFalse(bank.hasDevelopment(Development.monopoly, Component.MONOPOLIES));
        assertFalse(bank.hasDevelopment(Development.roadBuilding, Component.ROAD_BUILDINGS));
        assertFalse(bank.hasDevelopment(Development.victoryPoint, Component.VICTORY_POINTS));
        assertFalse(bank.hasDevelopment(Development.yearOfPlenty, Component.YEARS_OF_PLENTY));

        //endregion
    }

    @DisplayName("Check remove properties")
    @Test
    public void removeProperties() {
        for (Player player : players) {
            for (int index = 0; index < Component.ROADS; ++index) {
                assertNull(bank.removeRoad(player));
            }
            for (int index = 0; index < Component.SETTLEMENTS; ++index) {
                assertNull(bank.removeSettlement(player));
            }
            for (int index = 0; index < Component.CITIES; ++index) {
                assertNull(bank.removeCity(player));
            }
        }

        for (Player player : players) {
            assertFalse(bank.hasRoad(player));
            assertFalse(bank.hasSettlement(player));
            assertFalse(bank.hasCity(player));
        }
    }

    @DisplayName("Check sell developments")
    @Test
    public void sellDevelopments() {
        assertNull(bank.sellDevelopment(Development.knight));

        assertTrue(bank.hasResource(Resource.lumber, Component.RESOURCES_BY_TYPE));
        assertTrue(bank.hasResource(Resource.wool, Component.RESOURCES_BY_TYPE + Cost.DEVELOPMENT_WOOL));
        assertTrue(bank.hasResource(Resource.grain, Component.RESOURCES_BY_TYPE + Cost.DEVELOPMENT_GRAIN));
        assertTrue(bank.hasResource(Resource.brick, Component.RESOURCES_BY_TYPE));
        assertTrue(bank.hasResource(Resource.ore, Component.RESOURCES_BY_TYPE + Cost.DEVELOPMENT_ORE));
    }

    @DisplayName("Check sell properties")
    @Test
    public void sellProperties() {
        //region Road

        assertNull(bank.sellRoad(players.get(0)));

        assertTrue(bank.hasResource(Resource.lumber, Component.RESOURCES_BY_TYPE + Cost.ROAD_LUMBER));
        assertTrue(bank.hasResource(Resource.wool, Component.RESOURCES_BY_TYPE));
        assertTrue(bank.hasResource(Resource.grain, Component.RESOURCES_BY_TYPE));
        assertTrue(bank.hasResource(Resource.brick, Component.RESOURCES_BY_TYPE + Cost.ROAD_BRICK));
        assertTrue(bank.hasResource(Resource.ore, Component.RESOURCES_BY_TYPE));

        //endregion

        //region Settlement

        assertNull(bank.sellSettlement(players.get(0)));

        assertTrue(bank.hasResource(Resource.lumber, Component.RESOURCES_BY_TYPE + Cost.SETTLEMENT_LUMBER));
        assertTrue(bank.hasResource(Resource.wool, Component.RESOURCES_BY_TYPE + Cost.SETTLEMENT_WOOL));
        assertTrue(bank.hasResource(Resource.grain, Component.RESOURCES_BY_TYPE + Cost.SETTLEMENT_GRAIN));
        assertTrue(bank.hasResource(Resource.brick, Component.RESOURCES_BY_TYPE + Cost.SETTLEMENT_BRICK));
        assertTrue(bank.hasResource(Resource.ore, Component.RESOURCES_BY_TYPE));

        //endregion

        //region City

        assertNull(bank.sellCity(players.get(0)));

        assertTrue(bank.hasResource(Resource.lumber, Component.RESOURCES_BY_TYPE));
        assertTrue(bank.hasResource(Resource.wool, Component.RESOURCES_BY_TYPE));
        assertTrue(bank.hasResource(Resource.grain, Component.RESOURCES_BY_TYPE + Cost.CITY_GRAIN));
        assertTrue(bank.hasResource(Resource.brick, Component.RESOURCES_BY_TYPE));
        assertTrue(bank.hasResource(Resource.ore, Component.RESOURCES_BY_TYPE + Cost.CITY_ORE));

        //endregion
    }
}
