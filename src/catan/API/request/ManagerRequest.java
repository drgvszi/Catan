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
                    synchronized (randomString) {
                        do {
                            gameId = randomString.nextString();
                        } while (Application.games.containsKey(gameId) || Application.players.contains(gameId));
                    }
                    synchronized (Application.games) {
                        Application.games.put(gameId, new BaseGame());
                    }
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
                synchronized (randomString) {
                    do {
                        playerId = randomString.nextString();
                    } while (Application.games.containsKey(playerId) || Application.players.contains(playerId));
                }
                synchronized (Application.players) {
                    Application.players.add(playerId);
                }
                synchronized (Application.games.get(gameId)) {
                    game.addPlayer(playerId, new Player(playerId, Application.games.get(gameId)));
                    game.addNextPlayer(playerId);
                }
                Map<String, String> payload = new HashMap<>();
                payload.put("playerId", playerId);
                String responseJson = new ObjectMapper().writeValueAsString(payload);
                return new ManagerResponse(HttpStatus.SC_OK, "The player was added successfully.", responseJson);
            }
            case "removePlayer": {
                if (requestJson == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game identifier is not specified.", null);
                }
                String gameId = requestJson.get("gameId");
                Game game = Application.games.get(gameId);
                if (game == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game does not exist.", null);
                }
                String playerId = requestJson.get("playerId");
                Player player = game.getPlayer(playerId);
                if (player == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The player does not exist.", null);
                }
                if (game.getBank() != null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game has already started.", null);
                }
                game.getPlayersOrder().remove(game.getPlayer(playerId));
                game.getPlayers().remove(playerId);
                return new ManagerResponse(HttpStatus.SC_OK, "The player was removed successfully.", null);
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
                if (game.getBank() != null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game has already started.", null);
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
            case "changePlayerStatus": {
                if (requestJson == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game identifier is not specified.", null);
                }
                String gameId = requestJson.get("gameId");
                Game game = Application.games.get(gameId);
                if (game == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The game does not exist.", null);
                }
                String playerId = requestJson.get("playerId");
                Player player = game.getPlayer(playerId);
                if (player == null) {
                    return new ManagerResponse(HttpStatus.SC_ACCEPTED, "The player does not exist.", null);
                }
                boolean active = Boolean.parseBoolean(requestJson.get("active"));
                if (active == player.isActive()) {
                    return new ManagerResponse(HttpStatus.SC_OK, "The player status has not been changed.", null);
                }
                player.setActive(active);
                if (game.getBank() != null) {
                    if (game.getCurrentPlayer().getId().equals(playerId) && !active) {
                        game.changeTurn(1);
                    }
                }
                return new ManagerResponse(HttpStatus.SC_OK, "The player status has been changed successfully.", null);
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
                Application.games.remove(gameId);
                return new ManagerResponse(HttpStatus.SC_OK, "The game has ended successfully.", null);
            }
            default:
                return new ManagerResponse(HttpStatus.SC_ACCEPTED, Messages.getMessage(Code.InvalidRequest), command);
        }
    }
}
