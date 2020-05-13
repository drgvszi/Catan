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
public class provizoriu : MonoBehaviour
{
    public GameObject piece;
    public GameObject allPieces;
    public GameObject newPiece;
    public int numar;

    public int capat1;
    public int capat2;

    void Update()
    {
        this.numar = numar;
    }

    void OnMouseDown()
    {
      
        if(numar!=-1)
        {
            MakeRequestResponse command = new MakeRequestResponse(); 
            command.gameId = LoginScript.CurrentUserGameId;
            command.playerId = LoginScript.CurrentUserGEId;
            command.intersection = numar;
            RequestJson req = new RequestJson();
            RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/buildSettlement", command).Then(Response =>
            {
                req.code = Response.code;
                req.status = Response.status;
                if(req.code==200)
                {
                    allPieces.SetActive(false);
                    AfiseazaDrum.afiseaza(newPiece, piece);
                }
                Debug.Log(req.code);
                Debug.Log(req.status);
            }).Catch(err => { Debug.Log(err); });


        }
        else
        {
            MakeRequestResponse command = new MakeRequestResponse();
            command.gameId = LoginScript.CurrentUserGameId;
            command.playerId = LoginScript.CurrentUserGEId;
            command.intersection = 0;
            command.start = capat1;
            command.end = capat2;
            //Debug.Log(CurrentUserGame);
            //Debug.Log(CurrentUserId);
            RequestJson req = new RequestJson();
            RestClient.Post<RequestJson>("https://catan-connectivity.herokuapp.com/game/buildRoad", command).Then(Response =>
            {
                req.code = Response.code;
                req.status = Response.status;
                if (req.code == 200)
                {
                    allPieces.SetActive(false);
                    AfiseazaDrum.afiseaza(newPiece, piece);
                }
                Debug.Log(req.code);
                Debug.Log(req.status);
            }).Catch(err => { Debug.Log(err); });
        }
        
       

    }

}
