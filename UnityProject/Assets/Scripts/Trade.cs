using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Trade : MonoBehaviour
{
    public GameObject block;
    public GameObject makeitGONE;
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
        block.SetActive(true);
        ok = true;
    }
    public void Hide()
    {
        makeitGONE.SetActive(true);
        block.SetActive(false);
        ok = false;
    }


}
