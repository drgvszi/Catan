using System.Collections;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using UnityEngine;

public class provisoriuO : MonoBehaviour
{
    public GameObject piece;
    public GameObject newPiece;
    public bool ok = false;


    public void setOK()
    {
        ok = true;
    }

    void Update()
    {
        print(ok);
    }

    void OnMouseDown()
    {
        if (ok)
        {
            piece.SetActive(false);
            Instantiate(newPiece, piece.transform.position, piece.transform.rotation);
            ok = false;
        }
    }
}
