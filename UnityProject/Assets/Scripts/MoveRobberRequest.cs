using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
[Serializable]
public class MoveRobberArguments
{
    public string player_0;
    public string player_1;
    public string player_2;
}
[Serializable]
public class MoveRobberRequest 
{
    public string status;
    public int code;
    public MoveRobberArguments arguments = new MoveRobberArguments();
}
