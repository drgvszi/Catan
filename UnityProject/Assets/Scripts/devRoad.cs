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

public class devRoad : MonoBehaviour
{
    public GameObject allPieces;


    public GameObject piece;
    public GameObject newPiece1;
    public GameObject newPiece2;
    public GameObject newPiece3;
    public GameObject newPiece4;

    public int capat1;
    public int capat2;

    public Text ver;

    public SocketIOComponent socket;

    void OnMouseDown()
    {
        if (ver.text == "0")
        {
            GameObject go = GameObject.Find("SocketIO");
            socket = go.GetComponent<SocketIOComponent>();
            Text txt = FindTextFiel.find();
            MakeRequestResponse command = new MakeRequestResponse();
            command.gameId = LoginScript.CurrentUserGameId;
            command.playerId = LoginScript.CurrentUserGEId;
            command.start = capat1;
            command.end = capat2;
            //Debug.Log(CurrentUserGame);
            //Debug.Log(CurrentUserId);
            RequestJson req = new RequestJson();


            RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/buildDevelopmentRoad", command).Then(Response =>
            {
                req.code = Response.code;
                req.status = Response.status;
                if (req.code == 200)
                {
                    if (req.status == "The road was built successfully.\nYou do not have roads in bank.")
                    {
                        allPieces.SetActive(false);
                    }
                    ver.text = "1";
                    JSONObject json_message = new JSONObject();
                    json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);
                    json_message.AddField("username", LoginScript.CurrentUser);
                    json_message.AddField("start", command.start);
                    json_message.AddField("end", command.end);
                    socket.Emit("buildroad", json_message);

                    if (LoginScript.CurrentLobby.master == LoginScript.CurrentUser)
                    {
                        AfiseazaDrum.afiseaza(newPiece1, piece);

                    }
                    else if (LoginScript.CurrentLobby.first == LoginScript.CurrentUser)
                    {
                        AfiseazaDrum.afiseaza(newPiece2, piece);
                    }
                    else if (LoginScript.CurrentLobby.second == LoginScript.CurrentUser)
                    {
                        AfiseazaDrum.afiseaza(newPiece3, piece);

                    }
                    else if (LoginScript.CurrentLobby.third == LoginScript.CurrentUser)
                    {
                        AfiseazaDrum.afiseaza(newPiece4, piece);

                    }
                }
                Debug.Log(req.code);
                Debug.Log(req.status);

                txt.text = req.status;
            }).Catch(err => { Debug.Log(err); });
        }
        else
        {
            GameObject go = GameObject.Find("SocketIO");
            socket = go.GetComponent<SocketIOComponent>();
            Text txt = FindTextFiel.find();
            MakeRequestResponse command = new MakeRequestResponse();
            command.gameId = LoginScript.CurrentUserGameId;
            command.playerId = LoginScript.CurrentUserGEId;
            command.start = capat1;
            command.end = capat2;
            //Debug.Log(CurrentUserGame);
            //Debug.Log(CurrentUserId);
            RequestJson req = new RequestJson();


            RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/buildDevelopmentRoad", command).Then(Response =>
            {
                req.code = Response.code;
                req.status = Response.status;
                if (req.code == 200)
                {
                    ver.text = "1";
                    JSONObject json_message = new JSONObject();
                    json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);
                    json_message.AddField("username", LoginScript.CurrentUser);
                    json_message.AddField("start", command.start);
                    json_message.AddField("end", command.end);
                    socket.Emit("buildroad", json_message);
                    allPieces.SetActive(false);
                    if (LoginScript.CurrentLobby.master == LoginScript.CurrentUser)
                    {
                        AfiseazaDrum.afiseaza(newPiece1, piece);

                    }
                    else if (LoginScript.CurrentLobby.first == LoginScript.CurrentUser)
                    {
                        AfiseazaDrum.afiseaza(newPiece2, piece);
                    }
                    else if (LoginScript.CurrentLobby.second == LoginScript.CurrentUser)
                    {
                        AfiseazaDrum.afiseaza(newPiece3, piece);

                    }
                    else if (LoginScript.CurrentLobby.third == LoginScript.CurrentUser)
                    {
                        AfiseazaDrum.afiseaza(newPiece4, piece);

                    }

                }
                Debug.Log(req.code);
                Debug.Log(req.status);

                txt.text = req.status;
            }).Catch(err => { Debug.Log(err); });
        }
    }
}
