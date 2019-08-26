package org.tronder.words.model;

public class User {

    private String username;

    public User(String username) {
        setUsername(username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
