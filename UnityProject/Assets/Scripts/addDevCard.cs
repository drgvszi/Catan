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


public class addDevCard : MonoBehaviour
{
   
   public Text knight;
    public Text roadbuid;
    public Text monopoly;
    public Text yearofplenty;
    public Text univer;

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
            int nr;
            print(req.arguments.development);

            if (req.arguments.development== "roadBuilding")
                {
                nr = int.Parse(roadbuid.text) + 1;
                roadbuid.text = nr.ToString();
            }
            if(req.arguments.development == "knight")
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



            Debug.Log(req.code);
            Debug.Log(req.status);
            Debug.Log(req.arguments);
            txt.text = req.status;
        }).Catch(err => { Debug.Log(err); });
    }
}
