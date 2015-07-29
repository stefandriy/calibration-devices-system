package com.softserve.edu.controller.provider.util;

/**
 * Created by Володя on 28.07.2015.
 */
public class usernameDTO {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "usernameDTO{" +
                "username='" + username + '\'' +
                '}';
    }
}
