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

     public void Show()
    {
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

            j = int.Parse(req.arguments[0]);
            i = int.Parse(req.arguments[1]);
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
            txt.text = req.status;
        }).Catch(err => { Debug.Log(err); });


    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
