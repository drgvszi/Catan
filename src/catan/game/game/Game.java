package catan.game.game;

import catan.API.response.Code;
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
    protected List<Player> playersOrder;
    protected Player currentPlayer;

    protected Map<Resource, Integer> tradeOffer;
    protected Map<Resource, Integer> tradeRequest;
    protected List<String> tradePartners;
    protected Player tradePartner;

    protected Pair<String, Integer> currentLargestArmy;
    protected Pair<String, Integer> currentLongestRoad;

    protected int changeTurnDirection;
    protected boolean inDiscardState;

    public Game() {
        bank = null;
        board = new Board();

        maxPlayers = 0;
        players = new HashMap<>();
        playersOrder = new ArrayList<>();
        currentPlayer = null;

        tradeOffer = null;
        tradeRequest = null;
        tradePartners = new ArrayList<>();
        tradePartner = null;

        currentLargestArmy = null;
        currentLongestRoad = null;

        changeTurnDirection = 1;
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

    public List<Player> getPlayersOrder() {
        return playersOrder;
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

    public int getChangeTurnDirection() {
        return changeTurnDirection;
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

    public void setPlayersOrder(List<Player> playersOrder) {
        this.playersOrder = playersOrder;
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

    public void setChangeTurnDirection(int changeTurnDirection) {
        this.changeTurnDirection = changeTurnDirection;
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
        playersOrder.add(players.get(playerId));
    }

    public boolean startGame() {
        if (playersOrder.size() == 0) {
            return false;
        }
        bank = new Bank(new ArrayList<>(playersOrder));
        currentPlayer = playersOrder.get(0);
        return true;
    }

    //endregion

    //region Turn

    public UserResponse playTurn(String playerId, String command, Map<String, Object> requestArguments) {
        if (command.equals("update")) {
            return new UserResponse(HttpStatus.SC_OK, "Here is your information.",
                    getUpdateResult(players.get(playerId)));
        }
        UserResponse result = processGeneralCommand(playerId, command, requestArguments);
        if (result != null) {
            return result;
        }
        if (inDiscardState) {
            return new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.DiscardState), null);
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

    public UserResponse processGeneralCommand(String playerId, String command, Map<String,
            Object> requestArguments) {
        Map<String, Object> responseArguments = new HashMap<>();
        switch (command) {
            case "discardResources": {
                if (requestArguments == null) {
                    return new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest), null);
                }
                Code code = checkDiscardResources(playerId, requestArguments);
                if (code != null) {
                    return new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                }
                inDiscardState = checkInDiscardState();
                responseArguments.put("sentAll", !inDiscardState);
                return new UserResponse(HttpStatus.SC_OK, "The resource cards\nwere discarded successfully.", responseArguments);
            }
            case "wantToTrade": {
                Code code = wantToTrade(playerId);
                if (code != null) {
                    return new UserResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(code), null);
                }
                return new UserResponse(HttpStatus.SC_OK, "You can take part in the trade.", null);
            }
            default:
                return null;
        }
    }

    public Code changeTurn(int direction) {
        updateBonusPoints();
        if (currentPlayerWon()) {
            return Code.FoundWinner;
        }
        int activePlayers = 0;
        for (Player player : playersOrder) {
            if (player.isActive()) {
                ++activePlayers;
            }
        }
        if (activePlayers < 2) {
            return Code.NotEnoughPlayers;
        }
        int nextPlayer;
        do {
            nextPlayer = (playersOrder.indexOf(currentPlayer) + direction) % playersOrder.size();
            currentPlayer = playersOrder.get(nextPlayer);
        } while (!currentPlayer.isActive());
        return null;
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

    public Map<String, Object> getUpdateResult(Player player) {
        Map<String, Object> result = new HashMap<>();
        result.put("active", player.isActive());
        result.put("lumber", player.getResourcesNumber(Resource.lumber));
        result.put("wool", player.getResourcesNumber(Resource.wool));
        result.put("grain", player.getResourcesNumber(Resource.grain));
        result.put("brick", player.getResourcesNumber(Resource.brick));
        result.put("ore", player.getResourcesNumber(Resource.ore));
        result.put("knight", player.getDevelopmentsNumber(Development.knight));
        result.put("monopoly", player.getDevelopmentsNumber(Development.monopoly));
        result.put("roadBuilding", player.getDevelopmentsNumber(Development.roadBuilding));
        result.put("victoryPoint", player.getHiddenVictoryPoints());
        result.put("yearOfPlenty", player.getDevelopmentsNumber(Development.yearOfPlenty));
//        Set<int[]> roads = new HashSet<>();
//        for (Road road : player.getRoads()) {
//            roads.add(new int[]{road.getStart().getId(), road.getEnd().getId()});
//        }
//        result.put("roads", roads);
//        Set<Integer> settlements = new HashSet<>();
//        for (Intersection settlement : player.getSettlements()) {
//            settlements.add(settlement.getId());
//        }
//        result.put("settlements", settlements);
//        Set<Integer> cities = new HashSet<>();
//        for (Intersection city : player.getCities()) {
//            cities.add(city.getId());
//        }
//        result.put("cities", cities);
//        result.put("resourcesToDiscard", player.getResourcesToDiscard());
        result.put("usedKnights", player.getUsedKnights());
        result.put("longestRoad", player.getLongestRoadLength());
        result.put("roadsToBuild", player.getRoadsToBuild());
        result.put("hasLargestArmy", player.hasLargestArmy());
        result.put("hasLongestRoad", player.hasLongestRoad());
        result.put("publicScore", player.getPublicVictoryPoints());
        result.put("hiddenScore", player.getVictoryPoints());
        result.put("canBuyRoad", canBuyRoad(player));
        result.put("canBuySettlement", canBuySettlement(player));
        result.put("canBuyCity", canBuyCity(player));
        result.put("canBuyDevelopment", canBuyDevelopment(player));
//        result.put("availableRoadPositions", getAvailableRoadPositions(player));
//        result.put("availableSettlementPositions", getAvailableSettlementPositions(player));
//        result.put("availableCityPositions", getAvailableCityPositions(player));
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

    public Code changeTurn() {
        Player firstPlayer = playersOrder.get(0);
        Player lastPlayer = playersOrder.get(getPlayersNumber() - 1);
        if (lastPlayer.getRoadsNumber() != 1 && firstPlayer.getRoadsNumber() != 2) {
            if (changeTurn(changeTurnDirection) == Code.NotEnoughPlayers) {
                return Code.NotEnoughPlayers;
            }
        }
        if (lastPlayer.getRoadsNumber() == 1 || firstPlayer.getRoadsNumber() == 2) {
            changeTurnDirection = changeTurnDirection * -1;
        }
        return null;
    }

    //endregion

    //region Dice

    public Pair<Integer, Integer> rollDice() {
        Random dice = new Random();
        int firstDice = dice.nextInt(6) + 1;
        int secondDice = dice.nextInt(6) + 1;
//        while (firstDice + secondDice == 7) {
//            firstDice = dice.nextInt(6) + 1;
//            secondDice = dice.nextInt(6) + 1;
//        }
        return new Pair<>(firstDice, secondDice);
    }

    public void updateDiscardState() {
        for (Player player : playersOrder) {
            if (player.getResourcesNumber() > 7) {
                player.setResourcesToDiscard(player.getResourcesNumber() / 2);
            }
        }
    }

    public Map<String, Object> getRollSevenResult() {
        Map<String, Object> result = initializeRollDiceResult();
        for (Player player : playersOrder) {
            int playerIndex = playersOrder.indexOf(player);
            int resourceNumber = player.getResourcesNumber();
            if (resourceNumber > 7) {
                result.put("resourcesToDiscard_" + playerIndex, player.getResourcesToDiscard());
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
                    String argument = resource.toString() + '_' + playersOrder.indexOf(owner);
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
        for (Player player : playersOrder) {
            int playerIndex = playersOrder.indexOf(player);
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
        Player player = players.get(playerId);
        if (player.getResourcesNumber() <= 7) {
            return Code.NotDiscard;
        }
        Map<Resource, Integer> resourcesToDiscard = new HashMap<>();
        int resourcesToDiscardNumber = 0;
        for (String resourceString : requestArguments.keySet()) {
            Resource resource = Helper.getResourceFromString(resourceString);
            if (resource == null) {
                return Code.InvalidRequest;
            }
            int resourcesToDiscardByType = (Integer) requestArguments.get(resourceString);
            resourcesToDiscardNumber += resourcesToDiscardByType;
            resourcesToDiscard.put(resource, resourcesToDiscardByType);
        }
        if (resourcesToDiscardNumber != player.getResourcesToDiscard()) {
            return Code.NotHalf;
        }
        player.setResourcesToDiscard(0);
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

    public boolean checkInDiscardState() {
        for (Player player : playersOrder) {
            if (player.getResourcesToDiscard() != 0) {
                return true;
            }
        }
        return false;
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

    public List<Integer> getAvailableRoadPositions(Player player) {
        List<Integer> availableRoadPositions = new ArrayList<>();
        if (player.getSettlements().size() > 0 && player.getRoads().size() < 2) {
            Intersection lastBuilding = player.getSettlements().get(player.getSettlements().size() - 1);
            int start = lastBuilding.getId();
            for (Intersection intersection : board.getAdjacentIntersections(lastBuilding)) {
                int end = intersection.getId();
                if (end < start) {
                    availableRoadPositions.add(end);
                    availableRoadPositions.add(start);
                } else {
                    availableRoadPositions.add(start);
                    availableRoadPositions.add(end);
                }
            }
        } else {
            for (Road road : player.getRoads()) {
                for (Intersection intersection : board.getAdjacentIntersections(road.getStart())) {
                    int start = road.getStart().getId();
                    int end = intersection.getId();
                    if (start > end) {
                        int aux = start;
                        start = end;
                        end = aux;
                    }
                    if (!board.hasRoad(start, end)) {
                        availableRoadPositions.add(start);
                        availableRoadPositions.add(end);
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
                        availableRoadPositions.add(start);
                        availableRoadPositions.add(end);
                    }
                }
            }
        }
        return availableRoadPositions;
    }

    public Set<Integer> getAvailableSettlementPositions(Player player) {
        Set<Integer> availableSettlementPositions = new HashSet<>();
        if (player.getSettlements().size() < 2) {
            for (Intersection intersection : board.getIntersections()) {
                if (isAvailableSettlementPosition(intersection)) {
                    availableSettlementPositions.add(intersection.getId());
                }
            }
        } else {
            for (Road road : player.getRoads()) {
                Intersection start = road.getStart();
                Intersection end = road.getEnd();
                if (isAvailableSettlementPosition(start)) {
                    availableSettlementPositions.add(start.getId());
                }
                if (isAvailableSettlementPosition(end)) {
                    availableSettlementPositions.add(end.getId());
                }
            }
        }
        return availableSettlementPositions;
    }

    public Set<Integer> getAvailableCityPositions(Player player) {
        Set<Integer> availableCityPositions = new HashSet<>();
        for (Intersection settlement : player.getSettlements()) {
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

    public Pair<Code, Development> buyDevelopment() {
        if (!bank.hasDevelopment()) {
            return new Pair<>(Code.BankNoDevelopment, null);
        }
        Code code = currentPlayer.canBuyDevelopment();
        if (code != null) {
            return new Pair<>(code, null);
        }
        Development development = getRandomDevelopment();
        code = bank.sellDevelopment(development);
        if (code != null) {
            return new Pair<>(code, null);
        }
        code = currentPlayer.buyDevelopment(development);
        if (code != null) {
            return new Pair<>(code, null);
        }
        return new Pair<>(null, development);
    }

    protected Development getRandomDevelopment() {
        Development[] developments = {Development.knight, Development.monopoly, Development.yearOfPlenty};
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
            return Code.ForbiddenRequest;
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
            if (tradePartners.size() == 0) {
                return Code.NoPartner;
            }
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
            if (player != null && !player.equals(currentPlayer) && player.hasResource()) {
                players.put("player_" + index, player.getId());
                ++index;
            }
        }
        return players;
    }

    public Pair<Code, Resource> stealResource(String playerId) {
        Player player = players.get(playerId);
        if (player == null) {
            return new Pair<>(Code.InvalidRequest, null);
        }
        if (player.equals(currentPlayer)) {
            return new Pair<>(Code.SamePlayer, null);
        }
        if (!hasBuildingOnTile(board.getRobberPosition(), player)) {
            return new Pair<>(Code.InvalidRequest, null);
        }
        if (!player.hasResource()) {
            return new Pair<>(Code.PlayerNoResource, null);
        }
        Resource resource = player.getRandomResource();
        Code code = player.removeResource(resource);
        if (code != null) {
            return new Pair<>(Code.PlayerNoResource, null);
        }
        currentPlayer.addResource(resource);
        return new Pair<>(null, resource);
    }

    private boolean hasBuildingOnTile(Tile tile, Player player) {
        List<Intersection> adjacentIntersections = board.getAdjacentIntersections(tile);
        for (Intersection settlement : player.getSettlements()) {
            if (adjacentIntersections.contains(settlement)) {
                return true;
            }
        }
        for (Intersection city : player.getSettlements()) {
            if (adjacentIntersections.contains(city)) {
                return true;
            }
        }
        return false;
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
        for (Player player : playersOrder) {
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
