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
    public GameObject panel;


    public Text ilumber;
    public Text ibirck;
    public Text iore;
    public Text iwool;
    public Text igrain;

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
                    Debug.Log("Update arguments lumber " + Response1.arguments.lumber);

                    print("end turn info");
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
        }).Catch(err => { Debug.Log(err);});
    }
}
