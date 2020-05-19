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

public class provizoriu : MonoBehaviour
{
    public GameObject piece;
    public GameObject allPieces;
    public GameObject newPiece1;
    public GameObject newPiece2;
    public GameObject newPiece3;
    public GameObject newPiece4;
    public GameObject player1;
    public GameObject player2;
    public GameObject player3;
    public GameObject player4;
    public int numar;

    public int capat1;
    public int capat2;
    public SocketIOComponent socket;
    public int culoare = 1;

    void Update()
    {
        this.numar = numar;
    }

    void OnMouseDown()
    {
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        Text txt = FindTextFiel.find();

        if (numar != -1)
        {
            MakeRequestResponse command = new MakeRequestResponse();
            command.gameId = LoginScript.CurrentUserGameId;
            command.playerId = LoginScript.CurrentUserGEId;
            command.intersection = numar;
            RequestJson req = new RequestJson();
            RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/buildSettlement", command).Then(Response =>
            {
                req.code = Response.code;
                req.status = Response.status;
                if (req.code == 200)
                {
                    JSONObject json_message = new JSONObject();
                    json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);
                    json_message.AddField("username", LoginScript.CurrentUser);
                    json_message.AddField("intersection", command.intersection);
                    socket.Emit("buildsettlement", json_message);
                    allPieces.SetActive(false);
                    if (player1.activeSelf == true)
                    {
                        AfiseazaDrum.afiseaza(newPiece1, piece);
                    }
                    else if (player2.activeSelf == true)
                    {
                        AfiseazaDrum.afiseaza(newPiece2, piece);
                    }
                    else if (player3.activeSelf == true)
                    {
                        AfiseazaDrum.afiseaza(newPiece3, piece);
                    }
                    if (player4.activeSelf == true)
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
            MakeRequestResponse command = new MakeRequestResponse();
            command.gameId = LoginScript.CurrentUserGameId;
            command.playerId = LoginScript.CurrentUserGEId;
            command.intersection = 0;
            command.start = capat1;
            command.end = capat2;
            //Debug.Log(CurrentUserGame);
            //Debug.Log(CurrentUserId);
            RequestJson req = new RequestJson();
            RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/buildRoad", command).Then(Response =>
            {
                req.code = Response.code;
                req.status = Response.status;
                if (req.code == 200)
                {
                    JSONObject json_message = new JSONObject();
                    json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);
                    json_message.AddField("username", LoginScript.CurrentUser);
                    json_message.AddField("start", command.start);
                    json_message.AddField("end", command.end);
                    socket.Emit("buildroad", json_message);
                    allPieces.SetActive(false);
                    if (player1.activeSelf == true)
                    {
                        AfiseazaDrum.afiseaza(newPiece1, piece);
                    }
                    else if (player2.activeSelf == true)
                    {
                        AfiseazaDrum.afiseaza(newPiece2, piece);
                    }
                    else if (player3.activeSelf == true)
                    {
                        AfiseazaDrum.afiseaza(newPiece3, piece);
                    }
                    if (player4.activeSelf == true)
                    {
                        AfiseazaDrum.afiseaza(newPiece4, piece);
                    }
                }
                Debug.Log(req.code);
                Debug.Log(req.status);
                txt.text = req.status;
            }).Catch(err => { Debug.Log(err); });
        }


        allPieces.SetActive(false);
    }

}
