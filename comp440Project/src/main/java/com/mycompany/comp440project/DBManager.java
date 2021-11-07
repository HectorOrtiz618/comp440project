/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.comp440project;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import javafx.stage.Stage;
import javafx.scene.Node;
/**
 *
 * @author MonkeyBoy
 */
public class DBManager 
{        
    
    public static void changeWindow(ActionEvent event, String fxmlFile, String title, String username, String password)
    {
        Parent root = null;
        if(username != null && password != null)
        {
            try 
            {
                FXMLLoader loader =new FXMLLoader(DBManager.class.getResource(fxmlFile));
                root = loader.load();                      
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                root = FXMLLoader.load(DBManager.class.getResource(fxmlFile));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        //Stage.setTitle(title);
        stage.show();
    }
}
    

