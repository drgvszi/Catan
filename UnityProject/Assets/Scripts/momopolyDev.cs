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

public class momopolyDev : MonoBehaviour
{
    Text asd;
    public GameObject chosePanel;
    public void useMonopoly()
    {
        Text txt = FindTextFiel.find();
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = LoginScript.CurrentUserGameId;
        command.playerId = LoginScript.CurrentUserGEId;
        command.development = "monopoly";
        //Debug.Log(CurrentUserGame);
        //Debug.Log(CurrentUserId);
        RequestJsonDevelopmentExceptMonopoly req = new RequestJsonDevelopmentExceptMonopoly();
        RestClient.Post<RequestJsonDevelopmentExceptMonopoly>("https://catan-connectivity.herokuapp.com/game/useDevelopment", command).Then(Response =>
        {
            req.code = Response.code;
            req.status = Response.status;
            Debug.Log(req.code);
            Debug.Log(req.status);
            txt.text = req.status;
            if (req.code == 200)
            {
                chosePanel.SetActive(true);
                asd = GameObject.Find("MonopolyText").GetComponent<Text>();
                int x = int.Parse(asd.text);
                x = x - 1;
                asd.text = x.ToString();
            }

        }).Catch(err => { Debug.Log(err); });
    }
}
