package com.example.chat;

public class User {
    private String username;
    private String email ;
    private String profilePictures;

    public User(String username, String email, String profilePictures) {
        this.username = username;
        this.email = email;
        this.profilePictures = profilePictures;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePictures() {
        return profilePictures;
    }

    public void setProfilePictures(String profilePictures) {
        this.profilePictures = profilePictures;
    }
}



