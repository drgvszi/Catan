﻿using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AfiseazaDrum : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public static void afiseaza(GameObject o, GameObject x)
    {
        Instantiate(o, x.transform.position, x.transform.rotation);
    }

}
