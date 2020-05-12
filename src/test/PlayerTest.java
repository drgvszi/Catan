package test;

import catan.game.enumeration.Development;
import catan.game.enumeration.Resource;
import catan.game.game.BaseGame;
import catan.game.player.Player;
import catan.game.property.Intersection;
import catan.game.property.Road;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    BaseGame game;
    String id;
    Player player;

    public PlayerTest() {
        game = new BaseGame();
        id = "Gigel";
        player = new Player(id, game);
    }

    @DisplayName("Check create player")
    @Test
    public void createPlayer() {
        Map<Resource, Integer> resources = player.getResources();
        for (Map.Entry<Resource, Integer> resource : resources.entrySet()) {
            assertEquals(resource.getValue(), 0);
        }
        Map<Development, Integer> developments = player.getDevelopments();
        for (Map.Entry<Development, Integer> development : developments.entrySet()) {
            assertEquals(development.getValue(), 0);
        }
        List<Intersection> cities = player.getCities();
        assertEquals(cities.size(), 0);
        List<Intersection> settlements = player.getSettlements();
        assertEquals(settlements.size(), 0);
        List<Road> roads = player.getRoads();
        assertEquals(roads.size(), 0);
        assertEquals(player.getUsedKnights(), 0);
        assertEquals(player.getRoadsToBuild(), 0);
        assertEquals(player.getPublicVictoryPoints(), 0);
        assertEquals(player.getHiddenVictoryPoints(), 0);
        assertFalse(player.hasLongestRoad());
        assertFalse(player.hasLargestArmy());
    }

    @DisplayName("Check buy development")
    @Test
    void buyDevelopment(){
        player.addResource(Resource.grain);
        assertEquals(player.getResourcesNumber(Resource.grain),1);
        assertNotNull(player.buyDevelopment(Development.roadBuilding));
        player.addResource(Resource.wool);
        assertEquals(player.getResourcesNumber(Resource.wool),1);
        assertNotNull(player.buyDevelopment(Development.knight));
        player.addResource(Resource.ore);
        assertEquals(player.getResourcesNumber(Resource.ore),1);
        assertNull(player.buyDevelopment(Development.monopoly));
    }

    @DisplayName("Checks connect to road")
    @Test
    void connectsToRoad(){
        Intersection int1 = new Intersection(3);
        Intersection int2 = new Intersection(4);
        Road road1 = new Road(int1, int2);
        player.addRoad(road1);
        assertNotNull(player.connectsToRoad(5,6));
        assertNull(player.connectsToRoad(4,5));
    }

    @DisplayName("Checks connect to road Intersection")
    @Test
    void connectsToRoadMap(){
        Intersection int1 = new Intersection(3);
        Intersection int2 = new Intersection(4);
        Road road1 = new Road(int1, int2);
        player.addRoad(road1);
        assertNull(player.connectsToRoad(3));
        assertNotNull(player.connectsToRoad(10));
    }

    @DisplayName("Check can buy road")
    @Test
    void canBuyRoad(){
        Intersection int1 = new Intersection(3);
        Intersection int2 = new Intersection(4);
        Road road1 = new Road(int1, int2);
        player.addRoad(road1);
        assertNotNull(player.canBuyRoad(4,5));
        player.addResource(Resource.lumber);
        player.addResource(Resource.brick);
        assertNotNull(player.canBuyRoad(5,6));
        assertNull(player.canBuyRoad(4,5));
    }

    @DisplayName("Check buy road")
    @Test
    void buyRoad(){
        player.addResource(Resource.lumber);
        player.addResource(Resource.brick);
        player.buyRoad();
        assertEquals(player.getResourcesNumber(Resource.lumber),0);
        assertEquals(player.getResourcesNumber(Resource.lumber),0);
    }

    @DisplayName("Check build road")
    @Test
    void buildRoad(){
        Intersection int1 = new Intersection(3);
        Intersection int2 = new Intersection(4);
        Road road1 = new Road(int1, int2);
        player.buildRoad(road1);
        assertEquals(road1.getOwner(), player);
        assertEquals(player.getRoads().get(player.getRoads().size()-1),road1);
    }

    @DisplayName("Verificam ca atunci cand un player este initializat acesta sa aiba atributele necesare")
    @Test
    void TestAddAndRemove() {
        BaseGame game = new BaseGame();
        Player player = new Player("Toderas", game);
        Development development = Development.victoryPoint;

        //add resources
        for (Resource resource : Resource.values()) {
            if(!resource.toString().equalsIgnoreCase("desert"))
            {
                player.addResource(resource, 4);
                assertEquals(4, player.getResourcesNumber(resource));
            }
        }
        // development functions
        assertNull( player.canBuyDevelopment() );
        assertNull( player.buyDevelopment(development) );

        //get hiddenVP
        assertEquals(1,player.getHiddenVictoryPoints());

        //remove function
        assertEquals(3, player.getResourcesNumber(Resource.wool));
        assertEquals(3, player.getResourcesNumber(Resource.grain));
        assertEquals(3, player.getResourcesNumber(Resource.ore));

        // add development
        development = Development.monopoly;
        assertNull( player.buyDevelopment(development) );
        assertEquals(1,player.getDevelopmentsNumber(development));

        // add road/settlements/cities
        Road road = new Road(new Intersection(22),new Intersection(23));
        player.addRoad(road);
        assertEquals(1,player.getRoadsNumber() );

        player.addSettlement(new Intersection(22) );
        player.addCity(new Intersection(23) );

        assertEquals(1,player.getCitiesNumber() );
        assertEquals(1,player.getSettlementsNumber() );
    }

    @DisplayName("Verificarea metodelor din settlement region")
    @Test
    void TestSettlements(){
        BaseGame game = new BaseGame();
        Player player = new Player("Toderas", game);

        for (Resource resource : Resource.values()) {
            if(!resource.toString().equalsIgnoreCase("desert"))
            {
                player.addResource(resource, 4);
                assertEquals(4, player.getResourcesNumber(resource));
            }
        }

        player.addRoad(new Road(new Intersection(22),new Intersection(23)));
        assertNull(player.canBuySettlement(23));
        assertNull(player.buySettlement());
    }

    @DisplayName("Verificare metodele pentru cities")
    @Test
    void TestCities(){
        BaseGame game = new BaseGame();
        Player player = new Player("Toderas", game);

        for (Resource resource : Resource.values()) {
            if(!resource.toString().equalsIgnoreCase("desert"))
            {
                player.addResource(resource, 4);
                assertEquals(4, player.getResourcesNumber(resource));
            }
        }
        player.addRoad(new Road(new Intersection(22),new Intersection(23)));
        player.addSettlement(new Intersection(22));
        assertNull(player.canBuyCity(22));
        player.buildCity(new Intersection(22));
        assertEquals(2,player.getVictoryPoints());

    }

}
