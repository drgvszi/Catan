using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using FullSerializer;
using Proyecto26;

[Serializable]
public class UnityConnectivityCommand
{
    public String username;
    public String lobbyid = "";
    public bool active = false;
    public String gameId;
    public String playerId;
}
