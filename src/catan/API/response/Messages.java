package catan.API.response;

import java.util.HashMap;
import java.util.Map;

public class Messages {
    private static Map<Code, String> messages;

    static {
        messages = new HashMap<>();

        //region Dice

        messages.put(Code.DiceSeven, "The dice sum is seven.");
        messages.put(Code.DiceNotSeven, "The dice sum is not seven.");
        messages.put(Code.NotDiscard, "You do not have more than seven\nresource cards to discard half of them.");
        messages.put(Code.NotHalf, "You have not discarded\nhalf of your resource cards.");
        messages.put(Code.DiscardState, "Somebody has not discarded\nhalf of their resource cards yet.");

        //endregion

        //region Player

        messages.put(Code.PlayerNotEnoughLumber, "You do not have enough\nLumber resource cards.");
        messages.put(Code.PlayerNotEnoughWool, "You do not have enough\nWool resource cards.");
        messages.put(Code.PlayerNotEnoughGrain, "You do not have enough\nGrain resource cards.");
        messages.put(Code.PlayerNotEnoughBrick, "You do not have enough\nBrick resource cards.");
        messages.put(Code.PlayerNotEnoughOre, "You do not have enough\nOre resource cards.");

        messages.put(Code.PlayerNoLumber, "You do not have\nLumber resource cards.");
        messages.put(Code.PlayerNoWool, "You do not have\nWool resource cards.");
        messages.put(Code.PlayerNoGrain, "You do not have\nGrain resource cards.");
        messages.put(Code.PlayerNoBrick, "You do not have\nBrick resource cards.");
        messages.put(Code.PlayerNoOre, "You do not have\nOre resource cards.");

        messages.put(Code.PlayerNoKnight, "You do not have\nKnight development cards.");
        messages.put(Code.PlayerNoMonopoly, "You do not have\nMonopoly development cards.");
        messages.put(Code.PlayerNoRoadBuilding, "You do not have\nRoad Building development cards.");
        messages.put(Code.PlayerNoYearOfPlenty, "You do not have\nYear of Plenty development cards.");

        //endregion

        //region Bank

        messages.put(Code.BankNotEnoughLumber, "The bank does not have enough\nLumber resource cards.");
        messages.put(Code.BankNotEnoughWool, "The bank does not have enough\nWool resource cards.");
        messages.put(Code.BankNotEnoughGrain, "The bank does not have enough\nGrain resource cards.");
        messages.put(Code.BankNotEnoughBrick, "The bank does not have enough\nBrick resource cards.");
        messages.put(Code.BankNotEnoughOre, "The bank does not have enough\nOre resource cards.");

        messages.put(Code.BankNoResource, "The bank does not have\nany resource cards.");
        messages.put(Code.BankNoLumber, "The bank does not have\nLumber resource cards.");
        messages.put(Code.BankNoWool, "The bank does not have\nWool resource cards.");
        messages.put(Code.BankNoGrain, "The bank does not have\nGrain resource cards.");
        messages.put(Code.BankNoBrick, "The bank does not have\nBrick resource cards.");
        messages.put(Code.BankNoOre, "The bank does not have\nOre resource cards.");


        messages.put(Code.BankNoDevelopment, "The bank does not have\nany development cards.");
        messages.put(Code.BankNoKnight, "The bank does not have\nKnight development cards.");
        messages.put(Code.BankNoMonopoly, "The bank does not have\nMonopoly development cards.");
        messages.put(Code.BankNoRoadBuilding, "The bank does not have\nRoad Building development cards.");
        messages.put(Code.BankNoYearOfPlenty, "The bank does not have\nYear of Plenty development cards.");

        messages.put(Code.BankNoRoad, "You do not have roads in bank.");
        messages.put(Code.BankNoSettlement, "You do not have\nsettlements in bank.");
        messages.put(Code.BankNoCity, "You do not have cities in bank.");

        //endregion

        //region Properties

        messages.put(Code.NoAvailableRoadPosition, "You have no more available\nroad positions.");
        messages.put(Code.NoAvailableSettlementPosition, "You have no more available\nsettlement positions.");
        messages.put(Code.NoAvailableCityPosition, "You have no more available\ncity positions.");

        messages.put(Code.InvalidRoadPosition, "Invalid position for road.");
        messages.put(Code.InvalidSettlementPosition, "Invalid position for settlement.");
        messages.put(Code.InvalidCityPosition, "Invalid position for city.");

        messages.put(Code.IntersectionAlreadyOccupied, "Intersection already occupied.");
        messages.put(Code.DistanceRuleViolated, "The two roads distance rule\nis not satisfied.");
        messages.put(Code.NotConnectsToIntersection, "It does not connect\nto one of your intersections.");
        messages.put(Code.NotConnectsToRoad, "It does not connect\nto one of your roads.");
        messages.put(Code.RoadAlreadyExistent, "Road already existent.");

        messages.put(Code.NoRoad, "You have no more\nroads to build.");
        messages.put(Code.NoSettlement, "You have no more\nsettlements to build.");
        messages.put(Code.NoCity, "You have no more\ncities to build.");

        //endregion

        //region Robber

        messages.put(Code.SameTile, "You can not let the robber\non the same tile.");
        messages.put(Code.SamePlayer, "You can not steal\na resource card from yourself.");
        messages.put(Code.PlayerNoResource, "The player does not have\nresource cards.");

        //endregion

        //region Trade

        messages.put(Code.NoTradeAvailable, "No trade available.");
        messages.put(Code.AlreadyInTrade, "You are already in trade.");
        messages.put(Code.NotInTrade, "The selected player is not in trade.");
        messages.put(Code.NoPartner, "Nobody wanted to trade.");
        messages.put(Code.InvalidPortOffer, "The offer does not match the port.");

        //endregion

        //region Unknown

        messages.put(Code.InvalidRequest, "Invalid request.");
        messages.put(Code.ForbiddenRequest, "Forbidden request.");

        //endregion
    }

    public static String getMessage(Code code) {
        return messages.get(code);
    }
}
