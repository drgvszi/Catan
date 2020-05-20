using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameMapSettings : MonoBehaviour
{
    public GameObject settings;
    bool ok = false;

    public void showAndHideSettings()
    {
        if (ok == false)
        {
            settings.transform.gameObject.SetActive(true);
            ok = true;
        }
        else
        {
            settings.transform.gameObject.SetActive(false);
            ok = false;
        }
    }

    public void hideSettings()
    {
        settings.transform.gameObject.SetActive(false);
    }
}
