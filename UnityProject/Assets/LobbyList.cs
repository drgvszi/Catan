using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using SocketIO;
using Proyecto26;


public class JoinLobby
{
    public string username;
    public string lobbyid;
    public JoinLobby(string username, string lobbyid)
    {
        this.username = username;
        this.lobbyid = lobbyid;
    }
}


public class LobbyList : MonoBehaviour
{
    public static SocketIOComponent socket;
    public List<Lobby> lobbies;

    // Start is called before the first frame update
    void Start()
    {
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        setupEvents();
        
    }
    public int countPlayers(Lobby lobby)
    {
        int rez = 1;    // master always in lobby. Without master a lobby does not exist
        if (lobby.first != "-")
            rez++;
        if (lobby.second != "-")
            rez++;
        if (lobby.third != "-")
            rez++;

        return rez;
    }

    public void setupEvents()
    {
        socket.On("open", (E) =>
        {
            Debug.Log("Open");
        });

        socket.On("changed", (E) =>                       // updating a lobby(ex. a user joined)
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
            
            for (int i = 0; i < lobbies.Count; i++)
                if (lobby.gameid == lobbies[i].gameid)
                {
                    Debug.Log(lobby.lobbyid);                                                                       // change a lobby. Update number of players here
                    lobbies[i] = lobby;
                }
        });

         socket.On("added", (E) =>                 // updating lobbies list(by inserting new lobby)
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
            
              if(lobbies.Find(currentLobby => currentLobby.gameid == lobby.gameid) == null)
              {
                 Debug.Log(lobby.lobbyid);                                                                           // ADD A lobby. add a new gameobject or something here
                 lobbies.Add(lobby);
              }
         });
    }


    public void joinLobby(int i)                                                                                    // identify a lobby by its number
    {
        if (lobbies[i].extension == LoginScript.CurrentUserExtension && (countPlayers(lobbies[i]) != 4))
        {
            JoinLobby jl = new JoinLobby(LoginScript.CurrentUser, lobbies[i].lobbyid);
            RestClient.Post("https://catan-connectivity.herokuapp.com/lobby/join", jl).Then(joined_lobby =>
            {
                LoginScript.CurrentUserGameId = lobbies[i].gameid;
                LoginScript.CurrentUserLobbyId = lobbies[i].lobbyid;

                UnityConnectivityCommand getgeid = new UnityConnectivityCommand();
                //getgeid.username = "mmoruz";
                getgeid.username = LoginScript.CurrentUser;
                RestClient.Post("https://catan-connectivity.herokuapp.com/lobby/geid", getgeid).Then(response =>
                {
                    LoginScript.CurrentUserGEId = response.Text;




                    SceneChanger scene = new SceneChanger();
                    scene.goToWaitingRoom();





                }).Catch(err => { Debug.Log(err); });

            }).Catch(err => { Debug.Log(err); });
        }
    }

    public void createLobby()   // in case there are no lobby change the button from connect to create and call that function
    {
        UnityConnectivityCommand command = new UnityConnectivityCommand();
        //command.username = "mmoruz";
        command.username = LoginScript.CurrentUser;
        RestClient.Post<LobbyConnectivityJson>("https://catan-connectivity.herokuapp.com/lobby/add", command).Then(added_Lobby =>
        {
            LoginScript.CurrentUserGameId = added_Lobby.gameid;
            LoginScript.CurrentUserLobbyId = added_Lobby.lobbyid;

            UnityConnectivityCommand getgeid = new UnityConnectivityCommand();
            //getgeid.username = "mmoruz";
            getgeid.username = LoginScript.CurrentUser;
            RestClient.Post("https://catan-connectivity.herokuapp.com/lobby/geid", getgeid).Then(response =>
            {
                LoginScript.CurrentUserGEId = response.Text;
                        


                SceneChanger scene = new SceneChanger();
                scene.goToWaitingRoom();




            }).Catch(err => { Debug.Log(err); });

        }).Catch(err => { Debug.Log(err); });
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
