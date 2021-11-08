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

import java.sql.*;
/**
 *
 * @author MonkeyBoy
 */
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

    public static void signUpDB(ActionEvent e, String username, String password, String firstName, String lastName, String Email) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psIsExistingUser = null;
        ResultSet results = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp440", "root", "comp440");
            psIsExistingUser = connection.prepareStatement("Select * FROM users WHERE username = ? ");
            psIsExistingUser.setString(1, username);

            results = psIsExistingUser.executeQuery();
            if (results.isBeforeFirst())// checks if username is taken
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username is taken, please enter a diffrent username");
                alert.show();
            } else// username isnt taken and we can add it
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
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (psIsExistingUser != null) {
                try {
                    psIsExistingUser.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
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
            } else//credentials are fine, go to LoggedIn
            {
                while (rs.next()) {
                    String retrivedPassword = rs.getString("password");
                    if (retrivedPassword.equals(password)) {
                        changeWindow(e, "resetDB.fxml", "Welcome!", username, password);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Username or password do not match");
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
    public static void ClearDB(ActionEvent e)
    {

    }
}
    

