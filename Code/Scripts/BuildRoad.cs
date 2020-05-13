using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BuildRoad : MonoBehaviour
{
    public  void buildRoad()
    {
        MakeRequest.buildRoad(LoginScript.CurrentUserGameId, LoginScript.CurrentUserGEId);
    }
}
