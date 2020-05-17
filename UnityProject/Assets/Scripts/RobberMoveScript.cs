using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class RobberMoveScript : MonoBehaviour
{
    public GameObject robber;

    Vector3 startPos = new Vector3(3.625f, -634.0f, 3.0f);

    public void hexagon1()
    {
        startPos = new Vector3(7.15f, -634.0f, 3.0f);
    }
    public void hexagon2()
    {
        startPos = new Vector3(5.35f, -634.0f, 6.0f);
    }
    public void hexagon3()
    {
        startPos = new Vector3(1.9f, -634.0f, 6.0f);
    }
    public void hexagon4()
    {
        startPos = new Vector3(0.1f, -634.0f, 3.0f);
    }
    public void hexagon5()
    {
        startPos = new Vector3(1.9f, -634.0f, 0.0f);
    }
    public void hexagon6()
    {
        startPos = new Vector3(5.4f, -634.0f, 0.0f);
    }
    public void hexagon7()
    {
        startPos = new Vector3(10.5f, -634.0f, 3.0f);
    }
    public void hexagon8()
    {
        startPos = new Vector3(8.9f, -634.0f, 6.0f);
    }
    public void hexagon9()
    {
        startPos = new Vector3(7.1f, -634.0f, 9.0f);
    }
    public void hexagon10()
    {
        startPos = new Vector3(3.625f, -634.0f, 9.0f);
    }
    public void hexagon11()
    {
        startPos = new Vector3(0.1f, -634.0f, 9.0f);
    }
    public void hexagon12()
    {
        startPos = new Vector3(-1.6f, -634.0f, 6.0f);
    }
    public void hexagon13()
    {
        startPos = new Vector3(-3.35f, -634.0f, 3.0f);
    }
    public void hexagon14()
    {
        startPos = new Vector3(-1.6f, -634.0f, 0.0f);
    }
    public void hexagon15()
    {
        startPos = new Vector3(0.1f, -634.0f, -3.0f);
    }
    public void hexagon16()
    {
        startPos = new Vector3(3.625f, -634.0f, -3.0f);
    }
    public void hexagon17()
    {
        startPos = new Vector3(7.1f, -634.0f, -3.0f);
    }
    public void hexagon18()
    {
        startPos = new Vector3(8.9f, -634.0f, 0.0f);
    }
    public void Update()
    {
        transform.position = Vector3.MoveTowards(transform.position, startPos , Time.deltaTime * 5);
    }
}
