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
public class Dices : MonoBehaviour
{
    public GameObject side1;
    public GameObject side2;
    public GameObject side3;
    public GameObject side4;
    public GameObject side5;
    public GameObject side6;
    public GameObject sside1;
    public GameObject sside2;
    public GameObject sside3;
    public GameObject sside4;
    public GameObject sside5;
    public GameObject sside6;
    public Text lumber;
    public Text ore;
    public Text brick;
    public Text grain;
    public Text wool;
    public SocketIOComponent socket;

    public void Show()
    {
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        int j,i;
        Text txt = FindTextFiel.find();
        MakeRequestResponse command = new MakeRequestResponse();
        command.gameId = LoginScript.CurrentUserGameId;
        command.playerId = LoginScript.CurrentUserGEId;
        RequestJson req = new RequestJson();
        RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/rollDice", command).Then(Response =>
        {
            req.code = Response.code;
            req.status = Response.status;
            Debug.Log(Response.code);
            Debug.Log(Response.status);
            JSONObject json_message = new JSONObject();
            json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);
            json_message.AddField("username", LoginScript.CurrentUser);
            json_message.AddField("dice_1", Response.arguments.dice_1);
        json_message.AddField("dice_2", Response.arguments.dice_2);
        json_message.AddField("player_0", Response.arguments.player_0);
        json_message.AddField("lumber_0", Response.arguments.lumber_0);
        json_message.AddField("wool_0", Response.arguments.wool_0);
        json_message.AddField("grain_0", Response.arguments.grain_0);
        json_message.AddField("brick_0", Response.arguments.brick_0);
        json_message.AddField("ore_0", Response.arguments.ore_0);
            json_message.AddField("player_1", Response.arguments.player_1);
            json_message.AddField("lumber_1", Response.arguments.lumber_1);
            json_message.AddField("wool_1", Response.arguments.wool_1);
            json_message.AddField("grain_1", Response.arguments.grain_1);
            json_message.AddField("brick_1", Response.arguments.brick_1);
            json_message.AddField("ore_1", Response.arguments.ore_1);
            json_message.AddField("player_2", Response.arguments.player_2);
            json_message.AddField("lumber_2", Response.arguments.lumber_2);
            json_message.AddField("wool_2", Response.arguments.wool_2);
            json_message.AddField("grain_2", Response.arguments.grain_2);
            json_message.AddField("brick_2", Response.arguments.brick_2);
            json_message.AddField("ore_2", Response.arguments.ore_2);
            json_message.AddField("player_3", Response.arguments.player_3);
            json_message.AddField("lumber_3", Response.arguments.lumber_3);
            json_message.AddField("wool_3", Response.arguments.wool_3);
            json_message.AddField("grain_3", Response.arguments.grain_3);
            json_message.AddField("brick_3", Response.arguments.brick_3);
            json_message.AddField("ore_3", Response.arguments.ore_3);
            socket.Emit("RollDice", json_message);

            j = Response.arguments.dice_1;
            i = Response.arguments.dice_2;

            if (req.code == 200)
            {
                if (LoginScript.CurrentLobby.third == LoginScript.CurrentUser)
                {

                    lumber.text = Response.arguments.lumber_3.ToString();
                    ore.text = Response.arguments.ore_3.ToString();
                    grain.text = Response.arguments.grain_3.ToString();
                    brick.text = Response.arguments.brick_3.ToString();
                    wool.text = Response.arguments.wool_3.ToString();

                }
                else if (LoginScript.CurrentLobby.second == LoginScript.CurrentUser)
                {

                    lumber.text = Response.arguments.lumber_2.ToString();
                    ore.text = Response.arguments.ore_2.ToString();
                    grain.text = Response.arguments.grain_2.ToString();
                    brick.text = Response.arguments.brick_2.ToString();
                    wool.text = Response.arguments.wool_2.ToString();

                }
                else if (LoginScript.CurrentLobby.first == LoginScript.CurrentUser)
                {

                    lumber.text = Response.arguments.lumber_1.ToString();
                    ore.text = Response.arguments.ore_1.ToString();
                    grain.text = Response.arguments.grain_1.ToString();
                    brick.text = Response.arguments.brick_1.ToString();
                    wool.text = Response.arguments.wool_1.ToString();

                }
                else if (LoginScript.CurrentLobby.master == LoginScript.CurrentUser)
                {

                    lumber.text = Response.arguments.lumber_0.ToString();
                    ore.text = Response.arguments.ore_0.ToString();
                    grain.text = Response.arguments.grain_0.ToString();
                    brick.text = Response.arguments.brick_0.ToString();
                    wool.text = Response.arguments.wool_0.ToString();

                }


                switch (j)
                {
                    case 1:
                        side1.SetActive(true);
                        side2.SetActive(false);
                        side3.SetActive(false);
                        side4.SetActive(false);
                        side5.SetActive(false);
                        side6.SetActive(false);

                        break;

                    case 2:
                        side1.SetActive(false);
                        side2.SetActive(true);
                        side3.SetActive(false);
                        side4.SetActive(false);
                        side5.SetActive(false);
                        side6.SetActive(false);

                        break;
                    case 3:
                        side1.SetActive(false);
                        side2.SetActive(false);
                        side3.SetActive(true);
                        side4.SetActive(false);
                        side5.SetActive(false);
                        side6.SetActive(false);

                        break;

                    case 4:
                        side1.SetActive(false);
                        side2.SetActive(false);
                        side3.SetActive(false);
                        side4.SetActive(true);
                        side5.SetActive(false);
                        side6.SetActive(false);

                        break;
                    case 5:
                        side1.SetActive(false);
                        side2.SetActive(false);
                        side3.SetActive(false);
                        side4.SetActive(false);
                        side5.SetActive(true);
                        side6.SetActive(false);

                        break;

                    case 6:
                        side1.SetActive(false);
                        side2.SetActive(false);
                        side3.SetActive(false);
                        side4.SetActive(false);
                        side5.SetActive(false);
                        side6.SetActive(true);

                        break;
                }

                switch (i)
                {
                    case 1:
                        sside1.SetActive(true);
                        sside2.SetActive(false);
                        sside3.SetActive(false);
                        sside4.SetActive(false);
                        sside5.SetActive(false);
                        sside6.SetActive(false);

                        break;

                    case 2:
                        sside1.SetActive(false);
                        sside2.SetActive(true);
                        sside3.SetActive(false);
                        sside4.SetActive(false);
                        sside5.SetActive(false);
                        sside6.SetActive(false);
                        break;
                    case 3:
                        sside1.SetActive(false);
                        sside2.SetActive(false);
                        sside3.SetActive(true);
                        sside4.SetActive(false);
                        sside5.SetActive(false);
                        sside6.SetActive(false);
                        break;

                    case 4:
                        sside1.SetActive(false);
                        sside2.SetActive(false);
                        sside3.SetActive(false);
                        sside4.SetActive(true);
                        sside5.SetActive(false);
                        sside6.SetActive(false);
                        break;
                    case 5:
                        sside1.SetActive(false);
                        sside2.SetActive(false);
                        sside3.SetActive(false);
                        sside4.SetActive(false);
                        sside5.SetActive(true);
                        sside6.SetActive(false);
                        break;

                    case 6:
                        sside1.SetActive(false);
                        sside2.SetActive(false);
                        sside3.SetActive(false);
                        sside4.SetActive(false);
                        sside5.SetActive(false);
                        sside6.SetActive(true);
                        break;
                }
            }
            txt.text = req.status;
        }).Catch(err => { Debug.Log(err); });


    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
