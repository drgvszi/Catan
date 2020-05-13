using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EndTurn : MonoBehaviour
{
    public void endTurn()
    {
        MakeRequest.endTurn(LoginScript.CurrentUserGameId, LoginScript.CurrentUserGEId);
    }
}
