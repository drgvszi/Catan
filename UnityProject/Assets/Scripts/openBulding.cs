using System.Collections;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.Security.Cryptography;
using UnityEngine;
using UnityEngine.EventSystems;


public class openBulding : MonoBehaviour
{

    public GameObject cards;
    Animator animator;

    /*
    void Start()
    {
        animator = cards.GetComponent<Animator>();
    }

    void Update()
    {
        if (EventSystem.current.IsPointerOverGameObject() )
        {
            if (EventSystem.current.currentSelectedGameObject != null)
                print(EventSystem.current.currentSelectedGameObject.name);
            animator.SetBool("openB", true);
        }
        else
        {
            animator.SetBool("openB", false);
        }

    }

    */
    public void MouseOver()
    {
        Animator animator = cards.GetComponent<Animator>();
        animator.SetBool("openB", true);

    }
    public void MouseExit()
    {
        Animator animator = cards.GetComponent<Animator>();
        animator.SetBool("openB", false);
    }
}


