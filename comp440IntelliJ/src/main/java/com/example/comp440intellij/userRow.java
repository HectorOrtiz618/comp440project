package com.example.comp440intellij;

import javafx.beans.property.SimpleStringProperty;

public class userRow
{
    private SimpleStringProperty user;
    //Constructors
    userRow(String a)
    {
        this.user = new SimpleStringProperty(a);
    }
    //Getters
    public String getUser()
    {
        return user.get();
    }
    //Setters
    public void setUser(String a)
    {
        this.user.set(a);
    }
}
