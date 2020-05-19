using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class create_lobby : MonoBehaviour
{
    int i = 0;
    
    
    public Text[] obj1;
    public Text[] obj2;
    public Text[] obj3;


    public static void ChangeError(string lobby,string ext, string players,Text obj, Text obj2, Text obj3)
    {
        
        obj.text = lobby;
        obj2.text = ext;
        obj3.text = players;
        
    }
    public void createlob()
    {
        i++;
        for (int j = 0; j < 5; j++)
        {
            if (obj1[j].text != "")
                continue;
           
            ChangeError("Lobby " + i, "Default", i+"/4",obj1[j],obj2[j],obj3[j]);
            break;
            
        }
    }
}
