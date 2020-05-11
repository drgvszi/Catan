package catan.game.development;

import catan.game.player.Player;
import javafx.util.Pair;
import org.apache.http.HttpStatus;

import java.util.Random;

public class Knight extends Development {
    private Player player;

    public Knight() {
        super();
        player = null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Pair<Integer, String> moveRobber() {
        // TODO: Add logic.
        return new Pair<>(HttpStatus.SC_OK, "The robber was moved successfully.");
    }

    public Pair<Integer, String> stealResource() {
        if (owner == null || player == null) {
            return new Pair<>(HttpStatus.SC_ACCEPTED, "Owner or player were not set.");
        }
        int resourceNumber = player.getResourceNumber();
        if (resourceNumber == 0) {
            return new Pair<>(HttpStatus.SC_ACCEPTED, "The selected player has no more resources.");
        }
        Random random = new Random();
        int resourceIndex = random.nextInt(resourceNumber);
        owner.addResource(player.stealResource(resourceIndex));
        return new Pair<>(HttpStatus.SC_OK, "The resource was stolen successfully.");
    }
}
