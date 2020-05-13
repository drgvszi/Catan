using System.Collections;
using System.Collections.Generic;
using System.Threading;
using UnityEngine.UI;
using UnityEditor;
using UnityEngine;
using FullSerializer;
using Proyecto26;
using UnityEngine.SceneManagement;
using System.IO;
using System.Text;

public class MakeRequest 
{
    public static void rollDice( string CurrentUserGame, string CurrentUserId)
    {
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = CurrentUserGame;
        command.playerId = CurrentUserId;
        //Debug.Log(CurrentUserGame);
        //Debug.Log(CurrentUserId);
        RequestJson req = new RequestJson();
        RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/rollDice", command).Then(Response=>
        {
            req.code = Response.code;
            req.status = Response.status;
            Debug.Log(Response.code);
            Debug.Log(Response.status);
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
}


