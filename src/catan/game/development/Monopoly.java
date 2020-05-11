package catan.game.development;

import catan.game.player.Player;
import catan.game.enumeration.Resource;
import javafx.util.Pair;
import org.apache.http.HttpStatus;

import java.util.List;

public class Monopoly extends Development {
    private Resource resource;
    private List<Player> players;

    public Monopoly() {
        super();
        resource = null;
        players = null;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Pair<Integer, String> use() {
        if (owner == null || resource == null || players == null) {
            return new Pair<>(HttpStatus.SC_ACCEPTED, "Owner, resourceType or players were not set.");
        }
        for (Player player : players) {
            if (owner != player) {
                int resourceNumber = player.getResourceNumber(resource);
                if (resourceNumber > 0) {
                    owner.addResource(resource, resourceNumber);
                    player.removeResource(resource, resourceNumber);
                }
            }
        }
        return new Pair<>(HttpStatus.SC_OK, "The " + resource + " from all other players was took successfully.");
    }
}
