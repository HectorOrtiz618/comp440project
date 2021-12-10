package com.example.comp440intellij;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class searchBlogs implements Initializable
{
    private int currentOption;
    @FXML
    private TableView blogTable;

    @FXML
    private TableColumn<blogRow,String> dateColumn;
    @FXML
    private TableColumn<blogRow,String> subjectColumn;
    @FXML
    private TableColumn<blogRow,String> authorColumn;
    @FXML
    private TableColumn<blogRow,String> idColumn;

    @FXML
    private SplitMenuButton searchOptions;
    @FXML
    private MenuItem option1;


    @FXML
    private Button searchButton;
    @FXML
    private Button backButton;

    @FXML
    private TextField user1;

    @Override
    public void initialize(URL location, ResourceBundle resource)
    {
        option1.setOnAction((e)->
        {
            currentOption = 1;
            if(user1.isDisabled()&& !user1.isVisible())
            {
                user1.setDisable(false);
                user1.setVisible(true);
            }
        });

        dateColumn.setCellValueFactory(new PropertyValueFactory<blogRow, String>("date"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<blogRow, String>("subject"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<blogRow, String>("author"));
        idColumn.setCellValueFactory(new PropertyValueFactory<blogRow, String>("id"));

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
                if(currentOption == 0)
                {
                    //alert that an option is needs to be selected
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please select and option!");
                    alert.show();
                }
                else if(user1.getText().trim().isEmpty())
                {
                    //alert that the input field must be filled
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please enter a username!");
                    alert.show();
                }
                else//good to go
                {
                    blogTable.getItems().setAll(DBManager.getBlogQuery(currentOption,user1.getText().trim()));
                }
            }
        });

    }
}
