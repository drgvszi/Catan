using System.Collections;
using System.Collections.Generic;
using System.Threading;
using UnityEngine.UI;
using UnityEditor;
using UnityEngine;
using FullSerializer;
using Proyecto26;
using UnityEngine.SceneManagement;
using System.Text;
using SocketIO;
using System;
using System.Runtime.InteropServices;

public class test : MonoBehaviour
{
    public GameObject robbler;
    public Transform robber;
    public GameObject x;

    void Start()
    {
        print("test");
        Instantiate(robbler, robber.position, robber.rotation);
        x = GameObject.Find("Robber777(Clone)");
        print(x.name);
        x.SetActive(false);

    }

   
}
