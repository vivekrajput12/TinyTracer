package com.url.shortner.dtos.requestDto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private Integer role;
    private String password;
}