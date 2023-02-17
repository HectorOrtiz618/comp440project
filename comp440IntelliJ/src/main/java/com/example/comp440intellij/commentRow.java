package com.example.comp440intellij;

import javafx.beans.property.SimpleStringProperty;

public class commentRow
{
    private SimpleStringProperty date;
    private SimpleStringProperty author;
    private SimpleStringProperty sentiment;
    private SimpleStringProperty comment;
    //Constructors
    commentRow(String d,String a,String s,String c)
    {
        this.date = new SimpleStringProperty(d);
        this.author = new SimpleStringProperty(a);
        this.sentiment = new SimpleStringProperty(s);
        this.comment = new SimpleStringProperty(c);
    }
    //Getters
    public String getDate()
    {
        return date.get();
    }
    public String getAuthor()
    {
        return author.get();
    }
    public String getSentiment()
    {
        return sentiment.get();
    }
    public String getComment()
    {
        return comment.get();
    }
    //Setters
    public void setDate(String d)
    {
        this.date.set(d);
    }
    public void setAuthor(String a)
    {
        this.date.set(a);
    }
    public void setSentiment(String s)
    {
        this.date.set(s);
    }
    public void setComment(String c)
    {
        this.date.set(c);
    }
}
