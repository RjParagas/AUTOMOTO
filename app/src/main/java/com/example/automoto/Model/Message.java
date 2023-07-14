package com.example.automoto.Model;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class Message {

    private String sender;
    private String receiver;
    private String message;
    private String date_created;
    private String date_modified;

    public Message(String sender, String receiver, String message, String date_created, String date_modified){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.date_created = date_created;
        this.date_modified = date_modified;
    }

    public Message(){

    }

    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getDate_modified() {
        return date_modified;
    }

}