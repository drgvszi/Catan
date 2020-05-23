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

    public void trade()
    {
        print("playertrade");
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        TradePlayerJson command = new TradePlayerJson();
        command.gameId = LoginScript.CurrentUserGameId;
        command.playerId = LoginScript.CurrentUserGEId;
        command.lumber_o = int.Parse(infild0.text);
        command.wool_o = int.Parse(infild1.text);
        command.grain_o = int.Parse(infild2.text);
        command.brick_o = int.Parse(infild3.text);
        command.ore_o = int.Parse(infild4.text);
        command.lumber_r = int.Parse(infild5.text);
        command.wool_r = int.Parse(infild6.text);
        command.grain_r = int.Parse(infild7.text);
        command.brick_r = int.Parse(infild8.text);
        command.ore_r = int.Parse(infild9.text);
        RequestJson req = new RequestJson();
        Text txt = FindTextFiel.find();
        RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/playerTrade", command).Then(Response =>
        {
            Debug.Log(Response.code);
            Debug.Log(Response.status);
            txt.text = Response.status;
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

        }).Catch(err => { Debug.Log(err); });
    }
}
