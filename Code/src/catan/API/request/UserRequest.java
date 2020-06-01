package catan.API.request;

import catan.API.response.UserResponse;
import catan.Application;
import catan.game.game.Game;
import org.apache.http.HttpStatus;

import java.util.Map;

public class UserRequest implements GameRequest {
    private String gameId;
    private String playerId;
    private String command;
    private Map<String, Object> arguments;

    public UserRequest(String gameId, String playerId, String command, Map<String, Object> arguments) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.command = command;
        this.arguments = arguments;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }

    public UserResponse run() {
        Game game = Application.games.get(gameId);
        if (game == null) {
            return new UserResponse(HttpStatus.SC_ACCEPTED, "The game does not exist.", null);
        }
        if (game.getPlayer(playerId) == null) {
            return new UserResponse(HttpStatus.SC_ACCEPTED, "The player does not exist.", null);
        }
        if (game.getBank() == null) {
            return new UserResponse(HttpStatus.SC_ACCEPTED, "The game has not started yet.", null);
        }
        return game.playTurn(playerId, command, arguments);
    }
}
