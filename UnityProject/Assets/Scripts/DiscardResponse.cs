using System.Collections;
using System.Collections.Generic;
using UnityEngine;
[System.Serializable]
public class DiscardResponseArgs
{
    public bool sentAll;
}
[System.Serializable]
public class DiscardResponse 
{
    public int code;
    public string status;
    public DiscardResponseArgs arguments = new DiscardResponseArgs();
}
