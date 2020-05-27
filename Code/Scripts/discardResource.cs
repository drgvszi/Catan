using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using Proyecto26;
public class discardResource : MonoBehaviour
{
    public InputField lumber;
    public InputField birck;
    public InputField ore;
    public InputField wool;
    public InputField grain;

    public void discard()
    {
        int nr_lumber;
        int nr_brick;
        int nr_ore;
        int nr_wool;
        int nr_grain;


        if (lumber.text == "")
            nr_lumber = 0;
        else
           nr_lumber  = int.Parse(lumber.text);

        if (wool.text == "")
            nr_wool = 0;
        else
            nr_wool = int.Parse(wool.text);

        if (grain.text == "")
            nr_grain= 0;
        else
            nr_grain = int.Parse(grain.text);

        if (birck.text == "")
            nr_brick = 0;
        else
            nr_brick = int.Parse(birck.text);

        if (ore.text == "")
            nr_ore = 0;
        else
            nr_ore = int.Parse(ore.text);

        DiscardRequestJson command = new DiscardRequestJson();
        command.gameId = LoginScript.CurrentUserGameId;
        command.playerId = LoginScript.CurrentUserGEId;
        command.ore = nr_ore;
        command.wool = nr_wool;
        command.brick = nr_brick;
        command.grain = nr_grain;
        command.lumber = nr_lumber;
        DiscardResponse req = new DiscardResponse();
        RestClient.Post<DiscardResponse>("https://catan-connectivity.herokuapp.com/game/discardResources", command).Then(Response =>
        {
            Debug.Log(Response.code) ;
            Debug.Log(Response.status);
            Debug.Log(Response.arguments.sentAll) ;
        }).Catch(err => { Debug.Log(err);});
    }
}
