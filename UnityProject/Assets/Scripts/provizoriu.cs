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
    public GameObject verificare;
    public int numar;

    public int capat1;
    public int capat2;
    public SocketIOComponent socket;
    public int culoare = 1;
    bool ok = false;

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
                    if (LoginScript.CurrentLobby.master == LoginScript.CurrentUser && !verificare.activeSelf)
                    {
                        AfiseazaDrum.afiseaza(newPiece1, piece);
                        player1.SetActive(false);
                        player2.SetActive(true);
                        player3.SetActive(false);
                        player4.SetActive(false);

                    }
                    else if (LoginScript.CurrentLobby.first == LoginScript.CurrentUser && !verificare.activeSelf)
                    {
                        AfiseazaDrum.afiseaza(newPiece2, piece );
            
                            player2.SetActive(false);
                            player3.SetActive(true);
                            player1.SetActive(false);
                            player4.SetActive(false);
                        
                    }
                    else if (LoginScript.CurrentLobby.second == LoginScript.CurrentUser && !verificare.activeSelf)
                    {
                        AfiseazaDrum.afiseaza(newPiece3, piece);
                  
                            player3.SetActive(false);
                            player4.SetActive(true);
                            player1.SetActive(false);
                            player2.SetActive(false);
                        
                    }
                    else if (LoginScript.CurrentLobby.third == LoginScript.CurrentUser && !verificare.activeSelf)
                    {
                  
                        AfiseazaDrum.afiseaza(newPiece4, piece);
       
                            player4.SetActive(true);
                            player1.SetActive(false);
                            player3.SetActive(false);
                            player2.SetActive(false);
                            verificare.SetActive(true);
                      //  ok = true;
                        
                    }
                    else if (LoginScript.CurrentLobby.third == LoginScript.CurrentLobby.third && verificare.activeSelf)
                    {

                        AfiseazaDrum.afiseaza(newPiece4, piece);
                        player4.SetActive(false);
                        player1.SetActive(false);
                        player3.SetActive(true);
                        player2.SetActive(false);

                    }
                    else if (LoginScript.CurrentLobby.second == LoginScript.CurrentLobby.second && verificare.activeSelf)
                    {

                        AfiseazaDrum.afiseaza(newPiece4, piece);
                        player4.SetActive(false);
                        player1.SetActive(false);
                        player3.SetActive(false);
                        player2.SetActive(true);

                    }
                    else if (LoginScript.CurrentLobby.first == LoginScript.CurrentLobby.first && verificare.activeSelf)
                    {

                        AfiseazaDrum.afiseaza(newPiece4, piece);
                        player4.SetActive(false);
                        player1.SetActive(true);
                        player3.SetActive(false);
                        player2.SetActive(false);

                    }
                    else if (LoginScript.CurrentLobby.master == LoginScript.CurrentLobby.master && verificare.activeSelf)
                    {

                        AfiseazaDrum.afiseaza(newPiece4, piece);
                        player4.SetActive(false);
                        player1.SetActive(true);
                        player3.SetActive(false);
                        player2.SetActive(false);

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
