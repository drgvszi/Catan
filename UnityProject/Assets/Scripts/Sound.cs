using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Sound : MonoBehaviour
{
    void Awake()
    {
        DontDestroyOnLoad(this.gameObject);
        AudioSource music = GameObject.Find("Audio Source").GetComponent<AudioSource>();
        music.volume = PlayerPrefs.GetFloat("music");
    }
}
