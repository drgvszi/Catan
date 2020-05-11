using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Proyecto26;


[System.Serializable]
public class SendExt
{
    public string username;
    public string extension;
}

public class SetUserExtension : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        
    }

    public void setUserExtension(string str)
    {
        SendExt command = new SendExt();
        command.username = LoginScript.CurrentUser;
        //command.username = "mmoruz";
        command.extension = "extension" + str;
        RestClient.Post("https://catan-connectivity.herokuapp.com/extension/setExtension",command).Then(board =>
        {
            LoginScript.CurrentUserExtension = "extension" + str;
            Debug.Log("succes");
        }).Catch(err => { Debug.Log(err); });
    }

    public void SendMessage(string s)
    {
        setUserExtension(s);
        Debug.Log("Fine");
    }
    // Update is called once per frame
    void Update()
    {
        
    }
}
