using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BuildSettlement : MonoBehaviour
{
   public void  BuildSet()
    {
        MakeRequest.buildSettlement(LoginScript.CurrentUserGameId, LoginScript.CurrentUserGEId);
    }
}
