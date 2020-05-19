using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class changeTurn : MonoBehaviour
{
    public GameObject player1;
    public GameObject player2;
    public GameObject player3;
    public GameObject player4;

    public void turn()
    {
        

        if (player1.activeSelf == true)
        {
            player1.SetActive(false);
            player2.SetActive(true);
        }
        else if (player2.activeSelf == true)
        {
            player2.SetActive(false);
            player3.SetActive(true);
            player4.SetActive(false);
            player1.SetActive(false);

        }
        else if (player3.activeSelf == true)
        {
            player2.SetActive(false);
            player3.SetActive(false);
            player4.SetActive(true);
            player1.SetActive(false);
        }
        else if (player4.activeSelf == true)
        {
            player2.SetActive(false);
            player3.SetActive(false);
            player4.SetActive(false);
            player1.SetActive(true);

        }
    }
}
