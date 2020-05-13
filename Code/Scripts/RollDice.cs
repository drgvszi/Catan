using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class RollDice : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        /*{   "gameId" : "yEVbTUBMgyfIZHX0zC2xP",   "playerId" : "L7nagkPh24tlbX4BYLzOd",   "command" : "rollDice",   "arguments" : null }*/
    }
    public void Roll()
    {
        MakeRequest.rollDice(LoginScript.CurrentUserGameId, LoginScript.CurrentUserGEId);
    }
}
