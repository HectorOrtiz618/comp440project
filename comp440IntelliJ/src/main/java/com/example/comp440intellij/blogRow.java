package com.example.comp440intellij;

import javafx.beans.property.SimpleStringProperty;

public class blogRow
{
    private SimpleStringProperty date;
    private SimpleStringProperty subject;
    private SimpleStringProperty author;
    private SimpleStringProperty id;

    blogRow(String d, String s, String a, String i)
    {
        this.date = new SimpleStringProperty(d);
        this.subject = new SimpleStringProperty(s);
        this.author = new SimpleStringProperty(a);
        this.id = new SimpleStringProperty(i);
    }
    //getters
    public String getDate()
    {
        return this.date.get();
    }
    public String getSubject()
    {
        return this.subject.get();
    }
    public String getAuthor()
    {
        return this.author.get();
    }
    public String getId()
    {
        return this.id.get();
    }
}
