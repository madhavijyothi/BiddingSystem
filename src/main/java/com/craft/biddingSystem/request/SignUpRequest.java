package com.craft.biddingSystem.request;


import com.craft.biddingSystem.annotations.PasswordMatches;
import com.craft.biddingSystem.annotations.ValidEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

// Class for storing the data about new user entered by the user
@PasswordMatches
public class SignUpRequest {

    @NotBlank(message = "Please enter your username")
    private String username;
    @Email(message = "It should have email format")
    @NotBlank(message = "Please enter your email")
    @ValidEmail
    private String email;
    @NotBlank(message = "Please enter your password")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    private String confirmPassword;

    public SignUpRequest() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
