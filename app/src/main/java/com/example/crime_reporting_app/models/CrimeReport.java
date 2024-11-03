package com.example.crime_reporting_app.models;

public class CrimeReport {
    private String username;
    private String type;
    private String location;
    private String description;
    private String mediaUrl;


    public CrimeReport(String username, String type, String location, String description, String mediaUrl) {
        this.username = username;
        this.type = type;
        this.location = location;
        this.description = description;
        this.mediaUrl = mediaUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }
    public String getLocation() {
        return location;
    }
    public String getDescription() {
        return description;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }
}
