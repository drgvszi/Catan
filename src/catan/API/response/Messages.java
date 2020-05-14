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
        messages.put(Code.NotDiscard, "You do not have more than seven resource cards in order to discard half of them.");

        //endregion

        //region Player

        messages.put(Code.PlayerNotEnoughLumber, "You do not have enough Lumber resource cards.");
        messages.put(Code.PlayerNotEnoughWool, "You do not have enough Wool resource cards.");
        messages.put(Code.PlayerNotEnoughGrain, "You do not have enough Grain resource cards.");
        messages.put(Code.PlayerNotEnoughBrick, "You do not have enough Brick resource cards.");
        messages.put(Code.PlayerNotEnoughOre, "You do not have enough Ore resource cards.");

        messages.put(Code.PlayerNoLumber, "You do not have Lumber resource cards.");
        messages.put(Code.PlayerNoWool, "You do not have Wool resource cards.");
        messages.put(Code.PlayerNoGrain, "You do not have Grain resource cards.");
        messages.put(Code.PlayerNoBrick, "You do not have Brick resource cards.");
        messages.put(Code.PlayerNoOre, "You do not have Ore resource cards.");

        messages.put(Code.PlayerNoKnight, "You do not have Knight development cards.");
        messages.put(Code.PlayerNoMonopoly, "You do not have Monopoly development cards.");
        messages.put(Code.PlayerNoRoadBuilding, "You do not have Road Building development cards.");
        messages.put(Code.PlayerNoYearOfPlenty, "You do not have Year of Plenty development cards.");

        //endregion

        //region Bank

        messages.put(Code.BankNotEnoughLumber, "The bank does not have enough Lumber resource cards.");
        messages.put(Code.BankNotEnoughWool, "The bank does not have enough Wool resource cards.");
        messages.put(Code.BankNotEnoughGrain, "The bank does not have enough Grain resource cards.");
        messages.put(Code.BankNotEnoughBrick, "The bank does not have enough Brick resource cards.");
        messages.put(Code.BankNotEnoughOre, "The bank does not have enough Ore resource cards.");

        messages.put(Code.BankNoLumber, "The bank does not have Lumber resource cards.");
        messages.put(Code.BankNoWool, "The bank does not have Wool resource cards.");
        messages.put(Code.BankNoGrain, "The bank does not have Grain resource cards.");
        messages.put(Code.BankNoBrick, "The bank does not have Brick resource cards.");
        messages.put(Code.BankNoOre, "The bank does not have Ore resource cards.");

        messages.put(Code.BankNoDevelopment, "The bank does not have any development cards.");
        messages.put(Code.BankNoKnight, "The bank does not have Knight development cards.");
        messages.put(Code.BankNoMonopoly, "The bank does not have Monopoly development cards.");
        messages.put(Code.BankNoRoadBuilding, "The bank does not have Road Building development cards.");
        messages.put(Code.BankNoYearOfPlenty, "The bank does not have Year of Plenty development cards.");

        messages.put(Code.BankNoRoad, "You do not have roads in bank.");
        messages.put(Code.BankNoSettlement, "You do not have settlements in bank.");
        messages.put(Code.BankNoCity, "You do not have cities in bank.");

        //endregion

        //region Properties

        messages.put(Code.InvalidRoadPosition, "Invalid position for road.");
        messages.put(Code.InvalidSettlementPosition, "Invalid position for settlement.");
        messages.put(Code.InvalidCityPosition, "Invalid position for city.");

        messages.put(Code.IntersectionAlreadyOccupied, "Intersection already occupied.");
        messages.put(Code.DistanceRuleViolated, "The two roads distance rule is not satisfied.");
        messages.put(Code.NotConnectsToRoad, "You do not connect to one of your roads.");
        messages.put(Code.RoadAlreadyExistent, "Road already existent.");

        messages.put(Code.NoRoad, "You have no more roads to build.");
        messages.put(Code.NoSettlement, "You have no more settlements to build.");
        messages.put(Code.NoCity, "You have no more cities to build.");

        //endregion

        //region Robber

        messages.put(Code.SameTile, "You can not let the robber on the same tile.");
        messages.put(Code.SamePlayer, "You can not steal a resource card from yourself.");
        messages.put(Code.PlayerNoResource, "The player does not have resource cards.");

        //endregion

        //region Trade

        messages.put(Code.InvalidTradeRequest, "Invalid trade request.");
        messages.put(Code.NoTradeAvailable, "No trade available.");
        messages.put(Code.AlreadyInTrade, "You are already in trade.");
        messages.put(Code.NotInTrade, "The selected player is not in trade.");
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
