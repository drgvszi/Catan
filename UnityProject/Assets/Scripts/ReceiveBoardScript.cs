using System.Collections;
using System.Collections.Generic;
using System.Threading;
using UnityEngine.UI;
using UnityEditor;
using UnityEngine;
using FullSerializer;
using Proyecto26;
using UnityEngine.SceneManagement;
using System.IO;
using System.Text;

[System.Serializable]
public class ReceiveBoardScript
{
    public static BoardConnectivityJson ReceivedBoard = new BoardConnectivityJson();

    public void getGameBoard(string ReceivedGameID)
    {
        GameIDConnectivityJson gameid = new GameIDConnectivityJson();
        gameid.gameid = ReceivedGameID;
        RestClient.Post<BoardConnectivityJson>("https://catan-connectivity.herokuapp.com/lobby/startgame", gameid).Then(board =>
        {
            ReceiveBoardScript.ReceivedBoard.ports = board.ports;
            ReceiveBoardScript.ReceivedBoard.board = board.board;
        }).Catch(err => { Debug.Log(err); });
    }


    public void RequestLobbyidAndGameid()
    {
        UnityConnectivityCommand command = new UnityConnectivityCommand();
        command.username = LoginScript.CurrentUser;
        RestClient.Post<LobbyConnectivityJson>("https://catan-connectivity.herokuapp.com/lobby/add", command).Then(ReceivedLobby =>
        {
            LoginScript.CurrentUserGameId = ReceivedLobby.gameid;
            LoginScript.CurrentUserLobbyId = ReceivedLobby.lobbyid;
            getGameBoard(ReceivedLobby.gameid);
        }).Catch(err => { Debug.Log(err); });
    }

    public void getGameBoardNotMaster()
    {
        GameIDConnectivityJson gameid = new GameIDConnectivityJson();
        gameid.gameid = LoginScript.CurrentUserGameId;
        //gameid.gameid = "P9LapF9QcYQ2SKG8ph4hz";
        RestClient.Post<BoardConnectivityJson>("https://catan-connectivity.herokuapp.com/board/get", gameid).Then(board =>
        {
            ReceiveBoardScript.ReceivedBoard.ports = board.ports;
            ReceiveBoardScript.ReceivedBoard.board = board.board;
            Debug.Log("recive board not master");
            Debug.Log(ReceiveBoardScript.ReceivedBoard.board[0].resource);
            Debug.Log(ReceiveBoardScript.ReceivedBoard.board[1].resource);
            Debug.Log(ReceiveBoardScript.ReceivedBoard.board[2].resource);
            Debug.Log(ReceiveBoardScript.ReceivedBoard.board[3].resource);
            Debug.Log(ReceiveBoardScript.ReceivedBoard.board[4].resource);
            Debug.Log(ReceiveBoardScript.ReceivedBoard.board[5].resource);
            Debug.Log(ReceiveBoardScript.ReceivedBoard.board[6].resource);
            Debug.Log(ReceiveBoardScript.ReceivedBoard.board[7].resource);

        }).Catch(err => { Debug.Log(err); });
    }
}
