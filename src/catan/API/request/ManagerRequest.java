package catan.API.request;

import catan.API.response.Code;
import catan.API.response.ManagerResponse;
import catan.API.response.Messages;
import catan.Application;
import catan.game.game.BaseGame;
import catan.game.game.Game;
import catan.game.player.Player;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ManagerRequest implements GameRequest {
    private String username;
    private String password;
    private String command;
    private String arguments;

    public ManagerRequest(String username, String password, String command, String arguments) {
        this.username = username;
        this.password = password;
        this.command = command;
        this.arguments = arguments;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public ManagerResponse run() throws JsonProcessingException {
        Map<String, String> requestJson = GameRequest.getMapFromData(arguments);

        switch (command) {
            case "newGame":
                if (requestJson == null || requestJson.get("scenario") == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "No scenario is specified.", null);
                }
                if (requestJson.get("scenario").equals("SettlersOfCatan")) {
                    String gameId;
                    do {
                        gameId = randomString.nextString();
                    } while (Application.games.containsKey(gameId) || Application.players.contains(gameId));
                    Application.games.put(gameId, new BaseGame());
                    Map<String, String> payload = new HashMap<>();
                    payload.put("gameId", gameId);
                    String responseJson = new ObjectMapper().writeValueAsString(payload);
                    return new ManagerResponse(HttpStatus.SC_OK, "The game was created successfully.", responseJson);
                }
                return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The scenario is not implemented.", null);
            case "setMaxPlayers": {
                if (requestJson == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The maximum number of players is not specified.", null);
                }
                String gameId = requestJson.get("gameId");
                Game game = Application.games.get(gameId);
                if (game == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game does not exist.", null);
                }
                int maxPlayers = Integer.parseInt(requestJson.get("maxPlayers"));
                if (game.getPlayersNumber() > maxPlayers) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "There are already more players.", null);
                }
                if (game.getCurrentPlayer() != null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game has already started.", null);
                }
                game.setMaxPlayers(maxPlayers);
                return new ManagerResponse(HttpStatus.SC_OK, "The maximum number of players was set successfully.", null);
            }
            case "addPlayer": {
                if (requestJson == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game identifier is not specified.", null);
                }
                String gameId = requestJson.get("gameId");
                Game game = Application.games.get(gameId);
                if (game == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game does not exist.", null);
                }
                if (game.getPlayersNumber() == game.getMaxPlayers()) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "There is no room left.", null);
                }
                if (game.getBank() != null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game has already started.", null);
                }
                String playerId;
                do {
                    playerId = randomString.nextString();
                } while (Application.games.containsKey(playerId) || Application.players.contains(playerId));
                game.addPlayer(playerId, new Player(playerId, Application.games.get(gameId)));
                game.addNextPlayer(playerId);
                Map<String, String> payload = new HashMap<>();
                payload.put("playerId", playerId);
                String responseJson = new ObjectMapper().writeValueAsString(payload);
                return new ManagerResponse(HttpStatus.SC_OK, "The player was added successfully.", responseJson);
            }
            case "startGame": {
                if (requestJson == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game identifier is not specified.", null);
                }
                String gameId = requestJson.get("gameId");
                Game game = Application.games.get(gameId);
                if (game == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game does not exist.", null);
                }
                if (game.startGame()) {
                    Map<String, String> payload = new HashMap<>();
                    payload.put("board", game.getBoard().getBoardJson());
                    payload.put("ports", game.getBoard().getPortsJson());
                    String responseJson = new ObjectMapper().writeValueAsString(payload);
                    return new ManagerResponse(HttpStatus.SC_OK, "The game has started successfully.", responseJson);
                }
                return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game can not start without players.", null);
            }
            case "getRanking": {
                if (requestJson == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game identifier is not specified.", null);
                }
                String gameId = requestJson.get("gameId");
                Game game = Application.games.get(gameId);
                if (game == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game does not exist.", null);
                }
                if (game.getBank() == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game has not started yet.", null);
                }
                String responseJson = new ObjectMapper().writeValueAsString(game.getRankingResult());
                return new ManagerResponse(HttpStatus.SC_OK, "Here is the current ranking.", responseJson);
            }
            case "endGame": {
                if (requestJson == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game identifier is not specified.", null);
                }
                String gameId = requestJson.get("gameId");
                Game game = Application.games.get(gameId);
                if (game == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game does not exist.", null);
                }
                if (game.getBank() == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game has not started yet.", null);
                }
                //TODO
                Application.games.remove(gameId);
                return new ManagerResponse(HttpStatus.SC_OK, "The game has ended successfully.", null);
            }
            default:
                return new ManagerResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest), command);
        }
    }
}
