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

public class ChooseRobberPosition : MonoBehaviour
{
    public GameObject tataRoberi;
    public GameObject Rober;
    public GameObject Robler;
    public Transform rob;
    public GameObject choosePlayer;

    public GameObject Oplayer1;
    public GameObject Oplayer2;



    public Text player0;
    public Text player1;
    public Text player2;

    public static SocketIOComponent socket;

    void OnMouseDown()
    {
        int nrHexa = int.Parse(Rober.name);
        
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();

        MakeRequestResponse command1 = new MakeRequestResponse();
        command1.gameId = LoginScript.CurrentUserGameId;
        command1.playerId = LoginScript.CurrentUserGEId;
        command1.tile = nrHexa;
        RequestJson req1 = new RequestJson();
        RestClient.Post<MoveRobberRequest>("https://catan-connectivity.herokuapp.com/game/moveRobber", command1).Then(Response1 =>
        {
            if (Response1.code == 200)
            {




                GameObject x = GameObject.Find("Robber777(Clone)");
                Destroy(x);
                Instantiate(Robler, rob.position, rob.rotation);
                //Rober.SetActive(false);
                tataRoberi.SetActive(false);

                JSONObject json_message = new JSONObject();
                json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);
                json_message.AddField("intersection", nrHexa.ToString());
                socket.Emit("placeRobber", json_message);

                Debug.Log("Move robber " + Response1.code);
                Debug.Log("Move robber  " + Response1.status);
                Debug.Log("Mode Robber " + Response1.arguments.player_0);
                Debug.Log("Mode Robber " + Response1.arguments.player_1);
                Debug.Log("Mode Robber " + Response1.arguments.player_2);

                if (Response1.arguments.player_0 == null)
                {
                    MakeRequestResponse command2 = new MakeRequestResponse();
                    command2.gameId = LoginScript.CurrentUserGameId;
                    command2.playerId = LoginScript.CurrentUserGEId;
                    command2.answer = "no";
                    command2.player = Response1.arguments.player_0;
                    RestClient.Post<MoveRobberRequest>("https://catan-connectivity.herokuapp.com/game/stealResource", command2).Then(Response2 =>
                    {
                        Debug.Log("Not stolen");
                    }).Catch(err => { Debug.Log(err); });
                }
                else
                {
                    choosePlayer.SetActive(true);
                    player0.text = Response1.arguments.player_0;
                    if (Response1.arguments.player_1 == null)
                    {
                        Oplayer1.SetActive(false);
                    }
                    else
                        player1.text = Response1.arguments.player_1;
                    if (Response1.arguments.player_2 == null)
                    {
                        Oplayer2.SetActive(false);
                    }
                    else
                        player2.text = Response1.arguments.player_2;
                }


            }

           


        }).Catch(err => { Debug.Log(err); });




    }
}
