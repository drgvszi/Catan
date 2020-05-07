package catan;

import catan.API.response.Response;
import catan.API.controller.HttpClientPost;
import catan.API.request.GameRequest;
import catan.API.request.ManagerRequest;
import catan.API.request.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

public class ConnectivitySimulation {
    String username = "catan";
    String password = "catan";
    String gameID = null;
    List<String> playersID = new ArrayList<>();

    // region Manager Commands

    public String createGame() throws IOException {
        Response response;
        response = HttpClientPost.managerPost(new ManagerRequest(username, password, "newGame", "{\"scenario\": \"SettlersOfCatan\"}"));
        if (response.getCode() != HttpStatus.SC_OK)
            return null;
        else {
            Map<String, String> args = GameRequest.getMapFromData(response.getArguments());
            return args.get("gameId");
        }
    }

    public String startGame(String gameId) throws IOException {
        Map<String, String> payload = new HashMap<>();
        payload.put("gameId", gameId);
        String jsonArgs = new ObjectMapper().writeValueAsString(payload);
        Response response;
        response = HttpClientPost.managerPost(new ManagerRequest(username, password,
                "startGame", jsonArgs));
        if (response.getCode() != HttpStatus.SC_OK)
            return null;
        else
            return response.getStatus();
    }

    public boolean setMaxPlayers(String gameId, Integer no) throws IOException {
        Map<String, String> payload = new HashMap<>();
        payload.put("gameId", gameId);
        payload.put("maxPlayers", no.toString());
        String jsonArgs = new ObjectMapper().writeValueAsString(payload);
        Response response;
        response = HttpClientPost.managerPost(new ManagerRequest(username, password,
                "setMaxPlayers", jsonArgs));
        return response.getCode() == HttpStatus.SC_OK;
    }

    public String addPlayer(String gameId) throws IOException {
        Map<String, String> payload = new HashMap<>();
        payload.put("gameId", gameId);
        String jsonArgs = new ObjectMapper().writeValueAsString(payload);
        Response response;
        response = HttpClientPost.managerPost(new ManagerRequest(username, password,
                "addPlayer", jsonArgs));
        if (response.getCode() != HttpStatus.SC_OK)
            return null;
        else {
            Map<String, String> args = GameRequest.getMapFromData(response.getArguments());
            return args.get("playerId");
        }
    }

    // endregion

    // region User Commands

    public boolean rollDice(String gameID, String playerId) throws IOException {
        Response response;
        response = HttpClientPost.userPost(new UserRequest(gameID, playerId, "rollDice", ""));
        return response.getCode() == HttpStatus.SC_OK;
    }

    public boolean buyRoad(String gameId, String playerId, Integer intersection) throws IOException {
        Map<String, String> payload = new HashMap<>();
        payload.put("start", intersection.toString());
        payload.put("end", intersection.toString());
        String jsonArgs = new ObjectMapper().writeValueAsString(payload);
        Response response;
        response = HttpClientPost.userPost(new UserRequest(gameId, playerId,
                "buyRoad/" + intersection, jsonArgs));
        return response.getCode() == HttpStatus.SC_OK;
    }

    public boolean endTurn(String gameId, String playerId) throws IOException {
        Response response;
        response = HttpClientPost.userPost(new UserRequest(gameId, playerId,
                "endTurn", ""));
        return response.getCode() == HttpStatus.SC_OK;
    }

    public boolean buySettlement(String gameId, String playerId, Integer intersection) throws IOException {
        Map<String, Integer> payload = new HashMap<>();
        payload.put("intersection", intersection);
        String jsonArgs = new ObjectMapper().writeValueAsString(payload);
        Response response;
        response = HttpClientPost.userPost(new UserRequest(gameId, playerId,
                "buySettlement", jsonArgs));
        return response.getCode() == HttpStatus.SC_OK;
    }

    public boolean buyCity(String gameId, String playerId, Integer intersection) throws IOException {
        Map<String, Integer> payload = new HashMap<>();
        payload.put("intersection", intersection);
        String jsonArgs = new ObjectMapper().writeValueAsString(payload);
        Response response;
        response = HttpClientPost.userPost(new UserRequest(gameId, playerId,
                "buyCity", jsonArgs));
        return response.getCode() == HttpStatus.SC_OK;
    }

    public boolean useDevelopment(String gameId, String playerId, String development) throws IOException {
        Map<String, String> payload = new HashMap<>();
        payload.put("development", development);
        String jsonArgs = new ObjectMapper().writeValueAsString(payload);
        Response response;
        response = HttpClientPost.userPost(new UserRequest(gameId, playerId,
                "useDevelopment", jsonArgs));
        return response.getCode() == HttpStatus.SC_OK;
    }

    public boolean selectOpponent(String gameId, String playerId, String opponentId) throws IOException {
        Response response;
        response = HttpClientPost.userPost(new UserRequest(gameId, playerId,
                "selectOpponent", opponentId));
        return response.getCode() == HttpStatus.SC_OK;
    }
    public boolean buildSettlement(String gameId, String playerId,Integer intersection) throws IOException{
        Map<String, String> payload = new HashMap<>();
        payload.put("intersection", intersection.toString());
        String jsonArgs = new ObjectMapper().writeValueAsString(payload);
        Response response;
        response=HttpClientPost.userPost(new UserRequest(gameId,playerId,"buildSettlement",jsonArgs));
        return response.getCode() == HttpStatus.SC_OK;
    }
    public boolean buildRoad(String gameId, String playerId,Integer start,Integer end) throws IOException{
        Map<String, String> payload = new HashMap<>();
        payload.put("start", start.toString());
        payload.put("end", end.toString());
        String jsonArgs = new ObjectMapper().writeValueAsString(payload);
        Response response;
        response=HttpClientPost.userPost(new UserRequest(gameId,playerId,"buildRoad",jsonArgs));
        return response.getCode() == HttpStatus.SC_OK;
    }

    public boolean playerTrade(String gameId, String playerId, String offerRequest) throws IOException {
        Response response;
        response = HttpClientPost.userPost(new UserRequest(gameId, playerId,
                "playerTrade", offerRequest));
        return response.getCode() == HttpStatus.SC_OK;
    }


    //endregion

    public void simulation() throws IOException, InterruptedException {
        // Configure the game
        gameID = createGame();
        setMaxPlayers(gameID, 2);
        playersID.add(addPlayer(gameID));
        playersID.add(addPlayer(gameID));
        playersID.add(addPlayer(gameID));
        setMaxPlayers(gameID, 1);
        startGame(gameID);

        buildSettlement(gameID,playersID.get(0),20);
        buildRoad(gameID,playersID.get(0),20,19);

        buildSettlement(gameID,playersID.get(1),40);
        buildRoad(gameID,playersID.get(1),41,40);

        buildSettlement(gameID,playersID.get(1),10);
        buildRoad(gameID,playersID.get(1),10,11);

        buildSettlement(gameID,playersID.get(0),30);
        buildRoad(gameID,playersID.get(0),30,31);




        // Run the game
        for (int i = 0; i < 2; ++i) {
            rollDice(gameID, playersID.get(0));
            playerTrade(gameID, playersID.get(0), "");
            selectOpponent(gameID, playersID.get(0), "");
            buySettlement(gameID, playersID.get(0), 20);
            rollDice(gameID, playersID.get(0));
            sleep(100);
            buyRoad(gameID, playersID.get(1), 42);
            useDevelopment(gameID, playersID.get(0), "");
            endTurn(gameID, playersID.get(0));
            sleep(100);
            rollDice(gameID, playersID.get(1));
            playerTrade(gameID, playersID.get(1), "");
            selectOpponent(gameID, playersID.get(1), "");
            buySettlement(gameID, playersID.get(1), 22);
            sleep(100);
            endTurn(gameID, playersID.get(1));
        }
    }
}
