package com.example.chat;

import java.util.Date;

/**
 * Created by User on 25.11.2014.
 */
public class Message {
    private String from;
    private String to;
    private String message;
    private boolean read;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;

    public Message(String from, String to, String message, boolean read, Date date) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.read = read;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
