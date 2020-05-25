using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;


public class RegisterScript : MonoBehaviour
{
    public InputField usernameField;
    public InputField emailField;
    public InputField passwordField;
    public InputField passwordConfirmField;
    public GameObject panel;

    public Text errorBoxMsg;

    public void RegisterNewUser() // poate fi transforata in bool prin schimbare return si decomentare
    {
        bool valid = false;

        string username = usernameField.text;
        string email = emailField.text;
        string password = passwordField.text;
        string passwordConfirm = passwordConfirmField.text;

        if (username.Equals(""))
        {
            errorBoxMsg.text = "Username field is empty!";
            Debug.Log("Username field is empty!");
            panel.SetActive(true);

            //return false;
        }
        else if (email.Equals(""))
        {
            errorBoxMsg.text = "Email field is empty!";
            Debug.Log("Email field is empty!");
            panel.SetActive(true);
            //return false;
        }
        else if (IsValidEmail(email) == false)
        {
            errorBoxMsg.text = "Email is not valid";
            Debug.Log("Email is not valid");
            panel.SetActive(true);
            //return false;
        }
        else if (password.Equals(""))
        {
            errorBoxMsg.text = "Password field is empty!";
            Debug.Log("Password field is empty!");
            panel.SetActive(true);
            //return false;
        }
        else if (password.Length < 6)
        {
            errorBoxMsg.text = "The password is too short!";
            Debug.Log("The password is too short!");
            panel.SetActive(true);
            //return false;
        }
        else if (passwordConfirm.Equals(""))
        {
            errorBoxMsg.text = "Confirm Password field is empty!";
            Debug.Log("Confirm Password field is empty!");

            panel.SetActive(true);
            //return false;
        }
        else if (!(password.Equals(passwordConfirm)))
        {
            errorBoxMsg.text = "The passwords don't match!";
            Debug.Log("The passwords don't match!");
            panel.SetActive(true);
            //return false;
        }
        else
        {
            bool temp = true;
            DatabaseHandler.GetUserByUsername(username, user_returnat =>
                {
                    if (user_returnat == null) //se poate face insert
                    {
                        Debug.Log($"The username is available");
                        //Debug.Log($"Name: {usernameField.text}, Email: {emailField.text}, Password: {passwordField.text} PasswordConfirm: {passwordConfirmField.text}");
                        UserCredentials newUser = new UserCredentials(usernameField.text, passwordField.text, emailField.text);
                        DatabaseHandler.PostUser(newUser, () => Debug.Log("User created successfully!"));
                        errorBoxMsg.text = "User created successfully";
                        
                        LoginScript.CurrentUser = usernameField.text;
                        LoginScript.CurrentUserPassword = passwordField.text;
                        LoginScript.CurrentUserEmail = emailField.text;
                        SceneManager.LoadScene("MenuScene"); // Temporary
                        temp = true;
                    }
                    else if (user_returnat.username.Equals(username))
                    {
                        errorBoxMsg.text = "The username is already in use. Use another!";
                        panel.SetActive(true);
                        Debug.Log($"The username is already in use. Use another!");
                        temp = false;
                    }                   
                }
             );
            //return temp;

        }

        if (valid == true)
            errorBoxMsg.text = "Succes";

        //return valid;
    }

    public bool IsValidEmail(string email)
    {
        try
        {
            var addr = new System.Net.Mail.MailAddress(email);
            return addr.Address == email;
        }
        catch
        {
            return false;
        }
    }
}
