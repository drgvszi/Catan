using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using SocketIO;
using System.Runtime.InteropServices;
using UnityEngine.UI;

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
    public Text ver;
    public Text user1;
    public Text user2;
    public Text user3;
    public Text user4;
    public GameObject side1;
    public GameObject side2;
    public GameObject side3;
    public GameObject side4;
    public GameObject side5;
    public GameObject side6;
    public GameObject sside1;
    public GameObject sside2;
    public GameObject sside3;
    public GameObject sside4;
    public GameObject sside5;
    public GameObject sside6;
    public Text lumber;
    public Text ore;
    public Text brick;
    public Text grain;
    public Text wool;
    bool ok1 = false;
    public static int turn = 1;


    
    void Start()
    {
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        setupEvents();
        user1.text = LoginScript.CurrentLobby.master;
        user2.text = LoginScript.CurrentLobby.first;
        user3.text = LoginScript.CurrentLobby.second;
        user4.text = LoginScript.CurrentLobby.third;

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
                print(inter.name);
                
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
            turn++;
            if (E.data[0].str == LoginScript.CurrentUserLobbyId)
            {
                string user_who_built_road = E.data[1].str;
                string start = E.data[2].ToString();
                string end = E.data[3].ToString();
                drumuri.SetActive(true);
                inter = GameObject.Find(start + " " + end);
                inter1 = GameObject.Find(end + " " + start);
                if (inter == null)
                    inter = GameObject.Find(end + " " + start);

                if (user_who_built_road == LoginScript.CurrentLobby.master && ver.text == "0")
                {
                    
                    AfiseazaDrum.afiseaza(newPieceR1, inter);
                    player1.SetActive(false);
                    player2.SetActive(true);
                    player3.SetActive(false);
                    player4.SetActive(false);
                    
                }

                else if (user_who_built_road == LoginScript.CurrentLobby.first && ver.text == "0")
                {
              
                    AfiseazaDrum.afiseaza(newPieceR2, inter);
                    player2.SetActive(false);
                    player3.SetActive(true);
                    player1.SetActive(false);
                    player4.SetActive(false);
                }
                else if (user_who_built_road == LoginScript.CurrentLobby.second && ver.text == "0")
                {
              
                    AfiseazaDrum.afiseaza(newPieceR3, inter);
                    player3.SetActive(false);
                    player4.SetActive(true);
                    player1.SetActive(false);
                    player2.SetActive(false);
                }
                else if (user_who_built_road == LoginScript.CurrentLobby.third && ver.text == "0")
                {
       
                    AfiseazaDrum.afiseaza(newPieceR4, inter);
                    player4.SetActive(true);
                    player1.SetActive(false);
                    player3.SetActive(false);
                    player2.SetActive(false);
                    //ok1 = true;
                    ver.text = "1";
                }
                else if (user_who_built_road == LoginScript.CurrentLobby.third && ver.text == "1")
                {

                    AfiseazaDrum.afiseaza(newPieceR4, inter);
                    player4.SetActive(false);
                    player1.SetActive(false);
                    player3.SetActive(true);
                    player2.SetActive(false);
              
                }
                else if (user_who_built_road == LoginScript.CurrentLobby.second && ver.text == "1")
                {

                    AfiseazaDrum.afiseaza(newPieceR3, inter);
                    player4.SetActive(false);
                    player1.SetActive(false);
                    player3.SetActive(false);
                    player2.SetActive(true);
       
                }
                else if (user_who_built_road == LoginScript.CurrentLobby.first && ver.text == "1")
                {

                    AfiseazaDrum.afiseaza(newPieceR2, inter);
                    player4.SetActive(false);
                    player1.SetActive(true);
                    player3.SetActive(false);
                    player2.SetActive(false);
            
                }
                else if (user_who_built_road == LoginScript.CurrentLobby.master && ver.text == "1")
                {

                    AfiseazaDrum.afiseaza(newPieceR1, inter);
                    player4.SetActive(false);
                    player1.SetActive(true);
                    player3.SetActive(false);
                    player2.SetActive(false);
                    ver.text = "2";
                }

                drumuri.SetActive(false);
            }

            
        });
        socket.On("RollDice", (E) =>
        {
            if (LoginScript.CurrentLobby.third == LoginScript.CurrentUser)
            {

                lumber.text = E.data[23].ToString();
                ore.text = E.data[27].ToString();
                grain.text = E.data[25].ToString();
                brick.text = E.data[26].ToString();
                wool.text = E.data[24].ToString();

            }
            else if (LoginScript.CurrentLobby.second == LoginScript.CurrentUser)
            {

                lumber.text = E.data[17].ToString();
                ore.text = E.data[21].ToString();
                grain.text = E.data[19].ToString();
                brick.text = E.data[20].ToString();
                wool.text = E.data[18].ToString();

            }
            else if (LoginScript.CurrentLobby.first == LoginScript.CurrentUser)
            {

                lumber.text = E.data[11].ToString();
                ore.text = E.data[15].ToString();
                grain.text = E.data[13].ToString();
                brick.text = E.data[14].ToString();
                wool.text = E.data[12].ToString();

            }
            else if (LoginScript.CurrentLobby.master == LoginScript.CurrentUser)
            {

                lumber.text = E.data[5].ToString();
                ore.text = E.data[9].ToString();
                grain.text = E.data[7].ToString();
                brick.text = E.data[8].ToString();
                wool.text = E.data[6].ToString();

            }

            string dice_1 = E.data[2].ToString();
            string dice_2 = E.data[3].ToString();
            switch (dice_1)
            {
                case "1":
                    side1.SetActive(true);
                    side2.SetActive(false);
                    side3.SetActive(false);
                    side4.SetActive(false);
                    side5.SetActive(false);
                    side6.SetActive(false);

                    break;

                case "2":
                    side1.SetActive(false);
                    side2.SetActive(true);
                    side3.SetActive(false);
                    side4.SetActive(false);
                    side5.SetActive(false);
                    side6.SetActive(false);

                    break;
                case "3":
                    side1.SetActive(false);
                    side2.SetActive(false);
                    side3.SetActive(true);
                    side4.SetActive(false);
                    side5.SetActive(false);
                    side6.SetActive(false);

                    break;

                case "4":
                    side1.SetActive(false);
                    side2.SetActive(false);
                    side3.SetActive(false);
                    side4.SetActive(true);
                    side5.SetActive(false);
                    side6.SetActive(false);

                    break;
                case "5":
                    side1.SetActive(false);
                    side2.SetActive(false);
                    side3.SetActive(false);
                    side4.SetActive(false);
                    side5.SetActive(true);
                    side6.SetActive(false);

                    break;

                case "6":
                    side1.SetActive(false);
                    side2.SetActive(false);
                    side3.SetActive(false);
                    side4.SetActive(false);
                    side5.SetActive(false);
                    side6.SetActive(true);

                    break;
            }

            switch (dice_2)
            {
                case "1":
                    sside1.SetActive(true);
                    sside2.SetActive(false);
                    sside3.SetActive(false);
                    sside4.SetActive(false);
                    sside5.SetActive(false);
                    sside6.SetActive(false);

                    break;

                case "2":
                    sside1.SetActive(false);
                    sside2.SetActive(true);
                    sside3.SetActive(false);
                    sside4.SetActive(false);
                    sside5.SetActive(false);
                    sside6.SetActive(false);
                    break;
                case "3":
                    sside1.SetActive(false);
                    sside2.SetActive(false);
                    sside3.SetActive(true);
                    sside4.SetActive(false);
                    sside5.SetActive(false);
                    sside6.SetActive(false);
                    break;

                case "4":
                    sside1.SetActive(false);
                    sside2.SetActive(false);
                    sside3.SetActive(false);
                    sside4.SetActive(true);
                    sside5.SetActive(false);
                    sside6.SetActive(false);
                    break;
                case "5":
                    sside1.SetActive(false);
                    sside2.SetActive(false);
                    sside3.SetActive(false);
                    sside4.SetActive(false);
                    sside5.SetActive(true);
                    sside6.SetActive(false);
                    break;

                case "6":
                    sside1.SetActive(false);
                    sside2.SetActive(false);
                    sside3.SetActive(false);
                    sside4.SetActive(false);
                    sside5.SetActive(false);
                    sside6.SetActive(true);
                    break;
            }
            Debug.Log(dice_1);
            Debug.Log(dice_2);
        });


        socket.On("endturn", (E) =>
        {
            Debug.Log("endturn");
            if (E.data[0].str == LoginScript.CurrentUserLobbyId)
            {
                string user_who_end_turn = E.data[1].str;
                          if (LoginScript.CurrentLobby.master == user_who_end_turn)
                    {
                        player1.SetActive(false);
                        player2.SetActive(true);
                        player3.SetActive(false);
                        player4.SetActive(false);

                    }
                    else if (LoginScript.CurrentLobby.first == user_who_end_turn)
                    {
                        player2.SetActive(false);
                        player3.SetActive(true);
                        player1.SetActive(false);
                        player4.SetActive(false);
                    }
                    else if (LoginScript.CurrentLobby.second == user_who_end_turn)
                    {

                        player3.SetActive(false);
                        player4.SetActive(true);
                        player1.SetActive(false);
                        player2.SetActive(false);
                    }
                    else if (LoginScript.CurrentLobby.third == user_who_end_turn)
                    {

                        player4.SetActive(false);
                        player1.SetActive(true);
                        player3.SetActive(false);
                        player2.SetActive(false);
                    }
            }
        });

    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
