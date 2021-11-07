/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.comp440project;

/**
 *
 * @author MonkeyBoy
 */
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;

public class LoggedIn implements Initializable
{
    @FXML
    private Button resetButton;
    
    @Override
    public void initialize(URL location, ResourceBundle resource)
    {
        resetButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                clearDB(e);
            }
        });
    }
    private void clearDB(ActionEvent event)
    {
        DBManager.changeWindow(event,"SignIn.fxml","Log in!",null,null);
    }
}
