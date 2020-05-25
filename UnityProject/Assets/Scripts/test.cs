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
    
    void Start()
    {
        Text txt = FindTextFiel.find();
        txt.text = " aaaaaaaaaaaaaa \n bbbbbbbbbb";
    }

   
}
