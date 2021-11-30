package com.example.comp440intellij;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class blogView implements Initializable
{
    private String reception;

    @FXML
    private Text blogTitle;

    @FXML
    private Text blogDate;

    @FXML
    private Text blogAuthor;

    @FXML
    private Text blogID;

    @FXML
    private TextArea blogDesc;
    @FXML
    private Text blogTags;

    @FXML
    private TextArea commentField;

    @FXML
    private TableColumn<commentRow,String> dates;
    @FXML
    private TableColumn<commentRow,String> receptions;
    @FXML
    private TableColumn<commentRow,String> comments;
    @FXML
    private TableColumn<commentRow,String> usernames;
    @FXML
    private TableView commentTable;
    @FXML
    private Button commentButton;
    @FXML
    private SplitMenuButton commentReception;
    @FXML
    private Button backButton;

    /*public void getBlogData(String Title, String Author, String Date, String Desc)
    {
        blogTitle.setText(Title);
        blogDate.setText(Date);
        blogAuthor.setText(Author);
        blogDesc.setText(Desc);
    }*/

    @Override
    public void initialize(URL location, ResourceBundle resource)
    {
        blogID.setText(Integer.toString(DBManager.getCurrentId()));
        blogTitle.setText(DBManager.getTitle());
        blogDate.setText(DBManager.getDate());
        blogAuthor.setText(DBManager.getAuthor());
        blogDesc.setText(DBManager.getDesc());
        blogTags.setText(DBManager.getTags());

        MenuItem choice1 = new MenuItem("Positive");
        MenuItem choice2 = new MenuItem("Negative");

        choice1.setOnAction((e)->
        {
            reception = "positive";
            commentReception.setText("Positive");
        });
        choice2.setOnAction((e)-> {
            reception = "negative";
            commentReception.setText("Negative");
        });
        commentReception.getItems().addAll(choice1, choice2);

        dates.setCellValueFactory(new PropertyValueFactory<commentRow, String>("date"));
        receptions.setCellValueFactory(new PropertyValueFactory<commentRow, String>("sentiment"));
        comments.setCellValueFactory(new PropertyValueFactory<commentRow, String>("comment"));
        usernames.setCellValueFactory(new PropertyValueFactory<commentRow, String>("author"));

        commentTable.getItems().setAll(DBManager.getComments());

        commentButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e)
            {
                if(reception != null && !commentField.getText().isEmpty())// if the user inpputed reception and typed in text
                {
                    DBManager.insertComment(reception, commentField.getText());
                    commentTable.getItems().setAll(DBManager.getComments());// reload comment table
                }
                else
                {
                    //alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Make sure that you have selected a reception and typed your comment!");
                    alert.show();
                }
            }
        });
        backButton.setOnAction(new EventHandler<ActionEvent>()
        {
            //Go Back to blogView
            @Override
            public void handle(ActionEvent e)
            {
                //DBManager.setLoggedInUser(null);
                DBManager.changeWindow(e, "blogList.fxml", "Welcome!");
            }
        });
    }
}
