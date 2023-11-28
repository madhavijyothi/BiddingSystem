package com.craft.biddingSystem.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserRequest {
    @NotNull
    private String username;
    @NotNull
    private String emailId;
    @NotNull
    private String phoneNo;
    @NotNull
    private String password;
}
