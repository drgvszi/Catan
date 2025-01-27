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


public class addDevCard : MonoBehaviour
{
   
     Text knight;
     Text roadbuid;
     Text monopoly;
     Text yearofplenty;
     Text univer;


    public Text ilumber;
    public Text ibirck;
    public Text iore;
    public Text iwool;
    public Text igrain;

    public void buyDev()
    {
        Text txt = FindTextFiel.find();
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = LoginScript.CurrentUserGameId;
        command.playerId = LoginScript.CurrentUserGEId;
        RequestJsonDevelopmentExceptMonopoly req = new RequestJsonDevelopmentExceptMonopoly();
        RestClient.Post<RequestJsonDevelopmentExceptMonopoly>("https://catan-connectivity.herokuapp.com/game/buyDevelopment", command).Then(Response =>
        {
            req.code = Response.code;
            req.status = Response.status;
            req.arguments = Response.arguments;
            int nr = 0;
            print(req.arguments.development);
            if (req.code == 200)
            {

                knight = GameObject.Find("KnightText").GetComponent<Text>();
                roadbuid = GameObject.Find("RoadBuildingText").GetComponent<Text>();
                monopoly = GameObject.Find("MonopolyText").GetComponent<Text>();
                yearofplenty = GameObject.Find("YearOfPlentyText").GetComponent<Text>();
                univer = GameObject.Find("VictoryPointText").GetComponent<Text>();
                if (req.arguments.development == "roadBuilding")
                {
                    nr = int.Parse(roadbuid.text) + 1;
                    roadbuid.text = nr.ToString();
                }
                if (req.arguments.development == "knight")
                {
                    nr = int.Parse(knight.text) + 1;
                    knight.text = nr.ToString();
                }
                if (req.arguments.development == "monopoly")
                {
                    nr = int.Parse(monopoly.text) + 1;
                    monopoly.text = nr.ToString();
                }
                if (req.arguments.development == "yearOfPlenty")
                {
                    nr = int.Parse(yearofplenty.text) + 1;
                    yearofplenty.text = nr.ToString();
                    
                }
                int x;

                MakeRequestResponse command1 = new MakeRequestResponse();
                command1.gameId = LoginScript.CurrentUserGameId;
                command1.playerId = LoginScript.CurrentUserGEId;
                RequestJson req1 = new RequestJson();
                RestClient.Post<UpdateJson>("https://catan-connectivity.herokuapp.com/game/update", command1).Then(Response1 =>
                {
                    Debug.Log("Update code " + Response1.code);
                    Debug.Log("Update status " + Response1.status);
                    print(Response1.arguments.lumber.ToString());
                    print(Response1.arguments.ore.ToString());
                    print(Response1.arguments.grain.ToString());
                    print(Response1.arguments.brick.ToString());
                    print(Response1.arguments.wool.ToString());

                    ilumber.text = Response1.arguments.lumber.ToString();
                    iore.text = Response1.arguments.ore.ToString();
                    igrain.text = Response1.arguments.grain.ToString();
                    ibirck.text = Response1.arguments.brick.ToString();
                    iwool.text = Response1.arguments.wool.ToString();
                }).Catch(err => { Debug.Log(err); });
            }



            Debug.Log(req.code);
            Debug.Log(req.status);
            Debug.Log(req.arguments);
            txt.text = req.status;
        }).Catch(err => { Debug.Log(err); });
    }
}
