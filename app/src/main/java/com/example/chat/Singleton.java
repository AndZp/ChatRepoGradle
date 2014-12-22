package com.example.chat;

import com.parse.ParseUser;

import java.util.Vector;

/**
 * Created by User on 26.11.2014.
 */
public class Singleton {


    private Vector<ParseUser> usersVector;
    private Vector<Message> messageVector;
    private static Singleton instance = null;
    private ParseUser user;

    private Singleton() {
        usersVector = new Vector<ParseUser>();
        messageVector = new Vector<Message>();
        user = new ParseUser();
    }

    public Vector<Message> getMessageVector() {
        return messageVector;
    }

    public ParseUser getUser() {
        return user;
    }

    public void setUser(ParseUser user) {
        this.user = user;
    }


    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;

    }

    public Vector<ParseUser> getUsersVector() {
        return usersVector;
    }

    public int getUserIndex(String name) {
        for (int i = 0; i < usersVector.size(); i++) {
            if (usersVector.get(i).getUsername().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public void logOut() {
        user = null;
        usersVector.clear();
        messageVector.clear();
    }

}
