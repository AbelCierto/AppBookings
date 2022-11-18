package com.booking.appbookings.security.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class NewUser {
    @NotNull
    private String name;
    @NotNull
    private String userName;
    @Email
    private String email;
    @NotNull
    private String password;
    private Set<String> roles = new HashSet<>();
}
