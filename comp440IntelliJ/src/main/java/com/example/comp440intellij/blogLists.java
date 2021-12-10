package com.example.comp440intellij;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class blogLists implements Initializable
{
    @FXML
    private TableColumn<blogRow,String> blogDate;
    @FXML
    private TableColumn<blogRow,String> blogTitle;
    @FXML
    private TableColumn<blogRow,String> createdBy;
    @FXML
    private TableColumn<blogRow,String> blogId;

    @FXML
    private Button logOutButton;
    @FXML
    private Button viewButton;
    @FXML
    private Button searchBlogsButton;
    @FXML
    private Button searchUsersButton;

    @FXML
    private TableView blogTable;
    @FXML
    private Button newBlogButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        blogDate.setCellValueFactory(new PropertyValueFactory<blogRow, String>("date"));
        blogTitle.setCellValueFactory(new PropertyValueFactory<blogRow, String>("subject"));
        createdBy.setCellValueFactory(new PropertyValueFactory<blogRow, String>("author"));
        blogId.setCellValueFactory(new PropertyValueFactory<blogRow, String>("id"));

        blogTable.getItems().setAll(DBManager.getBLogList());

        blogTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() //View blog on selection
        {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1)
            {
                if(blogTable.getSelectionModel().selectedItemProperty() != null)
                {
                    blogRow b = (blogRow)t1;
                    DBManager.setCurrentId(Integer.valueOf(b.getId()));
                }
                else
                {
                    DBManager.setCurrentId(-1);
                }
            }
        });
        logOutButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                DBManager.setLoggedInUser(null);
                DBManager.changeWindow(actionEvent,"SignInBackUp.fxml", "Sign In");
            }
        });
        newBlogButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                DBManager.changeWindow(actionEvent, "newBlog.fxml", "Create a new Blog!");
            }

        });
        viewButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                if(DBManager.getCurrentId() != -1)
                {
                    DBManager.getBlogContent();
                    DBManager.changeWindow(actionEvent, "blogView.fxml", "Look at this blog!");
                }

            }

        });
        searchUsersButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                DBManager.changeWindow(actionEvent, "searchUsers.fxml", "Search Users!");
            }

        });
        searchBlogsButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                DBManager.changeWindow(actionEvent, "searchBlogs.fxml", "Search Blogs!");
            }

        });

    }

}

