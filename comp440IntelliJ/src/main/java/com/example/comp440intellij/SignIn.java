/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.comp440intellij;

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
public class SignIn implements Initializable
{
    @FXML
    private TextField userBox;
    
    @FXML
    private PasswordField passwordBox;
    
    @FXML
    private Button logInButton;
    
    @FXML
    private Button signUpButton;
    
    @Override
    public void initialize(URL location, ResourceBundle resource)
    {
       logInButton.setOnAction(new EventHandler<ActionEvent>()
       {
           @Override
           public void handle(ActionEvent e)
           {
               DBManager.logIn(e, userBox.getText(), passwordBox.getText());
           }
       });
       signUpButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                DBManager.changeWindow(e,"newUser.fxml","Sign Up!",null,null);
            }
        });
    }
}