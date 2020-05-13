package catan.util;

import catan.API.response.Code;
import catan.game.enumeration.Development;
import catan.game.enumeration.Port;
import catan.game.enumeration.Resource;

public class Helper {
    public static Resource getResourceFromString(String resource) {
        switch (resource) {
            case "lumber":
                return Resource.Lumber;
            case "wool":
                return Resource.Wool;
            case "grain":
                return Resource.Grain;
            case "brick":
                return Resource.Brick;
            case "ore":
                return Resource.Ore;
            default:
                return null;
        }
    }

    public static Resource getResourceOfferFromString(String resource) {
        switch (resource) {
            case "lumber_o":
                return Resource.Lumber;
            case "wool_o":
                return Resource.Wool;
            case "grain_o":
                return Resource.Grain;
            case "brick_o":
                return Resource.Brick;
            case "ore_o":
                return Resource.Ore;
            default:
                return null;
        }
    }

    public static Resource getResourceRequestFromString(String resource) {
        switch (resource) {
            case "lumber_r":
                return Resource.Lumber;
            case "wool_r":
                return Resource.Wool;
            case "grain_r":
                return Resource.Grain;
            case "brick_r":
                return Resource.Brick;
            case "ore_r":
                return Resource.Ore;
            default:
                return null;
        }
    }

    public static Resource getResourceFromPort(Port port) {
        switch (port) {
            case Lumber:
                return Resource.Lumber;
            case Wool:
                return Resource.Wool;
            case Grain:
                return Resource.Grain;
            case Brick:
                return Resource.Brick;
            case Ore:
                return Resource.Ore;
            default:
                return null;
        }
    }

    public static Development getDevelopmentFromString(String development) {
        switch (development) {
            case "knight":
                return Development.knight;
            case "monopoly":
                return Development.monopoly;
            case "roadBuilding":
                return Development.roadBuilding;
            case "victoryPoint":
                return Development.victoryPoint;
            case "yearOfPlenty":
                return Development.yearOfPlenty;
            default:
                return null;
        }
    }

    public static Code getPlayerNoResourceFromResource(Resource resource) {
        switch (resource) {
            case Lumber:
                return Code.PlayerNoLumber;
            case Wool:
                return Code.PlayerNoWool;
            case Grain:
                return Code.PlayerNoGrain;
            case Brick:
                return Code.PlayerNoBrick;
            case Ore:
                return Code.PlayerNoOre;
            default:
                return null;
        }
    }

    public static Code getPlayerNotEnoughResourceFromResource(Resource resource) {
        switch (resource) {
            case Lumber:
                return Code.PlayerNotEnoughLumber;
            case Wool:
                return Code.PlayerNotEnoughWool;
            case Grain:
                return Code.PlayerNotEnoughGrain;
            case Brick:
                return Code.PlayerNotEnoughBrick;
            case Ore:
                return Code.PlayerNotEnoughOre;
            default:
                return null;
        }
    }

    public static Code getPlayerNoDevelopmentFromDevelopment(Development development) {
        switch (development) {
            case knight:
                return Code.PlayerNoKnight;
            case monopoly:
                return Code.PlayerNoMonopoly;
            case roadBuilding:
                return Code.PlayerNoRoadBuilding;
            case yearOfPlenty:
                return Code.PlayerNoYearOfPlenty;
            default:
                return null;
        }
    }

    public static Code getBankNoResourceFromResource(Resource resource) {
        switch (resource) {
            case Lumber:
                return Code.BankNoLumber;
            case Wool:
                return Code.BankNoWool;
            case Grain:
                return Code.BankNoGrain;
            case Brick:
                return Code.BankNoBrick;
            case Ore:
                return Code.BankNoOre;
            default:
                return null;
        }
    }

    public static Code getBankNotEnoughResourceFromResource(Resource resource) {
        switch (resource) {
            case Lumber:
                return Code.BankNotEnoughLumber;
            case Wool:
                return Code.BankNotEnoughWool;
            case Grain:
                return Code.BankNotEnoughGrain;
            case Brick:
                return Code.BankNotEnoughBrick;
            case Ore:
                return Code.BankNotEnoughOre;
            default:
                return null;
        }
    }

    public static Code getBankNoDevelopmentFromDevelopment(Development development) {
        switch (development) {
            case knight:
                return Code.BankNoKnight;
            case monopoly:
                return Code.BankNoMonopoly;
            case roadBuilding:
                return Code.BankNoRoadBuilding;
            case yearOfPlenty:
                return Code.BankNoYearOfPlenty;
            default:
                return null;
        }
    }
}
