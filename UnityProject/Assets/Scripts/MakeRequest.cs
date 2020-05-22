﻿using System.Collections;
using System.Collections.Generic;
using System.Threading;
using UnityEngine.UI;
using UnityEditor;
using UnityEngine;
using FullSerializer;
using Proyecto26;
using UnityEngine.SceneManagement;
using System.IO;
using SocketIO;
using System.Text;

public class MakeRequest   
{
    public static SocketIOComponent socket;

    public static void rollDice( string CurrentUserGame, string CurrentUserId)
    {
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = CurrentUserGame;
        command.playerId = CurrentUserId;
        RequestJson req = new RequestJson();
        RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/rollDice", command).Then(Response=>
        {
            req.code = Response.code;
            req.status = Response.status;
            Debug.Log("Raspunsul este: " + CurrentUserGame);
            Debug.Log("Raspunsul2 este: " + CurrentUserId);
        }).Catch(err => { Debug.Log(err); });

    }
    public static void buildSettlement(string CurrentUserGame, string CurrentUserId)
    {
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = CurrentUserGame;
        command.playerId = CurrentUserId;
        command.intersection = 25;
        RequestJson req = new RequestJson();
        RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/buildSettlement", command).Then(Response =>
        {
            req.code = Response.code;
            req.status = Response.status;
            Debug.Log(req.code);
            Debug.Log(req.status);
        }).Catch(err => { Debug.Log(err); });
    }
    public static void buildRoad(string CurrentUserGame, string CurrentUserId)
    {
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = CurrentUserGame;
        command.playerId = CurrentUserId;
        command.intersection = 0;
        command.start = 25;
        command.end = 24;
        //Debug.Log(CurrentUserGame);
        //Debug.Log(CurrentUserId);
        RequestJson req = new RequestJson();
        RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/buildRoad", command).Then(Response =>
        {
            req.code = Response.code;
            req.status = Response.status;
            Debug.Log(req.code);
            Debug.Log(req.status);
        }).Catch(err => { Debug.Log(err); });
    }
     public static void endTurn(string CurrentUserGame, string CurrentUserId)
    {
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = CurrentUserGame;
        command.playerId = CurrentUserId;
        command.intersection = 0;
        command.start = 0;
        command.end = 0;
        //Debug.Log(CurrentUserGame);
        //Debug.Log(CurrentUserId);
        RequestJson req = new RequestJson();
        RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/endTurn", command).Then(Response =>
        {
            req.code = Response.code;
            req.status = Response.status;
            req.arguments = Response.arguments;
            Debug.Log(req.code);
            Debug.Log(req.arguments);
            Debug.Log(req.status);
        }).Catch(err => { Debug.Log(err); });
    }
    public static void buyCity(string CurrentUserGame, string CurrentUserId)
    {
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = CurrentUserGame;
        command.playerId = CurrentUserId;
        command.intersection = 20;
        RequestJson req = new RequestJson();
        RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/buyCity", command).Then(Response =>
        {
            Debug.Log(Response.code);
            Debug.Log(Response.status);
            if (Response.code == 202)
            {
                JSONObject json_message = new JSONObject();
                json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);
                json_message.AddField("username", LoginScript.CurrentUser);
                json_message.AddField("intersection", command.intersection);
                socket.Emit("buyCity", json_message);
            }
        }).Catch(err => { Debug.Log(err); });
    }
}


