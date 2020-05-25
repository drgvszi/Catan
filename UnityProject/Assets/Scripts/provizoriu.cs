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
    public Text ver;
    public int numar;

    public Text lumber;
    public Text ore;
    public Text brick;
    public Text grain;
    public Text wool;

    public int capat1;
    public int capat2;
    public SocketIOComponent socket;
    public int culoare = 1;
    bool ok = false;


    void OnMouseDown()
    {
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        Text txt = FindTextFiel.find();

        if (numar != -1)
        {

            if (ver.text == "2")
            {

                MakeRequestResponse command = new MakeRequestResponse();
                command.gameId = LoginScript.CurrentUserGameId;
                command.playerId = LoginScript.CurrentUserGEId;
                command.intersection = numar;
                RequestJson req = new RequestJson();

                RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/buySettlement", command).Then(Response =>
                {
                    req.code = Response.code;
                    req.status = Response.status;
                    if (req.code == 200)
                    {
                         JSONObject json_message = new JSONObject();
                        json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);
                        json_message.AddField("username", LoginScript.CurrentUser);
                        json_message.AddField("intersection", command.intersection);
                        print(command.intersection);
                        socket.Emit("buildsettlement", json_message);
                       

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
                        
                        allPieces.SetActive(false);
                        int x = int.Parse(lumber.text) - 1;
                        lumber.text = x.ToString();
                        x = int.Parse(brick.text) - 1;
                        brick.text = x.ToString();
                        x = int.Parse(grain.text) - 1;
                        grain.text = x.ToString();
                        x = int.Parse(wool.text) - 1;
                        wool.text = x.ToString();

                       
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
                        print(command.intersection);
                        socket.Emit("buildsettlement", json_message);
                      

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
                    allPieces.SetActive(false);
                    Debug.Log(req.code);
                    Debug.Log(req.status);

                    

                    txt.text = req.status;
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
                    lumber.text = Response1.arguments.lumber.ToString();
                    ore.text = Response1.arguments.ore.ToString();
                    grain.text = Response1.arguments.grain.ToString();
                    brick.text = Response1.arguments.brick.ToString();
                    wool.text = Response1.arguments.wool.ToString();

                }).Catch(err => { Debug.Log(err); });

            }
        }
        else
        {
            if (ver.text == "2")
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


                RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/buyRoad", command).Then(Response =>
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

                        int x = int.Parse(lumber.text) - 1;
                        lumber.text = x.ToString();
                        x = int.Parse(brick.text) - 1;
                        brick.text = x.ToString();

                       

                    }
                    Debug.Log(req.code);
                    Debug.Log(req.status);

                    txt.text = req.status;
                }).Catch(err => { Debug.Log(err); });
                allPieces.SetActive(false);
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
                        if (LoginScript.CurrentLobby.master == LoginScript.CurrentUser && ver.text == "0")
                        {
                            AfiseazaDrum.afiseaza(newPiece1, piece);
                            player1.SetActive(false);
                            player2.SetActive(true);
                            player3.SetActive(false);
                            player4.SetActive(false);

                        }
                        else if (LoginScript.CurrentLobby.first == LoginScript.CurrentUser && ver.text == "0")
                        {
                            AfiseazaDrum.afiseaza(newPiece2, piece);

                            player2.SetActive(false);
                            player3.SetActive(true);
                            player1.SetActive(false);
                            player4.SetActive(false);

                        }
                        else if (LoginScript.CurrentLobby.second == LoginScript.CurrentUser && ver.text == "0")
                        {
                            AfiseazaDrum.afiseaza(newPiece3, piece);

                            player3.SetActive(false);
                            player4.SetActive(true);
                            player1.SetActive(false);
                            player2.SetActive(false);

                        }
                        else if (LoginScript.CurrentLobby.third == LoginScript.CurrentUser && ver.text == "0")
                        {

                            AfiseazaDrum.afiseaza(newPiece4, piece);

                            player4.SetActive(true);
                            player1.SetActive(false);
                            player3.SetActive(false);
                            player2.SetActive(false);
                            //  ok = true;
                            ver.text = "1";
                        }
                        else if (LoginScript.CurrentLobby.third == LoginScript.CurrentUser && ver.text == "1")
                        {

                            AfiseazaDrum.afiseaza(newPiece4, piece);
                            player4.SetActive(false);
                            player1.SetActive(false);
                            player3.SetActive(true);
                            player2.SetActive(false);

                        }
                        else if (LoginScript.CurrentLobby.second == LoginScript.CurrentUser && ver.text == "1")
                        {

                            AfiseazaDrum.afiseaza(newPiece3, piece);
                            player4.SetActive(false);
                            player1.SetActive(false);
                            player3.SetActive(false);
                            player2.SetActive(true);

                        }
                        else if (LoginScript.CurrentLobby.first == LoginScript.CurrentUser && ver.text == "1")
                        {

                            AfiseazaDrum.afiseaza(newPiece2, piece);
                            player4.SetActive(false);
                            player1.SetActive(true);
                            player3.SetActive(false);
                            player2.SetActive(false);

                        }
                        else if (LoginScript.CurrentLobby.master == LoginScript.CurrentUser && ver.text == "1")
                        {

                            AfiseazaDrum.afiseaza(newPiece1, piece);
                            player4.SetActive(false);
                            player1.SetActive(true);
                            player3.SetActive(false);
                            player2.SetActive(false);
                            ver.text = "2";
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

}
