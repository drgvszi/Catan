using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using SocketIO;
using System.Runtime.InteropServices;

public class SocketIoscript : MonoBehaviour
{
    public SocketIOComponent socket;
    GameObject inter;
    GameObject inter1;
    public GameObject newPiece1;
    public GameObject newPiece2;
    public GameObject newPiece3;
    public GameObject newPiece4;
    public GameObject newPieceR1;
    public GameObject newPieceR2;
    public GameObject newPieceR3;
    public GameObject newPieceR4;
    public GameObject player1;
    public GameObject player2;
    public GameObject player3;
    public GameObject player4;
    public GameObject casute;
    public GameObject drumuri;
    bool ok1 = false;


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
                casute.SetActive(true);
                inter = GameObject.Find(intesection);
                Debug.Log("asd" + user_who_built_settelment);
                
                if (user_who_built_settelment == LoginScript.CurrentLobby.master)
                {
                    
                    AfiseazaDrum.afiseaza(newPiece1, inter);
                }

                else if (user_who_built_settelment == LoginScript.CurrentLobby.first)
                {
          
                    AfiseazaDrum.afiseaza(newPiece2, inter);
                }
                else if (user_who_built_settelment == LoginScript.CurrentLobby.second)
                {
              
                    AfiseazaDrum.afiseaza(newPiece3, inter);
                }
                else if (user_who_built_settelment == LoginScript.CurrentLobby.third)
                {
        
                    AfiseazaDrum.afiseaza(newPiece4, inter);
                }
                casute.SetActive(false);
            }
        });
        socket.On("buildroad", (E) =>
        {
            if (E.data[0].str == LoginScript.CurrentUserLobbyId)
            {
                string user_who_built_road = E.data[1].str;
                string start = E.data[2].ToString();
                string end = E.data[3].ToString();
                drumuri.SetActive(true);
                inter = GameObject.Find(start + " " + end);
                inter1 = GameObject.Find(end + " " + start);
                if (inter == null)
                    inter = inter1;

                if (user_who_built_road == LoginScript.CurrentLobby.master && ok1 == false)
                {
                    
                    AfiseazaDrum.afiseaza(newPieceR1, inter);
                    player1.SetActive(false);
                    player2.SetActive(true);
                    player3.SetActive(false);
                    player4.SetActive(false);
                    
                }

                else if (user_who_built_road == LoginScript.CurrentLobby.first && ok1 == false)
                {
              
                    AfiseazaDrum.afiseaza(newPieceR2, inter);
                    player2.SetActive(false);
                    player3.SetActive(true);
                    player1.SetActive(false);
                    player4.SetActive(false);
                }
                else if (user_who_built_road == LoginScript.CurrentLobby.second && ok1 == false)
                {
              
                    AfiseazaDrum.afiseaza(newPieceR3, inter);
                    player3.SetActive(false);
                    player4.SetActive(true);
                    player1.SetActive(false);
                    player2.SetActive(false);
                }
                else if (user_who_built_road == LoginScript.CurrentLobby.third && ok1 == false)
                {
       
                    AfiseazaDrum.afiseaza(newPieceR4, inter);
                    player4.SetActive(false);
                    player1.SetActive(true);
                    player3.SetActive(false);
                    player2.SetActive(false);
                    ok1 = true;
                }
                else if (user_who_built_road == LoginScript.CurrentLobby.third && ok1)
                {

                    AfiseazaDrum.afiseaza(newPieceR4, inter);
                    player4.SetActive(true);
                    player1.SetActive(false);
                    player3.SetActive(false);
                    player2.SetActive(false);
              
                }
                else if (user_who_built_road == LoginScript.CurrentLobby.second && ok1)
                {

                    AfiseazaDrum.afiseaza(newPieceR4, inter);
                    player4.SetActive(false);
                    player1.SetActive(false);
                    player3.SetActive(true);
                    player2.SetActive(false);
       
                }
                else if (user_who_built_road == LoginScript.CurrentLobby.first && ok1)
                {

                    AfiseazaDrum.afiseaza(newPieceR4, inter);
                    player4.SetActive(false);
                    player1.SetActive(false);
                    player3.SetActive(false);
                    player2.SetActive(true);
            
                }
                else if (user_who_built_road == LoginScript.CurrentLobby.master && ok1 )
                {

                    AfiseazaDrum.afiseaza(newPieceR4, inter);
                    player4.SetActive(false);
                    player1.SetActive(true);
                    player3.SetActive(false);
                    player2.SetActive(false);
          
                }

                drumuri.SetActive(false);
            }

            
        });
       
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
