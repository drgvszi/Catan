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


public class yearOfPlenty : MonoBehaviour
{
    Text asd;
    public void yearOP()
    {
        Text txt = FindTextFiel.find();
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = LoginScript.CurrentUserGameId;
        command.playerId = LoginScript.CurrentUserGEId;
        command.development = "yearOfPlenty";
        //Debug.Log(CurrentUserGame);
        //Debug.Log(CurrentUserId);
        RequestJsonDevelopmentExceptMonopoly req = new RequestJsonDevelopmentExceptMonopoly();
        RestClient.Post<RequestJsonDevelopmentExceptMonopoly>("https://catan-connectivity.herokuapp.com/game/useDevelopment", command).Then(Response =>
        {
            req.code = Response.code;
            req.status = Response.status;
            command.gameId = LoginScript.CurrentUserGameId;
            command.playerId = LoginScript.CurrentUserGEId;
            Debug.Log(req.code);
            Debug.Log(req.status);
            if (req.code == 200)
            {
                asd = GameObject.Find("YearOfPlentyText").GetComponent<Text>();
                int x = int.Parse(asd.text);
                x = x - 1;
                asd.text = x.ToString();
            }

        }).Catch(err => { Debug.Log(err); });




    }
}
