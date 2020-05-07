package catan.API.request;

import catan.API.response.Response;
import catan.Application;
import catan.game.game.Game;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpStatus;

public class UserRequest implements GameRequest {
    private String gameId;
    private String playerId;
    private String command;
    private String arguments;

    public UserRequest(String gameId, String playerId, String command, String arguments) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.command = command;
        this.arguments = arguments;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) { this.gameId = gameId; }

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

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) { this.arguments = arguments; }

    public Response run() {
        Game game = Application.games.get(gameId);
        if (game == null) {
            return new Response(HttpStatus.SC_ACCEPTED, "The game does not exist.","");
        }
        if (game.getPlayer(playerId) == null) {
            return new Response(HttpStatus.SC_ACCEPTED,"The player does not exist.","");
        }
        try {
            return game.playTurn(playerId, command, GameRequest.getMapFromData(arguments));
        } catch (JsonProcessingException e) {
            return new Response(HttpStatus.SC_ACCEPTED, "Wrong command.", "");
        }
    }
}