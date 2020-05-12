using System.Collections;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using UnityEngine;
using UnityEngine.UI;


public class addDevCard : MonoBehaviour
{
    public Text text;
    public int counter;


    public void countPP()
    {
        counter = counter + 1;
    }

    void Update ()
    {
        text.text = counter.ToString();
    }

}
