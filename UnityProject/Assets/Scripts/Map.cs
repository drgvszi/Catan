using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Threading;
using System.IO;
using UnityEngine.UI;
using System;
using FullSerializer;
using Proyecto26;


[System.Serializable]
public class Ext
{
    public string available;
    public string description;
}

public class Map : MonoBehaviour
{
   
    // Start is called before the first frame update
    void Start()
    {

        GameObject choose5 = GameObject.Find("Choose" + 5);
        choose5.SetActive(false);
        GameObject choose4 = GameObject.Find("Choose" + 4);
        choose4.SetActive(false);
        // ReceiveBoardScript recive = new ReceiveBoardScript();    /
        //recive.RequestLobbyidAndGameid();
    
        RestClient.Get<Ext>("https://catan-connectivity.herokuapp.com/extension/getExtensionByName?name=extension" + (1)).Then(result =>
        {
            Debug.Log(result.description);
            if (result.available != "true")
            {
                GameObject choose = GameObject.Find("Choose" + 1);
                choose.SetActive(false);
                GameObject text = GameObject.Find("Text1");
                Text line = text.GetComponent<Text>();
                line.text = "Not avalabile";

            }
        }).Catch(err => { Debug.Log(err); });

        /*RestClient.Get<Ext>("https://catan-connectivity.herokuapp.com/extension/getExtensionByName?name=extension" + (2)).Then(result =>
        {
            if (result.available != "true")
            {
                GameObject choose = GameObject.Find("Choose" + 2);
                choose.SetActive(false);
                GameObject text = GameObject.Find("Text2");
                Text line = text.GetComponent<Text>();
                line.text = "Not avalabile";

            }
        }).Catch(err => { Debug.Log(err); });

        RestClient.Get<Ext>("https://catan-connectivity.herokuapp.com/extension/getExtensionByName?name=extension" + (3)).Then(result =>
        {
            if (result.available != "true")
            {
                GameObject choose = GameObject.Find("Choose" + 3);
                choose.SetActive(false);
                GameObject text = GameObject.Find("Text3");
                Text line = text.GetComponent<Text>();
                line.text = "Not avalabile";
            }
        }).Catch(err => { Debug.Log(err); });
        */
    }
  

    // Update is called once per frame
    void Update()
    {

    }
    
}
