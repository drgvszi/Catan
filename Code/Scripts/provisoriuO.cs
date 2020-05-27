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
public class provisoriuO : MonoBehaviour
{
    public GameObject piece;
    public GameObject allPieces;
    public GameObject newPieceO1;
    public GameObject newPieceO2;
    public GameObject newPieceO3;
    public GameObject newPieceO4;


    public SocketIOComponent socket;




    void OnMouseDown()
    {
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        Text txt = FindTextFiel.find();

        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = LoginScript.CurrentUserGameId;
        command.playerId = LoginScript.CurrentUserGEId;
        command.intersection = int.Parse(piece.name);
        RequestJson req = new RequestJson();
        
        RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/buyCity", command).Then(Response =>
        {
            req.code = Response.code;
            req.status = Response.status;
            if (req.code == 200)
            {
                JSONObject json_message = new JSONObject();
                json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);
                json_message.AddField("username", LoginScript.CurrentUser);
                json_message.AddField("intersection", command.intersection);
                socket.Emit("buyCity", json_message);
                allPieces.SetActive(false);
                if (LoginScript.CurrentLobby.master == LoginScript.CurrentUser)
                {
                    AfiseazaDrum.afiseaza(newPieceO1, piece);

                }
                else if (LoginScript.CurrentLobby.first == LoginScript.CurrentUser)
                {
                    AfiseazaDrum.afiseaza(newPieceO2, piece);
                }
                else if (LoginScript.CurrentLobby.second == LoginScript.CurrentUser)
                {
                    AfiseazaDrum.afiseaza(newPieceO3, piece);

                }
                else if (LoginScript.CurrentLobby.third == LoginScript.CurrentUser)
                {
                    AfiseazaDrum.afiseaza(newPieceO4, piece);

                }

            }
            Debug.Log(req.code);
            Debug.Log(req.status);

            txt.text = req.status;
        }).Catch(err => { Debug.Log(err); });
        allPieces.SetActive(false);
    }
}