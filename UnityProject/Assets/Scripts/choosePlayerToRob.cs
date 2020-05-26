using System.Collections;
using System.Collections.Generic;
using System.Threading;
using UnityEngine.UI;
using UnityEditor;
using UnityEngine;
using FullSerializer;
using Proyecto26;
using UnityEngine.SceneManagement;
using System.Text;
using SocketIO;
using System;
using System.Runtime.InteropServices;

public class choosePlayerToRob : MonoBehaviour
{
    public Text tex1;

    public void pressed()
    {
        Text txt = FindTextFiel.find();
        MakeRequestResponse command2 = new MakeRequestResponse();
        command2.gameId = LoginScript.CurrentUserGameId;
        command2.playerId = LoginScript.CurrentUserGEId;
        command2.answer = "yes";
        command2.player = tex1.text;
        RestClient.Post<MoveRobberRequest>("https://catan-connectivity.herokuapp.com/game/stealResource", command2).Then(Response2 =>
        {
            Debug.Log("Stolen");
            txt.text = Response2.status;
        }).Catch(err => { Debug.Log(err); });
        
    }
}
