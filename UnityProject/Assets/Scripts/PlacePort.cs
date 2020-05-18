using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlacePort : MonoBehaviour
{
    public Transform Spawn;
    public GameObject port1;
    public GameObject port2;
    public GameObject port3;
    public GameObject port4;
    public GameObject port5;
    BoardConnectivityJson board = null;
    public int nr;
    bool done = false;
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        BoardConnectivityJson board = ReceiveBoardScript.ReceivedBoard;
        if (done == false)
        {
            if (ReceiveBoardScript.ReceivedBoard.board[0] != null)
            {
                done = true;
                string str = "";
                str = board.ports[nr];
               
                switch (str)
                {
                    case "ThreeForOne":
                        Instantiate(port1, Spawn.position, Spawn.rotation);
                        break;
                    case "Brick":
                        Instantiate(port2, Spawn.position, Spawn.rotation);
                        break;
                    case "Ore":
                        Instantiate(port3, Spawn.position, Spawn.rotation);
                        break;
                    case "Wool":
                        Instantiate(port4, Spawn.position, Spawn.rotation);
                        break;
                    case "Lumber":
                        Instantiate(port5, Spawn.position, Spawn.rotation);
                        break;
                    case "Grain":
                        Instantiate(port5, Spawn.position, Spawn.rotation);
                        break;


                }
            }
            // else
            //     Debug.Log("Null");
        }
    }
}
