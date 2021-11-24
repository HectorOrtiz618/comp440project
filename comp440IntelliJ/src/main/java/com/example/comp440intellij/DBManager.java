/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.comp440intellij;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class DBManager {

    public static void changeWindow(ActionEvent event, String fxmlFile, String title, String username, String password) {
        Parent root = null;
        if (username != null && password != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBManager.class.getResource(fxmlFile));
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            {
                try
                {
                    root = FXMLLoader.load(DBManager.class.getResource(fxmlFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root,600,400));
        stage.show();
    }
    public static void changeToBlogWindow(ActionEvent event, String fxmlFile, String title, String username, String password, String ID)
    {

    }

    public static void signUpDB(ActionEvent e, String username, String password, String firstName, String lastName, String Email) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psIsExistingUser = null;
        ResultSet resultsUser = null;

        PreparedStatement psIsExistingEmail = null;
        ResultSet resultsEmail = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp440", "root", "comp440");
            psIsExistingUser = connection.prepareStatement("Select * FROM users WHERE username = ? ");
            psIsExistingUser.setString(1, username);

            resultsUser = psIsExistingUser.executeQuery();

            psIsExistingEmail = connection.prepareStatement("Select * FROM users WHERE email = ? ");
            psIsExistingEmail.setString(1,Email);

            resultsEmail= psIsExistingEmail.executeQuery();
            if (resultsUser.isBeforeFirst())// checks if username is taken
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username is taken, please enter a different username");
                alert.show();
            }
            else if(resultsEmail.isBeforeFirst())//If email is already taken
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Email is taken, please enter a different email or contact customer service");
                alert.show();
            }
            else// username and isn't taken and we can add it
            {
                psInsert = connection.prepareStatement("INSERT INTO users (username,password,firstname,lastname,email) VALUES (?, ?, ?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, firstName);
                psInsert.setString(4, lastName);
                psInsert.setString(5, Email);
                psInsert.executeUpdate();

                changeWindow(e, "resetDB.fxml", "Welcome!", username, password);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally //we have to close the connections once done, or it will lead to memory leak
        {
            if (resultsUser != null) {
                try
                {
                    resultsUser.close();
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
            if (resultsEmail != null)
            {
                try
                {
                    resultsEmail.close();
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                }
                catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (psIsExistingUser != null)
            {
                try
                {
                    psIsExistingUser.close();
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
            if (psIsExistingEmail != null)
            {
                try
                {
                    psIsExistingEmail.close();
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
            if (connection != null)
            {
                try
                {
                    connection.close();
                } catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void logIn(ActionEvent e, String username, String password) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp440", "root", "comp440");
            ps = connection.prepareStatement("SELECT password FROM users WHERE username = ?");
            ps.setString(1,username);
            rs = ps.executeQuery();
            if (!rs.isBeforeFirst())// bad credentials
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username or Password is incorrect");
                alert.show();
            }
            else//bad credentials
            {
                while (rs.next()) {
                    String retrivedPassword = rs.getString("password");
                    if (retrivedPassword.equals(password)) {
                        changeWindow(e, "resetDB.fxml", "Welcome!", username, password);
                    }
                    else
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Username or Password is incorrect");
                        alert.show();
                    }
                }
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
            if (ps != null)
            {
                try
                {
                    ps.close();
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }
    public static void getBLogList()//GetList of Comments and Insert into TableView
    {
        //Get Date, Name, Blog Title and ID and push into TableView
    }
    public static void insertBlog(String username, String blogTitle, String description, String tags)
    {
        //we can gererate date with SELECT CAST( GETDATE() AS Date ); which will generate the Date as YYYY-MM-DD
        //No more than 2 Blogs a day, we can check this by counting the blogs made by suer and todays date
        //We can split the blogs using the string split function https://www.geeksforgeeks.org/split-string-java-examples/ we can store the slplit string withan array of strings
        // We will then push the tags with the same blogID until we go through the array fo strings

    }
    public static void getComments()//Get the list of comments and insert into TableView
    {
        //Get Date, Name, Sentiment, and Comment and push into the tableview on BlogView
    }
    public static void insertComment(String username, String blogAuthor, String reception, String comment, String blogId)//Insert comment into DataBase
    {
        //we can genrerate date with SELECT CAST( GETDATE() AS Date ); which will generate the Date as YYYY-MM-DD
        //Check if the commenting user isn't the blogAuthor, throw alert if they're the same
        //No more than 3 comments a day, we can check by counting the amount of comments containing the same username and today's date
    }
    public static void ClearDB(ActionEvent e)
    {
        Connection connection = null;
        PreparedStatement psClearTable = null;
        PreparedStatement psInsertTable = null;
        PreparedStatement psInsertComp440User = null;
        try
        {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp440", "root", "comp440");
            psClearTable = connection.prepareStatement("DROP TABLE IF EXISTS users");
            psClearTable.executeUpdate();
            psInsertTable = connection.prepareStatement("CREATE TABLE `users` (`username` varchar(50) NOT NULL,`password` varchar(255) NOT NULL,`firstname` varchar(50) NOT NULL,`lastname` varchar(50) NOT NULL,`email` varchar(50) NOT NULL, PRIMARY KEY (`username`), UNIQUE KEY `email` (`email`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            psInsertTable.executeUpdate();
            psInsertComp440User = connection.prepareStatement("INSERT INTO users (username, password, firstname, lastname, email) VALUES ('Comp440', 'pass1234', 'John','Doe', 'JohnD@email.com')");
            psInsertComp440User.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Database has been reset! Returning to Login Screen!");
            alert.show();

            if(psClearTable != null)
            {
                psClearTable.close();
            }
            if(psInsertTable != null)
            {
                psInsertTable.close();
            }
            if(psInsertComp440User != null)
            {
                psInsertComp440User.close();
            }
            if(connection != null)
            {
                connection.close();
            }
            changeWindow(e,"SignInBackup.FXML","Log In!",null,null);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
}
    

