package com.belvinard.libraryManagementSystem.activity;

import java.util.Date;

public class Activity {
    private String username;
    private String action; // "Add User", "Search Book", "Update User", "Borrowed", "Returned", etc.
    private String details; // Description de l'action, comme "book: The Great Gatsby"
    private Date timestamp;

    public Activity(String username, String action, String details) {
        this.username = username;
        this.action = action;
        this.details = details;
        this.timestamp = new Date(); // Date et heure actuelles
    }

    @Override
    public String toString() {
        return String.format("Activity {user: '%s', action: '%s', details: '%s', time: '%s'}",
                username, action, details, timestamp.toString());
    }
}