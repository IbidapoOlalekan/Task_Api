package com.serverless.tenant_saas_platform.service;


import com.serverless.tenant_saas_platform.auth.JwtTokenProvider;
import com.serverless.tenant_saas_platform.models.*;
import com.serverless.tenant_saas_platform.repo.LandlordRepository;
import com.serverless.tenant_saas_platform.repo.RoleRepository;
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
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final LandlordRepository landlordRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository, LandlordRepository landlordRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.landlordRepository = landlordRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = tokenProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(user.getEmail(),user.getPassword(),new ArrayList<>());
    }

    public UserEntity registerUser(String email, String username, String password, RoleType roleType) {
        if(userRepository.existsByEmail(email)) {
            throw new RuntimeException("Username Already Exists!!!");
        }

        Role role = roleRepository.findByName(roleType).orElseThrow(() -> new RuntimeException("Role not found!"));

        String hashedPassword = passwordEncoder.encode(password);
        UserEntity user;
        if(roleType == RoleType.LANDLORD) {
            user = new Landlord();
            user.setEmail(email);
            user.setPassword(hashedPassword);
            user.setRole(Set.of(roleType));
            landlordRepository.save((Landlord) user);

        } else if(roleType == RoleType.TENANT) {
            user = new Tenant();
        } else{
            user = new UserEntity();
        }
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setRole(Set.of(roleType));
        return userRepository.save(user);
    }

    public String authenticateUser(String email, String password) {
        UserEntity user = userRepository.findByEmail(email);
        if(user == null || !passwordEncoder.matches(password,user.getPassword())) {
            return null;
        }

        return jwtTokenProvider.createToken(email);
    }

}
