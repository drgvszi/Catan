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
public class choseResource : MonoBehaviour
{
    public string resource;

    public Text ilumber;
    public Text ibirck;
    public Text iore;
    public Text iwool;
    public Text igrain;
    public SocketIOComponent socket;
    public void resouce()
    {
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        Text txt = FindTextFiel.find();
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = LoginScript.CurrentUserGameId;
        command.playerId = LoginScript.CurrentUserGEId;
        command.resource = resource;
        //Debug.Log(CurrentUserGame);
        //Debug.Log(CurrentUserId);s
        RequestJsonDevelopmentMonopoly req = new RequestJsonDevelopmentMonopoly();
    RestClient.Post<RequestJsonDevelopmentMonopoly>("https://catan-connectivity.herokuapp.com/game/takeResourceFromAll", command).Then(Response =>
        {
        req.code = Response.code;
        req.status = Response.status;
        req.arguments = Response.arguments;
        Debug.Log(req.code);
        Debug.Log(req.arguments);
        Debug.Log(req.status);
            txt.text = Response.status;
            JSONObject json_message = new JSONObject();
            json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);
            socket.Emit("Stolen", json_message);

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
        }).Catch(err => { Debug.Log(err); });
    }
   
}
