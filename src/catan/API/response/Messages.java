package catan.API.response;

import java.util.HashMap;
import java.util.Map;

public class Messages {
    private static Map<Code, String> messages;

    static {
        messages = new HashMap<>();
        messages.put(Code.RolledSeven, "Rolled a seven.");
        messages.put(Code.RolledNotSeven, "Rolled not a seven.");

        messages.put(Code.PlayerNotEnoughLumber, "You do not have enough lumber.");
        messages.put(Code.PlayerNotEnoughWool, "You do not have enough wool.");
        messages.put(Code.PlayerNotEnoughGrain, "You do not have enough grain.");
        messages.put(Code.PlayerNotEnoughBrick, "You do not have enough brick.");
        messages.put(Code.PlayerNotEnoughOre, "You do not have enough ore.");

        messages.put(Code.PlayerNoLumber, "You have no more lumber.");
        messages.put(Code.PlayerNoWool, "You have no more wool.");
        messages.put(Code.PlayerNoGrain, "You have no more grain.");
        messages.put(Code.PlayerNoBrick, "You have no more brick.");
        messages.put(Code.PlayerNoOre, "You have no more ore.");

        messages.put(Code.NotConnectsToRoad, "You do not connect to one of your roads.");

        messages.put(Code.PlayerHasDev, "You have the development.");
        messages.put(Code.PlayerNoDev, "You don't have that development.");
        messages.put(Code.MonopolySuccess, "Monopoly successfully used.");
        messages.put(Code.KnightSuccess, "Knight successfully used.");
        messages.put(Code.RoadBuildingSuccess, "Road Building successfully used.");
        messages.put(Code.YearOfPlentySuccess, "Year of Plenty successfully used.");
        messages.put(Code.ResourceNotSet, "The resource wasn't set.");
        messages.put(Code.FirstResourceNotSet, "The first resource wasn't set.");
        messages.put(Code.SecondResourceNotSet, "The second resource wasn't set.");
        messages.put(Code.BankNoLumber, "The bank has no more lumber.");
        messages.put(Code.BankNoWool, "The bank has no more wool.");
        messages.put(Code.BankNoGrain, "The bank has no more grain.");
        messages.put(Code.BankNoBrick, "The bank has no more brick.");
        messages.put(Code.BankNoOre, "The bank has no more ore.");
        messages.put(Code.NoRoad, "You have no more roads to build.");
        messages.put(Code.NoSettlement, "You have no more settlements to build.");
        messages.put(Code.NoCity, "You have no more cities to build.");
        messages.put(Code.SameTile, "You can not move robber on the same tile.");
        messages.put(Code.IntersectionAlreadyOccupied, "The intersection is already occupied by someone.");
        messages.put(Code.NotTwoRoadsDistance, "The two road distance is not satisfied.");
        messages.put(Code.NoSuchIntersection, "There is not such intersection");
        messages.put(Code.RoadAlreadyExistent, "Road already existent");
        messages.put(Code.RoadInvalidPosition, "Invalid position for the road");
        messages.put(Code.RoadStartNotOwned, "Road has no owned start.");
        messages.put(Code.InvalidCityPosition, "Invalid position for city.");
        messages.put(Code.InvalidSettlementPosition, "Invalid position for the settlement");
        messages.put(Code.NotEnoughResources, "Not enough resources");

        messages.put(Code.InvalidRequest, "Invalid request.");
    }

    public static String getMessage(Code code) {
        return messages.get(code);
    }
}
