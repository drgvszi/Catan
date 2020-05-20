using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.Audio;
using UnityEngine.UI;
using System.Linq;

public class mainMenu : MonoBehaviour
{

    Resolution[] resolutions;
    public Dropdown resolutionDropdown;
    int currentResolutionIndex = 0;

    public Slider volumeSlider;
    public Slider musicSlider;
    public Dropdown qualityDropdown;

    private void Start()
    {

        //setari memorate
        AudioListener.volume = PlayerPrefs.GetFloat("volum");
        volumeSlider.value = PlayerPrefs.GetFloat("volum");
        audioMixer.SetFloat("volume", PlayerPrefs.GetFloat("volum"));

        //music.volume = PlayerPrefs.GetFloat("music");
        //musicSlider.value = PlayerPrefs.GetFloat("music");

        QualitySettings.SetQualityLevel(PlayerPrefs.GetInt("qualityIndex"));
        qualityDropdown.value= PlayerPrefs.GetInt("qualityIndex");
        qualityDropdown.RefreshShownValue();

        Screen.fullScreen = PlayerPrefs.GetInt("fullScreen") == 1 ? true : false;

        Resolution res = resolutions[PlayerPrefs.GetInt("resolution")];
        Screen.SetResolution(res.width, res.height, Screen.fullScreen);


        resolutions = Screen.resolutions.Select(resolution => new Resolution { width = resolution.width, height = resolution.height }).Distinct().ToArray();

        resolutionDropdown.ClearOptions();

        List<string> options = new List<string>();
        Debug.Log(resolutions.Length);
        for (int i = 0; i < resolutions.Length; i++)
        {
            string option = resolutions[i].width + "x" + resolutions[i].height;
            options.Add(option);

            if (resolutions[i].width == Screen.currentResolution.width && resolutions[i].height == Screen.currentResolution.height)
            {
                currentResolutionIndex = i;
            }
        }

        resolutionDropdown.AddOptions(options);
        resolutionDropdown.value = currentResolutionIndex;
        resolutionDropdown.RefreshShownValue();
    }

    public AudioMixer audioMixer;
    public void SetVolume (float volume)
    {
        audioMixer.SetFloat("volume", volume);
        AudioListener.volume = volume;
        PlayerPrefs.SetFloat("volum", volume);
    }

    public AudioSource music;
    public void SetMusic(float volume)
    {
        music.volume = volume;
        PlayerPrefs.SetFloat("music",volume);
    }

    public void SetQuality (int qualityIndex)
    {
        QualitySettings.SetQualityLevel(qualityIndex);
        PlayerPrefs.SetInt("qualityIndex", qualityIndex);
    }

    public void SetFullscreen (bool isFullscreen)
    {
        Screen.fullScreen = isFullscreen;
        PlayerPrefs.SetInt("fullScreen", isFullscreen ? 1 : 0);

    }

    public void SetResolution (int resolutionIndex)
    {
        Resolution resolution = resolutions[resolutionIndex];
        Screen.SetResolution(resolution.width, resolution.height, Screen.fullScreen);
        PlayerPrefs.SetInt("resolution", resolutionIndex);
    }

}
