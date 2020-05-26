package catan.game.player;

import catan.API.response.Code;
import catan.game.enumeration.Development;
import catan.game.enumeration.Resource;
import catan.game.game.Game;
import catan.game.property.Intersection;
import catan.game.property.Road;
import catan.game.rule.Cost;
import catan.game.rule.VictoryPoint;
import catan.game.turn.TurnFlow;
import catan.util.Helper;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

public class Player {
    private final String id;
    private final Game game;
    private TurnFlow turnFlow;
    private boolean active;

    private Map<Resource, Integer> resources;
    private Map<Development, Integer> developments;

    private List<Road> roads;
    private List<Intersection> settlements;
    private List<Intersection> cities;

    private int resourcesToDiscard;
    private int usedKnights;
    private int roadsToBuild;

    private boolean hasLongestRoad;
    private boolean hasLargestArmy;

    private int publicVictoryPoints;
    private int hiddenVictoryPoints;

    public Player(String id, Game game) {
        this.id = id;
        this.game = game;
        try {
            turnFlow = new TurnFlow(game);
        } catch (IOException | SAXException | ParserConfigurationException exception) {
            exception.printStackTrace();
        }

        active = true;
        resources = new HashMap<>();
        resources.put(Resource.lumber, 0);
        resources.put(Resource.wool, 0);
        resources.put(Resource.grain, 0);
        resources.put(Resource.brick, 0);
        resources.put(Resource.ore, 0);

        developments = new HashMap<>();
        developments.put(Development.knight, 0);
        developments.put(Development.monopoly, 0);
        developments.put(Development.roadBuilding, 0);
        developments.put(Development.yearOfPlenty, 0);

        roads = new ArrayList<>();
        settlements = new ArrayList<>();
        cities = new ArrayList<>();

        resourcesToDiscard = 0;
        usedKnights = 0;
        roadsToBuild = 0;

        hasLongestRoad = false;
        hasLargestArmy = false;

        publicVictoryPoints = 0;
        hiddenVictoryPoints = 0;
    }

    // region Getters


    public String getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public TurnFlow getTurnFlow() {
        return turnFlow;
    }

    public boolean isActive() {
        return active;
    }

    public Map<Resource, Integer> getResources() {
        return resources;
    }

    public int getResourcesNumber(Resource resource) {
        return resources.get(resource);
    }

    public int getResourcesNumber() {
        int resourceNumber = 0;
        for (Resource resource : resources.keySet()) {
            resourceNumber += resources.get(resource);
        }
        return resourceNumber;
    }

    public Map<Development, Integer> getDevelopments() {
        return developments;
    }

    public int getDevelopmentsNumber(Development development) {
        return developments.get(development);
    }

    public List<Road> getRoads() {
        return roads;
    }

    public int getRoadsNumber() {
        return roads.size();
    }

    public List<Intersection> getSettlements() {
        return settlements;
    }

    public int getSettlementsNumber() {
        return settlements.size();
    }

    public List<Intersection> getCities() {
        return cities;
    }

    public int getCitiesNumber() {
        return cities.size();
    }

    public int getResourcesToDiscard() {
        return resourcesToDiscard;
    }

    public int getUsedKnights() {
        return usedKnights;
    }

    public int getRoadsToBuild() {
        return roadsToBuild;
    }

    public boolean hasLargestArmy() {
        return hasLargestArmy;
    }

    public boolean hasLongestRoad() {
        return hasLongestRoad;
    }

    public int getPublicVictoryPoints() {
        return publicVictoryPoints;
    }

    public int getHiddenVictoryPoints() {
        return hiddenVictoryPoints;
    }

    public int getVictoryPoints() {
        return publicVictoryPoints + hiddenVictoryPoints;
    }

    //endregion

    //region Setters


    public void setTurnFlow(TurnFlow turnFlow) {
        this.turnFlow = turnFlow;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setResources(Map<Resource, Integer> resources) {
        this.resources = resources;
    }

    public void setDevelopments(Map<Development, Integer> developments) {
        this.developments = developments;
    }

    public void setRoads(List<Road> roads) {
        this.roads = roads;
    }

    public void setSettlements(List<Intersection> settlements) {
        this.settlements = settlements;
    }

    public void setCities(List<Intersection> cities) {
        this.cities = cities;
    }

    public void setResourcesToDiscard(int resourcesToDiscard) {
        this.resourcesToDiscard = resourcesToDiscard;
    }

    public void setUsedKnights(int usedKnights) {
        this.usedKnights = usedKnights;
    }

    public void setRoadsToBuild(int roadsToBuild) {
        this.roadsToBuild = roadsToBuild;
    }

    public void setHasLongestRoad(boolean hasLongestRoad) {
        this.hasLongestRoad = hasLongestRoad;
    }

    public void setHasLargestArmy(boolean hasLargestArmy) {
        this.hasLargestArmy = hasLargestArmy;
    }

    public void setPublicVictoryPoints(int publicVictoryPoints) {
        this.publicVictoryPoints = publicVictoryPoints;
    }

    public void setHiddenVictoryPoints(int hiddenVictoryPoints) {
        this.hiddenVictoryPoints = hiddenVictoryPoints;
    }

    //endregion

    //region Checkers

    public boolean hasResource() {
        return getResourcesNumber() > 0;
    }

    public boolean hasResource(Resource resource, int resourceNumber) {
        return resources.get(resource) >= resourceNumber;
    }

    public boolean hasResource(Resource resource) {
        return hasResource(resource, 1);
    }

    public Code hasResources(Map<Resource, Integer> resourcesToVerify) {
        for (Resource resource : resourcesToVerify.keySet()) {
            if (!hasResource(resource, resourcesToVerify.get(resource))) {
                return Helper.getPlayerNotEnoughResourceFromResource(resource);
            }
        }
        return null;
    }

    public boolean hasDevelopment(Development development) {
        return getDevelopmentsNumber(development) > 0;
    }

    public boolean hasRoadWith(Intersection intersection) {
        for (Road road : roads) {
            if (road.getStart().equals(intersection)) {
                return true;
            }
            if (road.getEnd().equals(intersection)) {
                return true;
            }
        }
        return false;
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

    public void addDevelopment(Development development) {
        developments.put(development, developments.get(development) + 1);
    }

    public void addRoad(Road road) {
        roads.add(road);
    }

    public void addSettlement(Intersection settlement) {
        settlements.add(settlement);
    }

    public void addCity(Intersection city) {
        cities.add(city);
    }

    //endregion

    //region Remove

    public Code removeResource(Resource resource, int resourceNumber) {
        if (resourceNumber < 0) {
            return Code.InvalidRequest;
        }
        if (!hasResource(resource, resourceNumber)) {
            return Helper.getPlayerNotEnoughResourceFromResource(resource);
        }
        resources.put(resource, resources.get(resource) - resourceNumber);
        return null;
    }

    public Code removeResource(Resource resource) {
        if (!hasResource(resource)) {
            return Helper.getPlayerNoResourceFromResource(resource);
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
            } else if (resourceNumber > 1) {
                result = removeResource(resource, resourceNumber);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public Code removeDevelopment(Development development) {
        if (!hasDevelopment(development)) {
            return Helper.getPlayerNoDevelopmentFromDevelopment(development);
        }
        developments.put(development, developments.get(development) - 1);
        return null;
    }

    //endregion

    //region Steal

    public Resource getRandomResource() {
        Resource[] resources = {Resource.lumber, Resource.wool, Resource.grain, Resource.brick, Resource.ore};
        Random random = new Random();
        int index = random.nextInt(resources.length);
        while (getResourcesNumber(resources[index]) <= 0) {
            index = random.nextInt(resources.length);
        }
        return resources[index];
    }

    //endregion

    //region Development

    public Code canBuyDevelopment() {
        return hasDevelopmentResources();
    }

    public Code hasDevelopmentResources() {
        if (resources.get(Resource.wool) < Cost.DEVELOPMENT_WOOL) {
            return Code.PlayerNotEnoughWool;
        }
        if (resources.get(Resource.grain) < Cost.DEVELOPMENT_GRAIN) {
            return Code.PlayerNotEnoughGrain;
        }
        if (resources.get(Resource.ore) < Cost.DEVELOPMENT_ORE) {
            return Code.PlayerNotEnoughOre;
        }
        return null;
    }

    public Code buyDevelopment(Development development) {
        Code code = removeDevelopmentResources();
        if (code != null) {
            return code;
        }
        if (development == Development.victoryPoint) {
            ++hiddenVictoryPoints;
        } else {
            addDevelopment(development);
        }
        return null;
    }

    private Code removeDevelopmentResources() {
        Code code = removeResource(Resource.wool, Cost.DEVELOPMENT_WOOL);
        if (code != null) {
            return code;
        }
        code = removeResource(Resource.grain, Cost.DEVELOPMENT_GRAIN);
        if (code != null) {
            return code;
        }
        code = removeResource(Resource.ore, Cost.DEVELOPMENT_ORE);
        return code;
    }

    //endregion

    // region Road

    public Code canBuyRoad(int start, int end) {
        if (start == end) {
            return Code.InvalidRequest;
        }
        Code result = hasRoadResources();
        if (result != null) {
            return result;
        }
        result = connectsToIntersection(start, end);
        if (result == null) {
            return null;
        }
        return connectsToRoad(start, end);
    }

    public Code hasRoadResources() {
        if (resources.get(Resource.lumber) < Cost.ROAD_LUMBER) {
            return Code.PlayerNotEnoughLumber;
        }
        if (resources.get(Resource.brick) < Cost.ROAD_BRICK) {
            return Code.PlayerNotEnoughBrick;
        }
        return null;
    }

    public Code connectsToIntersection(int start, int end) {
        for (Intersection settlement : settlements) {
            if (start == settlement.getId() || end == settlement.getId()) {
                return null;
            }
        }

        for (Intersection city : cities) {
            if (start == city.getId() || end == city.getId()) {
                return null;
            }
        }
        return Code.NotConnectsToIntersection;
    }

    public Code connectsToRoad(int start, int end) {
        for (Road road : roads) {
            if (road.connectsToRoad(start, end)) {
                return null;
            }
        }
        return Code.NotConnectsToRoad;
    }

    public Code buyRoad() {
        return removeRoadResources();
    }

    public void buildRoad(Road road) {
        road.setOwner(this);
        addRoad(road);
    }

    private Code removeRoadResources() {
        Code code = removeResource(Resource.lumber, Cost.ROAD_LUMBER);
        if (code != null) {
            return code;
        }
        return removeResource(Resource.brick, Cost.ROAD_BRICK);
    }

    // endregion

    // region Settlement

    public Code canBuySettlement(int intersection) {
        Code code = hasSettlementResources();
        if (code != null) {
            return code;
        }
        return connectsToRoad(intersection);
    }

    public Code hasSettlementResources() {
        if (resources.get(Resource.lumber) < Cost.SETTLEMENT_LUMBER) {
            return Code.PlayerNotEnoughLumber;
        }
        if (resources.get(Resource.wool) < Cost.SETTLEMENT_WOOL) {
            return Code.PlayerNotEnoughWool;
        }
        if (resources.get(Resource.grain) < Cost.SETTLEMENT_GRAIN) {
            return Code.PlayerNotEnoughGrain;
        }
        if (resources.get(Resource.brick) < Cost.SETTLEMENT_BRICK) {
            return Code.PlayerNotEnoughBrick;
        }
        return null;
    }

    public Code connectsToRoad(int intersection) {
        for (Road road : roads) {
            if (road.connectsToRoad(intersection)) {
                return null;
            }
        }
        return Code.NotConnectsToRoad;
    }

    public Code buySettlement() {
        return removeSettlementResources();
    }

    public void buildSettlement(Intersection intersection) {
        intersection.setOwner(this);
        addSettlement(intersection);
        ++publicVictoryPoints;
    }

    private Code removeSettlementResources() {
        Code code = removeResource(Resource.lumber, Cost.SETTLEMENT_LUMBER);
        if (code != null) {
            return code;
        }
        code = removeResource(Resource.wool, Cost.SETTLEMENT_WOOL);
        if (code != null) {
            return code;
        }
        removeResource(Resource.grain, Cost.SETTLEMENT_GRAIN);
        return removeResource(Resource.brick, Cost.SETTLEMENT_BRICK);
    }

    // endregion

    // region City

    public Code canBuyCity(int intersection) {
        if (!haveSettlement(intersection)) {
            return Code.InvalidRequest;
        }
        Code result = hasCityResources();
        if (result != null) {
            return result;
        }
        return connectsToRoad(intersection);
    }

    private boolean haveSettlement(int intersection) {
        for (Intersection settlement : settlements) {
            if (settlement.getId() == intersection) {
                return true;
            }
        }
        return false;
    }

    public Code hasCityResources() {
        if (resources.get(Resource.grain) < Cost.CITY_GRAIN) {
            return Code.PlayerNotEnoughGrain;
        }
        if (resources.get(Resource.ore) < Cost.CITY_ORE) {
            return Code.PlayerNotEnoughOre;
        }
        return null;
    }

    public Code buyCity() {
        return removeCityResources();
    }

    public void buildCity(Intersection intersection) {
        removeSettlement(intersection);
        intersection.setOwner(this);
        addCity(intersection);
        publicVictoryPoints += 2;
    }

    private void removeSettlement(Intersection settlement) {
        settlements.remove(settlement);
    }

    private Code removeCityResources() {
        Code code = removeResource(Resource.grain, Cost.CITY_GRAIN);
        if (code != null) {
            return code;
        }
        return removeResource(Resource.ore, Cost.CITY_ORE);
    }

    // endregion

    //region Longest Road

    public void addLongestRoad() {
        hasLongestRoad = true;
        publicVictoryPoints += VictoryPoint.LONGEST_ROAD;
    }

    public void removeLongestRoad() {
        hasLongestRoad = false;
        publicVictoryPoints -= VictoryPoint.LONGEST_ROAD;
    }

    public int getLongestRoadLength() {
        int longestRoadLength = 0;
        roads.sort(Comparator.comparingInt(road -> road.getStart().getId()));
        List<Integer> longestRoadLengths = new ArrayList<>();
        for (int road = 0; road < roads.size(); road++) {
            longestRoadLengths.add(road, 1);
        }
        if (roads.size() > 0) {
            longestRoadLength = 1;
        }
        for (int road = 1; road < roads.size(); road++) {
            for (int previousRoad = road - 1; previousRoad >= 0; previousRoad--) {
                if (roads.get(road).getStart().getId() == roads.get(previousRoad).getEnd().getId()) {
                    longestRoadLengths.add(road, longestRoadLengths.get(previousRoad) + 1);
                    break;
                }
            }
            if (longestRoadLengths.get(road) > longestRoadLength)
                longestRoadLength = longestRoadLengths.get(road);
        }
        return longestRoadLength;
    }

    //endregion

    // region Largest Army

    public void addLargestArmy() {
        hasLargestArmy = true;
        publicVictoryPoints += VictoryPoint.LARGEST_ARMY;
    }

    public void removeLargestArmy() {
        hasLargestArmy = false;
        publicVictoryPoints -= VictoryPoint.LARGEST_ARMY;
    }

    // endregion

    //region Overrides

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Player)) {
            return false;
        }
        Player player = (Player) object;
        return Objects.equals(id, player.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                '}';
    }

    //endregion
}
