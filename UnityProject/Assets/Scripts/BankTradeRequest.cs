using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BankTradeRequest :  MonoBehaviour
{
    public void bankTradeRequest()
    {
        MakeRequest.bankTrade(LoginScript.CurrentUserGameId, LoginScript.CurrentUserGEId);
    }
}
