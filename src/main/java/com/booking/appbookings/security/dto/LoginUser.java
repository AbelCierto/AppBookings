package com.booking.appbookings.security.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginUser {
    @NotBlank
    private String userName;
    @NotBlank
    private String password;

}
