using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class RobberShowCanvas : MonoBehaviour
{

    public GameObject canvasMoveRobber;
    public void enableRobber()
    {
        canvasMoveRobber.transform.gameObject.SetActive(true);
    }

    public void disableRobber()
    {
        canvasMoveRobber.transform.gameObject.SetActive(false);
    }
}
