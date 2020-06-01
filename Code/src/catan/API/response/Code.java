package catan.API.response;

public enum Code {
    //region Dice

    DiceSeven,
    DiceNotSeven,
    NotDiscard,
    NotHalf,
    DiscardState,

    //endregion

    //region Player

    PlayerNoLumber,
    PlayerNoWool,
    PlayerNoGrain,
    PlayerNoBrick,
    PlayerNoOre,

    PlayerNotEnoughLumber,
    PlayerNotEnoughWool,
    PlayerNotEnoughGrain,
    PlayerNotEnoughBrick,
    PlayerNotEnoughOre,

    PlayerNoKnight,
    PlayerNoMonopoly,
    PlayerNoRoadBuilding,
    PlayerNoYearOfPlenty,

    //endregion

    //region Bank

    BankNotEnoughLumber,
    BankNotEnoughWool,
    BankNotEnoughGrain,
    BankNotEnoughBrick,
    BankNotEnoughOre,

    BankNoLumber,
    BankNoWool,
    BankNoGrain,
    BankNoBrick,
    BankNoOre,

    BankNoResource,

    BankNoDevelopment,
    BankNoKnight,
    BankNoMonopoly,
    BankNoRoadBuilding,
    BankNoYearOfPlenty,

    BankNoRoad,
    BankNoSettlement,
    BankNoCity,

    //endregion Bank

    //region Properties

    NoAvailableRoadPosition,
    NoAvailableSettlementPosition,
    NoAvailableCityPosition,

    InvalidRoadPosition,
    InvalidSettlementPosition,
    InvalidCityPosition,

    IntersectionAlreadyOccupied,
    DistanceRuleViolated,
    NotConnectsToIntersection,
    NotConnectsToRoad,
    RoadAlreadyExistent,

    NoRoad,
    NoSettlement,
    NoCity,

    //endregion

    //region Robber

    SameTile,
    SamePlayer,
    PlayerNoResource,

    //endregion

    //region Trade

    NoTradeAvailable,
    AlreadyInTrade,
    NotInTrade,
    NoPartner,
    InvalidPortOffer,

    //endregion Trade

    //region Turn

    NotEnoughPlayers,
    FoundWinner,

    //endregion

    //region Unknown

    InvalidRequest,
    ForbiddenRequest

    //endregion
}
