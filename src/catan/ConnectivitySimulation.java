package catan;

import catan.API.controller.HttpClientPost;
import catan.API.request.GameRequest;
import catan.API.request.ManagerRequest;
import catan.API.request.UserRequest;
import catan.API.response.ManagerResponse;
import catan.API.response.UserResponse;
import catan.game.enumeration.Resource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectivitySimulation {
    private String username = "catan";
    private String password = "catan";
    private String gameId = null;
    private List<String> playerIds = new ArrayList<>();

    // region Manager

    public ManagerResponse createGame(String scenario) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("scenario", scenario);
        String requestJson = new ObjectMapper().writeValueAsString(request);
        ManagerResponse response =  HttpClientPost.managerPost(new ManagerRequest(username, password,
                "newGame", requestJson));
        if (response.getCode() == HttpStatus.SC_OK) {
            Map<String, String> arguments = GameRequest.getMapFromData(response.getArguments());
            if (arguments != null) {
                gameId = arguments.get("gameId");
            }
        }
        return response;
    }

    public ManagerResponse setMaxPlayers(String gameId, int players) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("gameId", gameId);
        request.put("maxPlayers", players);
        String requestJson = new ObjectMapper().writeValueAsString(request);
        return HttpClientPost.managerPost(new ManagerRequest(username, password,
                "setMaxPlayers", requestJson));
    }

    public ManagerResponse addPlayer(String gameId) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("gameId", gameId);
        String requestJson = new ObjectMapper().writeValueAsString(request);
        ManagerResponse response = HttpClientPost.managerPost(new ManagerRequest(username, password,
                "addPlayer", requestJson));
        if (response.getCode() == HttpStatus.SC_OK) {
            Map<String, String> arguments = GameRequest.getMapFromData(response.getArguments());
            if (arguments != null) {
                playerIds.add(arguments.get("playerId"));
            }
        }
        return response;
    }

    public ManagerResponse startGame(String gameId) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("gameId", gameId);
        String requestJson = new ObjectMapper().writeValueAsString(request);
        return HttpClientPost.managerPost(new ManagerRequest(username, password,
                "startGame", requestJson));
    }

    public ManagerResponse getRanking(String gameId) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("gameId", gameId);
        String requestJson = new ObjectMapper().writeValueAsString(request);
        return HttpClientPost.managerPost(new ManagerRequest(username, password,
                "getRanking", requestJson));
    }

    public ManagerResponse endGame(String gameId) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("gameId", gameId);
        String requestJson = new ObjectMapper().writeValueAsString(request);
        return HttpClientPost.managerPost(new ManagerRequest(username, password,
                "endGame", requestJson));
    }

    // endregion

    // region User

    //region First Two Rounds

    public UserResponse buildSettlement(String gameId, String playerId, int intersection) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("intersection", intersection);
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "buildSettlement", request));
    }

    public UserResponse buildRoad(String gameId, String playerId, int start, int end) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("start", start);
        request.put("end", end);
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "buildRoad", request));
    }

    //endregion

    //region Dice

    public UserResponse rollDice(String gameId, String playerId) throws IOException {
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "rollDice", null));
    }

    public UserResponse discardResources(String gameId, String playerId, Map<Resource, Integer> resources)
            throws IOException {
        if (resources == null) {
            return HttpClientPost.userPost(new UserRequest(gameId, playerId, "discardResources", null));
        }
        Map<String, Object> request = new HashMap<>();
        for (Resource resource : resources.keySet()) {
            request.put(resource.toString(), resources.get(resource));
        }
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "discardResources", request));
    }

    //endregion

    //region Robber

    public UserResponse moveRobber(String gameId, String playerId,
                                         int tile) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("tile", tile);
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "moveRobber", request));
    }

    public UserResponse stealResource(String gameId, String playerId, String player) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("player", player);
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "stealResource", request));
    }

    //endregion

    //region Trade

    public UserResponse playerTrade(String gameId, String playerId, Map<Resource, Integer> offer,
                                    Map<Resource, Integer> request) throws IOException {
        if (offer == null || request == null) {
            return HttpClientPost.userPost(new UserRequest(gameId, playerId, "playerTrade", null));
        }
        Map<String, Object> requestArguments = new HashMap<>();
        for (Resource resource : offer.keySet()) {
            requestArguments.put(resource.toString() + "_o", offer.get(resource));
        }
        for (Resource resource : request.keySet()) {
            requestArguments.put(resource.toString() + "_r", request.get(resource));
        }
        return HttpClientPost.userPost(new UserRequest(gameId, playerId,
                "playerTrade", requestArguments));
    }

    public UserResponse wantToTrade(String gameId, String playerId) throws IOException {
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "wantToTrade", null));
    }

    public UserResponse sendPartners(String gameId, String playerId) throws IOException {
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "sendPartners", null));
    }

    public UserResponse selectPartner(String gameId, String playerId, String player) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("player", player);
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "selectPartner", request));
    }

    public UserResponse noPlayerTrade(String gameId, String playerId, int port, String offer, String request)
            throws IOException {
        Map<String, Object> requestArguments = new HashMap<>();
        requestArguments.put("port", port);
        requestArguments.put("offer", offer);
        requestArguments.put("request", request);
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "noPlayerTrade", requestArguments));
    }

    //endregion

    //region Buy

    public UserResponse buyRoad(String gameId, String playerId, int start, int end) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("start", start);
        request.put("end", end);
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "buyRoad", request));
    }

    public UserResponse buySettlement(String gameId, String playerId, int intersection) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("intersection", intersection);
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "buySettlement", request));
    }

    public UserResponse buyCity(String gameId, String playerId, int intersection) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("intersection", intersection);
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "buyCity", request));
    }

    public UserResponse buyDevelopment(String gameId, String playerId) throws IOException {
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "buyDevelopment", null));
    }

    //endregion

    //region Development

    public UserResponse useDevelopment(String gameId, String playerId, String development) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("development", development);
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "useDevelopment", request));
    }

    public UserResponse takeResourceFromAll(String gameId, String playerId, String resource) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("resource", resource);
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "takeResourceFromAll", request));
    }

    public UserResponse buildDevelopmentRoad(String gameId, String playerId, int start, int end) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("start", start);
        request.put("end", end);
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "buildDevelopmentRoad", request));
    }

    public UserResponse takeTwoResources(String gameId, String playerId, String firstResource, String secondResource)
            throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("resource_1", firstResource);
        request.put("resource_2", secondResource);
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "takeTwoResources", request));
    }

    //endregion

    //region Update

    public UserResponse update(String gameId, String playerId) throws IOException {
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "update", null));
    }

    public UserResponse endTurn(String gameId, String playerId) throws IOException {
        return HttpClientPost.userPost(new UserRequest(gameId, playerId, "endTurn", null));
    }

    //endregion

    //endregion

    public void simulation() throws IOException {
        createGame("SettlersOfCatan");
        setMaxPlayers(gameId, 2);
        addPlayer(gameId);
        addPlayer(gameId);
        setMaxPlayers(gameId, 1);
        addPlayer(gameId);
        startGame(gameId);

        buildSettlement(gameId, playerIds.get(0), 20);
        buildRoad(gameId, playerIds.get(0), 18, 19);
        buildRoad(gameId, playerIds.get(0), 19, 20);
        buildSettlement(gameId, playerIds.get(1), 40);
        buildRoad(gameId, playerIds.get(1), 41, 40);

        buildSettlement(gameId, playerIds.get(1), 10);
        buildRoad(gameId, playerIds.get(1), 10, 11);
        buildSettlement(gameId, playerIds.get(0), 30);
        buildRoad(gameId, playerIds.get(0), 30, 31);

        update(gameId, playerIds.get(0));

        for (int i = 0; i < 2; ++i) {
            rollDice(gameId, playerIds.get(0));
            discardResources(gameId, playerIds.get(0), null);
            moveRobber(gameId, playerIds.get(0), 3);
            stealResource(gameId, playerIds.get(0), playerIds.get(1));

            playerTrade(gameId, playerIds.get(0), null, null);
            wantToTrade(gameId, playerIds.get(1));
            sendPartners(gameId, playerIds.get(0));
            selectPartner(gameId, playerIds.get(0), playerIds.get(1));

            buySettlement(gameId, playerIds.get(0), 20);
            rollDice(gameId, playerIds.get(0));
            buyRoad(gameId, playerIds.get(0), 10, 11);

            buyDevelopment(gameId, playerIds.get(0));
            useDevelopment(gameId, playerIds.get(0), "roadBuilding");
            buildDevelopmentRoad(gameId, playerIds.get(0), 2, 3);
            takeResourceFromAll(gameId, playerIds.get(0), "ores");
            takeTwoResources(gameId, playerIds.get(0), "wool", "lumber");

            buildRoad(gameId, playerIds.get(0), 20, 30);
            endTurn(gameId, playerIds.get(0));

            rollDice(gameId, playerIds.get(1));
            buySettlement(gameId, playerIds.get(0), 25);
            buyCity(gameId, playerIds.get(1), 22);
            endTurn(gameId, playerIds.get(0));
            endTurn(gameId, playerIds.get(1));
            getRanking(gameId);
        }
        endGame(gameId);
        getRanking(gameId);
    }
}
