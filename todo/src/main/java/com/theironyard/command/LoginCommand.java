package com.theironyard.command;

/**
 * Created by sparatan117 on 1/10/17.
 */
public class LoginCommand {
    String username;
    String password;

    public LoginCommand(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
