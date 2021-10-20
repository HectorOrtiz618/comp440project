package com.mycompany.comp440project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


/**
 * JavaFX App
 */

public class App extends Application 
{

    @Override
    public void start(Stage stage) 
    {

        
        stage.setTitle("Group 8 Login");

        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        Scene scene = new Scene(grid, 400, 375);
        stage.setScene(scene);
        
        Text scenetitle = new Text("Comp 440 Group 8: Please Sign In");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);
        Button signInButton = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(signInButton);
        grid.add(hbBtn, 1, 4);
        
        signInButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {
                System.out.println("Log in button Pressed!");
                //This is where we want to verify login info.
            }
            
        });
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
         
        
        stage.show();
    }

    public static void main(String[] args) 
    {
        launch(args);
    }

}