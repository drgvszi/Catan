using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using FullSerializer;
using Proyecto26;

[Serializable]
public class RegisterConnectivityJson
{
    public string email;
    public string username;
    public string password;
    public string confirmpassword;

    public RegisterConnectivityJson(string email, string username, string password, string confirmpassword)
    {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmpassword = confirmpassword;
    }
}
