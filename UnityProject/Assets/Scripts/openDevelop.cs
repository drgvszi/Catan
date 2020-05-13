using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

public class openDevelop : MonoBehaviour
{
    public GameObject cards;
    Animator animator;


    public void MouseOver()
    {
        Animator animator = cards.GetComponent<Animator>();
        animator.SetBool("openD", true);
  
    }
    public void MouseExit()
    {
        Animator animator = cards.GetComponent<Animator>();
        animator.SetBool("openD", false);
    }
}
