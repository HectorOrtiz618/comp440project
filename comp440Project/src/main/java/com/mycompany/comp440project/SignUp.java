/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.comp440project;

import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

/**
 *
 * @author MonkeyBoy
 */
public class SignUp implements Initializable
{
    @FXML
    private TextField userBox;
    
    @FXML
    private PasswordField passwordBox;
    
    @FXML
    private PasswordField confirmpasswordBox;
    
    @FXML
    private TextField firstNameBox;
    
    @FXML
    private TextField lastNameBox;
    
    @FXML
    private TextField emailBox;
    
    @FXML 
    private Button continueButton;
    @Override
    public void initialize(URL location, ResourceBundle resource)
    {
        
    }

public static void signUp(ActionEvent event, String userName, String password, String password2, String email, String firstName, String LastName)
    {
        if(!password.equals(password2))//passwords do not match
        {
            //Use Alerts to signify that passwords do not match
        }
        //check that email and username isnt alrady taken, Use Alerts to signify
    }    
}
