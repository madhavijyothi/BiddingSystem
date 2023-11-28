package com.craft.biddingSystem.request;

import javax.validation.constraints.NotBlank;

// Class for storing the data entered by the user
public class LoginRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public LoginRequest() {
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
