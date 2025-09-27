package com.url.shortner.service;

import com.url.shortner.models.User;
import com.url.shortner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository UserRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = UserRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("user not found with this username "+ username));
        return UserDetailsImpl.build(user);
    }
}
