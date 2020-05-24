using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AcceptTrade : MonoBehaviour
{
    public void acceptTrade()
    {
        MakeRequest.acceptTrade(LoginScript.CurrentUserGameId, LoginScript.CurrentUserGEId);
    }
    
}
