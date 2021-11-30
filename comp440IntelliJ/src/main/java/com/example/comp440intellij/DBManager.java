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
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static String loggedInUser = null;
    public static void  setLoggedInUser(String user){ loggedInUser = user;};
    public static String getLoggedInUser() { return loggedInUser;};

    private static int currentBlogId = -1;

    private static String title;
    public static String getTitle() {return title;}

    private static String date;
    public static String getDate(){return date;}

    private static String desc;
    public static String getDesc(){return desc;}

    private static String author;
    public static String getAuthor(){return author;}

    private static String tags;
    public static String getTags(){return tags;}

    public static int getCurrentId() { return currentBlogId;}
    public static void setCurrentId(int i){ currentBlogId = i;};

    public static void changeWindow(ActionEvent event, String fxmlFile, String title)
    {
        Parent root = null;
        if (loggedInUser != null)
        {
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
        if(fxmlFile.equals("blogView.fxml"))
        {
            stage.setScene(new Scene(root, 600, 800));
        }
        else
        {
            stage.setScene(new Scene(root, 600, 400));
        }
        stage.show();
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

                setLoggedInUser(username);
                changeWindow(e, "resetDB.fxml", "Welcome!");

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

    public static void logIn(ActionEvent e, String username, String password)
    {
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
                        setLoggedInUser(username);
                        changeWindow(e, "blogList.fxml", "Welcome " + loggedInUser);
                        getBLogList();
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
    public static List<blogRow> getBLogList()//GetList of blogs and Insert into TableView
    {
        //Get Date, Name, Blog Title and ID and push into TableView
        Connection connection = null;
        PreparedStatement psGetBlogs = null;
        ResultSet resultsBlogs = null;

        List<blogRow> blogs = new ArrayList<blogRow>();
        //If result set is empty, don't push anything and leave the table blank.
        try
        {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp440", "root", "comp440");
            psGetBlogs = connection.prepareStatement("Select * FROM blogs ORDER BY pdate");// if we wanted by earliest date, we use ORDER BY date ASC instead
            resultsBlogs = psGetBlogs.executeQuery();

            while(resultsBlogs.next())
            {
                //Push in the contents into blogLists
                blogs.add(new blogRow(resultsBlogs.getString("pdate"),resultsBlogs.getString("subject"),resultsBlogs.getString("created_by"),resultsBlogs.getString("blogid")));
                //return blogs;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(resultsBlogs != null)
            {
                try
                {
                  resultsBlogs.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psGetBlogs != null)
            {
                try
                {
                    psGetBlogs.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(connection != null)
            {
                try
                {
                    connection.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return blogs;

    }
    public static void getBlogContent()
    {
        //Get Title, author, subject, date, tags and push to blogView
        Connection connection = null;
        PreparedStatement psGetBlog = null;
        ResultSet resultsBlogContent = null;

        PreparedStatement psGetTags = null;
        ResultSet resultTags = null;

        tags = "Tags: ";

        try
        {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp440", "root", "comp440");
            psGetBlog = connection.prepareStatement("SELECT subject, description, created_by, pdate from blogs WHERE blogid = ?");
            psGetBlog.setInt(1, currentBlogId);
            resultsBlogContent = psGetBlog.executeQuery();
            psGetTags = connection.prepareStatement("Select tag from blogstags where blogid = ?");
            psGetTags.setInt(1,currentBlogId);
            resultTags = psGetTags.executeQuery();

            //push contents into blogVIew
            resultsBlogContent.next();
            title = resultsBlogContent.getString("subject");
            date = resultsBlogContent.getString("pdate");
            desc = resultsBlogContent.getString("description");
            author = resultsBlogContent.getString("created_by");
            while(resultTags.next())
            {
                tags += resultTags.getString("tag") + ", ";
            }



        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(resultsBlogContent != null)//close results, preparedStatements, and connections to prevent memory leaks
            {
                try
                {
                    resultsBlogContent.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(resultTags != null)
            {
                try
                {
                    resultTags.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psGetBlog != null)//close results, preparedStatements, and connections to prevent memory leaks
            {
                try
                {
                    psGetBlog.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(connection != null)//close results, preparedStatements, and connections to prevent memory leaks
            {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void insertBlog(String blogTitle, String description, String tags)
    {
        //we can gererate date with SELECT CAST( GETDATE() AS Date ); which will generate the Date as YYYY-MM-DD
        //No more than 2 Blogs a day, we can check this by counting the blogs made by suer and todays date
        //We can split the blogs using the string split function https://www.geeksforgeeks.org/split-string-java-examples/ we can store the slplit string withan array of strings
        // We will then push the tags with the same blogID until we go through the array fo strings
        Connection connection = null;

        PreparedStatement psGetDate = null;
        PreparedStatement psInsertBlog = null;
        PreparedStatement psIsOverDailyLimit = null;
        PreparedStatement psGetBlogId = null;
        PreparedStatement psInsertTags = null;

        ResultSet rsDate = null;
        ResultSet resultsBlogsInserted = null;
        ResultSet rsBlogId = null;
        try
        {
            //See if there are blogs created today
            connection = connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp440", "root", "comp440");
            psGetDate = connection.prepareStatement("Select DATE(NOW())");
            rsDate = psGetDate.executeQuery();

            rsDate.next();
            String today = rsDate.getString("DATE(NOW())");
            psIsOverDailyLimit = connection.prepareStatement("SELECT COUNT(pdate) from blogs where ((pdate = ?) AND (created_by = ?))");
            psIsOverDailyLimit.setString(1,today);
            psIsOverDailyLimit.setString(2,loggedInUser);
            resultsBlogsInserted = psIsOverDailyLimit.executeQuery();
            resultsBlogsInserted.next();
            if(resultsBlogsInserted.getInt("COUNT(pdate)") >= 2)
            {
                //send an alert where the user is notified that they reached their daily limit
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("you have reached your daily blogging limit!");
                alert.show();

            }
            else//push to blogs and blogtags
            {
                psInsertBlog = connection.prepareStatement("INSERT into blogs(subject,description,pdate,created_by) VALUES(?,?,?,?)");
                psInsertBlog.setString(1,blogTitle);
                psInsertBlog.setString(2,description);
                psInsertBlog.setString(3,today);
                psInsertBlog.setString(4,loggedInUser);
                psInsertBlog.executeUpdate();

                //break tags
                String newTags[] = tags.split(",");
                //get blog id, the last entry should be our blog in question
                psGetBlogId = connection.prepareStatement("Select blogid from blogs ORDER BY blogid DESC LIMIT 1");
                rsBlogId = psGetBlogId.executeQuery();

                rsBlogId.next();//rs be default is pointed before first row, we need it pointed at first row

                int id = rsBlogId.getInt("blogid");
                for(int i = 0; i < newTags.length;i++)
                {
                    psInsertTags = connection.prepareStatement("INSERT INTO blogstags(blogid,tag) VALUES (?,?)");
                    psInsertTags.setInt(1,id);
                    psInsertTags.setString(2,newTags[i].trim());//trimming to remove leading or following spaces
                    psInsertTags.executeUpdate();
                }
                //push alert when done
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Your blog has been added!");
                alert.show();
            }

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(rsBlogId != null)
            {
                try
                {
                    rsBlogId.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(resultsBlogsInserted != null)
            {
                try
                {
                    resultsBlogsInserted.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(rsDate != null)
            {
                try
                {
                    rsDate.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psGetBlogId != null)
            {
                try
                {
                    psGetBlogId.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psInsertTags != null)
            {
                try
                {
                    psInsertTags.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psInsertBlog != null)
            {
                try
                {
                    psInsertBlog.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psGetDate != null)
            {
                try
                {
                    psGetDate.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psIsOverDailyLimit != null)
            {
                try
                {
                    psIsOverDailyLimit.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(connection != null)
            {
                try
                {
                    connection.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }

    }
    public static List getComments()//Get the list of comments and insert into TableView
    {
        //Get Date, Name, Sentiment, and Comment and push into the tableview on BlogView
        Connection connection = null;
        PreparedStatement psGetComments = null;
        ResultSet resultComments = null;

        List<commentRow> comments = new ArrayList<commentRow>();
        //If result set is empty, leave the table blank
        try
        {
            connection = connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp440", "root", "comp440");
            psGetComments = connection.prepareStatement("SELECT cdate,posted_by,sentiment,description FROM comments WHERE blogid = ?");
            psGetComments.setInt(1, currentBlogId);
            resultComments = psGetComments.executeQuery();

            while(resultComments.next())//Push results to blogViewController
            {
                final String date = resultComments.getString("cdate");
                final String commentUser =resultComments.getString("posted_by");
                final String sentiment = resultComments.getString("sentiment");
                final String desc = resultComments.getString("description");

                comments.add(new commentRow(date,commentUser,sentiment,desc));
            }

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(resultComments != null)//close results, preparedStatements, and connections to prevent memory leaks
            {
                try
                {
                    resultComments.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psGetComments != null)//close results, preparedStatements, and connections to prevent memory leaks
            {
                try
                {
                    psGetComments.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(connection != null)//close results, preparedStatements, and connections to prevent memory leaks
            {
                try
                {
                    connection.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return comments;
    }
    public static void insertComment(String reception, String comment)//Insert comment into DataBase
    {
        //we can genrerate date with SELECT CAST( GETDATE() AS Date ); which will generate the Date as YYYY-MM-DD
        //Check if the commenting user isn't the blogAuthor, throw alert if they're the same
        //No more than 3 comments a day, we can check by counting the amount of comments containing the same username and today's date

        Connection connection = null;
        PreparedStatement psInsertComment = null;
        PreparedStatement psIsOverDailyLimit = null;
        PreparedStatement psDate = null;
        PreparedStatement psIsOwnBlog = null;
        PreparedStatement psOneCommentPerBlog = null;

        ResultSet rsDate = null;
        ResultSet rsDailyLimit = null;
        ResultSet rsOneCommentPerBlog = null;
        ResultSet rsIsOwnBlog = null;
        try
        {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp440", "root", "comp440");

            psDate = connection.prepareStatement("SELECT DATE(NOW())");
            rsDate = psDate.executeQuery();

            rsDate.next();
            String date = rsDate.getString(1);
            psIsOverDailyLimit = connection.prepareStatement("select count(cdate) from comments where cdate = ? AND posted_by = ?");
            psIsOverDailyLimit.setString(1,date);
            psIsOverDailyLimit.setString(2, loggedInUser);

            psIsOwnBlog = connection.prepareStatement("Select created_by from blogs where blogid = ?");
            psIsOwnBlog.setInt(1,currentBlogId);
            rsIsOwnBlog = psIsOwnBlog.executeQuery();
            rsIsOwnBlog.next();//Initially pointed before first row, wee need to have it point to first row

            psOneCommentPerBlog = connection.prepareStatement("Select count(posted_by) from comments where (posted_by = ? and blogid = ?)");
            psOneCommentPerBlog.setString(1,loggedInUser);
            psOneCommentPerBlog.setInt(2,currentBlogId);

            rsOneCommentPerBlog= psOneCommentPerBlog.executeQuery();
            rsOneCommentPerBlog.next();

            rsDailyLimit = psIsOverDailyLimit.executeQuery();
            rsDailyLimit.next();//Initially pointed before first row, wee need to have it point to first row
            String bA = rsIsOwnBlog.getString("created_by");

            if(rsDailyLimit.getInt("count(cdate)") >= 3)
            {
                //push alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("you have reached your daily commenting limit!");
                alert.show();
            }
            else if(loggedInUser.equals(bA))
            {
                //push alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot comment on your own blog!");
                alert.show();
            }
            else if(rsOneCommentPerBlog.getInt(1) >= 1)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You have already commented on this blog!");
                alert.show();
            }
            else
            {
                //insert comment
                psInsertComment = connection.prepareStatement("INSERT into comments(sentiment,description,cdate,blogid,posted_by)  VALUES (?,?,?,?,?)");
                psInsertComment.setString(1,reception);
                psInsertComment.setString(2,comment);
                psInsertComment.setString(3,date);
                psInsertComment.setInt(4,currentBlogId);
                psInsertComment.setString(5,loggedInUser);
                psInsertComment.executeUpdate();

                //alert user
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Your comment has been added!");
                alert.show();
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(rsDailyLimit != null)
            {
                try
                {
                    rsDailyLimit.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(rsIsOwnBlog!= null)
            {
                try
                {
                    rsIsOwnBlog.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(rsOneCommentPerBlog!= null)
            {
                try
                {
                    rsOneCommentPerBlog.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psIsOverDailyLimit != null)
            {
                try
                {
                    psIsOverDailyLimit.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psIsOwnBlog != null)
            {
                try
                {
                    psIsOwnBlog.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psInsertComment != null)
            {
                try
                {
                    psInsertComment.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(connection != null)
            {
                try
                {
                    connection.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
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
            changeWindow(e,"SignInBackup.FXML","Log In!");
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }
}
    

