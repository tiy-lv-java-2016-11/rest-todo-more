package com.theironyard.CommandObjects;

/**
 * Created by darionmoore on 1/10/17.
 */
public class ValidateLogin {
    String userName;
    String password;

    public ValidateLogin() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
