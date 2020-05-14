using System.Collections;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Security.Cryptography;
using UnityEngine;

public class CameraController : MonoBehaviour
{

    public Transform cameraTransform;

    public float movementSpeed;
    public float movementTime;
    public float rotation;
    public Vector3 zoomAmount;

    public Vector3 newPos;
    public Quaternion newRotation;
    public Vector3 newZoom;
    

    // Start is called before the first frame update
    void Start()
    {
        newPos = gameObject.transform.position;
        newRotation = gameObject.transform.rotation;
        newZoom = cameraTransform.localPosition;
       
    }

    // Update is called once per frame
    void Update()
    {
        HandleMovementInp();
    }
    void HandleMovementInp()
    {
        if (Input.GetKey(KeyCode.W) || Input.GetKey(KeyCode.UpArrow))
        {
            if (newPos.z <= 18.5)
                newPos += (transform.forward * movementSpeed);

        }
        if (Input.GetKey(KeyCode.S) || Input.GetKey(KeyCode.DownArrow))
        {
            if (newPos.z >= -2.3)
                newPos += (transform.forward * -movementSpeed);

        }
        if (Input.GetKey(KeyCode.D) || Input.GetKey(KeyCode.RightArrow))
        {
            if (newPos.x <=17.3)
                newPos += (transform.right * movementSpeed);

        }
        if (Input.GetKey(KeyCode.A) || Input.GetKey(KeyCode.LeftArrow))
        {
            if (newPos.x >= 1.5)
                newPos += (transform.right * -movementSpeed);

        }
        if (Input.GetKey(KeyCode.Q))
        {
            newRotation *= Quaternion.Euler(Vector3.up * rotation);

        }
        if (Input.GetKey(KeyCode.E))
        {
            newRotation *= Quaternion.Euler(Vector3.up * -rotation);

        }
        if (Input.GetKey(KeyCode.R))
        {
            if (newZoom.z<=12)
                newZoom += zoomAmount;

        }
        if (Input.GetKey(KeyCode.F))
        {
            if (newZoom.z>=-13.5)
                newZoom -= zoomAmount;

        }
         
        transform.position = Vector3.Lerp(transform.position, newPos, Time.deltaTime * movementTime);
         transform.rotation = Quaternion.Lerp(transform.rotation, newRotation, Time.deltaTime * movementTime);
         cameraTransform.localPosition = Vector3.Lerp(cameraTransform.localPosition, newZoom, Time.deltaTime * movementTime);
    }
}
