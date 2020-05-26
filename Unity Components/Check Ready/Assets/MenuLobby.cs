using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;


public class MenuLobby : MonoBehaviour
{
    public Image CheckImage1;
    public Image CheckImage2;
    public Image SelectImage1;
    public Image SelectImage2;
    public Image SelectImage3;
    public Image SelectImage4;
    public Image SelectImage5;
    public Image imgExtensii1;
    public Text textExtensii1;
    public Image imgExtensii2;
    public Text textExtensii2;
    public Image imgExtensii3;
    public Text textExtensii3;
    public Image imgExtensii4;
    public Text textExtensii4;
    public Image imgExtensii5;
    public Text textExtensii5;



    void Start()
    {
        CheckImage1 = GameObject.Find("Image1").GetComponent<Image>();
        CheckImage1.enabled = false;

        CheckImage2 = GameObject.Find("Image2").GetComponent<Image>();
        CheckImage2.enabled = false;

        SelectImage1 = GameObject.Find("ImageSelectExp1").GetComponent<Image>();
        SelectImage1.enabled = false;

        SelectImage2 = GameObject.Find("ImageSelectExp2").GetComponent<Image>();
        SelectImage2.enabled = false;

        SelectImage3 = GameObject.Find("ImageSelectExp3").GetComponent<Image>();
        SelectImage3.enabled = false;

        SelectImage4 = GameObject.Find("ImageSelectExp4").GetComponent<Image>();
        SelectImage4.enabled = false;

        SelectImage5 = GameObject.Find("ImageSelectExp5").GetComponent<Image>();
        SelectImage5.enabled = false;


        imgExtensii1 = GameObject.Find("ImageTextExtensie1").GetComponent<Image>();
        imgExtensii1.enabled = false;
        textExtensii1 = GameObject.Find("TextExtensie1").GetComponent<Text>();
        textExtensii1.enabled = false;

        imgExtensii1 = GameObject.Find("ImageTextExtensie2").GetComponent<Image>();
        imgExtensii1.enabled = false;
        textExtensii1 = GameObject.Find("TextExtensie2").GetComponent<Text>();
        textExtensii1.enabled = false;

        imgExtensii1 = GameObject.Find("ImageTextExtensie3").GetComponent<Image>();
        imgExtensii1.enabled = false;
        textExtensii1 = GameObject.Find("TextExtensie3").GetComponent<Text>();
        textExtensii1.enabled = false;

        imgExtensii1 = GameObject.Find("ImageTextExtensie4").GetComponent<Image>();
        imgExtensii1.enabled = false;
        textExtensii1 = GameObject.Find("TextExtensie4").GetComponent<Text>();
        textExtensii1.enabled = false;

        imgExtensii1 = GameObject.Find("ImageTextExtensie5").GetComponent<Image>();
        imgExtensii1.enabled = false;
        textExtensii1 = GameObject.Find("TextExtensie5").GetComponent<Text>();
        textExtensii1.enabled = false;


    }
    public void apasaCheck(Image customImage)
    {

        CheckImage2.enabled = false;
        CheckImage1.enabled = false;
        customImage.enabled = true;

    }

    public void apasaExansion(Image customImage)
    {
        SelectImage1.enabled = false;
        SelectImage2.enabled = false;
        SelectImage3.enabled = false;
        SelectImage4.enabled = false;
        SelectImage5.enabled = false;
        customImage.enabled = true;
    }

    
}
