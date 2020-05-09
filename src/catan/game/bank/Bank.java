package catan.game.bank;

import catan.API.response.Code;
import catan.game.player.Player;
import catan.game.development.*;
import catan.game.enumeration.Resource;
import catan.game.property.Intersection;
import catan.game.rule.Component;
import catan.util.Helper;

import java.util.*;

public class Bank {
    private List<Player> players;
    private Map<Resource, Integer> resources;
    private int knightsCounter;
    private int monopoliesCounter;
    private int roadBuildingsCounter;
    private int victoryPointsCounter;
    private int yearsOfPlentyCounter;
    private Map<Player, Integer> roads;
    private Map<Player, Integer> settlements;
    private Map<Player, Integer> cities;

    public Bank(List<Player> players) {
        this.players = players;
        createResources();
        createProperties();
    }

    public boolean existsResource(Resource resource, int resourceNumber) {
        return resources.get(resource) >= resourceNumber;
    }

    public boolean existsResource(Resource resource) {
        return existsResource(resource, 1);
    }

    public boolean hasRoad(Player player) {
        return roads.get(player) > 0;
    }

    public boolean hasSettlement(Player player) {
        return settlements.get(player) > 0;
    }

    public boolean hasCity(Player player) {
        return settlements.get(player) > 0;
    }

    public int getNumberOfRoads(Player player){
        return roads.get(player);
    }

    public Code takeResource(Resource resource, int resourceNumber) {
        int oldResourceNumber = resources.get(resource);
        if (existsResource(resource, resourceNumber)) {
            resources.put(resource, oldResourceNumber - resourceNumber);
            return Helper.getBankCodeFromResourceType(resource);
        }
        return null;
    }

    public Code takeResource(Resource resource) {
        return takeResource(resource, 1);
    }

    public Code takeResources(Map<Resource, Integer> resources) {
        for (Resource resource : resources.keySet()) {
            Code code = takeResource(resource, resources.get(resource));
            if (code != null) {
                return code;
            }
        }
        return null;
    }

    public boolean takeKnight(Player player) {
        if (knightsCounter == 0) {
            return false;
        }
        knightsCounter--;

        return true;
    }

    public boolean takeMonopoly(Player player) {
        if (monopoliesCounter == 0) {
            return false;
        }
        monopoliesCounter--;
        return true;
    }

    public boolean takeRoadBuilding(Player player) {
        if (roadBuildingsCounter == 0) {
            return false;
        }
        roadBuildingsCounter--;
        return true;
    }

    public boolean takeVictoryPoint(Player player) {
        if (victoryPointsCounter == 0) {
            return false;
        }
        victoryPointsCounter--;
        return true;
    }

    public boolean takeYearOfPlenty(Player player) {
        if (yearsOfPlentyCounter == 0) {
            return false;
        }
        yearsOfPlentyCounter--;
        return true;
    }

    public Code takeRoad(Player player) {
        int playerRoads = roads.get(player);
        if (playerRoads == 0) {
            return Code.NoRoad;
        }
        roads.put(player, --playerRoads);
        return null;
    }

    public Code takeSettlement(Player player) {
        int playerSettlements = settlements.get(player);
        if (playerSettlements == 0) {
            return Code.NoSettlement;
        }
        settlements.put(player, --playerSettlements);
        return null;
    }

    public Code takeCity(Player player) {
        int playerCities = cities.get(player);
        if (playerCities == 0) {
            return Code.NoCity;
        }
        cities.put(player, --playerCities);
        return null;
    }

    public void giveResource(Resource resource, int resourceNumber) {
        int oldResourceNumber = resources.get(resource);
        resources.put(resource, oldResourceNumber + resourceNumber);
    }

    public void giveResource(Resource resource) {
        giveResource(resource, 1);
    }

    public void giveResources(Map<Resource, Integer> resources) {
        for (Resource resource : resources.keySet()) {
            giveResource(resource, resources.get(resource));
        }
    }

    public void giveSettlement(Player player, Intersection intersection) {
        settlements.put(player, settlements.get(player) + 1);
    }

    private void createResources() {
        resources = new HashMap<>(5);
        for (Resource resource : Resource.values()) {
            resources.put(resource, Component.RESOURCES_BY_TYPE);
        }
    }



    private void createProperties() {
        createRoads();
        createSettlements();
        createCities();
    }

    private void createRoads() {
        roads = new HashMap<>();
        for (Player player : players) {
            roads.put(player, Component.ROADS);
        }
    }

    private void createSettlements() {
        settlements = new HashMap<>();
        for (Player player : players) {
            settlements.put(player, Component.SETTLEMENTS);
        }
    }

    private void createCities() {
        cities = new HashMap<>();
        for (Player player : players) {
            cities.put(player, Component.CITIES);
        }
    }
}
