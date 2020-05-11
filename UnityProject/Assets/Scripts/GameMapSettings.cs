using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameMapSettings : MonoBehaviour
{
    public GameObject settings;

    public void showSettings()
    {
        settings.transform.gameObject.SetActive(true);
    }

    public void hideSettings()
    {
        settings.transform.gameObject.SetActive(false);
    }
}
