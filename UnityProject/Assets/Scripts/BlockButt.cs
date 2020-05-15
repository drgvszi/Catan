using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BlockButt : MonoBehaviour
{
    public GameObject block;
    public GameObject makeitGONE;
    public GameObject makeitGONE1;
    public GameObject makeitGONE2;
    public GameObject makeitGONE3;
    //public GameObject dices;
    public bool ok = false;
    public void ShowOrHide()
    {
        if (ok == false)
            Show();
        else
            Hide();
    }
    public void Show()
    {
        makeitGONE.SetActive(false);
        makeitGONE1.SetActive(false);
        makeitGONE2.SetActive(false);
        makeitGONE3.SetActive(true);
        block.SetActive(true);
        ok = true;
    }
    public void Hide()
    {
        makeitGONE.SetActive(true);
        makeitGONE1.SetActive(true);
        makeitGONE2.SetActive(true);
        makeitGONE3.SetActive(false);
        block.SetActive(false);
        ok = false;
    }
    

}
