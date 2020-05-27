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

public class YOPchooseresource : MonoBehaviour
{
    public Toggle togle0;
    public Toggle togle1;
    public Toggle togle2;
    public Toggle togle3;
    public Toggle togle4;
    string resource1;
    string resource2;
    public Text ilumber;
    public Text ibirck;
    public Text iore;
    public Text iwool;
    public Text igrain;
    public SocketIOComponent socket;

    public GameObject panel;
    public void choose()
    {
        resource2 = "";
        resource1 = "";
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        int i=0;
        if(togle0.isOn)
        {
            i++;
            resource1 = "lumber";
        }

        if (togle1.isOn)
        {if(i==0)
                resource1 = "brick";
            else
                resource2 = "brick";
            i++;
            

        }
        if (togle2.isOn)
        { if (i == 0)
                resource1 = "wool";
            else
                resource2 = "wool";
            i++;
           
        }
        if (togle3.isOn)
        { if (i == 0)
                resource1 = "grain";
            else
                resource2 = "grain";
            i++;
           
        }
        if (togle4.isOn)
        { if (i == 0)
                resource1 = "ore";
            else
                resource2 = "ore";
            i++;
           
        }
        print(resource1);
        print(resource2);

 Text txt = FindTextFiel.find();
        if(i==2)
        {
           
            
            MakeRequestResponse command = new MakeRequestResponse();
            command.gameId = LoginScript.CurrentUserGameId;
            command.playerId = LoginScript.CurrentUserGEId;
            command.resource_0 = resource1;
            command.resource_1 = resource2;
            //Debug.Log(CurrentUserGame);
            //Debug.Log(CurrentUserId);
            RequestJsonDevelopmentExceptMonopoly req = new RequestJsonDevelopmentExceptMonopoly();
            RestClient.Post<RequestJsonDevelopmentExceptMonopoly>("https://catan-connectivity.herokuapp.com/game/takeTwoResources", command).Then(Response =>
            {
                req.code = Response.code;
                req.status = Response.status;
                Debug.Log(req.code);
                Debug.Log(req.status);
                txt.text = Response.status;
                if(Response.code==200)
                {
                panel.SetActive(false);
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
               

            }).Catch(err => { Debug.Log(err); });
        }
        else
        {
            txt.text = "Select 2 resources";
        }


    }
}
