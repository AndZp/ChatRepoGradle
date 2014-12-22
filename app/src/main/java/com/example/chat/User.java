package com.example.chat;

import android.location.Location;

/**
 * Created by User on 25.11.2014.
 */
public class User {
    private String login;
    private String password;
    private String email;
    private Location locationUser;
    private int colorText;
    private int avatarka;
    private boolean gender;
    private boolean online = false;

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Location getLocationUser() {
        return locationUser;
    }

    public void setLocationUser(Location locationUser) {
        this.locationUser = locationUser;
    }

    public int getColorText() {
        return colorText;
    }

    public void setColorText(int colorText) {
        this.colorText = colorText;
    }

    public int getAvatarka() {
        return avatarka;
    }

    public void setAvatarka(int avatarka) {
        this.avatarka = avatarka;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }





    public User(String login) {
        this.login = login;

    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
