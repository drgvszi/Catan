using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.IO;
using UnityEngine.UI;
using System.Threading;

public class ChangeTexture : MonoBehaviour
{
    public Transform SpawnForestOre;
    public Transform SpawnHills;
    public Transform SpawnPasture;
    public Transform SpawnDesertField;
    public Transform robber;
    public GameObject objForest;
    public GameObject robbler;
    public GameObject objPasture;
    public GameObject objBrick;
    public GameObject objOre;
    public GameObject objGrain;
    public GameObject objDesert;
    public GameObject tataLaRobberi;
    public TextMesh text;
    public int nr;
    BoardConnectivityJson board = null;
    bool done = false;

    // Start is called before the first frame update
 
    void Start()
    {
        done = false;
    }

    // Update is called once per frame
    void Update()
    {
        Text txt = FindTextFiel.find();
        BoardConnectivityJson board = ReceiveBoardScript.ReceivedBoard;
        if (done == false) { 
            if (ReceiveBoardScript.ReceivedBoard.board[0] != null)
            {
                
                string str = "";
                str = board.board[nr].number.ToString();
                text.text = str;
                string resource = board.board[nr].resource;
                txt.text = resource;
                Debug.Log(resource);
                switch (resource)
                {
                    case "lumber":
                        Instantiate(objForest, SpawnForestOre.position, SpawnForestOre.rotation);
                        break;
                    case "wool":
                        Instantiate(objPasture, SpawnPasture.position, SpawnPasture.rotation);
                        break;
                    case "ore":
                        Instantiate(objOre, SpawnForestOre.position, SpawnForestOre.rotation);
                        break;
                    case "grain":
                        Instantiate(objGrain, SpawnPasture.position, SpawnPasture.rotation);
                        break;
                    case "desert":
                        tataLaRobberi.SetActive(true);
                        Instantiate(objDesert, SpawnDesertField.position, SpawnDesertField.rotation);
                        Instantiate(robbler, robber.position, robber.rotation);
                        tataLaRobberi.SetActive(false);
                        break;
                    case "brick":
                        Instantiate(objBrick, SpawnHills.position, SpawnHills.rotation);
                        break;

                } 
                done = true;
            }
           
           // else
           //     Debug.Log("Null");
        }
    }
}