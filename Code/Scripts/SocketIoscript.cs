using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using SocketIO;

public class SocketIoscript : MonoBehaviour
{
    public SocketIOComponent socket;
    // Start is called before the first frame update
    void Start()
    {
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        setupEvents();

    }

    public void setupEvents()
    {
        socket.On("buildsettlement", (E) =>
        {
            if (E.data[0].str == LoginScript.CurrentUserLobbyId)
            {
                Debug.Log("builtsettelment");
                string user_who_built_settelment = E.data[1].str;
                string intesection = E.data[2].ToString();
            }
        });
        socket.On("buildroad", (E) =>
        {
            if (E.data[0].str == LoginScript.CurrentUserLobbyId)
            {
                string user_who_built_road = E.data[1].str;
                string start = E.data[2].ToString();
                string end = E.data[3].ToString();
            }
        });
       
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
