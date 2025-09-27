package com.url.shortner.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private Integer role;
    private String password;
}