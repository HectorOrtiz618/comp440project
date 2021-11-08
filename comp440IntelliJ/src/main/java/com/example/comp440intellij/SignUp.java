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
    private PasswordField confirmPasswordBox;
    
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
        continueButton.setOnAction(new EventHandler<ActionEvent>()
        {
           @Override
           public void handle(ActionEvent e)
            {
                String pw = passwordBox.getText();
                String cpw = confirmPasswordBox.getText();
                if(!pw.equals(cpw))
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Passwords do not match");
                    alert.show();
                }
                else
                    DBManager.signUpDB(e,userBox.getText(),passwordBox.getText(),firstNameBox.getText(),lastNameBox.getText(),emailBox.getText());
            }
        });
    }

}
