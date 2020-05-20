using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
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
        AddToLob();

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
        socket.On("removed", (E) =>
        {
            Debug.Log(E.data[0][0].str);
            for (int i = 0; i < lobbies.Count; i++)
            {
                if(lobbies[i].lobbyid == E.data[0][0].str)
                {
                    lobbies.Remove(lobbies[i]);
                    break;
                }
            }
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
            string started = E.data[0][7].str;
            for (int i = 0; i < lobbies.Count; i++)
                if (lobby.gameid == lobbies[i].gameid && started != "1")
                {                                                                     // change a lobby. Update number of players here
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
             string started = E.data[0][7].str;
             if (lobbies.Find(currentLobby => currentLobby.gameid == lobby.gameid) == null && started != "1")
              {
                 Debug.Log(lobby.lobbyid);                                                                          // ADD A lobby. add a new gameobject or something here

                 lobbies.Add(lobby);
              }
         });
    }


    public void joinLobby(Text txt)                                                                                    // identify a lobby by its lobbyid
    {
        string lobbyid = txt.text;
        print(lobbyid);
        for (int i = 0; i < lobbies.Count; i++)
            if (lobbies[i].lobbyid == lobbyid && lobbies[i].extension == LoginScript.CurrentUserExtension && (countPlayers(lobbies[i]) != 4))
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
                        LoginScript.CurrentLobby = lobbies[i];


                        
                        SceneChanger scene = new SceneChanger();
                        scene.goToWaitingRoom();





                    }).Catch(err => { Debug.Log(err); });

                }).Catch(err => { Debug.Log(err); });
                break;
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
                LoginScript.CurrentLobby = new Lobby(LoginScript.CurrentUserExtension, "-","-", "-",LoginScript.CurrentUser, LoginScript.CurrentUserGameId, LoginScript.CurrentUserLobbyId);

                
                SceneChanger scene = new SceneChanger();
                scene.goToWaitingRoom();




            }).Catch(err => { Debug.Log(err); });

        }).Catch(err => { Debug.Log(err); });
    }

    
    public Text[] obj1;
    public Text[] obj2;
    public Text[] obj3;
    public void ChangeError(string lobby, string ext, string players, Text obj, Text obj2, Text obj3)
    {

        obj.text = lobby;
        obj2.text = ext;
        obj3.text = players;

    }
    /* public void createlob(string lobby, string ext, string players)
     {

         for (int j = 0; j < 5; j++)
         {
             if (obj1[j].text != "")
                 continue;

             ChangeError(lobby, ext, players, obj1[j], obj2[j], obj3[j]);
             break;

         }
     }*/

    public void AddToLob()
    {
        for (int i = 0; i < lobbies.Count; i++)
        {
            if (i < 10)
                ChangeError(lobbies[i].lobbyid, lobbies[i].extension, "" + countPlayers(lobbies[i]) + "/4", obj1[i], obj2[i], obj3[i]);
        }
    }
    // Update is called once per frame
   
}
