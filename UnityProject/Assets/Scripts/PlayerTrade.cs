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

public class PlayerTrade : MonoBehaviour
{
   public InputField infild0;
    public InputField infild1;
    public InputField infild2;
    public InputField infild3;
    public InputField infild4;
    public InputField infild5;
    public InputField infild6;
    public InputField infild7;
    public InputField infild8;
    public InputField infild9;
    public SocketIOComponent socket;

    public Text lumber;
    public Text ore;
    public Text brick;
    public Text grain;
    public Text wool;

    public void trade()
    {
        print("playertrade");
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        TradePlayerJson command = new TradePlayerJson();
        command.gameId = LoginScript.CurrentUserGameId;
        command.playerId = LoginScript.CurrentUserGEId;
       
        if (infild0.text == "")
            command.lumber_o = 0;
        else
        command.lumber_o = int.Parse(infild0.text);

        if (infild1.text =="")
            command.wool_o = 0;
        else
        command.wool_o = int.Parse(infild1.text);

        if (infild2.text =="")
            command.grain_o = 0;
        else
        command.grain_o = int.Parse(infild2.text);

        if (infild3.text == "")
            command.brick_o = 0;
        else
            command.brick_o = int.Parse(infild3.text);

        if (infild4.text == "")
            command.ore_o = 0;
        else
        command.ore_o = int.Parse(infild4.text);

        if (infild5.text == "")
            command.lumber_r = 0;
        else
            command.lumber_r = int.Parse(infild5.text);

        if (infild6.text == "")
            command.wool_r = 0;
        else
            command.wool_r = int.Parse(infild6.text);

        if (infild7.text == "")
            command.grain_r = 0;
        else
            command.grain_r = int.Parse(infild7.text);

        if (infild8.text == "")
            command.brick_r = 0;
        else
            command.brick_r = int.Parse(infild8.text);

        if (infild9.text == "")
            command.ore_r = 0;
        else
            command.ore_r = int.Parse(infild9.text);

        print(command.lumber_o);
        print(command.brick_o);
        print(command.brick_r);
        print(command.ore_r);
        print("aaaaaa");

        RequestJson req = new RequestJson();
        Text txt = FindTextFiel.find();
        RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/playerTrade", command).Then(Response =>
        {

            Debug.Log(Response.code);
            Debug.Log(Response.status);
            txt.text = Response.status;
            if (Response.code == 200)
            {

                JSONObject json_message = new JSONObject();
                json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);
                json_message.AddField("username", LoginScript.CurrentUser);
                json_message.AddField("lumber_o", command.lumber_o);
                json_message.AddField("wool_o", command.wool_o);
                json_message.AddField("grain_o", command.grain_o);
                json_message.AddField("brick_o", command.brick_o);
                json_message.AddField("ore_o", command.ore_o);
                json_message.AddField("lumber_r", command.lumber_r);
                json_message.AddField("wool_r", command.wool_r);
                json_message.AddField("grain_r", command.grain_r);
                json_message.AddField("brick_r", command.brick_r);
                json_message.AddField("ore_r", command.ore_r);

                socket.Emit("playerTrade", json_message);
            }


            

        }).Catch(err => { Debug.Log(err); });

            MakeRequestResponse command1 = new MakeRequestResponse();
            command1.gameId = LoginScript.CurrentUserGameId;
            command1.playerId = LoginScript.CurrentUserGEId;
            RequestJson req1 = new RequestJson();
            RestClient.Post<UpdateJson>("https://catan-connectivity.herokuapp.com/game/update", command1).Then(Response1 =>
            {
                Debug.Log("Update code " + Response1.code);
                Debug.Log("Update status " + Response1.status);
                Debug.Log("Update arguments lumber " + Response1.arguments.lumber);
                Debug.Log("Update arguments settle " + Response1.arguments.settlements[1]);
                // Debug.Log("Update roads " + Response.arguments.roads[0][1]);//NU MERGE ASTA
                // Debug.Log("Update roads " + Response.arguments.roads[0][0]);//NU MERGE NICI ASTA

                lumber.text = Response1.arguments.lumber.ToString();
                ore.text = Response1.arguments.ore.ToString();
                grain.text = Response1.arguments.grain.ToString();
                brick.text = Response1.arguments.brick.ToString();
                wool.text = Response1.arguments.wool.ToString();

            }).Catch(err => { Debug.Log(err); });

    }
}
