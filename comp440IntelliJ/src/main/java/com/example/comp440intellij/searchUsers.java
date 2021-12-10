package com.example.comp440intellij;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;


public class searchUsers implements Initializable
{
    @FXML
    private TableView userTable;

    @FXML
    private TableColumn<userRow,String> usersColumn;

    @FXML
    private SplitMenuButton searchOptions;

    @FXML
    private MenuItem option1;
    @FXML
    private MenuItem option2;
    @FXML
    private MenuItem option3;
    @FXML
    private MenuItem option4;
    @FXML
    private MenuItem option5;

    @FXML
    private Button searchButton;
    @FXML
    private Button backButton;

    @FXML
    private TextField user1;

    @FXML
    private TextField user2;

    private int currentOption = 0;

    @Override
    public void initialize(URL location, ResourceBundle resource)
    {
        option1.setOnAction((e)->
        {
            currentOption = 1;
            searchOptions.setText(option1.getText());
            if(user1.isVisible() && user2.isVisible()&& !user1.isDisabled() && !user2.isDisabled()) //if visible and enabled, make them invisible and disable
            {
                user1.setVisible(false);
                user1.setDisable(true);
                user1.clear();

                user2.setVisible(false);
                user2.setDisable(true);
                user2.clear();
            }
        });
        option2.setOnAction((e)->
        {
            currentOption = 2;
            searchOptions.setText(option2.getText());
            if(!user1.isVisible() && !user2.isVisible()&& user1.isDisabled() && user2.isDisabled()) //if invisible and disabled, make them visible and enable
            {
                user1.setVisible(true);
                user1.setDisable(false);

                user2.setVisible(true);
                user2.setDisable(false);
            }
        });
        option3.setOnAction((e)->
        {
            currentOption = 3;
            searchOptions.setText(option3.getText());
            if(user1.isVisible() && user2.isVisible()&& !user1.isDisabled() && !user2.isDisabled()) //if visible and enabled, make them invisible and disable
            {
                user1.setVisible(false);
                user1.setDisable(true);
                user1.clear();

                user2.setVisible(false);
                user2.setDisable(true);
                user2.clear();
            }
        });
        option4.setOnAction((e)->
        {
            currentOption = 4;
            searchOptions.setText(option4.getText());
            if(user1.isVisible() && user2.isVisible()&& !user1.isDisabled() && !user2.isDisabled()) //if visible and enabled, make them invisible and disable
            {
                user1.setVisible(false);
                user1.setDisable(true);
                user1.clear();

                user2.setVisible(false);
                user2.setDisable(true);
                user2.clear();
            }
        });
        option5.setOnAction((e)->
        {
            currentOption = 5;
            searchOptions.setText(option5.getText());
            if(user1.isVisible() && user2.isVisible()&& !user1.isDisabled() && !user2.isDisabled()) //if visible and enabled, make them invisible and disable
            {
                user1.setVisible(false);
                user1.setDisable(true);
                user1.clear();

                user2.setVisible(false);
                user2.setDisable(true);
                user2.clear();
            }
        });
        //searchOptions.getItems().addAll(option1,option2,option3,option4,option5);

        usersColumn.setCellValueFactory(new PropertyValueFactory<userRow, String>("user"));
        backButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                DBManager.changeWindow(actionEvent,"blogList.fxml", "Welcome " + DBManager.getLoggedInUser());
            }
        });
        searchButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                if(currentOption == 0 )
                {
                    //alert user to make a selection
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please select an option!");
                    alert.show();
                }
                else if(currentOption == 2 && (user1.getText().trim().isEmpty() || user2.getText().trim().isEmpty() ))// option 2 is selected and fields are empty
                {
                    //alert user that field must be completed
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Make sure that you filled both fields for this option!");
                    alert.show();
                }
                else//good to go
                {
                    userTable.getItems().setAll(DBManager.getUserQuery(currentOption, user1.getText().trim(), user2.getText().trim()));
                }
            }
        });
    }
}
