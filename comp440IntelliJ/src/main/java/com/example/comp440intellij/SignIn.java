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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;


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
    @FXML
    private Button initDBButton;
    
    @Override
    public void initialize(URL location, ResourceBundle resource)
    {
       logInButton.setOnAction(new EventHandler<ActionEvent>()
       {
           @Override
           public void handle(ActionEvent e)
           {
               if(userBox.getText().trim().isEmpty()|| passwordBox.getText().trim().isEmpty())//fields are empty, we cant continue
               {
                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setContentText("Please fill out all fields and try again");
                   alert.show();
               }
               else
                DBManager.logIn(e, userBox.getText(), passwordBox.getText());
           }
       });
       signUpButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                DBManager.changeWindow(e,"newUser.fxml","Sign Up!");
            }
        }
        );
        initDBButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                DBManager.ClearDB(e);
            }
        });
    }
}
