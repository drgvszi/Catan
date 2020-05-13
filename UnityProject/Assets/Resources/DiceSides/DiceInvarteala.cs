using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;

public class DiceInvarteala : MonoBehaviour
{
    public GameObject side1;
    public GameObject side2;
    public GameObject side3;
    public GameObject side4;
    public GameObject side5;
    public GameObject side6;
    public int nr;
    public void Show()
    {
        System.Random rnd = new System.Random();
        int i = rnd.Next(1, 7);
        int j = rnd.Next(1, 7);
        switch (i)
            {
                case 1:
                    side1.SetActive(true);
                    side2.SetActive(false);
                    side3.SetActive(false);
                    side4.SetActive(false);
                    side5.SetActive(false);
                    side6.SetActive(false);
                    nr = 1;
                    break;

                case 2:
                    side1.SetActive(false);
                    side2.SetActive(true);
                    side3.SetActive(false);
                    side4.SetActive(false);
                    side5.SetActive(false);
                    side6.SetActive(false);
                    nr = 2;
                    break;
                case 3:
                    side1.SetActive(false);
                    side2.SetActive(false);
                    side3.SetActive(true);
                    side4.SetActive(false);
                    side5.SetActive(false);
                    side6.SetActive(false);
                    nr = 3;
                    break;

                case 4:
                    side1.SetActive(false);
                    side2.SetActive(false);
                    side3.SetActive(false);
                    side4.SetActive(true);
                    side5.SetActive(false);
                    side6.SetActive(false);
                    nr = 4;
                    break;
                case 5:
                    side1.SetActive(false);
                    side2.SetActive(false);
                    side3.SetActive(false);
                    side4.SetActive(false);
                    side5.SetActive(true);
                    side6.SetActive(false);
                    nr = 5;
                    break;

                case 6:
                    side1.SetActive(false);
                    side2.SetActive(false);
                    side3.SetActive(false);
                    side4.SetActive(false);
                    side5.SetActive(false);
                    side6.SetActive(true);
                    nr = 6;
                    break;
            }
        switch (j)
        {
            case 1:
                side1.SetActive(true);
                side2.SetActive(false);
                side3.SetActive(false);
                side4.SetActive(false);
                side5.SetActive(false);
                side6.SetActive(false);
                nr = 1;
                break;

            case 2:
                side1.SetActive(false);
                side2.SetActive(true);
                side3.SetActive(false);
                side4.SetActive(false);
                side5.SetActive(false);
                side6.SetActive(false);
                nr = 2;
                break;
            case 3:
                side1.SetActive(false);
                side2.SetActive(false);
                side3.SetActive(true);
                side4.SetActive(false);
                side5.SetActive(false);
                side6.SetActive(false);
                nr = 3;
                break;

            case 4:
                side1.SetActive(false);
                side2.SetActive(false);
                side3.SetActive(false);
                side4.SetActive(true);
                side5.SetActive(false);
                side6.SetActive(false);
                nr = 4;
                break;
            case 5:
                side1.SetActive(false);
                side2.SetActive(false);
                side3.SetActive(false);
                side4.SetActive(false);
                side5.SetActive(true);
                side6.SetActive(false);
                nr = 5;
                break;

            case 6:
                side1.SetActive(false);
                side2.SetActive(false);
                side3.SetActive(false);
                side4.SetActive(false);
                side5.SetActive(false);
                side6.SetActive(true);
                nr = 6;
                break;
        }
        }
}
