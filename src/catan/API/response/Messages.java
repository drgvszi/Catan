package catan.API.response;

import java.util.HashMap;
import java.util.Map;

public class Messages {
    private static final Messages instance = new Messages();
    private static Map<Code, String> messages;

    private Messages() {
        messages = new HashMap<>();
        messages.put(Code.RolledSeven, "Rolled a seven.");
        messages.put(Code.RolledNotSeven, "Rolled not a seven.");
        messages.put(Code.PlayerNoLumber, "You have no more lumber.");
        messages.put(Code.PlayerNoWool, "You have no more wool.");
        messages.put(Code.PlayerNoGrain, "You have no more grain.");
        messages.put(Code.PlayerNoBrick, "You have no more brick.");
        messages.put(Code.PlayerNoOre, "You have no more ore.");
        messages.put(Code.BankNoLumber, "The bank has no more lumber.");
        messages.put(Code.BankNoWool, "The bank has no more wool.");
        messages.put(Code.BankNoGrain, "The bank has no more grain.");
        messages.put(Code.BankNoBrick, "The bank has no more brick.");
        messages.put(Code.BankNoOre, "The bank has no more ore.");
        messages.put(Code.NoRoad, "You have no more roads to build.");
        messages.put(Code.NoSettlement, "You have no more settlements to build.");
        messages.put(Code.NoCity, "You have no more cities to build.");
        messages.put(Code.SameTile, "You can not move robber on the same tile.");
        messages.put(Code.IntersectionAlreadyOccupied,"The intersection is already occupied by someone.");
        messages.put(Code.NotTwoRoadsDistance,"The two road distance is not satisfied.");
        messages.put(Code.NoSuchIntersection,"There is not such intersection");
        messages.put(Code.RoadAlreadyExistent,"Road already existent");
        messages.put(Code.RoadInvalidPosition,"Invalid position for the road");
        messages.put(Code.RoadStartNotOwned,"Road has no owned start.");
        messages.put(Code.InvalidCityPosition,"Invalid position for city.");
        messages.put(Code.InvalidSettlementPosition,"Invalid position for the settlement");
        messages.put(Code.NotEnoughResources,"Not enough resources");
    }
    public static Messages getInstance(){
        return instance;
    }

    public static String getMessage(Code code) {
        return messages.get(code);
    }
}
