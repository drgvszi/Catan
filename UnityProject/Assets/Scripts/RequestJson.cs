using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using FullSerializer;
using Proyecto26;
[Serializable]
public class ArgumentsRollDice
{
    public int dice_1;
    public int dice_2;
    public string player_0;
    public int lumber_0;
    public int wool_0;
    public int grain_0;
    public int brick_0;
    public int ore_0;
    public int resourcesToDiscard_0;
    public string player_1;
    public int lumber_1;
    public int wool_1;
    public int grain_1;
    public int brick_1;
    public int ore_1;
    public int resourcesToDiscard_1;
    public string player_2;
    public int lumber_2;
    public int wool_2;
    public int grain_2;
    public int brick_2;
    public int ore_2;
    public int resourcesToDiscard_2;
    public string player_3;
    public int lumber_3;
    public int wool_3;
    public int grain_3;
    public int brick_3;
    public int ore_3;
    public int resourcesToDiscard_3;
}

[Serializable]
public class RequestJson
{
    public  int code;
    public ArgumentsRollDice arguments = new ArgumentsRollDice();
    public string status;
}
