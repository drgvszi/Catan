using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;

[Serializable]
public class ArgumentsDevelopmentExceptMonopoly
{
    public string development;
}


[Serializable]
public class RequestJsonDevelopmentExceptMonopoly
{
    public int code;
    public ArgumentsDevelopmentExceptMonopoly arguments = new ArgumentsDevelopmentExceptMonopoly();
    public string status;
}
