using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class provizoriu : MonoBehaviour
{
    public GameObject piece;
    public GameObject allPieces;
    public GameObject newPiece;
    public int numar;

    void Update()
    {
        this.numar = numar;
    }

    void OnMouseDown()
    {
      
        allPieces.SetActive(false);
        Instantiate(newPiece, piece.transform.position, piece.transform.rotation);

    }

}
