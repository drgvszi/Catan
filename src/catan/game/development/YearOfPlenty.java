package catan.game.development;

import catan.game.bank.Bank;
import catan.game.enumeration.Resource;
import javafx.util.Pair;
import org.apache.http.HttpStatus;

public class YearOfPlenty extends Development {
    private Bank bank;
    private Resource resource;

    public YearOfPlenty() {
        super();
        resource = null;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Pair<Integer, String> takeResource() {
        if (owner == null || bank == null || resource == null) {
            return new Pair<>(HttpStatus.SC_ACCEPTED, "Owner, bank or resourceType were not set.");
        }
        if (!bank.hasResource(resource)) {
            return new Pair<>(HttpStatus.SC_ACCEPTED, "The bank has no more " + resource + ".");
        }
        owner.addResource(resource);
        return new Pair<>(HttpStatus.SC_ACCEPTED, "The " + resource + " from the bank was took successfully.");
    }
}
