package catan.game.game;

import catan.API.response.Code;
import catan.API.response.GameResponse;
import catan.API.response.Messages;
import catan.API.response.UserResponse;
import catan.game.bank.Bank;
import catan.game.board.Board;
import catan.game.board.Tile;
import catan.game.enumeration.Building;
import catan.game.enumeration.Development;
import catan.game.enumeration.Port;
import catan.game.enumeration.Resource;
import catan.game.player.Player;
import catan.game.property.Intersection;
import catan.game.property.Road;
import catan.game.rule.Component;
import catan.game.rule.VictoryPoint;
import catan.util.Helper;
import javafx.util.Pair;
import org.apache.http.HttpStatus;

import java.util.*;

public abstract class Game {
    protected Bank bank;
    protected Board board;

    protected int maxPlayers;
    protected Map<String, Player> players;
    protected List<Player> playerOrder;
    protected Player currentPlayer;

    protected Map<Resource, Integer> tradeOffer;
    protected Map<Resource, Integer> tradeRequest;
    protected List<String> tradePartners;
    protected Player tradePartner;

    protected Pair<String, Integer> currentLargestArmy;
    protected Pair<String, Integer> currentLongestRoad;

    protected boolean inDiscardState;


    public Game() {
        bank = null;
        board = new Board();

        maxPlayers = 0;
        players = new HashMap<>();
        playerOrder = new ArrayList<>();
        currentPlayer = null;

        tradeOffer = null;
        tradeRequest = null;
        tradePartners = new ArrayList<>();
        tradePartner = null;

        currentLargestArmy = null;
        currentLongestRoad = null;

        inDiscardState = false;

    }

    //region Getters

    public Bank getBank() {
        return bank;
    }

    public Board getBoard() {
        return board;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public int getPlayersNumber() {
        return players.size();
    }

    public List<Player> getPlayerOrder() {
        return playerOrder;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }

    public Map<Resource, Integer> getTradeOffer() {
        return tradeOffer;
    }

    public Map<Resource, Integer> getTradeRequest() {
        return tradeRequest;
    }

    public List<String> getTradePartners() {
        return tradePartners;
    }

    public Player getTradePartner() {
        return tradePartner;
    }

    public Pair<String, Integer> getCurrentLargestArmy() {
        return currentLargestArmy;
    }

    public Pair<String, Integer> getCurrentLongestRoad() {
        return currentLongestRoad;
    }

    public boolean isInDiscardState() {
        return inDiscardState;
    }


    //endregion

    //region Setters

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }

    public void setPlayerOrder(List<Player> playerOrder) {
        this.playerOrder = playerOrder;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setTradeOffer(Map<Resource, Integer> tradeOffer) {
        this.tradeOffer = tradeOffer;
    }

    public void setTradeRequest(Map<Resource, Integer> tradeRequest) {
        this.tradeRequest = tradeRequest;
    }

    public void setTradePartners(List<String> tradePartners) {
        this.tradePartners = tradePartners;
    }

    public void setTradePartner(Player tradePartner) {
        this.tradePartner = tradePartner;
    }

    public void setCurrentLargestArmy(Pair<String, Integer> currentLargestArmy) {
        this.currentLargestArmy = currentLargestArmy;
    }

    public void setCurrentLongestRoad(Pair<String, Integer> currentLongestRoad) {
        this.currentLongestRoad = currentLongestRoad;
    }

    public void setInDiscardState(boolean inDiscardState) {
        this.inDiscardState = inDiscardState;
    }


    //endregion

    //region Checkers

    protected boolean validIntersection(int intersection) {
        return intersection >= 0 && intersection < Component.INTERSECTIONS;
    }

    protected boolean isDistanceRuleViolated(Integer intersection) {
        List<Integer> adjacentIntersections = board.getIntersectionGraph().getAdjacentIntersections(intersection);
        for (int adjacentIntersection : adjacentIntersections) {
            if (board.getIntersection(adjacentIntersection).hasOwner()) {
                return true;
            }
        }
        return false;
    }

    //endregion

    //region Initialize Game

    public void addPlayer(String playerId, Player player) {
        players.put(playerId, player);
    }

    public void addNextPlayer(String playerId) {
        playerOrder.add(players.get(playerId));
    }

    public boolean startGame() {
        if (playerOrder.size() == 0) {
            return false;
        }
        bank = new Bank(new ArrayList<>(playerOrder));
        currentPlayer = playerOrder.get(0);
        return true;
    }

    //endregion

    //region Turn

    public UserResponse playTurn(String playerId, String command, Map<String, Object> requestArguments) {
        if (command.equals("update")) {
            return new UserResponse(HttpStatus.SC_OK, "Here is your information.", getUpdateResult());
        }
        Pair<Code, Map<String, Object>> result = processGeneralCommand(playerId, command, requestArguments);
        Code code = result.getKey();
        if (code != null) {
            return new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), result.getValue());
        }
        if (inDiscardState) {
            return new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.ForbiddenRequest), null);
        }
        if (playerId.equals(currentPlayer.getId())) {
            players.get(playerId).getTurnFlow().fsm.setShareData(requestArguments);
            players.get(playerId).getTurnFlow().fsm.ProcessFSM(command);
            UserResponse response = players.get(playerId).getTurnFlow().response;
            players.get(playerId).getTurnFlow().response = new UserResponse(HttpStatus.SC_ACCEPTED,
                    Messages.getMessage(Code.ForbiddenRequest), null);
            return response;
        }
        return new UserResponse(HttpStatus.SC_ACCEPTED, "It is not your turn.", null);
    }

    public Pair<Code, Map<String, Object>> processGeneralCommand(String playerId, String command, Map<String,
            Object> requestArguments) {
        Map<String, Object> responseArguments = new HashMap<>();
        switch (command) {
            case "discardResources": {
                Code code = checkDiscardResources(playerId, requestArguments);
                if (code != null) {
                    return new Pair<>(code, null);
                }
                boolean sentAll = checkAllHaveSent();
                inDiscardState = !sentAll;
                responseArguments.put("sentAll", sentAll);
                return new Pair<>(null, responseArguments);
            }
            case "wantToTrade": {
                Code code = wantToTrade(playerId);
                if (code != null) {
                    return new Pair<>(code, null);
                }
                return new Pair<>(null, null);
            }
            default:
                return new Pair<>(null, null);
        }
    }

    public boolean changeTurn(int direction) {
        updateBonusPoints();
        if (currentPlayerWon()) {
            return false;
        }
        int nextPlayer = (playerOrder.indexOf(currentPlayer) + direction) % playerOrder.size();
        currentPlayer = playerOrder.get(nextPlayer);
        return true;
    }

    protected void updateBonusPoints() {
        // Update largest army.
        int usedKnights = currentPlayer.getUsedKnights();
        if (currentLargestArmy == null) {
            if (usedKnights >= Component.KNIGHTS_FOR_LARGEST_ARMY) {
                currentPlayer.addLargestArmy();
                currentLargestArmy = new Pair<>(currentPlayer.getId(), usedKnights);
            }
        } else if (usedKnights > currentLargestArmy.getValue() &&
                !(currentPlayer.getId().equals(currentLargestArmy.getKey()))) {
            players.get(currentLargestArmy.getKey()).removeLargestArmy();
            currentPlayer.addLargestArmy();
            currentLargestArmy = new Pair<>(currentPlayer.getId(), usedKnights);
        }

        // Update longest road.
        int builtRoads = currentPlayer.getLongestRoadLength();
        if (currentLongestRoad == null) {
            if (builtRoads >= Component.ROADS_FOR_LONGEST_ROAD) {
                currentPlayer.addLongestRoad();
                currentLongestRoad = new Pair<>(currentPlayer.getId(), builtRoads);
            }
        } else if (builtRoads > currentLongestRoad.getValue() &&
                !(currentPlayer.getId().equals(currentLongestRoad.getKey()))) {
            players.get(currentLongestRoad.getKey()).removeLongestRoad();
            currentPlayer.addLongestRoad();
            currentLongestRoad = new Pair<>(currentPlayer.getId(), builtRoads);
        }
    }

    protected boolean currentPlayerWon() {
        return currentPlayer.getVictoryPoints() >= VictoryPoint.FINISH_VICTORY_POINTS;
    }

    //endregion

    //region Update

    public Map<String, Object> getUpdateResult() {
        Map<String, Object> result = new HashMap<>();
        result.put("canBuyRoad", canBuyRoad(currentPlayer));
        result.put("canBuySettlement", canBuySettlement(currentPlayer));
        result.put("canBuyCity", canBuyCity(currentPlayer));
        result.put("canBuyDevelopment", canBuyDevelopment(currentPlayer));
        result.put("availableRoadPositions", getAvailableRoadPositions());
        result.put("availableSettlementPositions", getAvailableSettlementPositions());
        result.put("availableCityPositions", getAvailableCityPositions());
        result.put("hasLargestArmy", currentPlayer.hasLargestArmy());
        result.put("hasLongestRoad", currentPlayer.hasLongestRoad());
        result.put("publicScore", currentPlayer.getPublicVictoryPoints());
        result.put("hiddenScore", currentPlayer.getVictoryPoints());
        return result;
    }

    //endregion

    //region Ranking

    public Map<String, Object> getRankingResult() {
        Map<String, Object> result = new HashMap<>();
        List<Player> ranking = new ArrayList<>(players.values());
        ranking.sort(Comparator.comparingInt(Player::getVictoryPoints).thenComparing(Player::getPublicVictoryPoints));
        boolean foundWinner = false;
        for (Player player : ranking) {
            int playerIndex = ranking.indexOf(player);
            result.put("player_" + playerIndex, player.getId());
            result.put("publicScore_" + playerIndex, player.getPublicVictoryPoints());
            result.put("hiddenScore_" + playerIndex, player.getVictoryPoints());
            if (player.getVictoryPoints() >= VictoryPoint.FINISH_VICTORY_POINTS) {
                foundWinner = true;
            }
        }
        result.put("foundWinner", foundWinner);
        return result;
    }

    //endregion

    // region First Two Rounds

    public Code buildSettlement(int intersection) {
        if (!bank.hasSettlement(currentPlayer)) {
            return Code.BankNoSettlement;
        }
        Code code = checkBuildSettlement(intersection);
        if (code != null) {
            return code;
        }
        return buildSettlement(board.getIntersection(intersection));
    }

    protected Code checkBuildSettlement(int intersection) {
        if (!validIntersection(intersection)) {
            return Code.InvalidSettlementPosition;
        }
        Intersection settlement = board.getIntersection(intersection);
        if (settlement.hasOwner()) {
            return Code.IntersectionAlreadyOccupied;
        }
        if (isDistanceRuleViolated(intersection)) {
            return Code.DistanceRuleViolated;
        }
        return null;
    }

    protected Code buildSettlement(Intersection settlement) {
        Code code = bank.removeSettlement(currentPlayer);
        if (code != null) {
            return code;
        }
        settlement.setBuilding(Building.Settlement);
        currentPlayer.buildSettlement(settlement);
        return null;
    }

    public Code buildRoad(int start, int end) {
        if (!bank.hasRoad(currentPlayer)) {
            return Code.BankNoRoad;
        }
        Code code = checkBuildRoad(start, end);
        if (code != null) {
            return code;
        }
        return buildRoad(board.getIntersection(start), board.getIntersection(end));
    }

    protected Code checkBuildRoad(int startId, int endId) {
        if (!(validIntersection(startId) && validIntersection(endId))) {
            return Code.InvalidRoadPosition;
        }
        if (!board.getIntersectionGraph().areAdjacent(startId, endId)) {
            return Code.InvalidRoadPosition;
        }
        if (board.hasRoad(startId, endId)) {
            return Code.RoadAlreadyExistent;
        }
        Intersection start = board.getIntersection(startId);
        Intersection end = board.getIntersection(endId);
        if (!(start.getOwner() == currentPlayer || end.getOwner() == currentPlayer)) {
            if (!(currentPlayer.hasRoadWith(start) || currentPlayer.hasRoadWith(end))) {
                return Code.InvalidRoadPosition;
            }
        }
        return null;
    }

    protected Code buildRoad(Intersection start, Intersection end) {
        Code code = bank.removeRoad(currentPlayer);
        if (code != null) {
            return code;
        }
        Road road = new Road(start, end);
        board.addRoad(road);
        currentPlayer.buildRoad(road);
        return null;
    }

    public Map<String, Integer> getSecondSettlementResources() {
        if (!(currentPlayer.getSettlementsNumber() == 2 && currentPlayer.getCitiesNumber() == 0)) {
            return null;
        }
        Map<String, Integer> resources = new HashMap<>();
        for (Resource resource : Resource.values()) {
            if (resource != Resource.desert) {
                resources.put(resource.toString(), 0);
            }
        }
        Intersection settlement = currentPlayer.getSettlements().get(1);
        for (int tile : board.getAdjacentTilesToIntersection(settlement.getId())) {
            Resource resource = board.getTile(tile).getResource();
            if (resource != Resource.desert) {
                bank.removeResource(resource);
                currentPlayer.addResource(resource);
                String resourceString = resource.toString();
                resources.put(resourceString, resources.get(resourceString) + 1);
            }
        }
        return resources;
    }

    public void changeTurn() {
        Player firstPlayer = playerOrder.get(0);
        Player lastPlayer = playerOrder.get(getPlayersNumber() - 1);
        if (currentPlayer.equals(lastPlayer)) {
            if (currentPlayer.getRoadsNumber() == 1) {
                changeTurn(0);
            } else if (currentPlayer.getRoadsNumber() == 2) {
                changeTurn(-1);
            }
        } else {
            if (currentPlayer.equals(firstPlayer) && currentPlayer.getRoadsNumber() == 2) {
                changeTurn(1);
            }
            if (lastPlayer.getRoadsNumber() == 0) {
                changeTurn(1);
            } else if (lastPlayer.getRoadsNumber() == 2) {
                changeTurn(-1);
            }
        }
    }

    //endregion

    //region Dice

    public Pair<Integer, Integer> rollDice() {
        Random dice = new Random();
        int firstDice = dice.nextInt(6) + 1;
        int secondDice = dice.nextInt(6) + 1;
        //TODO: Remove "while" after GI adds robber.
        while (firstDice + secondDice == 7) {
            firstDice = dice.nextInt(6) + 1;
            secondDice = dice.nextInt(6) + 1;
        }
        return new Pair<>(firstDice, secondDice);
    }

    public Map<String, Object> getRollSevenResult() {
        Map<String, Object> result = initializeRollDiceResult();
        for (Player player : playerOrder) {
            int playerIndex = playerOrder.indexOf(player);
            int resourceNumber = player.getResourcesNumber();
            if (resourceNumber > 7) {
                result.put("resourcesToDiscard_" + playerIndex, resourceNumber / 2);
            }
        }
        return result;
    }

    public Map<String, Object> getRollNotSevenResult(int diceSum) {
        Map<String, Object> result = initializeRollDiceResult();
        List<Tile> tiles = board.getTilesFromNumber(diceSum);
        for (Tile tile : tiles) {
            Resource resource = tile.getResource();
            List<Intersection> intersections = board.getAdjacentIntersections(tile);
            int requiredResourcesNumber = getRequiredResources(intersections);
            if (!bank.hasResource(resource, requiredResourcesNumber)) {
                continue;
            }
            if (board.getRobberPosition().getId() == tile.getId()) {
                continue;
            }
            for (Intersection intersection : intersections) {
                Player owner = intersection.getOwner();
                if (owner != null) {
                    String argument = resource.toString() + '_' + playerOrder.indexOf(owner);
                    int previousValue = (int) result.get(argument);
                    switch (intersection.getBuilding()) {
                        case Settlement:
                            bank.removeResource(resource);
                            owner.addResource(resource);
                            result.put(argument, previousValue + 1);
                            break;
                        case City:
                            bank.removeResource(resource, 2);
                            owner.addResource(resource, 2);
                            result.put(argument, previousValue + 2);
                    }
                }
            }
        }
        return result;
    }

    protected Map<String, Object> initializeRollDiceResult() {
        Map<String, Object> result = new HashMap<>();
        for (Player player : playerOrder) {
            int playerIndex = playerOrder.indexOf(player);
            result.put("player_" + playerIndex, player.getId());
            for (Resource resource : Resource.values()) {
                if (resource != Resource.desert) {
                    result.put(resource.toString() + '_' + playerIndex, 0);
                }
            }
            result.put("resourcesToDiscard_" + playerIndex, 0);
        }
        return result;
    }

    public int getRequiredResources(List<Intersection> intersections) {
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

    //endregion

    //region Discard Resource Cards

    protected Code checkDiscardResources(String playerId, Map<String, Object> requestArguments) {
        if (!inDiscardState) {
            return Code.DiceNotSeven;
        }
        if (players.get(playerId).getResourcesNumber() <= 7) {
            return Code.NotDiscard;
        }
        Map<Resource, Integer> resourcesToDiscard = new HashMap<>();
        for (String resourceString : requestArguments.keySet()) {
            Resource resource = Helper.getResourceFromString(resourceString);
            if (resource == null) {
                return Code.InvalidRequest;
            }
            resourcesToDiscard.put(resource, (Integer) requestArguments.get(resourceString));
        }
        return discardResources(playerId, resourcesToDiscard);
    }

    public Code discardResources(String playerId, Map<Resource, Integer> resourcesToDiscard) {
        Code code = players.get(playerId).removeResources(resourcesToDiscard);
        if (code != null) {
            return code;
        }
        bank.addResources(resourcesToDiscard);
        return null;
    }

    protected boolean checkAllHaveSent() {
        for (Player player : playerOrder) {
            if (player.getResourcesNumber() > 7) {
                return false;
            }
        }
        return true;
    }

    //endregion

    //region Check Buy

    private boolean canBuyRoad(Player player) {
        return player.hasRoadResources() == null;
    }

    private boolean canBuySettlement(Player player) {
        return player.hasSettlementResources() == null;
    }

    private boolean canBuyCity(Player player) {
        return player.hasCityResources() == null;
    }

    private boolean canBuyDevelopment(Player player) {
        return player.hasDevelopmentResources() == null;
    }

    //endregion

    //region Available Properties Positions

    protected Set<int[]> getAvailableRoadPositions() {
        Set<int[]> availableRoadPositions = new HashSet<>();
        for (Road road : currentPlayer.getRoads()) {
            for (Intersection intersection : board.getAdjacentIntersections(road.getStart())) {
                int start = road.getStart().getId();
                int end = intersection.getId();
                if (start > end) {
                    int aux = start;
                    start = end;
                    end = aux;
                }
                if (!board.hasRoad(start, end)) {
                    availableRoadPositions.add(new int[]{start, end});
                }
            }
            for (Intersection intersection : board.getAdjacentIntersections(road.getEnd())) {
                int start = road.getEnd().getId();
                int end = intersection.getId();
                if (start > end) {
                    int aux = start;
                    start = end;
                    end = aux;
                }
                if (!board.hasRoad(start, end)) {
                    availableRoadPositions.add(new int[]{start, end});
                }
            }
        }
        return availableRoadPositions;
    }

    protected Set<Integer> getAvailableSettlementPositions() {
        Set<Integer> availableSettlementPositions = new HashSet<>();
        for (Road road : currentPlayer.getRoads()) {
            Intersection start = road.getStart();
            Intersection end = road.getEnd();
            if (isAvailableSettlementPosition(start)) {
                availableSettlementPositions.add(start.getId());
            }
            if (isAvailableSettlementPosition(end)) {
                availableSettlementPositions.add(end.getId());
            }
        }
        return availableSettlementPositions;
    }

    protected Set<Integer> getAvailableCityPositions() {
        Set<Integer> availableCityPositions = new HashSet<>();
        for (Intersection settlement : currentPlayer.getSettlements()) {
            availableCityPositions.add(settlement.getId());
        }
        return availableCityPositions;
    }

    private boolean isAvailableSettlementPosition(Intersection intersection) {
        if (intersection.hasOwner()) {
            return false;
        }
        for (Intersection adjacentIntersection : board.getAdjacentIntersections(intersection)) {
            if (adjacentIntersection.hasOwner()) {
                return false;
            }
        }
        return true;
    }

    //endregion

    //region Buy

    public Code buyDevelopment() {
        if (!bank.hasDevelopment()) {
            return Code.BankNoDevelopment;
        }
        Code code = currentPlayer.canBuyDevelopment();
        if (code != null) {
            return code;
        }
        Development development = getRandomDevelopment();
        code = bank.sellDevelopment(development);
        if (code != null) {
            return code;
        }
        return currentPlayer.buyDevelopment(development);
    }

    protected Development getRandomDevelopment() {
        Development[] developments = {Development.knight, Development.monopoly, Development.roadBuilding,
                Development.yearOfPlenty};
        Random random = new Random();
        int index = random.nextInt(developments.length);
        while (bank.getDevelopmentsNumber(developments[index]) <= 0) {
            index = random.nextInt(developments.length);
        }
        return developments[index];
    }

    public Code buyRoad(int start, int end) {
        if (!bank.hasRoad(currentPlayer)) {
            return Code.BankNoRoad;
        }
        Code code = currentPlayer.canBuyRoad(start, end);
        if (code != null) {
            return code;
        }
        code = checkBuildRoad(start, end);
        if (code != null) {
            return code;
        }
        code = bank.sellRoad(currentPlayer);
        if (code != null) {
            return code;
        }
        code = currentPlayer.buyRoad();
        if (code != null) {
            return code;
        }
        return buildRoad(board.getIntersection(start), board.getIntersection(end));
    }

    public Code buySettlement(int intersection) {
        if (!bank.hasSettlement(currentPlayer)) {
            return Code.BankNoSettlement;
        }
        Code code = currentPlayer.canBuySettlement(intersection);
        if (code != null) {
            return code;
        }
        code = checkBuildSettlement(intersection);
        if (code != null) {
            return code;
        }
        code = bank.sellSettlement(currentPlayer);
        if (code != null) {
            return code;
        }
        code = currentPlayer.buySettlement();
        if (code != null) {
            return code;
        }
        return buildSettlement(board.getIntersection(intersection));
    }

    public Code buyCity(int intersection) {
        if (!bank.hasCity(currentPlayer)) {
            return Code.BankNoCity;
        }
        Code code = currentPlayer.canBuyCity(intersection);
        if (code != null) {
            return code;
        }
        if (!validIntersection(intersection)) {
            return Code.InvalidCityPosition;
        }
        code = bank.sellCity(currentPlayer);
        if (code != null) {
            return code;
        }
        code = currentPlayer.buyCity();
        if (code != null) {
            return code;
        }
        return buildCity(board.getIntersection(intersection));
    }

    protected Code buildCity(Intersection city) {
        Code code = bank.removeCity(currentPlayer);
        if (code != null) {
            return code;
        }
        city.setBuilding(Building.City);
        currentPlayer.buildCity(city);
        return null;
    }

    //endregion

    //region Trade

    public Code playerTrade(Map<Resource, Integer> offer, Map<Resource, Integer> request) {
        tradeRequest = null;
        tradeOffer = null;
        tradePartners.clear();
        Code code = currentPlayer.hasResources(offer);
        if (code != null) {
            return code;
        }
        tradeOffer = offer;
        tradeRequest = request;
        return null;
    }

    public Code wantToTrade(String player) {
        if (player.equals(currentPlayer.getId())) {
            return Code.InvalidTradeRequest;
        }
        if (tradeOffer == null || tradeRequest == null) {
            return Code.NoTradeAvailable;
        }
        if (tradePartners.contains(player)) {
            return Code.AlreadyInTrade;
        }
        Code code = getPlayer(player).hasResources(tradeRequest);
        if (code != null) {
            return code;
        }
        tradePartners.add(player);
        return null;
    }

    public Map<String, Object> sendPartners() {
        if (tradePartners.size() == 0) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        int index = 0;
        for (String player : tradePartners) {
            result.put("player_" + index, player);
            ++index;
        }
        return result;
    }

    public Code selectPartner(String playerId) {
        if (!tradePartners.contains(playerId)) {
            return Code.NotInTrade;
        }
        Player partner = players.get(playerId);
        Code code = currentPlayer.removeResources(tradeOffer);
        if (code != null) {
            return code;
        }
        partner.addResources(tradeOffer);
        code = partner.removeResources(tradeRequest);
        if (code != null) {
            return code;
        }
        currentPlayer.addResources(tradeRequest);
        return null;
    }

    public Code noPlayerTrade(int port, String offer, String request) {
        if (port == -1) {
            return bankTrade(offer, request);
        }
        if (port >= 0 && port < Component.INTERSECTIONS) {
            return portTrade(port, offer, request);
        }
        return Code.InvalidRequest;
    }

    public Code portTrade(int portId, String offerString, String requestString) {
        Port port = board.getPort(portId);
        if (port == null || port == Port.None) {
            return Code.InvalidRequest;
        }
        Resource offer = Helper.getResourceFromString(offerString);
        if (offer == null) {
            return Code.InvalidRequest;
        }
        Resource request = Helper.getResourceFromString(requestString);
        if (request == null) {
            return Code.InvalidRequest;
        }
        Code code;
        if (port == Port.ThreeForOne) {
            code = currentPlayer.removeResource(offer, 3);
            if (code != null) {
                return code;
            }
            code = bank.removeResource(request);
            if (code != null) {
                return code;
            }
            currentPlayer.addResource(request);
            bank.addResource(offer, 3);
            return null;
        }
        Resource portResource = Helper.getResourceFromPort(port);
        if (offer != portResource) {
            return Code.InvalidPortOffer;
        }
        code = currentPlayer.removeResource(offer, 2);
        if (code != null) {
            return code;
        }
        code = bank.removeResource(request);
        if (code != null) {
            return code;
        }
        currentPlayer.addResource(request);
        bank.addResource(offer, 2);
        return null;
    }

    public Code bankTrade(String offerString, String requestString) {
        Resource offer = Helper.getResourceFromString(offerString);
        if (offer == null) {
            return Code.InvalidRequest;
        }
        Resource request = Helper.getResourceFromString(requestString);
        if (request == null) {
            return Code.InvalidRequest;
        }
        Code code = currentPlayer.removeResource(offer, 4);
        if (code != null) {
            return code;
        }
        code = bank.removeResource(request);
        if (code != null) {
            return code;
        }
        currentPlayer.addResource(request);
        bank.addResource(offer, 4);
        return null;
    }

    //endregion

    //region Robber

    public Code moveRobber(int tile) {
        if (board.getRobberPosition().getId() == tile) {
            return Code.SameTile;
        }
        board.setRobberPosition(board.getTile(tile));
        return null;
    }

    public Map<String, Object> getPlayersToStealResourceFrom(int tileId) {
        Tile tile = board.getTile(tileId);
        if (tile.getResource() == Resource.desert) {
            return null;
        }
        Map<String, Object> players = new HashMap<>();
        List<Intersection> intersections = board.getAdjacentIntersections(tile);
        int index = 0;
        for (Intersection intersection : intersections) {
            Player player = intersection.getOwner();
            if (!(player == null || player.equals(currentPlayer))) {
                players.put("player_" + index, player.getId());
                ++index;
            }
        }
        return players;
    }

    public Resource stealResource(String playerId) {
        Player player = players.get(playerId);
        if (!player.hasResource()) {
            return null;
        }
        Resource resource = player.getRandomResource();
        Code code = player.removeResource(resource);
        if (code != null) {
            return null;
        }
        currentPlayer.addResource(resource);
        return resource;
    }

    //endregion

    //region Development

    public String useDevelopment(String development) {
        switch (development) {
            case "knight":
                return "useKnight";
            case "monopoly":
                return "useMonopoly";
            case "roadBuilding":
                return "useRoadBuilding";
            case "yearOfPlenty":
                return "useYearOfPlenty";
            default:
                return null;
        }
    }

    public Code useDevelopment(Development development) {
        if (!currentPlayer.hasDevelopment(development)) {
            return Helper.getPlayerNoDevelopmentFromDevelopment(development);
        }
        return currentPlayer.removeDevelopment(development);
    }

    public Pair<Code, Map<String, Object>> takeResourceFromAll(String resourceString) {
        Resource resource = Helper.getResourceFromString(resourceString);
        if (resource == null) {
            return new Pair<>(Code.InvalidRequest, null);
        }
        Code code;
        int index = 0;
        Map<String, Object> result = new HashMap<>();
        for (Player player : playerOrder) {
            if (!player.equals(currentPlayer)) {
                int resourcesNumber = player.getResourcesNumber(resource);
                code = player.removeResource(resource, resourcesNumber);
                if (code != null) {
                    return new Pair<>(code, null);
                }
                currentPlayer.addResource(resource, resourcesNumber);
                result.put("player_" + index, player.getId());
                result.put("resources_" + index, resourcesNumber);
                ++index;
            }
        }
        return new Pair<>(null, result);
    }

    public Code takeTwoResources(String firstResourceString, String secondResourceString) {
        Resource firstResource = Helper.getResourceFromString(firstResourceString);
        if (firstResource == null) {
            return Code.InvalidRequest;
        }
        Resource secondResource = Helper.getResourceFromString(secondResourceString);
        if (secondResource == null) {
            return Code.InvalidRequest;
        }
        Code code = bank.removeResource(firstResource);
        if (code != null) {
            return code;
        }
        code = bank.removeResource(secondResource);
        if (code != null) {
            return code;
        }
        currentPlayer.addResource(firstResource);
        currentPlayer.addResource(secondResource);
        return null;
    }

    //endregion

    //region Overrides

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Game)) {
            return false;
        }
        Game game = (Game) object;
        return Objects.equals(getPlayers(), game.getPlayers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayers());
    }

    //endregion
}
