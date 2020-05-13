using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using FullSerializer;
using Proyecto26;

[Serializable]
public class RequestJson
{
    public  int code;
    public string[] arguments = new string[256];
    public string status;
}
