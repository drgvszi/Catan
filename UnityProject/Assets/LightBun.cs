﻿using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LightBun : MonoBehaviour
{
    bool ok=false;
    public GameObject lighton;
    public GameObject lightoff;
    void OnMouseDown()
    {
        if (ok == false)
            Show();
        else
            Hide();
    }

public void Show()
{
    lighton.SetActive(false);
    lightoff.SetActive(true);
    
    ok = true;
}
public void Hide()
{
    lighton.SetActive(true);
    lightoff.SetActive(false);
    
    ok = false;
}
}
