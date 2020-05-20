using System.Collections;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using UnityEngine;

public class test : MonoBehaviour
{
    GameObject inter;

    // Update is called once per frame
    void Update()
    {
        inter = GameObject.Find("4" + " " + "3");
        print(inter.name);
    }
}
