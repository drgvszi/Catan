package catan.game.game;

import catan.API.response.Code;
import catan.API.response.GameResponse;
import catan.API.response.Messages;
import catan.API.response.UserResponse;
import catan.game.development.Knight;
import catan.game.development.Monopoly;
import catan.game.development.RoadBuilding;
import catan.game.development.YearOfPlenty;
import catan.game.player.Player;
import catan.game.board.Board;
import catan.game.board.Tile;
import catan.game.bank.Bank;
import catan.game.enumeration.Building;
import catan.game.enumeration.Resource;
import catan.game.property.Intersection;
import catan.game.property.Road;
import catan.game.rule.Component;
import catan.game.rule.VictoryPoint;
import catan.util.Helper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.ankzz.dynamicfsm.fsm.FSM;
import javafx.util.Pair;
import org.apache.http.HttpStatus;

import java.util.*;

public abstract class Game {
    protected Bank bank;
    protected Board board;
    protected Map<String, Player> players;
    protected List<String> playerOrder;
    protected int maxPlayers;
    protected String currentPlayer;
    protected Pair<String, Integer> currentLargestArmy;
    protected Pair<String, Integer> currentLongestRoad;
    protected Map<Resource, Integer> tradeOffer;
    protected Map<Resource, Integer> tradeRequest;
    protected List<String> tradeOpponents;
    protected String tradeOpponent;
    protected boolean notDiscardedAll;
    protected boolean inversion;
    private Deque<GameResponse> logs=new ArrayDeque<>();

    public Game() {
        bank = null;
        board = new Board();
        players = new HashMap<>();
        playerOrder = new ArrayList<>();
        maxPlayers = 0;
        currentPlayer = null;
        inversion = false;
        currentLargestArmy = null;
        currentLongestRoad = null;
        tradeOffer = null;
        tradeRequest = null;
        notDiscardedAll = false;
    }

    //region Getters

    public Deque<GameResponse> getLogs() {
        return logs;
    }

    public Bank getBank() {
        return bank;
    }

    public Board getBoard() {
        return board;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public List<String> getPlayerOrder() {
        return playerOrder;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getNoPlayers() {
        return players.size();
    }

    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }


    public Pair<String, Integer> getCurrentLargestArmy() {
        return currentLargestArmy;
    }

    public Pair<String, Integer> getCurrentLongestRoad() {
        return currentLongestRoad;
    }

    public Map<Resource, Integer> getTradeOffer() {
        return tradeOffer;
    }

    public Map<Resource, Integer> getTradeRequest() {
        return tradeRequest;
    }

    public List<String> getTradeOpponents() {
        return tradeOpponents;
    }

    public String getTradeOpponent() {
        return tradeOpponent;
    }

    public boolean isNotDiscardedAll() {
        return notDiscardedAll;
    }

    public boolean isInversion() {
        return inversion;
    }
    //endregion

    //region Setters


    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }

    public void setPlayerOrder(List<String> playerOrder) {
        this.playerOrder = playerOrder;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setCurrentLargestArmy(Pair<String, Integer> currentLargestArmy) {
        this.currentLargestArmy = currentLargestArmy;
    }

    public void setCurrentLongestRoad(Pair<String, Integer> currentLongestRoad) {
        this.currentLongestRoad = currentLongestRoad;
    }

    public void setTradeOffer(Map<Resource, Integer> tradeOffer) {
        this.tradeOffer = tradeOffer;
    }

    public void setTradeRequest(Map<Resource, Integer> tradeRequest) {
        this.tradeRequest = tradeRequest;
    }

    public void setTradeOpponents(List<String> tradeOpponents) {
        this.tradeOpponents = tradeOpponents;
    }

    public void setTradeOpponent(String tradeOpponent) {
        this.tradeOpponent = tradeOpponent;
    }

    public void setNotDiscardedAll(boolean notDiscardedAll) {
        this.notDiscardedAll = notDiscardedAll;
    }

    public void setInversion() {
        this.inversion = !inversion;
    }
    //endregion

    //region Turn

    public void addPlayer(String playerId, Player player) {
        players.put(playerId, player);
    }

    public void addNextPlayer(String playerId) {
        playerOrder.add(playerId);
    }

    public void changeTurn(int direction) {
        updateBonusPoints();
        if (currentPlayerWon()) {
            //TODO: End game and show ranking.
        }
        int nextPlayer = (playerOrder.indexOf(currentPlayer) + direction) % playerOrder.size();
        currentPlayer = playerOrder.get(nextPlayer);
    }

    protected void updateBonusPoints() {
        // Update largest army.
        int usedKnights = players.get(currentPlayer).getUsedKnights();
        if (currentLargestArmy == null) {
            if (usedKnights >= Component.KNIGHTS_FOR_LARGEST_ARMY) {
                players.get(currentPlayer).takeLargestArmy();
                currentLargestArmy = new Pair<>(currentPlayer, usedKnights);
            }
        } else if (usedKnights > currentLargestArmy.getValue() &&
                !(currentPlayer.equals(currentLargestArmy.getKey()))) {
            players.get(currentLargestArmy.getKey()).giveLargestArmy();
            players.get(currentPlayer).takeLargestArmy();
            currentLargestArmy = new Pair<>(currentPlayer, usedKnights);
        }

        // Update largest army.
        int builtRoads = players.get(currentPlayer).getBuiltRoads();
        if (currentLongestRoad == null) {
            if (builtRoads >= Component.ROADS_FOR_LONGEST_ROAD) {
                players.get(currentPlayer).takeLongestRoad();
                currentLongestRoad = new Pair<>(currentPlayer, builtRoads);
            }
        } else if (builtRoads > currentLongestRoad.getValue() &&
                !(currentPlayer.equals(currentLongestRoad.getKey()))) {
            players.get(currentLongestRoad.getKey()).giveLongestRoad();
            players.get(currentPlayer).takeLongestRoad();
            currentLongestRoad = new Pair<>(currentPlayer, builtRoads);
        }
    }

    protected boolean currentPlayerWon() {
        return players.get(currentPlayer).getVictoryPoints() >= VictoryPoint.FINISH_VICTORY_POINTS;
    }

    public UserResponse playTurn(String playerId, String command, Map<String, Object> requestArguments)
            throws JsonProcessingException {
        UserResponse otherResponse = processOtherCommand(playerId, command, requestArguments);
        if (otherResponse != null) {
            return otherResponse;
        }
        if (playerId.equals(currentPlayer)) {
            players.get(playerId).getState().fsm.setShareData(requestArguments);
            players.get(playerId).getState().fsm.ProcessFSM(command);
            UserResponse response = players.get(playerId).getState().response;
            // Reset player response.
            players.get(playerId).getState().response = new UserResponse(HttpStatus.SC_ACCEPTED,
                    "The request is forbidden.", null);
            return response;
        }
        return new UserResponse(HttpStatus.SC_ACCEPTED, "It is not your turn.", null);
    }

    public UserResponse processOtherCommand(String playerId, String command, Map<String, Object> requestArguments)
            throws JsonProcessingException {
        Map<String, Object> responseArguments = new HashMap<>();
        responseArguments.put("sentAll", false);
        switch (command) {
            case "update":
                return new UserResponse(HttpStatus.SC_OK,"Here are your information",initializeUpdateResponse());
            case "getRanking":
                return new UserResponse(HttpStatus.SC_OK,"Here is the current ranking",initializeRankingResponse());
            case "discardResources":
                return discardResources(playerId, requestArguments, responseArguments);
            case "wantToTrade":
        }
        if (notDiscardedAll) {
            return new UserResponse(HttpStatus.SC_ACCEPTED, "The request is forbidden.", null);
        }
        return null;
    }

    public UserResponse discardResources(String playerId, Map<String, Object> requestArguments,
                                         Map<String, Object> responseArguments) throws JsonProcessingException {
        if (!notDiscardedAll) {
            return new UserResponse(HttpStatus.SC_ACCEPTED, "Dice does not sum seven.",
                    responseArguments);
        }
        if (players.get(playerId).getResourceNumber() <= 7) {
            return new UserResponse(HttpStatus.SC_ACCEPTED,
                    "You do not have more than seven resource cards.",
                    responseArguments);
        }
        Map<Resource, Integer> resources = new HashMap<>();
        for (String resource : requestArguments.keySet()) {
            Resource resourceType = Helper.getResourceTypeFromString(resource);
            if (resourceType == null) {
                return new UserResponse(HttpStatus.SC_ACCEPTED, "An argument is invalid.", null);
            }
            resources.put(resourceType, (Integer) requestArguments.get(resource));
        }
        Code code = discardResources(playerId, resources);
        if (code != null) {
            return new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code),
                    responseArguments);
        }
        for (String player : playerOrder) {
            if (players.get(player).getResourceNumber() > 7) {
                return new UserResponse(HttpStatus.SC_OK, "Discarded resources successfully.",
                        responseArguments);
            }
        }
        responseArguments.put("sentAll", true);
        notDiscardedAll = false;
        return new UserResponse(HttpStatus.SC_OK, "Discarded resources successfully.",
                responseArguments);
    }

    public boolean startGame() {
        if (playerOrder.size() == 0) {
            return false;
        }
        bank = new Bank(new ArrayList<>(players.values()));
        for (Player player : players.values()) {
            player.setBank(bank);
        }
        currentPlayer = playerOrder.get(0);
        return true;
    }

    //endregion

    //region Dice

    public void rollDice() {
        Random dice = new Random();
        int firstDice = dice.nextInt(6) + 1;
        int secondDice = dice.nextInt(6) + 1;
        while (firstDice + secondDice == 7) {
            firstDice = dice.nextInt(6) + 1;
            secondDice = dice.nextInt(6) + 1;
        }
        Map<String, Object> responseArguments = initializeRollDiceResponse();
        responseArguments.put("dice_1", firstDice);
        responseArguments.put("dice_2", secondDice);
        int diceSum = firstDice + secondDice;
        FSM currentState = players.get(currentPlayer).getState().fsm;
        if (diceSum != 7) {
            notDiscardedAll = false;
            responseArguments.putAll(giveResourcesFromDice(diceSum, responseArguments));
            currentState.setShareData(responseArguments);
            currentState.ProcessFSM("rollNotSeven");
        } else {
            notDiscardedAll = true;
            for (String player : playerOrder) {
                int playerIndex = playerOrder.indexOf(player);
                int resourceNumber = players.get(player).getResourceNumber();
                if (resourceNumber > 7) {
                    responseArguments.put("resourcesToDiscard_" + playerIndex,
                            players.get(player).getResourceNumber() / 2);
                }
            }
            currentState.setShareData(responseArguments);
            currentState.ProcessFSM("rollSeven");
        }
    }

    public Map<String, Object> giveResourcesFromDice(int diceSum, Map<String, Object> responseArguments) {
        List<Tile> tiles = board.getTilesFromNumber(diceSum);
        for (Tile tile : tiles) {
            Resource resource = tile.getResource();
            List<Intersection> intersections = board.getAdjacentIntersections(tile);
            int neededResources = getNeededResources(intersections);
            if (!bank.existsResource(resource, neededResources)) {
                continue;
            }
            if (board.getRobberPosition().getId() == tile.getId()) {
                continue;
            }
            for (Intersection intersection : intersections) {
                Player owner = intersection.getOwner();
                if (owner != null) {
                    String playerId = owner.getId();
                    String argument = resource.toString() + '_' + playerOrder.indexOf(playerId);
                    int previousValue = (int) responseArguments.get(argument);
                    switch (intersection.getBuilding()) {
                        case Settlement:
                            bank.takeResource(resource);
                            players.get(playerId).takeResource(resource);
                            responseArguments.put(argument, previousValue + 1);
                            break;
                        case City:
                            bank.takeResource(resource, 2);
                            players.get(playerId).takeResource(resource, 2);
                            responseArguments.put(argument, previousValue + 2);
                    }
                }
            }
        }
        return responseArguments;
    }
    public Map<String,Object> initializeRankingResponse() {
        Map<String, Object> responseArguments = new HashMap<>();
        boolean finish=false;
        for (String player : playerOrder) {
            int playerIndex = playerOrder.indexOf(player);
            responseArguments.put("player_" + playerIndex, player);
            Player player1 = players.get(player);
            responseArguments.put("publicScore_" + playerIndex, player1.getPublicVP());
            responseArguments.put("hiddenScore_" + playerIndex, player1.getVictoryPoints());
            if(player1.getVictoryPoints()>=VictoryPoint.FINISH_VICTORY_POINTS)
                finish=true;
        }
        responseArguments.put("foundWinner",finish);
        return responseArguments;
    }
    public Map<String,Object> initializeUpdateResponse(){
        Map<String, Object> responseArguments = new HashMap<>();
        Player player1=players.get(currentPlayer);
        responseArguments.put("publicScore", player1.getPublicVP());
        responseArguments.put("hiddenScore", player1.getVictoryPoints());
        responseArguments.put("hasLargestArmy", player1.isHasLargestArmy());
        responseArguments.put("hasLongestRoad" , player1.isHasLongestRoad());
        responseArguments.put("availableHouses",getAvailableHousePlacements());
        responseArguments.put("availableRoads",getAvailableRoadPlacements());
        return responseArguments;
    }
    public Map<String, Object> initializeRollDiceResponse() {
        Map<String, Object> responseArguments = new HashMap<>();
        for (String player : playerOrder) {
            int playerIndex = playerOrder.indexOf(player);
            responseArguments.put("player_" + playerIndex, player);
            for (Resource resource : Resource.values()) {
                if (resource != Resource.desert) {
                    responseArguments.put(resource.toString() + '_' + playerIndex, 0);
                }
            }
            responseArguments.put("resourcesToDiscard_" + playerIndex, 0);
        }
        return responseArguments;
    }

    public int getNeededResources(List<Intersection> intersections) {
        int neededResources = 0;
        for (Intersection intersection : intersections) {
            switch (intersection.getBuilding()) {
                case Settlement:
                    ++neededResources;
                    break;
                case City:
                    neededResources += 2;

            }
        }
        return neededResources;
    }

    public Code discardResources(String playerId, Map<Resource, Integer> resourcesToDiscard) {
        Code code = players.get(playerId).removeResources(resourcesToDiscard);
        if (code != null) {
            return code;
        }
        bank.giveResources(resourcesToDiscard);
        return null;
    }

    //region get available placements for settlements, cities and roads
    public boolean isAvailableHousePlacement(Intersection intersection) {
        if (intersection.getBuilding() == Building.Settlement
                || intersection.getBuilding() == Building.City)
            return false;
        for (Intersection intersection1 :
                board.getAdjacentIntersections(intersection)) {
            if (intersection1.getBuilding() == Building.Settlement
                    || intersection1.getBuilding() == Building.City)
                return false;
        }
        return true;
    }

    public Set<List<Integer>> getAvailableRoadPlacements() {
        Player player = players.get(currentPlayer);
        Set<List<Integer>> availableRoadPlacements = new HashSet<>();
        for (Road road:
                player.getRoads()) {
            for (Intersection intersection:
                    board.getAdjacentIntersections(road.getStart())) {
                Integer start=road.getStart().getId();
                Integer end=intersection.getId();
                if(start>end){ Integer aux=start;start=end;end=aux; } //swap
                if (!board.existsRoad(road.getEnd().getId(), intersection.getId()))
                    availableRoadPlacements.add(new ArrayList<>(Arrays.asList(start,end)));
            }
            for (Intersection intersection:
                    board.getAdjacentIntersections(road.getEnd())) {
                Integer start=intersection.getId();
                Integer end=road.getEnd().getId();
                if(start>end){ Integer aux=start;start=end;end=aux; } //swap
                if (!board.existsRoad(road.getEnd().getId(), intersection.getId()))
                    availableRoadPlacements.add(new ArrayList<>(Arrays.asList(start,end)));
            }
        }
        return availableRoadPlacements;
    }

    public Set<Integer> getAvailableHousePlacements() {
        Player player = players.get(currentPlayer);
        Set<Integer> availableHousePlacements = new HashSet<>();
        for (Road road:
                player.getRoads()) {
            if (isAvailableHousePlacement(road.getStart()))
                availableHousePlacements.add(road.getStart().getId());
            if (isAvailableHousePlacement(road.getEnd()))
                availableHousePlacements.add(road.getEnd().getId());
        }
        return availableHousePlacements;
    }
    public Set<Integer> getAvailableCityPlacement() {
        Player player = players.get(currentPlayer);
        Set<Integer> availableCityPlacement = new HashSet<>();
        for (Intersection building :
                player.getSettlements()) {
            availableCityPlacement.add(building.getId());
        }
        return availableCityPlacement;
    }
    //endregion

    //region place house and road region (INITIAL)
    //TODO: use this function to make response arguments
    public void giveInitialResources() {
        for (String player : playerOrder) {
            Player player1 = players.get(player);
            Intersection intersection = player1.getSettlements().get(player1.getSettlements().size() - 1);

            for (Integer tileID : board.getAdjacentTilesToIntersection(intersection.getId())) {
                Tile tile = board.getTiles().get(tileID);
                if (tile.getResource() != Resource.desert) {
                    bank.takeResource(tile.getResource(), 1);
                    player1.addResource(tile.getResource());
                }
            }
        }

    }

    public Code buildSettlement(int intersection) {
        Player player = players.get(currentPlayer);
        if (bank.hasSettlement(player)) {
            Intersection building = board.getIntersections().get(intersection);
            if (building == null) {
                return Code.NoSuchIntersection;
            }
            if (building.getBuilding() != Building.None || building.getOwner() != null) {
                return Code.IntersectionAlreadyOccupied;
            }
            if (!isTwoRoadsDistance(intersection)) {
                return Code.NotTwoRoadsDistance;
            }
            bank.takeSettlement(player);
            board.getIntersections().get(intersection).setOwner(player);
            board.getIntersections().get(intersection).setBuilding(Building.Settlement);
            player.placeSettlement(building);
            return null;
        }
        return Code.NoSettlement;
    }

    public Code buildRoad(int intersectionId1, int intersectionId2) {

        Player player = players.get(currentPlayer);
        if (bank.hasRoad(player)) {
            Intersection firstIntersection = board.getIntersections().get(intersectionId1);
            Intersection secondIntersection = board.getIntersections().get(intersectionId2);

            if (firstIntersection == null || secondIntersection == null)
                return Code.NoSuchIntersection;
            if (board.existsRoad(intersectionId1, intersectionId2)) {
                return Code.RoadAlreadyExistent;
            }
            if (firstIntersection.getOwner() != player && secondIntersection.getOwner() != player) {
                return Code.RoadInvalidPosition;
            }
            if (!board.getIntersectionGraph().areAdjacent(intersectionId1, intersectionId2)) {
                return Code.RoadStartNotOwned;
            }

            bank.takeRoad(player);
            Road road = new Road(firstIntersection, secondIntersection);
            board.addRoad(road);
            player.addRoad(road);
            return null;
        }
        return Code.NoRoad;
    }

    //endregion

    //region Buy

    public Code buyDevelopment(String playerId) {
        Player player = players.get(playerId);
        if (player.getResourceNumber(Resource.wool) >= 1 && player.getResourceNumber(Resource.ore) >= 1 && player.getResourceNumber(Resource.grain) >= 1) {
            player.removeResource(Resource.wool);
            player.removeResource(Resource.ore);
            player.removeResource(Resource.grain);
            bank.giveResource(Resource.ore);
            bank.giveResource(Resource.wool);
            bank.giveResource(Resource.grain);
            boolean ok = true;
            boolean noKnight = false;
            boolean noVP = false;
            boolean noYear = false;
            boolean noMonopoly = false;
            boolean noRoadBuilding = false;
            Random rand = new Random();
            int choice = 0;
            while (ok) {
                choice = rand.nextInt(5);
                if (choice == 0) {
                    Knight k = bank.takeKnight(player);
                    if (k != null) {
                        ok = false;
                        player.addKnight(k);
                    } else {
                        noKnight = true;
                    }
                }
                if (choice == 1) {
                    Monopoly m = bank.takeMonopoly(player);
                    if (m != null) {
                        ok = false;
                        player.addMonopolies(m);
                    } else {
                        noMonopoly = true;
                    }
                }
                if (choice == 2) {
                    YearOfPlenty y = bank.takeYearOfPlenty(player);
                    if (y != null) {
                        ok = false;
                        player.addYearsOfPlenty(y);
                    } else {
                        noYear = true;
                    }
                }
                if (choice == 3) {
                    RoadBuilding r = bank.takeRoadBuilding(player);
                    if (r != null) {
                        ok = false;
                        player.addRoadBuilding(r);
                    } else {
                        noRoadBuilding = true;
                    }
                }
                if (choice == 4) {
                    catan.game.development.VictoryPoint v = bank.takeVictoryPoint(player);
                    if (v != null) {
                        ok = false;
                        player.addVictoryPoint();
                    } else {
                        noVP = true;
                    }
                }
                if (noKnight == true && noMonopoly == true && noRoadBuilding == true && noVP == true && noYear == true) {
                    return Code.BankNoDev;
                }
            }
        } else {
            if (player.getResourceNumber(Resource.wool) == 0) return Code.PlayerNoWool;
            if (player.getResourceNumber(Resource.grain) == 0) return Code.PlayerNoGrain;
            if (player.getResourceNumber(Resource.ore) == 0) return Code.PlayerNoOre;
        }
        return null;
    }

    protected int currentPlayerIndex() {
        for (int i = 0; i < playerOrder.size(); i++) {
            if (playerOrder.get(i).equals(currentPlayer))
                return i;
        }
        return -1;
    }

    protected boolean isTwoRoadsDistance(int buildingId) {
        if (board.getIntersections() == null || board.getIntersections().size() == 0) {
            return true;
        }
        List<Integer> neighborIntersections = board.getIntersectionGraph().getAdjacentIntersectionIDs(buildingId);
        for (Integer neighbour : neighborIntersections) {
            if (board.getIntersections().get(neighbour).getOwner() != null)
                return false;
        }
        return true;
    }

    public Code buySettlement(int intersectionId) {
        Player player = players.get(currentPlayer);
        if (bank.hasSettlement(player)) {
            Intersection intersection = board.getIntersections().get(intersectionId);
            if (intersection == null) {
                return Code.NoSuchIntersection;
            }
            if (!(intersection.getOwner() == null || intersection.getOwner().equals(player))) {
                return Code.IntersectionAlreadyOccupied;
            }
            if (!intersection.getBuilding().equals(Building.None)) {
                return Code.InvalidSettlementPosition;
            }
            if (!isTwoRoadsDistance(intersectionId)) {
                return Code.NotTwoRoadsDistance;
            }
            if (!player.buildSettlement(intersection))
                return Code.NotEnoughResources;

            bank.takeSettlement(player);
            board.getIntersections().get(intersectionId).setOwner(player);
            board.getIntersections().get(intersectionId).setBuilding(Building.Settlement);
            return null;
        }
        return Code.NoSettlement;
    }

    public Code buyCity(int intersectionId) {
        Player player = players.get(currentPlayer);
        if (bank.hasCity(player)) {
            Intersection intersection = board.getIntersections().get(intersectionId);

            if (intersection == null)
                return Code.NoSuchIntersection;
            if (!intersection.getOwner().equals(player)) {
                return Code.IntersectionAlreadyOccupied;
            }
            if (!intersection.getBuilding().equals(Building.Settlement)) {
                return Code.InvalidCityPosition;
            }
            if (!player.buildCity(intersection))
                return Code.NotEnoughResources;

            bank.takeCity(player);
            board.getIntersections().get(intersectionId).setBuilding(Building.City);
            return null;
        }
        return Code.NoCity;

    }

    public Code buyRoad(int intersectionId1, int intersectionId2) {
        Player player = players.get(currentPlayer);
        Intersection firstIntersection = board.getIntersections().get(intersectionId1);
        Intersection secondIntersection = board.getIntersections().get(intersectionId2);
        if (bank.hasRoad(player)) {

            if (firstIntersection == null || secondIntersection == null)
                return Code.NoSuchIntersection;
            if (board.existsRoad(intersectionId1, intersectionId2)) {
                return Code.RoadAlreadyExistent;
            }
            if (firstIntersection.getOwner() != player && secondIntersection.getOwner() != player) {
                return Code.RoadInvalidPosition;
            }
            if (!board.getIntersectionGraph().areAdjacent(intersectionId1, intersectionId2)) {
                return Code.RoadStartNotOwned;
            }
            Road road = new Road(firstIntersection, secondIntersection);
            if (!player.buyRoad(road)) {
                return Code.NotEnoughResources;
            }

            bank.takeRoad(player);
            board.addRoad(road);
            return null;
        }
        return Code.NoRoad;

    }

    //endregion

    //region Trade

    public void playerTrade(List<Player> playersThatAccepted, List<Pair<Resource, Integer>> offer,
                            List<Pair<Resource, Integer>> request) {
        Player traderPlayer = players.get(currentPlayer);
        Random rand = new Random();
        int index = rand.nextInt(playersThatAccepted.size());
        Player trader = playersThatAccepted.get(index);
        traderPlayer.updateTradeResources(offer, request);
        trader.updateTradeResources(request, offer);
    }

    //endregion

    //region Robber

    public boolean giveResources(List<Pair<String, Map<Resource, Integer>>> playersRes) {
        for (Pair<String, Map<Resource, Integer>> pair : playersRes) {
            players.get(pair.getKey()).removeResources(pair.getValue());
        }
        return true;

    }

    //TODO:
    public Code moveRobber(int tileId) {
        if (board.getRobberPosition().getId() == tileId) {
            return Code.SameTile;
        }
        board.setRobberPosition(board.getTiles().get(tileId));
        return null;
    }

    public boolean stealResource(Pair<String, String> playerPair) {
        Resource type = players.get(playerPair.getValue()).removeRandomResources();
        players.get(playerPair.getValue()).takeResource(type);
        return true;
    }

    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return Objects.equals(getPlayers(), game.getPlayers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayers());
    }


}
