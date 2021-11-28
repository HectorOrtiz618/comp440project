package com.example.comp440intellij;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class blogView implements Initializable
{
    private String username;
    private String password;
    private String reception;
    @FXML
    private Text title;

    @FXML
    private Text date;

    @FXML
    private Text author;

    @FXML
    private Text blogID;

    @FXML
    private TextField commentField;

    @FXML
    private TableColumn<String,String> dates;
    @FXML
    private TableColumn<String,String> receptions;
    @FXML
    private TableColumn<String,String> comments;
    @FXML
    private TableColumn<String,String> usersnames;
    @FXML
    private Button commentButton;
    @FXML
    private TextField backButton;


    public static void getBlogData(String Title, String Date, String Description, String Tags)//results from DBmanager gets inserted into blog
    {

    }
    public static void getBlogComments(String date,String user,String sentiment)// results from DB manager gets inserted into comments
    {

    }

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        commentButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {DBManager.insertComment(username,author.getText(),reception,commentField.getText(),blogID.getText()); //TODO: error checking, we dont want to push null values into DB
            }
        });
        backButton.setOnAction(new EventHandler<ActionEvent>()
        {
            //Go Back to blogView
            @Override
            public void handle(ActionEvent e) {
                DBManager.changeWindow(e, "blogView.fxml", "Welcome!", username, password);
            }
        });
    }
}
