using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using SocketIO;
using Proyecto26;

[System.Serializable]
public class Lobby
{
    public string extension;
    public string first;
    public string second;
    public string third;
    public string master;
    public string lobbyid;
    public string gameid;

    public Lobby(string extension, string first, string second, string third, string master, string gameid, string lobbyid)
    {
        this.extension = extension;
        this.first = first;
        this.second = second;
        this.third = third;
        this.master = master;
        this.gameid = gameid;
        this.lobbyid = lobbyid;
    }
}



public class LobbyHandler : MonoBehaviour
{
    public SocketIOComponent socket;                                                               // BETTER ASK Datco Maxim what he have done here. Only God and him know
    GameObject startbutton;
    void Start()
    {
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        setupEvents();
        startbutton = GameObject.Find("Start");
        startbutton.SetActive(false);

    }

    public void setupEvents()
    {
        socket.On("open", (E) =>
        {
            Debug.Log("Open");
        });
        socket.On("gamestart", EmittedStartGame);
        socket.On("changed", (E) =>                       // updating lobbies list
        {
            
            Lobby lobby = new Lobby(
                 E.data.GetField("lobby").GetField("extension").str,
                 E.data.GetField("lobby").GetField("first").str,
                 E.data.GetField("lobby").GetField("second").str,
                 E.data.GetField("lobby").GetField("third").str,
                 E.data.GetField("lobby").GetField("master").str,
                 E.data.GetField("lobby").GetField("gameid").str,
                 E.data.GetField("lobby").GetField("lobbyid").str
                 );
            if (lobby.gameid == LoginScript.CurrentLobby.gameid)
           {
                LoginScript.CurrentLobby = lobby;
           }
        });
    }

    public void EmitStartGame()
    {
        Debug.Log(LoginScript.CurrentUserGEId);
        GameIDConnectivityJson gameid = new GameIDConnectivityJson();
        gameid.gameid = LoginScript.CurrentUserGameId;
        RestClient.Post<BoardConnectivityJson>("https://catan-connectivity.herokuapp.com/lobby/startgame", gameid).Then(board =>
        {
            ReceiveBoardScript.ReceivedBoard.ports = board.ports;
            ReceiveBoardScript.ReceivedBoard.board = board.board;

            JSONObject json_message = new JSONObject();
            json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);
            socket.Emit("gamestart", json_message);

            SceneChanger n = new SceneChanger();
            n.startGame();

        }).Catch(err => { Debug.Log(err); });

    }

    public void EmittedStartGame(SocketIOEvent e)
    {
        string lobbyid = e.data.GetField("lobbyid").str;
        if (lobbyid == LoginScript.CurrentUserLobbyId) 
        {
            ReceiveBoardScript recive = new ReceiveBoardScript();
            recive.getGameBoardNotMaster();

            SceneChanger n = new SceneChanger();
            n.startGame();
        }
    }

    public void LeaveLobby()
    {
        UnityConnectivityCommand command = new UnityConnectivityCommand();
        command.lobbyid = LoginScript.CurrentUserLobbyId;
        command.username = LoginScript.CurrentUser;

        RestClient.Post("https://catan-connectivity.herokuapp.com/lobby/leaveLobby", command).Then(res =>
        {
            Debug.Log(res.Text);
        }).Catch(err => { Debug.Log(err); });

    }


    public void LeaveGame()
    {
        UnityConnectivityCommand command = new UnityConnectivityCommand();
        command.active = false;
        command.playerId = LoginScript.CurrentUserGEId;
        command.gameId = LoginScript.CurrentUserGameId;
        Debug.Log(LoginScript.CurrentUserGEId);
        RestClient.Post("https://catan-connectivity.herokuapp.com/lobby/leaveGame", command).Then(res =>
        {
            Debug.Log(res.Text);

        }).Catch(err => { Debug.Log(err); });

    }


    // Update is called once per frame
    void Update()
    {
        startbutton.SetActive(false);
        // display the names of the members of the lobby. If not master then cannot start game
        GameObject name1 = GameObject.Find("Text1");
        Text line1 = name1.GetComponent<Text>();
        GameObject name2 = GameObject.Find("Text2");
        Text line2 = name2.GetComponent<Text>();
        GameObject name3 = GameObject.Find("Text3");
        Text line3 = name3.GetComponent<Text>();
        GameObject name4 = GameObject.Find("Text4");
        Text line4 = name4.GetComponent<Text>();

       
        line1.text = LoginScript.CurrentLobby.master;
        line2.text = LoginScript.CurrentLobby.first;
        line3.text = LoginScript.CurrentLobby.second;
        line4.text = LoginScript.CurrentLobby.third;

        if (LoginScript.CurrentLobby.first != "-" && LoginScript.CurrentLobby.second != "-" && LoginScript.CurrentLobby.third != "-" && LoginScript.CurrentLobby.master == LoginScript.CurrentUser)
            startbutton.SetActive(true);

    }
}
