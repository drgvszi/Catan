using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
[Serializable]
public class ArgumentsSendParteners
{
    public string player_0 = null;
    public string player_1 = null;
    public string player_2 = null;
}
[Serializable]
public class SendPartenersJson 
{
    public int code;
    public ArgumentsSendParteners arguments = new ArgumentsSendParteners();
    public string status;
}
