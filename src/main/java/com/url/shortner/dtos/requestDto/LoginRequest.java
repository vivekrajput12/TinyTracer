package com.url.shortner.dtos.requestDto;

import lombok.AllArgsConstructor;
//import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
}
