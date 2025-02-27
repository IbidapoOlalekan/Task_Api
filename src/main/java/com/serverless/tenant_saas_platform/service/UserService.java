package com.serverless.tenant_saas_platform.service;


import com.serverless.tenant_saas_platform.models.UserEntity;
import com.serverless.tenant_saas_platform.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(user.getEmail(),user.getPassword(),new ArrayList<>());
    }

    public UserEntity registerUser(String email, String password) {
        if(userRepository.existsByEmail(email)) {
            throw new RuntimeException("Username Already Exists!!!");
        }

        String hashedPassword = passwordEncoder.encode(password);
        UserEntity user = new UserEntity(email,hashedPassword);
        return userRepository.save(user);
    }

    public String authenticateUser(String email, String password) {
        UserEntity user = userRepository.findByEmail(email);
        if(user == null || !passwordEncoder.matches(password,user.getPassword())) {
            return null;
        }

        return "fake-jwt-token-" + email;
    }

}
