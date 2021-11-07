package com.mycompany.comp440project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

/**
 * JavaFX App
 */

public class App extends Application 
{

    @Override
    public void start(Stage stage) throws Exception 
    {
        Parent root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
        stage.setTitle("Group 8 Login");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void main(String[] args) 
    {
        launch(args);
    }

}