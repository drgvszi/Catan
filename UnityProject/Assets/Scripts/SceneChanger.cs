using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class SceneChanger : MonoBehaviour
{
    // Start is called before the first frame update
    public void goToLogin()
    {
       SceneManager.LoadScene("LoginScene");
    }

    public void goToRegister()
    {
        SceneManager.LoadScene("RegisterScene");
    }

    public void goToTerms()
    {
        SceneManager.LoadScene("TermsScene");
    }

    public void goToSettings()
    {
        SceneManager.LoadScene("SettingsScene");
    }
    public void goToLobbyList()
    {
        SceneManager.LoadScene("LobbyList");
    }

    public void playOnline()
    {
        SceneManager.LoadScene("LobbyScene");
    }

    public void startGame()
    {
        SceneManager.LoadScene("GameMap");
    }

    public void playSinglePlayer()
    {
        //Nothing yet
    }

    public void goToProfile()
    {
        SceneManager.LoadScene("ProfileSettingsScene");
    }

    public void goToProfileStatistics()
    {
        SceneManager.LoadScene("ProfileStatisticsScene");
    }

    public void goToMenu()
    {
       // ChangeTexture.done = false;
        SceneManager.LoadScene("MenuScene");
    }

    public void goToWaitingRoom()
    {
        SceneManager.LoadScene("WaitingRoom");
    }

    public void quit()
    {
        Application.Quit();
    }
}
