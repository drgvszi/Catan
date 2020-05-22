using System.Collections;
using System.Collections.Generic;
using System.Globalization;
using System.Runtime.InteropServices;
using System.Security.Cryptography;
using UnityEngine;
using UnityEngine.UI;

public class developAction : MonoBehaviour
{
    public GameObject cardPopUp;
    public GameObject noPopUp;
    public Text txt;
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }
    public void asd1()
    {
        print(txt.text);
        if (txt.text != "0")
            cardPopUp.SetActive(true);
        else
            noPopUp.SetActive(true); 
    }
}
