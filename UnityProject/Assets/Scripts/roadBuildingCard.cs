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
using SocketIO;


public class roadBuildingCard : MonoBehaviour
{
    public GameObject tataRoads;
    public Text ver;
    Text asd;

    public void asd1()
    {

        ver.text = "0";
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = LoginScript.CurrentUserGameId;
        command.playerId = LoginScript.CurrentUserGEId;
        command.development = "roadBuilding";
        //Debug.Log(CurrentUserGame);
        //Debug.Log(CurrentUserId);
        RequestJsonDevelopmentExceptMonopoly req = new RequestJsonDevelopmentExceptMonopoly();
        RestClient.Post<RequestJsonDevelopmentExceptMonopoly>("https://catan-connectivity.herokuapp.com/game/useDevelopment", command).Then(Response =>
        {
            req.code = Response.code;
            req.status = Response.status;
            Debug.Log(req.code);
            Debug.Log(req.status);
            if (req.code == 200)
            {
                tataRoads.SetActive(true);
                asd = GameObject.Find("RoadBuildingText").GetComponent<Text>();
                int x = int.Parse(asd.text);
                x = x - 1;
                asd.text = x.ToString();
            }
        }).Catch(err => { Debug.Log(err); });




    }
}
