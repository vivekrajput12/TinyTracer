package com.url.shortner.service;
import com.url.shortner.dtos.LoginRequest;
import com.url.shortner.dtos.RegisterRequest;
import com.url.shortner.dtos.UserDto;
import com.url.shortner.models.Role;
import com.url.shortner.models.User;
import com.url.shortner.repository.RoleRepository;
import com.url.shortner.repository.UserRepository;
import com.url.shortner.security.JwtAuthResponse;
import com.url.shortner.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    public UserDto registerUser(RegisterRequest request){
        Role role = roleRepository.findById(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        User user = new User();
        user.setUsername(request.getUsername());
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(request.getPassword()) );
        User savedUser = userRepository.save(user);
        return UserDto.from(user);
    }

    public JwtAuthResponse authenticateUser(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        System.out.println("authenticationn " + authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);
        return new JwtAuthResponse(jwt);
    }

    public User findUserByUserName(String name) {
        return userRepository.findByUsername(name).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
    public User findUserByUid(long uid) {
        return userRepository.findByUid(uid).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}