using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using FullSerializer;
using Proyecto26;

[Serializable]
public class ArgumentsDevelopmentMonopoly
{
    public string player_0;   
    public int resources_0; 
    public string player_1;
    public int resources_1;
}


[Serializable]
public class RequestJsonDevelopmentMonopoly
{
    public int code;
    public ArgumentsDevelopmentMonopoly arguments = new ArgumentsDevelopmentMonopoly();
    public string status;
}
