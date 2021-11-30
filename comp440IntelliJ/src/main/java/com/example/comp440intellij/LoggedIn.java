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

public class LoggedIn implements Initializable
{

    @FXML
    private Button resetButton;
    @FXML
    private Button logOutButton;
    
    @Override
    public void initialize(URL location, ResourceBundle resource)
    {
        resetButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                DBManager.ClearDB(e);
            }
        });
        logOutButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                DBManager.setLoggedInUser(null);
                DBManager.changeWindow(e,"SignInBackup.fxml","Log In!");
            }
        });
    }

}
