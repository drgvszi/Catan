using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class changetogle : MonoBehaviour
{
    public Toggle togle0;
    public Toggle togle1;
    public Toggle togle2;
    public Toggle togle3;
    public void chg()
    {
         togle0.isOn=false;
        togle1.isOn = false;
        togle2.isOn = false;
        togle3.isOn = false;

    }
}
