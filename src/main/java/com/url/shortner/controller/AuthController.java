package com.url.shortner.controller;
import com.url.shortner.dtos.ApiResponse;
import com.url.shortner.dtos.LoginRequest;
import com.url.shortner.dtos.RegisterRequest;
import com.url.shortner.dtos.UserDto;
import com.url.shortner.security.JwtAuthResponse;
import com.url.shortner.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private UserService userService;
    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody LoginRequest loginRequest){
        try{
            JwtAuthResponse data = userService.authenticateUser(loginRequest);
            ApiResponse<JwtAuthResponse> response = new ApiResponse<>(0, "Logged In Successfully", data);
            return ResponseEntity.ok(response);
        } catch(BadCredentialsException e){
            ApiResponse<String> response = new ApiResponse<>(-1, "Invalid Credentials");
            return ResponseEntity.ok(response);
        } catch (Exception e){
            ApiResponse<String> response = new ApiResponse<>(-1, "Something went wrong");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/public/register")
    public ResponseEntity<?> RegisterUser(@RequestBody RegisterRequest registerRequest){
        try {
            UserDto data = userService.registerUser(registerRequest);
            ApiResponse<UserDto> response = new ApiResponse<>(0, "User Registered Successfully", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("checking "+ e);
            ApiResponse<String> response = new ApiResponse<>(-1, "Registration Failed");
            return ResponseEntity.ok(response);
        }
    }
}
