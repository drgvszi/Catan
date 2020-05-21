using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class BlockButt : MonoBehaviour
{
    public GameObject block;
    public GameObject makeitGONE;
    public GameObject makeitGONE1;
    public GameObject makeitGONE2;
    public GameObject makeitGONE3;
    public Text ver;
    public GameObject EndTURN;
    public GameObject EndTURN1;
    //public GameObject dices;
    public bool ok = false;
    void Start()
    {
        ShowForFirst();
    }
    public void ShowOrHide()
    {
        Show();
        /*if (ver.text == "2")
        {
            ok = true;
            if (ok == false)
                Show();
            else
                Hide();
        }*/
    }
    public void Show()
    {
        makeitGONE.SetActive(false);
        makeitGONE1.SetActive(false);
        makeitGONE2.SetActive(false);
        makeitGONE3.SetActive(true);
        block.SetActive(true);
        EndTURN.SetActive(false);
        EndTURN1.SetActive(true);
        ok = true;
    }
    public void Hide()
    {
        makeitGONE.SetActive(true);
        makeitGONE1.SetActive(true);
        makeitGONE2.SetActive(true);
        makeitGONE3.SetActive(false);
        block.SetActive(false);
        EndTURN.SetActive(true);
        EndTURN.SetActive(false);
        ok = false;
    }

    void ShowForFirst()
    {
        makeitGONE1.SetActive(false);
        makeitGONE2.SetActive(false);
        block.SetActive(true);
        EndTURN.SetActive(false);
        EndTURN1.SetActive(true);
    }
    

}
