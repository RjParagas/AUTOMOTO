package com.example.automoto.Model;

public class Post {

    String subject, body, date;

    public Post(){}

    public Post(String subject, String body, String date) {
        this.subject = subject;
        this.body = body;
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
