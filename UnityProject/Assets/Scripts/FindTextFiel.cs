using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class FindTextFiel : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public static Text find()
    {
        Text txt;
        txt=GameObject.Find("text_raspuns").GetComponent<Text>();
        return txt;
    }
}
