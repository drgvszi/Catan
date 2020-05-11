package catan.util;

import catan.API.response.Code;
import catan.game.enumeration.Development;
import catan.game.enumeration.Resource;

public class Helper {
    public static Resource getResourceFromString(String resource) {
        switch (resource) {
            case "lumber":
                return Resource.lumber;
            case "wool":
                return Resource.wool;
            case "grain":
                return Resource.grain;
            case "brick":
                return Resource.brick;
            case "ore":
                return Resource.ore;
            case "desert":
                return Resource.desert;
            default:
                return null;
        }
    }

    public static Code getPlayerNoResourceFromResource(Resource resource) {
        switch (resource) {
            case lumber:
                return Code.PlayerNoLumber;
            case wool:
                return Code.PlayerNoWool;
            case grain:
                return Code.PlayerNoGrain;
            case brick:
                return Code.PlayerNoBrick;
            case ore:
                return Code.PlayerNoOre;
            default:
                return null;
        }
    }

    public static Code getPlayerNotEnoughResourceFromResource(Resource resource) {
        switch (resource) {
            case lumber:
                return Code.PlayerNotEnoughLumber;
            case wool:
                return Code.PlayerNotEnoughWool;
            case grain:
                return Code.PlayerNotEnoughGrain;
            case brick:
                return Code.PlayerNotEnoughBrick;
            case ore:
                return Code.PlayerNotEnoughOre;
            default:
                return null;
        }
    }

    public static Code getBankNoResourceFromResource(Resource resource) {
        switch (resource) {
            case lumber:
                return Code.BankNoLumber;
            case wool:
                return Code.BankNoWool;
            case grain:
                return Code.BankNoGrain;
            case brick:
                return Code.BankNoBrick;
            case ore:
                return Code.BankNoOre;
            default:
                return null;
        }
    }

    public static Code getBankNotEnoughResourceFromResource(Resource resource) {
        switch (resource) {
            case lumber:
                return Code.BankNotEnoughLumber;
            case wool:
                return Code.BankNotEnoughWool;
            case grain:
                return Code.BankNotEnoughGrain;
            case brick:
                return Code.BankNotEnoughBrick;
            case ore:
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
