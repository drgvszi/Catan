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

public class BankTrade : MonoBehaviour
{    
    public Toggle togle0;
    public Toggle togle1;
    public Toggle togle2;
    public Toggle togle3;
    public Toggle togle4;
    public Toggle togle5;
    public Toggle togle6;
    public Toggle togle7;
    public Toggle togle8;
    public Toggle togle9;

    
    public void Trade()
    {
        print("banktrade");
        string str=null;
        string str1=null;
        if (togle0.isOn)
            str = "lumber";
        else
            if (togle1.isOn)
            str = "brick";
        else
            if (togle2.isOn)
            str = "wool";
        else
            if (togle3.isOn)
            str = "grain";
        else
            if (togle4.isOn)
            str = "ore";

        if (togle5.isOn)
            str1 = "lumber";
        else
            if (togle6.isOn)
            str1 = "brick";
        else
            if (togle7.isOn)
            str1 = "wool";
        else
            if (togle8.isOn)
            str1 = "grain";
        else
            if (togle9.isOn)
            str1 = "ore";

        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = LoginScript.CurrentUserGameId;
        command.playerId = LoginScript.CurrentUserGEId;
        command.port = -1;
        command.offer = str;
        command.request = str1;
        print(str);
        print(str1);
        RequestJson req = new RequestJson();
        Text txt = FindTextFiel.find();
        RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/noPlayerTrade", command).Then(Response =>
        {
            Debug.Log(Response.code);
            Debug.Log(Response.status);
            txt.text = Response.status;


        }).Catch(err => { Debug.Log(err); });
    }
}
