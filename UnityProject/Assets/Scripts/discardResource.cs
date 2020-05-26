using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class discardResource : MonoBehaviour
{
    public InputField lumber;
    public InputField birck;
    public InputField ore;
    public InputField wool;
    public InputField grain;

    public void discard()
    {
        int nr_lumber;
        int nr_brick;
        int nr_ore;
        int nr_wool;
        int nr_grain;


        if (lumber.text == "")
            nr_lumber = 0;
        else
           nr_lumber  = int.Parse(lumber.text);

        if (wool.text == "")
            nr_wool = 0;
        else
            nr_wool = int.Parse(wool.text);

        if (grain.text == "")
            nr_grain= 0;
        else
            nr_grain = int.Parse(grain.text);

        if (birck.text == "")
            nr_brick = 0;
        else
            nr_brick = int.Parse(birck.text);

        if (ore.text == "")
            nr_ore = 0;
        else
            nr_ore = int.Parse(ore.text);


        //request discard
    }
}
