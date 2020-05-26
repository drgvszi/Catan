using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
[Serializable]
public class RoadsAndPositions{
    public int number1;
    public int number2;
}
[Serializable]
public class UpdateJsonArguments
{
    public bool active;
    public int lumber;
    public int wool;
    public int grain;
    public int brick;
    public int ore;
    public int knight;
    public int monopoly;
    public int roadBuilding;
    public int victoryPoint;
    public int yearOfPlenty;
    public int[] settlements = new int[100];
    public int[] cities = new int[100];
    //ROADS SI availableRoadPositions  NU MERG
    /*public int[][] roads = new int[100][];
    public int[][] availableRoadPositions = new int[100][];*/
    /*public RoadsAndPositions[] roads = new RoadsAndPositions[100];
    public RoadsAndPositions[] availableRoadPositions = new RoadsAndPositions[100];*/
    public int[] availableCityPositions = new int[100];
    public int[] availableSettlementPositions = new int[100];
    public int longestRoad;
    public int usedKnights;
    public int roadsToBuild;
    public bool hasLargestArmy;
    public bool hasLongestRoad;
    public int publicScore;
    public int hiddenScore;
    public bool canBuyRoad;
    public bool canBuySettlement;
    public bool canBuyCity;
    public bool canBuyDevelopment;
}
[Serializable]
public class UpdateJson 
{
    public int code;
    public string status;
    public UpdateJsonArguments arguments = new UpdateJsonArguments();

}
