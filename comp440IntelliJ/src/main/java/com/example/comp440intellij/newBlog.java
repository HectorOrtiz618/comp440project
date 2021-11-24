package com.example.comp440intellij;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class newBlog implements Initializable
{
    public String username;
    public String password;
    @FXML
    private TextField blogTitle;

    @FXML
    private TextField blogDesc;

    @FXML
    private TextField blogTags;

    @FXML
    private TableView<String> commentLists;
    @FXML
    private Button cancelButton;
    @FXML
    private Button submitButton;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                DBManager.insertBlog(username,blogTitle.getText(),blogDesc.getText(),blogTags.getText());//TODO: error handling, cant push null values into DB
            }
        });
        cancelButton.setOnAction(new EventHandler<ActionEvent>()
        {
            //Go Back to blogView
            @Override
            public void handle(ActionEvent e) {
                DBManager.changeWindow(e, "blogView.fxml", "Welcome!", username, password);
            }
        });
    }
}
