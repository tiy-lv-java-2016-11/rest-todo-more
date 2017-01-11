package com.theironyard.commands;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginCommand {
    @NotNull
    @Size(min = 5, max = 50)
    private String username;
    @NotNull
    @Size(min = 8, max = 50)
    private String password;

    public LoginCommand() {
    }

    public LoginCommand(String username, String password) {
        this.username = username;
        this.password = password;
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
