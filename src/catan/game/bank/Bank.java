package catan.game.bank;

import catan.API.response.Code;
import catan.game.enumeration.Development;
import catan.game.player.Player;
import catan.game.enumeration.Resource;
import catan.game.rule.Component;
import catan.util.Helper;

import java.util.*;

public class Bank {
    private List<Player> players;

    private Map<Resource, Integer> resources;
    private Map<Development, Integer> developments;

    private Map<Player, Integer> roads;
    private Map<Player, Integer> settlements;
    private Map<Player, Integer> cities;

    public Bank(List<Player> players) {
        this.players = players;
        createResources();
        createDevelopments();
        createProperties();
    }

    //region Getters

    public List<Player> getPlayers() {
        return players;
    }

    public Map<Resource, Integer> getResources() {
        return resources;
    }

    public Map<Development, Integer> getDevelopments() {
        return developments;
    }

    public Map<Player, Integer> getRoads() {
        return roads;
    }

    public int getRoadsNumber(Player player) {
        return roads.get(player);
    }

    public Map<Player, Integer> getSettlements() {
        return settlements;
    }

    public Map<Player, Integer> getCities() {
        return cities;
    }

    //endregion

    //region Setters

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setResources(Map<Resource, Integer> resources) {
        this.resources = resources;
    }

    public void setDevelopments(Map<Development, Integer> developments) {
        this.developments = developments;
    }

    public void setRoads(Map<Player, Integer> roads) {
        this.roads = roads;
    }

    public void setSettlements(Map<Player, Integer> settlements) {
        this.settlements = settlements;
    }

    public void setCities(Map<Player, Integer> cities) {
        this.cities = cities;
    }

    //endregion

    //region Checkers

    public boolean hasResource(Resource resource, int resourceNumber) {
        return resources.get(resource) >= resourceNumber;
    }

    public boolean hasResource(Resource resource) {
        return hasResource(resource, 1);
    }

    public boolean hasResources(Map<Resource, Integer> resourcesToVerify) {
        for (Resource resource : resourcesToVerify.keySet()) {
            if (resources.get(resource) < resourcesToVerify.get(resource))
                return false;
        }
        return true;
    }

    public boolean hasDevelopment(Development development) {
        return developments.get(development) > 0;
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

    //endregion

    //region Add

    public void addResource(Resource resource) {
        resources.put(resource, resources.get(resource) + 1);
    }

    public void addResource(Resource resource, int resourceNumber) {
        resources.put(resource, resources.get(resource) + resourceNumber);
    }

    public void addResources(Map<Resource, Integer> resources) {
        for (Resource resource : resources.keySet()) {
            addResource(resource, resources.get(resource));
        }
    }

    public void addSettlement(Player player) {
        settlements.put(player, settlements.get(player) + 1);
    }

    //endregion

    //region Remove

    public Code removeResource(Resource resource, int resourceNumber) {
        if (resourceNumber < 0) {
            return Code.InvalidRequest;
        }
        if (!hasResource(resource, resourceNumber)) {
            return Helper.getBankNotEnoughResourceFromResource(resource);
        }
        resources.put(resource, resources.get(resource) - resourceNumber);
        return null;
    }

    public Code removeResource(Resource resource) {
        if (!hasResource(resource)) {
            return Helper.getBankNoResourceFromResource(resource);
        }
        resources.put(resource, resources.get(resource) - 1);
        return null;
    }

    public Code removeResources(Map<Resource, Integer> resourcesToRemove) {
        Code result;
        for (Resource resource : resourcesToRemove.keySet()) {
            int resourceNumber = resourcesToRemove.get(resource);
            if (resourceNumber < 0) {
                return Code.InvalidRequest;
            }
            if (resourceNumber == 1) {
                result = removeResource(resource);
                if (result != null) {
                    return result;
                }
            }
            else if (resourceNumber > 1) {
                result = removeResource(resource, resourceNumber);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public Code removeDevelopment(Development development, Player player) {
        if (!hasDevelopment(development)) {
            return Helper.getBankNoDevelopmentFromDevelopment(development);
        }
        developments.put(development, developments.get(development) - 1);
        return null;
    }

    public Code removeRoad(Player player) {
        if (!hasRoad(player)) {
            return Code.BankNoRoad;
        }
        roads.put(player, roads.get(player) - 1);
        return null;
    }

    public Code removeSettlement(Player player) {
        if (!hasSettlement(player)) {
            return Code.BankNoSettlement;
        }
        settlements.put(player, settlements.get(player) - 1);
        return null;
    }

    public Code removeCity(Player player) {
        if (!hasCity(player)) {
            return Code.BankNoCity;
        }
        cities.put(player, cities.get(player) - 1);
        return null;
    }

    //endregion

    //region Create

    private void createResources() {
        resources = new HashMap<>();
        for (Resource resource : Resource.values()) {
            resources.put(resource, Component.RESOURCES_BY_TYPE);
        }
    }

    private void createDevelopments() {
        developments = new HashMap<>();
        developments.put(Development.knight, Component.KNIGHTS);
        developments.put(Development.monopoly, Component.MONOPOLIES);
        developments.put(Development.roadBuilding, Component.ROAD_BUILDINGS);
        developments.put(Development.yearOfPlenty, Component.YEARS_OF_PLENTY);
        developments.put(Development.victoryPoint, Component.VICTORY_POINTS);
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

    //endregion
}
