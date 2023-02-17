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
    private TextArea blogDesc;

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
            public void handle(ActionEvent e)
            {
                if(!blogTitle.getText().trim().isEmpty() && !blogDesc.getText().trim().isEmpty() && !blogTags.getText().trim().isEmpty()) {
                    DBManager.insertBlog(blogTitle.getText().trim(), blogDesc.getText().trim(), blogTags.getText().trim());//trim extra spaces
                    DBManager.changeWindow(e, "blogList.fxml", "Welcome!");
                }
                else
                {
                    //alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Make sure that you have entered in every field!");
                    alert.show();
                }
            }
        });
        cancelButton.setOnAction(new EventHandler<ActionEvent>()
        {
            //Go Back to blogView
            @Override
            public void handle(ActionEvent e)
            {
                DBManager.changeWindow(e, "blogList.fxml", "Welcome!");
            }
        });
    }
}
