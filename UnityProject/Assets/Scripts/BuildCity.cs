using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BuildCity : MonoBehaviour
{
    public void buyCity()
    {
        MakeRequest.buyCity(LoginScript.CurrentUserGameId, LoginScript.CurrentUserGEId);
    }
}
