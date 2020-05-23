using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerTradeRequest : MonoBehaviour
{
    public void playerTradeRequest()
    {
        MakeRequest.playerTrade(LoginScript.CurrentUserGameId, LoginScript.CurrentUserGEId);
    }
}
