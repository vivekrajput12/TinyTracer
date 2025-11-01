package com.url.shortner.dtos.responseDto;

import com.url.shortner.models.User;
import lombok.Data;

@Data
public class UserDto {
    private String userName;
    private Integer role;
    public UserDto(String userName, Integer role) {
        this.userName = userName;
        this.role = role;
    }
    public static UserDto from(User user) {
        return new UserDto(user.getUsername(), user.getRole().getId());
    }
}
