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

public class DBManager
{
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
                changeWindow(e, "blogList.fxml", "Welcome " + loggedInUser);

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
    public static List<blogRow> getBlogQuery(int option, String input)
    {
        List<blogRow> blogs = new ArrayList<blogRow>();
        Connection connection = null;
        PreparedStatement psGetBlogs = null;
        ResultSet rsGetBlogs = null;
        try
        {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp440", "root", "comp440");
            switch(option)
            {
                case 1: // Search blogs that only have positive comments
                    psGetBlogs = connection.prepareStatement("SELECT pdate, subject, created_by,blogid FROM blogs WHERE (SELECT count(commentid) FROM comments where blogid = blogs.blogid) > 0 AND (SELECT count(blogid) FROM comments WHERE sentiment = 'negative' AND blogid = blogs.blogid)  = 0 AND created_by = ?");
                    psGetBlogs.setString(1,input);
                    break;
                default:
                    psGetBlogs = connection.prepareStatement("SELECT pdate, subject, created_by, blogid FROM Blogs");//give all blogs if nothing else applies
                    break;
            }
            rsGetBlogs = psGetBlogs.executeQuery();
            while(rsGetBlogs.next())
            {
                //add blogs to row
                blogs.add(new blogRow(rsGetBlogs.getString("pdate"),rsGetBlogs.getString("subject"),rsGetBlogs.getString("created_by"),rsGetBlogs.getString("blogid")));
            }
            if (blogs.isEmpty())//if no results are found, return a "blog" that says no results found
            {
                blogs.add(new blogRow("No Results Found","","",""));
            }

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(rsGetBlogs != null)
            {
                try
                {
                    rsGetBlogs.close();
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
    public static List<userRow> getUserQuery(int option,String follower1, String follower2)
    {
        List<userRow> users = new ArrayList<userRow>();
        Connection connection = null;
        PreparedStatement psGetUsers = null;
        ResultSet rsGetUsers = null;
        try
        {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp440", "root", "comp440");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp440", "root", "comp440");
            switch(option)
            {
                case 1:// List who posted the most blogs today
                    psGetUsers = connection.prepareStatement("SELECT created_by, COUNT(*) AS numOfBlogs FROM blogs WHERE pdate= DATE(NOW()) GROUP BY created_by ORDER BY numOfBlogs DESC");
                    break;
                case 2:// List those followed by x and y
                    psGetUsers = connection.prepareStatement("SELECT Leader, followerOne, followerTwo FROM (  SELECT A.leadername as Leader, A.followername as followerOne, B.followername as followerTwo  FROM follows A, follows B WHERE A.leadername = B.leadername AND A.followername <> B.followername) AS main  WHERE followerOne =? AND followerTwo = ?");
                    psGetUsers.setString(1,follower1);
                    psGetUsers.setString(2,follower2);
                    break;
                case 3://List users who never posted a blog
                    psGetUsers = connection.prepareStatement("SELECT username FROM users WHERE(SELECT count(created_by) FROM blogs WHERE created_by = username ) = 0");
                    break;
                case 4://List users who only posted negative comments
                    psGetUsers = connection.prepareStatement("SELECT username FROM users WHERE (SELECT count(posted_by) FROM comments WHERE sentiment = 'positive' AND posted_by = username) = 0 AND (SELECT count(posted_by) FROM comments WHERE posted_by = username) > 0");
                    break;
                case 5://List users who never got a negative comment
                    psGetUsers = connection.prepareStatement("SELECT username from users WHERE (SELECT COUNT(created_by) from blogs,comments WHERE blogs.blogid = comments.blogid AND comments.sentiment = \"negative\" and created_by = username) = 0 AND (SELECT COUNT(*) FROM blogs,comments WHERE blogs.blogid = comments.blogid AND blogs.created_by = username) > 0");
                    break;
                default:
                    psGetUsers = connection.prepareStatement("SELECT username FROM users");// return all users if nothing else applies
                    break;
            }
            rsGetUsers = psGetUsers.executeQuery();
            while(rsGetUsers.next())// add results to user list
            {
                users.add(new userRow(rsGetUsers.getString(1)));
            }
            if (users.isEmpty())//if no results are found, return a "user" that says no results found
            {
                users.add(new userRow("No Results Found"));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(rsGetUsers != null)
            {
                try
                {
                    rsGetUsers.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(psGetUsers != null)
            {
                try
                {
                    psGetUsers.close();
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
        return users;
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
        PreparedStatement psInsertTable = null;
        try
        {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp440", "root", "comp440");

            psInsertTable = connection.prepareStatement("DROP TABLE IF EXISTS `hobbies`;");
            psInsertTable.executeUpdate();
            psInsertTable = connection.prepareStatement("DROP TABLE IF EXISTS `follows`;");
            psInsertTable.executeUpdate();
            psInsertTable = connection.prepareStatement("DROP TABLE IF EXISTS `comments`;");
            psInsertTable.executeUpdate();
            psInsertTable = connection.prepareStatement("DROP TABLE IF EXISTS `blogstags`;");
            psInsertTable.executeUpdate();
            psInsertTable = connection.prepareStatement("DROP TABLE IF EXISTS `blogs`;");
            psInsertTable.executeUpdate();
            psInsertTable = connection.prepareStatement("DROP TABLE IF EXISTS users");
            psInsertTable.executeUpdate();

            psInsertTable = connection.prepareStatement("CREATE TABLE `users` (`username` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,`password` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,`firstName` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL,`lastName` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL,`email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,PRIMARY KEY (`username`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;");
            psInsertTable.executeUpdate();
            psInsertTable = connection.prepareStatement("INSERT INTO `users` VALUES ('batman','1234','bat','bat','nananana@batman.com'),('bob','12345','bob','bob','bobthatsme@yahoo.com'),('catlover','abcd','cat','cat','catlover@whiskers.com'),('doglover','efds','dog','dog','doglover@bark.net'),('jdoe','25478','joe','jod','jane@doe.com'),('jsmith','1111','john','smith','jsmith@gmail.com'),('matty','2222','mat','mat','matty@csun.edu'),('notbob','5555','not','bob','stopcallingmebob@yahoo.com'),('pacman','9999','pacman','pacman','pacman@gmail.com'),('scooby','8888','scoby','scoby','scooby@doo.net');");
            psInsertTable.executeUpdate();

            psInsertTable = connection.prepareStatement("CREATE TABLE `blogs` (`blogid` int(10) unsigned NOT NULL AUTO_INCREMENT,`subject` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,`description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,`pdate` date DEFAULT NULL,`created_by` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL,PRIMARY KEY (`blogid`),KEY `FK1_idx` (`description`),KEY `FK1` (`created_by`),CONSTRAINT `FK1` FOREIGN KEY (`created_by`) REFERENCES `users` (`username`)) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;");
            psInsertTable.executeUpdate();
            psInsertTable = connection.prepareStatement("INSERT INTO `blogs` VALUES (1,'Hello World','Hey everyone, this is my first blog. Hello world and all who inhabit it!','2020-03-15','jsmith'),(2,'I love cats!','Cats are amazing. They\\'re awesome, and fuzzy, and cute. Who DOESN\\'T love cats?','2020-03-17','catlover'),(3,'Dogs are the best.','So I saw a post the other day talking about cats. Now, I love cats. They\\'re great. But here\\'s the thing: dogs are just the best, okay? There\\'s no question about it. That is all.','2020-03-19','doglover'),(4,'I am the night.','To all you lowly criminals out there, this is a warning to know I am watching. I am justice. I am righteousness. I am the NIGHT.','2020-03-24','batman'),(5,'Waka waka','waka waka waka waka waka waka waka waka waka waka waka waka waka waka waka waka','2020-03-31','pacman'),(6,'Who is this Bob guy?','Decided to start tracking down this mysterious human known as \\'Bob.\\' Who is Bob? What does he do? WHY does he do it? There is a lot of mystery surrounding this person, and I will be going into detail in future posts. Stay tuned!','2020-04-02','notbob'),(7,'Re: I love cats.','A reader recently reached out to me about my last post. To be clear, I\\'m not dissing our canine companions! But we\\'ve got to be honest here, why are cats better? They\\'re smart, affectionate, and great cuddle partners. Cats are better. It\\'s just fact.','2020-04-04','catlover'),(8,'Scooby Dooby Doo!','The search for scooby snacks: Where did they go? I know this whole quarantine thing is affecting businesses, but aren\\'t scooby snacks counted as an essential service? Please post below if you find anything! I\\'m going crazy here!','2020-04-05','scooby'),(9,'Bob Update','Dear readers, I know you have been waiting anxiously for an update on Bob, but there is not much to share so far. He appears to have little to no online presence. Just a clarification: I am decidedly NOT Bob. Thanks all. Stay tuned for more!','2020-04-06','notbob'),(10,'Lizard People.','What are your guys\\' thoughts on them? I, for one, welcome out reptitlian overlords.','2020-04-12','jdoe');");
            psInsertTable.executeUpdate();

            psInsertTable = connection.prepareStatement("CREATE TABLE `blogstags` (`blogid` int(10) unsigned NOT NULL,`tag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,PRIMARY KEY (`blogid`,`tag`),CONSTRAINT `blogstags_ibfk_1` FOREIGN KEY (`blogid`) REFERENCES `blogs` (`blogid`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;");
            psInsertTable.executeUpdate();
            psInsertTable = connection.prepareStatement("INSERT INTO `blogstags` VALUES (1,'hello world'),(2,'animals'),(2,'cats'),(3,'animals'),(3,'dogs'),(4,'crime'),(4,'justice'),(5,'cartoon'),(5,'waka'),(6,'bob'),(6,'update'),(7,'cats'),(7,'dogs'),(8,'dogs'),(8,'quarantine'),(8,'scooby snacks'),(9,'bob'),(9,'update'),(10,'lizards');");
            psInsertTable.executeUpdate();

            psInsertTable = connection.prepareStatement("CREATE TABLE `comments` (`commentid` int(10) NOT NULL AUTO_INCREMENT,`sentiment` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,`description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,`cdate` date DEFAULT NULL,`blogid` int(10) unsigned DEFAULT NULL,`posted_by` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL,PRIMARY KEY (`commentid`),KEY `comments_ibfk_1` (`blogid`),KEY `comments_ibfk_2` (`posted_by`),CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`blogid`) REFERENCES `blogs` (`blogid`),CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`posted_by`) REFERENCES `users` (`username`),CONSTRAINT `sentiment_types` CHECK ((`sentiment` in (_utf8mb4'negative',_utf8mb4'positive')))) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;");
            psInsertTable.executeUpdate();
            psInsertTable = connection.prepareStatement("INSERT INTO `comments` VALUES (1,'negative','Cats are cool and all, but dogs are where it\\'s at.','2020-03-17',2,'doglover'),(2,'negative','What\\'s all the hype about? Cats are clearly superior.','2020-03-20',3,'catlover'),(3,'positive','Nice.','2020-03-20',4,'scooby'),(4,'positive','Who IS Bob? I can\\'t wait to find out.','2020-04-02',6,'jsmith'),(5,'negative','I guess cat people just don\\'t know what they\\'re missing.','2020-04-05',7,'doglover'),(6,'positive','This is totally unrelated, but I just wanted to say I am a HUGE fan of yours. I love your work!','2020-04-05',8,'doglover'),(7,'positive','Have you checked out Dog-Mart? They\\'ve got everything.','2020-04-06',8,'matty'),(8,'negative','I was hoping there\\'d be more of an update. Sorry, Bob.','2020-04-07',9,'jsmith'),(9,'positive','I think they\\'re all secretly cats. Open your eyes, sheeple!','2020-04-13',10,'doglover'),(10,'negative','Who? Me? Multimillionaire philanthropist of Arkham? A lizard person? Nope. Nothing to see here!','2020-04-15',10,'batman');");
            psInsertTable.executeUpdate();

            psInsertTable = connection.prepareStatement("CREATE TABLE `follows` (`leadername` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,`followername` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,PRIMARY KEY (`leadername`,`followername`),KEY `follows_ibfk_2` (`followername`),CONSTRAINT `follows_ibfk_1` FOREIGN KEY (`leadername`) REFERENCES `users` (`username`),CONSTRAINT `follows_ibfk_2` FOREIGN KEY (`followername`) REFERENCES `users` (`username`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;");
            psInsertTable.executeUpdate();
            psInsertTable = connection.prepareStatement("INSERT INTO `follows` VALUES ('jsmith','bob'),('batman','catlover'),('doglover','catlover'),('pacman','catlover'),('catlover','doglover'),('jsmith','jdoe'),('bob','notbob'),('jdoe','notbob'),('batman','pacman'),('scooby','pacman'),('doglover','scooby'),('pacman','scooby');");
            psInsertTable.executeUpdate();

            psInsertTable = connection.prepareStatement("CREATE TABLE `hobbies` (`username` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,`hobby` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,PRIMARY KEY (`hobby`,`username`),KEY `hobbies_ibfk_1` (`username`),CONSTRAINT `hobbies_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`),CONSTRAINT `hobby_types` CHECK ((`hobby` in (_utf8mb4'hiking',_utf8mb4'swimming',_utf8mb4'calligraphy',_utf8mb4'bowling',_utf8mb4'movie',_utf8mb4'cooking',_utf8mb4'dancing')))) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;");
            psInsertTable.executeUpdate();
            psInsertTable = connection.prepareStatement("INSERT INTO `hobbies` VALUES ('batman','movie'),('bob','movie'),('catlover','movie'),('doglover','hiking'),('jdoe','dancing'),('jdoe','movie'),('jsmith','hiking'),('matty','bowling'),('notbob','calligraphy'),('pacman','dancing'),('pacman','movie'),('scooby','cooking');");
            psInsertTable.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Database has been reset! Returning to Login Screen!");
            alert.show();

            if(psInsertTable != null)
            {
                psInsertTable.close();
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
    

