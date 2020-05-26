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
    public Text ilumber;
    public Text ibirck;
    public Text iore;
    public Text iwool;
    public Text igrain;
    public static SocketIOComponent socket;
    public void pressed()
    {
        Text txt = FindTextFiel.find();
        MakeRequestResponse command2 = new MakeRequestResponse();
        command2.gameId = LoginScript.CurrentUserGameId;
        command2.playerId = LoginScript.CurrentUserGEId;
        command2.answer = "yes";
        command2.player = tex1.text;

        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        RestClient.Post<MoveRobberRequest>("https://catan-connectivity.herokuapp.com/game/stealResource", command2).Then(Response2 =>
        {
            Debug.Log("Stolen");
            txt.text = Response2.status;
            JSONObject json_message = new JSONObject();
            json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);
            socket.Emit("Stolen", json_message);

            MakeRequestResponse command1 = new MakeRequestResponse();
             command1.gameId = LoginScript.CurrentUserGameId;
            command1.playerId = LoginScript.CurrentUserGEId;
             RequestJson req1 = new RequestJson();

           

            RestClient.Post<UpdateJson>("https://catan-connectivity.herokuapp.com/game/update", command1).Then(Response1 =>
        {
            Debug.Log("Update code " + Response1.code);
            Debug.Log("Update status " + Response1.status);
            Debug.Log("Update arguments lumber " + Response1.arguments.lumber);

            print("end turn info");
            print(Response1.arguments.lumber.ToString());
            print(Response1.arguments.ore.ToString());
            print(Response1.arguments.grain.ToString());
            print(Response1.arguments.brick.ToString());
            print(Response1.arguments.wool.ToString());

            ilumber.text = Response1.arguments.lumber.ToString();
            iore.text = Response1.arguments.ore.ToString();
            igrain.text = Response1.arguments.grain.ToString();
            ibirck.text = Response1.arguments.brick.ToString();
            iwool.text = Response1.arguments.wool.ToString();
        }).Catch(err => { Debug.Log(err); });


        }).Catch(err => { Debug.Log(err); });




        


    }
}
