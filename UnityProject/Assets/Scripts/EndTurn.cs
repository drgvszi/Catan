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

    public Text ilumber;
    public Text ibirck;
    public Text iore;
    public Text iwool;
    public Text igrain;

    Text vPoints0;
    Text lRoad0;
    Text uKnight0;
    Text devCards0;
    Text resCards0;

    Text vPoints1;
    Text lRoad1;
    Text uKnight1;
    Text devCards1;
    Text resCards1;
    
    Text vPoints2;
    Text lRoad2;
    Text uKnight2;
    Text devCards2;
    Text resCards2;

    Text vPoints3;
    Text lRoad3;
    Text uKnight3;
    Text devCards3;
    Text resCards3;



    public void endTurn()
    {


        /*
        vPoints0= GameObject.Find("PointsText").GetComponent<Text>();
        lRoad0 = GameObject.Find("RoadText").GetComponent<Text>();
        uKnight0 = GameObject.Find("KnightText").GetComponent<Text>();
        devCards0 = GameObject.Find("DevelopmentCardsText").GetComponent<Text>();
        resCards0 = GameObject.Find("CardsNumberText").GetComponent<Text>();

        vPoints1 = GameObject.Find("PointsText1").GetComponent<Text>();
        lRoad1 = GameObject.Find("RoadText1").GetComponent<Text>();
        uKnight1 = GameObject.Find("KnightText1").GetComponent<Text>();
        devCards1 = GameObject.Find("DevelopmentCardsText1").GetComponent<Text>();
        resCards1 = GameObject.Find("CardsNumberText1").GetComponent<Text>();

        vPoints2 = GameObject.Find("PointsText2").GetComponent<Text>();
        lRoad2 = GameObject.Find("RoadText2").GetComponent<Text>();
        uKnight2 = GameObject.Find("KnightText2").GetComponent<Text>();
        devCards2 = GameObject.Find("DevelopmentCardsText2").GetComponent<Text>();
        resCards2 = GameObject.Find("CardsNumberText2").GetComponent<Text>();

        vPoints3 = GameObject.Find("PointsText3").GetComponent<Text>();
        lRoad3 = GameObject.Find("RoadText3").GetComponent<Text>();
        uKnight3 = GameObject.Find("KnightText3").GetComponent<Text>();
        devCards3 = GameObject.Find("DevelopmentCardsText3").GetComponent<Text>();
        resCards3 = GameObject.Find("CardsNumberText3").GetComponent<Text>();

        */

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

                MakeRequestResponse command1 = new MakeRequestResponse();
                command1.gameId = LoginScript.CurrentUserGameId;
                command1.playerId = LoginScript.CurrentUserGEId;
                RequestJson req1 = new RequestJson();



                RestClient.Post<UpdateJson>("https://catan-connectivity.herokuapp.com/game/update", command1).Then(Response1 =>
                {
                    Debug.Log("Update code " + Response1.code);
                    Debug.Log("Update status " + Response1.status);
                    Debug.Log("Update arguments lumber " + Response1.arguments.lumber);

                    print("end turn info");
                    print(Response1.arguments.lumber.ToString());
                    print(Response1.arguments.ore.ToString());
                    print(Response1.arguments.grain.ToString());
                    print(Response1.arguments.brick.ToString());
                    print(Response1.arguments.wool.ToString());

                    if (LoginScript.CurrentLobby.master == LoginScript.CurrentUser)
                    {

                        vPoints0 = GameObject.Find("PointsText").GetComponent<Text>();
                        lRoad0 = GameObject.Find("RoadText").GetComponent<Text>();
                        uKnight0 = GameObject.Find("KnightText").GetComponent<Text>();
                        devCards0 = GameObject.Find("DevelopmentCardsText").GetComponent<Text>();
                        resCards0 = GameObject.Find("CardsNumberText").GetComponent<Text>();

                        vPoints0.text = Response1.arguments.publicScore.ToString();
                        lRoad0.text = Response1.arguments.longestRoad.ToString();
                        uKnight0.text = Response1.arguments.usedKnights.ToString();
                        devCards0.text = (Response1.arguments.knight + Response1.arguments.monopoly + Response1.arguments.roadBuilding + Response1.arguments.victoryPoint + Response1.arguments.yearOfPlenty).ToString();
                        resCards0.text = (Response1.arguments.lumber + Response1.arguments.ore + Response1.arguments.grain + Response1.arguments.brick + Response1.arguments.wool).ToString();
                        print(Response1.arguments.ToString());
                    }
                    else if (LoginScript.CurrentLobby.first == LoginScript.CurrentUser)
                    {
                        vPoints1 = GameObject.Find("PointsText1").GetComponent<Text>();
                        lRoad1 = GameObject.Find("RoadText1").GetComponent<Text>();
                        uKnight1 = GameObject.Find("KnightText1").GetComponent<Text>();
                        devCards1 = GameObject.Find("DevelopmentCardsText1").GetComponent<Text>();
                        resCards1 = GameObject.Find("CardsNumberText1").GetComponent<Text>();

                        vPoints1.text = Response1.arguments.publicScore.ToString();
                        lRoad1.text = Response1.arguments.longestRoad.ToString();
                        uKnight1.text = Response1.arguments.usedKnights.ToString();
                        devCards1.text = (Response1.arguments.knight + Response1.arguments.monopoly + Response1.arguments.roadBuilding + Response1.arguments.victoryPoint + Response1.arguments.yearOfPlenty).ToString();
                        resCards1.text = (Response1.arguments.lumber + Response1.arguments.ore + Response1.arguments.grain + Response1.arguments.brick + Response1.arguments.wool).ToString();
                    }
                    else if (LoginScript.CurrentLobby.second == LoginScript.CurrentUser)
                    {

                        vPoints2 = GameObject.Find("PointsText2").GetComponent<Text>();
                        lRoad2 = GameObject.Find("RoadText2").GetComponent<Text>();
                        uKnight2 = GameObject.Find("KnightText2").GetComponent<Text>();
                        devCards2 = GameObject.Find("DevelopmentCardsText2").GetComponent<Text>();
                        resCards2 = GameObject.Find("CardsNumberText2").GetComponent<Text>();

                        vPoints2.text = Response1.arguments.publicScore.ToString();
                        lRoad2.text = Response1.arguments.longestRoad.ToString();
                        uKnight2.text = Response1.arguments.usedKnights.ToString();
                        devCards2.text = (Response1.arguments.knight + Response1.arguments.monopoly + Response1.arguments.roadBuilding + Response1.arguments.victoryPoint + Response1.arguments.yearOfPlenty).ToString();
                        resCards2.text = (Response1.arguments.lumber + Response1.arguments.ore + Response1.arguments.grain + Response1.arguments.brick + Response1.arguments.wool).ToString();
                    }
                    else if (LoginScript.CurrentLobby.third == LoginScript.CurrentUser)
                    {

                        vPoints3 = GameObject.Find("PointsText3").GetComponent<Text>();
                        lRoad3 = GameObject.Find("RoadText3").GetComponent<Text>();
                        uKnight3 = GameObject.Find("KnightText3").GetComponent<Text>();
                        devCards3 = GameObject.Find("DevelopmentCardsText3").GetComponent<Text>();
                        resCards3 = GameObject.Find("CardsNumberText3").GetComponent<Text>();

                        vPoints3.text = Response1.arguments.publicScore.ToString();
                        lRoad3.text = Response1.arguments.longestRoad.ToString();
                        uKnight3.text = Response1.arguments.usedKnights.ToString();
                        devCards3.text = (Response1.arguments.knight + Response1.arguments.monopoly + Response1.arguments.roadBuilding + Response1.arguments.victoryPoint + Response1.arguments.yearOfPlenty).ToString();
                        resCards3.text = (Response1.arguments.lumber + Response1.arguments.ore + Response1.arguments.grain + Response1.arguments.brick + Response1.arguments.wool).ToString();
                    }


                    ilumber.text = Response1.arguments.lumber.ToString();
                    iore.text = Response1.arguments.ore.ToString();
                    igrain.text = Response1.arguments.grain.ToString();
                    ibirck.text = Response1.arguments.brick.ToString();
                    iwool.text = Response1.arguments.wool.ToString();
                }).Catch(err => { Debug.Log(err); });

            }).Catch(err => { Debug.Log(err); });
        }
    }
}
