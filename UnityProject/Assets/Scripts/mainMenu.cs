using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.Audio;
using UnityEngine.UI;
using System.Linq;

public class mainMenu : MonoBehaviour
{
    public Slider volumeSlider;
    public Slider musicSlider;
    public Dropdown qualityDropdown;
    public AudioMixer audioMixer;

    private void Start()
    {
        AudioListener.volume = PlayerPrefs.GetFloat("volum");
        volumeSlider.value = PlayerPrefs.GetFloat("volum");
        audioMixer.SetFloat("volume", PlayerPrefs.GetFloat("volum"));

        musicSlider.value = PlayerPrefs.GetFloat("music");
        AudioSource music = GameObject.Find("Audio Source").GetComponent<AudioSource>();
        music.volume = PlayerPrefs.GetFloat("music");

        QualitySettings.SetQualityLevel(PlayerPrefs.GetInt("qualityIndex"));
        qualityDropdown.value= PlayerPrefs.GetInt("qualityIndex");
        qualityDropdown.RefreshShownValue();

        Screen.fullScreen = PlayerPrefs.GetInt("fullScreen") == 1 ? true : false;
    }
    public void SetVolume (float volume)
    {
        audioMixer.SetFloat("volume", volume);
        AudioListener.volume = volume;
        PlayerPrefs.SetFloat("volum", volume);
    }
    public void SetMusic(float volume)
    {
        AudioSource music = GameObject.Find("Audio Source").GetComponent<AudioSource>();
        music.volume = volume;
        PlayerPrefs.SetFloat("music",volume);
    }
    public void SetQuality (int qualityIndex)
    {
        PlayerPrefs.SetInt("qualityIndex", qualityIndex);
        QualitySettings.SetQualityLevel(qualityIndex);
    }
    public void SetFullscreen (bool isFullscreen)
    {
        Screen.fullScreen = isFullscreen;
        PlayerPrefs.SetInt("fullScreen", isFullscreen ? 1 : 0);

    }

}
