package catan.game.player;

import catan.API.response.Code;
import catan.game.turn.TurnFlow;
import catan.game.bank.Bank;
import catan.game.development.Knight;
import catan.game.development.Monopoly;
import catan.game.development.RoadBuilding;
import catan.game.development.YearOfPlenty;
import catan.game.enumeration.Resource;
import catan.game.game.Game;
import catan.game.property.Intersection;
import catan.game.property.Road;
import catan.game.rule.Component;
import catan.game.rule.Cost;
import catan.game.rule.VictoryPoint;
import catan.util.Helper;
import javafx.util.Pair;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

public class Player {

    /* Fields */

    private final String id;
    private final Game game;
    private Bank bank;
    private TurnFlow turnFlow;

    private List<Road> roads;
    private List<Intersection> settlements;
    private List<Intersection> cities;

    private Map<Resource, Integer> resources;
    private int knightsCounter;
    private int monopoliesCounter;
    private int roadBuildingsCounter;
    private int yearsOfPlentyCounter;

    public int getRoadsToBuildCounter() {
        return roadsToBuildCounter;
    }

    public void setRoadsToBuildCounter(int roadsToBuildCounter) {
        this.roadsToBuildCounter = roadsToBuildCounter;
    }

    private int roadsToBuildCounter=0;
    private int usedArmyCards;

    private boolean hasLongestRoad;
    private boolean hasLargestArmy;

    private int publicVP;
    private int hiddenVP;

    // region Constructors

    public Player(String id, Game game) {
        this.id = id;
        this.game = game;
        try {
            turnFlow = new TurnFlow(game);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        roads = new ArrayList<>();
        settlements = new ArrayList<>();
        cities = new ArrayList<>();

        resources = new HashMap<>();
        resources.put(Resource.ore, 0);
        resources.put(Resource.wool, 0);
        resources.put(Resource.grain, 0);
        resources.put(Resource.lumber, 0);
        resources.put(Resource.brick, 0);

        publicVP = 0;
        hiddenVP = 0;
        usedArmyCards = 0;
        hasLongestRoad = false;
        hasLargestArmy = false;
    }

    // endregion

    // region Getters

    public String getId() {
        return id;
    }

    public TurnFlow getState() {
        return turnFlow;
    }

    public List<Road> getRoads() {
        return roads;
    }

    public List<Intersection> getSettlements() {
        return settlements;
    }

    public boolean isHasLargestArmy() {
        return hasLargestArmy;
    }

    public boolean isHasLongestRoad() {
        return hasLongestRoad;
    }

    public List<Intersection> getCities() {
        return cities;
    }

    public Map<Resource, Integer> getResources() {
        return resources;
    }

    public int getResourceNumber(Resource resource) {
        return resources.get(resource);
    }

    public int getResourceNumber() {
        int resourceNumber = 0;
        for (Resource resource : resources.keySet()) {
            resourceNumber += resources.get(resource);
        }
        return resourceNumber;
    }

    public Resource stealResource(int resourceIndex) {
        int resourceNumber = 0;
        for (Resource resource : resources.keySet()) {
            resourceNumber += resources.get(resource);
            if (resourceNumber + resources.get(resource) > resourceIndex) {
                resources.put(resource, resources.get(resource) - 1);
                return resource;
            }
        }
        return null;
    }

    public int getKnightsCounter() {
        return knightsCounter;
    }

    public int getMonopoliesCounter() {
        return monopoliesCounter;
    }

    public int getRoadBuildingsCounter() {
        return roadBuildingsCounter;
    }

    public int getYearsOfPlentyCounter() {
        return yearsOfPlentyCounter;
    }

    public int getUsedKnights() {
        return usedArmyCards;
    }

    public int getPublicVP() {
        return publicVP;
    }

    public int getVictoryPoints() {
        return publicVP + hiddenVP;
    }

    // endregion

    // region Setters and Adders

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public void takeResource(Resource resource) {
        resources.put(resource, resources.get(resource) + 1);
    }

    public void takeResource(Resource resource, int resourceNumber) {
        resources.put(resource, resources.get(resource) + resourceNumber);
    }

    public void addKnight() {
        knightsCounter++;
    }

    public void addMonopolies() {
        monopoliesCounter++;
    }

    public void addRoadBuilding() {
        roadBuildingsCounter++;
    }

    public void addYearsOfPlenty() {
        yearsOfPlentyCounter++;
    }

    public void addVictoryPoint() {
        hiddenVP++;
    }

    // endregion

    //region Remove

    public Code removeResources(Map<Resource, Integer> playerResources) {
        for (Resource type : playerResources.keySet()) {
            int previousResources = resources.get(type);
            if (previousResources < playerResources.get(type))
                return Helper.getPlayerCodeFromResourceType(type);
            resources.put(type, previousResources - playerResources.get(type));
        }
        return null;
    }

    public boolean removeResource(Resource type) {
        int previousResources = resources.get(type);
        if (previousResources < 1) {
            return false;
        }
        resources.put(type, previousResources - 1);
        return true;
    }

    public boolean removeResource(Resource type, int resourceNumber) {
        int previousResources = resources.get(type);
        if (previousResources < resourceNumber) {
            return false;
        }
        resources.put(type, previousResources - resourceNumber);
        return true;
    }

    public Resource removeRandomResources() {
        Resource[] resources = Resource.values();
        Random random = new Random();
        int i = random.nextInt(resources.length);
        while (getResourceNumber(resources[i]) <= 0) {
            i = random.nextInt(resources.length);
        }
        if (removeResource(resources[i])) {
            return resources[i];
        }
        return null;
    }
    //endregion

    // region Road

    // TODO REMINDER: This is used by GAME class

    // Called only for the first two roads or when using RoadBuilding development.

    public boolean buyRoad(Road road) {
        if (!canBuildRoad(road)) {
            return false;
        }
        roads.add(road);
        removeRoadResources();
        return true;
    }

    public boolean addRoad(Road road){
        roads.add(road);
        return true;
    }

    // TODO REMINDER: The GAME class verifies if the id is free
    private boolean canBuildRoad(Road road) {
        if (!canBuildRoad()) {
            return false;
        }
        // Cazul de baza cand punem primele doua drumuri
        if (roads.size() < Component.INITIAL_FREE_ROADS) {
            for (Intersection intersection : settlements) {
                if (intersection.getId() == road.getStart().getId() || intersection.getId() == road.getEnd().getId()) {
                    return true;
                }
            }
        }
        // Adaugam un drum doar daca este adiacent cu altul.
        else {
            for (Road value : roads) {
                if (value.hasCommonIntersection(road)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canBuildRoad() {
        return roads.size() < Component.ROADS && hasRoadResources();
    }

    private boolean hasRoadResources() {
        return resources.get(Resource.lumber) >= Cost.ROAD_LUMBER
                && resources.get(Resource.brick) >= Cost.ROAD_BRICK;
    }

    private void removeRoadResources() {
        int bricks = resources.get(Resource.brick);
        int lumbers = resources.get(Resource.lumber);

        resources.put(Resource.brick, bricks - Cost.ROAD_BRICK);
        resources.put(Resource.lumber, lumbers - Cost.ROAD_LUMBER);
    }

    // endregion

    // region Settlement

    public boolean buildSettlement(Intersection settlement) {
        if (!canBuildSettlement(settlement)) {
            return false;
        }
        settlements.add(settlement);
        removeSettlementResources();
        publicVP++;
        return true;
    }
    public boolean placeSettlement(Intersection settlement){
        settlements.add(settlement);
        publicVP++;
        return true;
    }
    public boolean canBuildSettlement(Intersection settlement) {
        if (!canBuildSettlement()) {
            return false;
        }
        boolean isValid = false;

        if (settlements.size() < Component.INITIAL_FREE_SETTLEMENTS && cities.size() == 0) {
            return true;
        }
        // Adaugam doar la capatul unui drum.
        for (Road road : roads) {
            if (road.getStart().getId() == settlement.getId() || road.getEnd().getId() == settlement.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean canBuildSettlement() {
        return settlements.size() < Component.SETTLEMENTS && hasSettlementResources();
    }

    private boolean hasSettlementResources() {
        return resources.get(Resource.lumber) >= Cost.SETTLEMENT_LUMBER &&
                resources.get(Resource.grain) >= Cost.SETTLEMENT_GRAIN &&
                resources.get(Resource.brick) >= Cost.SETTLEMENT_BRICK &&
                resources.get(Resource.wool) >= Cost.SETTLEMENT_WOOL;
    }

    private void removeSettlementResources() {
        int lumbers = resources.get(Resource.lumber);
        int grains = resources.get(Resource.grain);
        int bricks = resources.get(Resource.brick);
        int wools = resources.get(Resource.wool);

        resources.put(Resource.lumber, lumbers - Cost.SETTLEMENT_LUMBER);
        resources.put(Resource.grain, grains - Cost.SETTLEMENT_GRAIN);
        resources.put(Resource.wool, wools - Cost.SETTLEMENT_WOOL);
        resources.put(Resource.brick, bricks - Cost.SETTLEMENT_BRICK);

    }

    // endregion

    // region City

    // TODO REMINDER: This is used by GAME class
    public boolean buildCity(Intersection city) {
        if (!canBuildCity(city)) {
            return false;
        }
        cities.add(city);
        // REMOVE SETTLEMENT
        for (Intersection settlement : settlements) {
            if (settlement.getId() == city.getId()) {
                settlements.remove(settlement);
                break;
            }
        }
        publicVP++;
        removeCityResources();
        return true;
    }

    // TODO REMINDER: The GAME class verifies if the id is possessed by the player.
    public boolean canBuildCity(Intersection city) {
        if (!canBuildCity()) {
            return false;
        }
        for (Intersection settlement : settlements) {
            if (settlement.getId() == city.getId())
                return true;
        }
        return false;
    }

    public boolean canBuildCity() {
        return cities.size() < Component.CITIES && hasCityResources();
    }

    private boolean hasCityResources() {
        return resources.get(Resource.ore) >= Cost.CITY_ORES &&
                resources.get(Resource.grain) >= Cost.CITY_GRAINS;
    }

    private void removeCityResources() {
        int ores = resources.get(Resource.ore);
        int grains = resources.get(Resource.grain);

        resources.put(Resource.ore, ores - Cost.CITY_ORES);
        resources.put(Resource.grain, grains - Cost.CITY_GRAINS);

    }

    // endregion

    // region Trade


    public boolean hasResources(Map<Resource, Integer> resources) {
        for (Resource resource : resources.keySet()) {
            if (this.resources.get(resource) < resources.get(resource))
                return false;
        }
        return true;
    }

    public boolean startTrade(Map<Resource, Integer> offer, Map<Resource, Integer> request) {
        if (hasResources(offer)) {
            game.setTradeOffer(offer);
            game.setTradeRequest(request);
            return true;
        }
        return false;
    }


    public boolean wantToTrade(String answer) {
        if (hasResources(game.getTradeRequest())) {
            return answer.equalsIgnoreCase("yes");
        }
        return false;
    }

    public boolean selectOpponent(String playerId) {
        if (playerId != null) {
            //TODO: Move offer to specified player.
            //TODO: Move request to current player.
            return true;
        }
        return false;
    }

    public void updateTradeResources(List<Pair<Resource, Integer>> offer,
                                     List<Pair<Resource, Integer>> request) {
        for (Pair<Resource, Integer> pair : offer) {
            Resource resource = pair.getKey();
            Integer resourceCount = resources.get(resource);
            resources.put(resource, resourceCount - pair.getValue());
        }
        for (Pair<Resource, Integer> pair : request) {
            Resource type = pair.getKey();
            Integer resourceCount = resources.get(type);
            resources.put(type, resourceCount + pair.getValue());
        }
    }

    // endregion

    //region Longest Road

    public int getBuiltRoads() {
        int maxRoadLength = 0;
        roads.sort(new Comparator<Road>() {
            @Override
            public int compare(Road road1, Road road2) {
                return road1.getStart().getId() - road2.getStart().getId();
            }
        });
        List<Integer> roadsMax = new ArrayList<>(roads.size());
        for(int i=0;i<roads.size();i++)
            roadsMax.add(i,1);

        for (int i = 1; i < roads.size(); i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (roads.get(i).getStart().getId() == roads.get(j).getEnd().getId()) {
                    roadsMax.add(i, roadsMax.get(j) + 1);
                    break;
                }
            }
            if (roadsMax.get(i) > maxRoadLength)
                maxRoadLength = roadsMax.get(i);
        }
        return maxRoadLength;
    }
    //endregion

    // region Bonus Victory Points

    public void takeLongestRoad() {
        hasLongestRoad = true;
        publicVP += VictoryPoint.LONGEST_ROAD;
    }

    public void giveLongestRoad() {
        hasLongestRoad = false;
        publicVP -= VictoryPoint.LONGEST_ROAD;
    }

    public void takeLargestArmy() {
        hasLargestArmy = true;
        publicVP += VictoryPoint.LARGEST_ARMY;
    }

    public void giveLargestArmy() {
        hasLargestArmy = false;
        publicVP -= VictoryPoint.LARGEST_ARMY;
    }

    // endregion


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return Objects.equals(getId(), player.getId());
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

    public void addResource(Resource resource) {
        int resourceNumber=resources.get(resource)+1;
        resources.put(resource,1);
    }
}