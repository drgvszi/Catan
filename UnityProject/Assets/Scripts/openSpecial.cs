using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class openSpecial : MonoBehaviour
{
    public GameObject cards;
    Animator animator;


    public void MouseOver()
    {
        Animator animator = cards.GetComponent<Animator>();
        animator.SetBool("openS", true);

    }
    public void MouseExit()
    {
        Animator animator = cards.GetComponent<Animator>();
        animator.SetBool("openS", false);
    }
}
