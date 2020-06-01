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

public class ConnectivitySimulation implements Runnable {
    private String username = "catan";
    private String password = "catan";
    private String gameId = null;
    private List<String> playerIds = new ArrayList<>();

    // region Manager

    public ManagerResponse createGame(String scenario) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("scenario", scenario);
        String requestJson = new ObjectMapper().writeValueAsString(request);
        ManagerResponse response = HttpClientPost.managerPost(new ManagerRequest(username, password,
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

    public ManagerResponse removePlayer(String gameId, String playerId) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("gameId", gameId);
        request.put("playerId", playerId);
        String requestJson = new ObjectMapper().writeValueAsString(request);
        ManagerResponse response = HttpClientPost.managerPost(new ManagerRequest(username, password,
                "removePlayer", requestJson));
        if (response.getCode() == HttpStatus.SC_OK) {
            playerIds.remove(playerId);
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

    public ManagerResponse changePlayerStatus(String gameId, String playerId, boolean active) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("gameId", gameId);
        request.put("playerId", playerId);
        request.put("active", active);
        String requestJson = new ObjectMapper().writeValueAsString(request);
        return HttpClientPost.managerPost(new ManagerRequest(username, password,
                "changePlayerStatus", requestJson));
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

    public UserResponse stealResource(String gameId, String playerId, String answer, String player) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("answer", answer);
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

    @Override
    public void run() {
        try {
            createGame("SettlersOfCatan");

            setMaxPlayers(gameId, 3);
            addPlayer(gameId);
            addPlayer(gameId);
            addPlayer(gameId);

            removePlayer(gameId, playerIds.get(2));
            addPlayer(gameId);

            setMaxPlayers(gameId, 1);
            addPlayer(gameId);

            changePlayerStatus(gameId, playerIds.get(2), true);

            buildSettlement(gameId, playerIds.get(2), 35);

            startGame(gameId);

            startGame(gameId);

            removePlayer(gameId, playerIds.get(2));

            update(gameId, playerIds.get(1));
            update(gameId, playerIds.get(0));
            buildSettlement(gameId, playerIds.get(0), 19);
            update(gameId, playerIds.get(0));
            buildRoad(gameId, playerIds.get(0), 19, 20);

            update(gameId, playerIds.get(1));
            buildSettlement(gameId, playerIds.get(1), 40);
            update(gameId, playerIds.get(1));
            buildRoad(gameId, playerIds.get(1), 41, 40);

            update(gameId, playerIds.get(2));
            buildSettlement(gameId, playerIds.get(2), 15);
            update(gameId, playerIds.get(2));
            buildRoad(gameId, playerIds.get(2), 15, 14);

            update(gameId, playerIds.get(2));
            buildSettlement(gameId, playerIds.get(2), 35);
            update(gameId, playerIds.get(2));
            buildRoad(gameId, playerIds.get(2), 15, 4);
            buildRoad(gameId, playerIds.get(2), 34, 35);

            update(gameId, playerIds.get(1));
            buildSettlement(gameId, playerIds.get(1), 10);
            update(gameId, playerIds.get(1));
            buildRoad(gameId, playerIds.get(1), 10, 11);

            update(gameId, playerIds.get(0));
            buildSettlement(gameId, playerIds.get(0), 21);
            update(gameId, playerIds.get(0));
            buildRoad(gameId, playerIds.get(0), 20, 21);

            getRanking(gameId);

            update(gameId, playerIds.get(0));
            update(gameId, playerIds.get(1));
            update(gameId, playerIds.get(2));

            for (int index = 0; index < 18; ++index) {
                int turn = index % 3;
                if (turn == 1) {
                    changePlayerStatus(gameId, playerIds.get(0), false);
                }

                String currentPlayer = playerIds.get(turn);
                rollDice(gameId, currentPlayer);
                update(gameId, currentPlayer);
                discardResources(gameId, currentPlayer, null);
                moveRobber(gameId, currentPlayer, 3);
                stealResource(gameId, currentPlayer, "yes", playerIds.get(2 - turn));

                playerTrade(gameId, currentPlayer, null, null);
                wantToTrade(gameId, playerIds.get(turn));
                wantToTrade(gameId, playerIds.get((turn + 1) % 3));
                wantToTrade(gameId, playerIds.get((turn + 2) % 3));
                sendPartners(gameId, currentPlayer);
                selectPartner(gameId, currentPlayer, playerIds.get((turn + 1) % 3));
                update(gameId, currentPlayer);
                update(gameId, playerIds.get((turn + 1) % 3));

                buySettlement(gameId, currentPlayer, 20);

                rollDice(gameId, currentPlayer);

                buyDevelopment(gameId, currentPlayer);
                useDevelopment(gameId, currentPlayer, "roadBuilding");
                update(gameId, currentPlayer);
                buildDevelopmentRoad(gameId, currentPlayer, 31, 32);
                buildDevelopmentRoad(gameId, currentPlayer, 32, 33);

                if (turn == 0) {
                    changePlayerStatus(gameId, playerIds.get(0), false);
                }

                buyDevelopment(gameId, currentPlayer);
                useDevelopment(gameId, currentPlayer, "monopoly");
                takeResourceFromAll(gameId, currentPlayer, "desert");

                buyDevelopment(gameId, currentPlayer);
                useDevelopment(gameId, currentPlayer, "yearOfPlenty");
                takeTwoResources(gameId, currentPlayer, "wool", "lumber");

                buildRoad(gameId, currentPlayer, 20, 30);

                noPlayerTrade(gameId, currentPlayer, 10, "brick", "lumber");
                buyRoad(gameId, currentPlayer, 0, 21);

                buyCity(gameId, currentPlayer, 22);

                endTurn(gameId, currentPlayer);

                getRanking(gameId);
            }

            getRanking(gameId);
            endGame(gameId);
            getRanking(gameId);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
