using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;


public class showCards : MonoBehaviour
{
    public GameObject cards;
    Animator animator;

    /*void Start()
    {
        animator = cards.GetComponent<Animator>();
    }

    void Update()
    {
        if (EventSystem.current.IsPointerOverGameObject())
        {
            animator.SetBool("open", true);
        }
        else
        {
            animator.SetBool("open", false);
        }

    }

    */
    public void MouseOver()
    {
        Animator animator = cards.GetComponent<Animator>();
        animator.SetBool("open", true);

    }
    public void MouseExit()
    {
        Animator animator = cards.GetComponent<Animator>();
        animator.SetBool("open", false);
    }
}
