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
    public static void bankTrade(string CurrentUserGame, string CurrentUserId)
    {
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = CurrentUserGame;
        command.playerId = CurrentUserId;
        command.port = -1;
        command.offer = "wool";
        command.request = "lumber";
        RequestJson req = new RequestJson();
        RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/noPlayerTrade", command).Then(Response =>
        {
            Debug.Log(Response.code);
            Debug.Log(Response.status);
        }).Catch(err => { Debug.Log(err); });
    }
    public static void playerTrade(string CurrentUserGame, string CurrentUserId)
    {
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        TradePlayerJson command = new TradePlayerJson();
        command.gameId = CurrentUserGame;
        command.playerId = CurrentUserId;
        command.lumber_o = 1;
        command.wool_o = 2;
        command.grain_o = 3;
        command.brick_o = 4;
        command.ore_o = 5;
        command.lumber_r = 6;
        command.wool_r = 7;
        command.grain_r = 8;
        command.brick_r = 9;
        command.ore_r = 10;
        RequestJson req = new RequestJson();
        RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/playerTrade", command).Then(Response =>
        {
            Debug.Log(Response.code);
            Debug.Log(Response.status);

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
    public static void acceptTrade(string CurrentUserGame, string CurrentUserId)
    {
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        TradePlayerJson command = new TradePlayerJson();
        command.gameId = CurrentUserGame;
        command.playerId = CurrentUserId;
        RequestJson req = new RequestJson();
        RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/wantToTrade", command).Then(Response =>
        {
            Debug.Log("Accept Trade " + Response.code);
            Debug.Log("Accdept Trade " + Response.status);
            JSONObject json_message = new JSONObject();
            json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);
            json_message.AddField("username", LoginScript.CurrentUser);
            json_message.AddField("gameEngineId", LoginScript.CurrentUserGEId);
            json_message.AddField("wantToTrade","true");
            socket.Emit("wantToTrade", json_message);

        }).Catch(err => { Debug.Log(err); });
    }
    public static void declineTrade(string CurrentUserGame, string CurrentUserId)
    {
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        JSONObject json_message = new JSONObject();
        json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);
        json_message.AddField("username", LoginScript.CurrentUser);
        json_message.AddField("gameEngineId", LoginScript.CurrentUserGEId);
        json_message.AddField("wantToTrade", "false");
        socket.Emit("wantToTrade", json_message);
    }

    /* public static void sendParteners(string CurrentUserGame, string CurrentUserId)
     {
         TradePlayerJson command = new TradePlayerJson();
         command.gameId = CurrentUserGame;
         command.playerId = CurrentUserId;
         RequestJson req = new RequestJson();
         RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/sendParteners", command).Then(Response =>
         {
             Debug.Log("SendParteners " + Response.code);
             Debug.Log("Send Parteners " + Response.status);

         }).Catch(err => { Debug.Log(err); });
     }*/


    public static void buyDevelopment(string CurrentUserGame, string CurrentUserId)
    {
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = CurrentUserGame;
        command.playerId = CurrentUserId;
        //Debug.Log(CurrentUserGame);
        //Debug.Log(CurrentUserId);
        RequestJsonDevelopmentExceptMonopoly req = new RequestJsonDevelopmentExceptMonopoly();
        RestClient.Post<RequestJsonDevelopmentExceptMonopoly>("https://catan-connectivity.herokuapp.com/game/buyDevelopment", command).Then(Response =>
        {
            req.code = Response.code;
            req.status = Response.status;
            req.arguments = Response.arguments;
            Debug.Log(req.code);
            Debug.Log(req.status);
            Debug.Log(req.arguments);
        }).Catch(err => { Debug.Log(err); });
    }

    public static void useDevelopment(string CurrentUserGame, string CurrentUserId, string development)
    {
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = CurrentUserGame;
        command.playerId = CurrentUserId;
        command.development = development;
        //Debug.Log(CurrentUserGame);
        //Debug.Log(CurrentUserId);
        RequestJsonDevelopmentExceptMonopoly req = new RequestJsonDevelopmentExceptMonopoly();
        RestClient.Post<RequestJsonDevelopmentExceptMonopoly>("https://catan-connectivity.herokuapp.com/game/useDevelopment", command).Then(Response =>
        {
            req.code = Response.code;
            req.status = Response.status;
            Debug.Log(req.code);
            Debug.Log(req.status);
        }).Catch(err => { Debug.Log(err); });
    }

    public static void takeResourceFromAll(string CurrentUserGame, string CurrentUserId, string resource)
    {
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = CurrentUserGame;
        command.playerId = CurrentUserId;
        command.resource = resource;
        //Debug.Log(CurrentUserGame);
        //Debug.Log(CurrentUserId);s
        RequestJsonDevelopmentMonopoly req = new RequestJsonDevelopmentMonopoly();
        RestClient.Post<RequestJsonDevelopmentMonopoly>("https://catan-connectivity.herokuapp.com/game/takeResourceFromAll", command).Then(Response =>
        {
            req.code = Response.code;
            req.status = Response.status;
            req.arguments = Response.arguments;
            Debug.Log(req.code);
            Debug.Log(req.arguments);
            Debug.Log(req.status);
        }).Catch(err => { Debug.Log(err); });
    }

    public static void buildDevelopmentRoad(string CurrentUserGame, string CurrentUserId, int start, int end)
    {
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = CurrentUserGame;
        command.playerId = CurrentUserId;
        command.start = start;
        command.end = end;
        //Debug.Log(CurrentUserGame);
        //Debug.Log(CurrentUserId);
        RequestJsonDevelopmentExceptMonopoly req = new RequestJsonDevelopmentExceptMonopoly();
        RestClient.Post<RequestJsonDevelopmentExceptMonopoly>("https://catan-connectivity.herokuapp.com/game/buildDevelopmentRoad", command).Then(Response =>
        {
            req.code = Response.code;
            req.status = Response.status;
            Debug.Log(req.code);
            Debug.Log(req.status);
        }).Catch(err => { Debug.Log(err); });
    }

    public static void takeTwoResources(string CurrentUserGame, string CurrentUserId, string resource_0, string resource_1)
    {
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = CurrentUserGame;
        command.playerId = CurrentUserId;
        command.resource_0 = resource_0;
        command.resource_1 = resource_1;
        //Debug.Log(CurrentUserGame);
        //Debug.Log(CurrentUserId);
        RequestJsonDevelopmentExceptMonopoly req = new RequestJsonDevelopmentExceptMonopoly();
        RestClient.Post<RequestJsonDevelopmentExceptMonopoly>("https://catan-connectivity.herokuapp.com/game/takeTwoResources", command).Then(Response =>
        {
            req.code = Response.code;
            req.status = Response.status;
            Debug.Log(req.code);
            Debug.Log(req.status);
        }).Catch(err => { Debug.Log(err); });
    }

}


