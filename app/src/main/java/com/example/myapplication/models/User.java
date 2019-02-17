package com.example.myapplication.models;

import java.util.HashMap;

public class User {
    public String displayName;
    public String email;
    public boolean onlineStatus;
    public HashMap<String, Object> location;
    public boolean availability;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String displayName, String email, boolean onlineStatus, HashMap<String, Object> location, boolean availability) {
        this.displayName = displayName;
        this.email = email;
        this.onlineStatus = onlineStatus;
        this.location = location;
        this.availability = availability;
    }

}
