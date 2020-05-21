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

public class EndTurn : MonoBehaviour
{
    public Text ver;
    public GameObject player1;
    public GameObject player2;
    public GameObject player3;
    public GameObject player4;
    public SocketIOComponent socket;
    public void endTurn()
    {
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        Text txt = FindTextFiel.find();

        if (ver.text == "2")
        {
            MakeRequestResponse command = new MakeRequestResponse();
            command.gameId = LoginScript.CurrentUserGameId;
            command.playerId = LoginScript.CurrentUserGEId;
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
                txt.text = req.status;
                if (req.code == 200)
                {
                    JSONObject json_message = new JSONObject();
                    json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);
                    json_message.AddField("username", LoginScript.CurrentUser);
                    socket.Emit("endturn", json_message);
                    if (LoginScript.CurrentLobby.master == LoginScript.CurrentUser)
                    {
                        player1.SetActive(false);
                        player2.SetActive(true);
                        player3.SetActive(false);
                        player4.SetActive(false);

                    }
                    else if (LoginScript.CurrentLobby.first == LoginScript.CurrentUser)
                    {
                        player2.SetActive(false);
                        player3.SetActive(true);
                        player1.SetActive(false);
                        player4.SetActive(false);
                    }
                    else if (LoginScript.CurrentLobby.second == LoginScript.CurrentUser)
                    {

                        player3.SetActive(false);
                        player4.SetActive(true);
                        player1.SetActive(false);
                        player2.SetActive(false);
                    }
                    else if (LoginScript.CurrentLobby.third == LoginScript.CurrentUser)
                    {

                        player4.SetActive(false);
                        player1.SetActive(true);
                        player3.SetActive(false);
                        player2.SetActive(false);
                    }
                }

            }).Catch(err => { Debug.Log(err); });
        }
    }
}
