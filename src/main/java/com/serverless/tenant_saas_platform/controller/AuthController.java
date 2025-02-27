package com.serverless.tenant_saas_platform.controller;

import com.serverless.tenant_saas_platform.auth.JwtTokenProvider;
import com.serverless.tenant_saas_platform.dto.LoginDTO;
import com.serverless.tenant_saas_platform.dto.UserDTO;
import com.serverless.tenant_saas_platform.models.UserEntity;
import com.serverless.tenant_saas_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    //Endpoint to register a new user
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        try {
            if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()
                    || userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body("All fields are required");
            }
            userService.registerUser(userDTO.getEmail(), userDTO.getPassword());
            return ResponseEntity.ok("User registered successfully!!");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong. Please try again later");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        String token = userService.authenticateUser(loginDTO.getEmail(), loginDTO.getPassword());
        if (token != null) {
            return ResponseEntity.ok().body("Login successful! Token: " + token);
        } else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username or Password");
        }
    }

//    @Autowired
//    private JwtTokenProvider tokenProvider;
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody UserEntity user){
//        //Hash password before saving
//        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
//        UserEntity savedUser = userService.registerUser(user.getEmail(), user.getPassword());
//        return ResponseEntity.ok(savedUser);
//    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody UserEntity user){
//        UserEntity existingUser = userService.getUserByEmail(user.getEmail());
//
//        if(existingUser == null || !new BCryptPasswordEncoder().matches(user.getPassword(), existingUser.getPassword())){
//            return ResponseEntity.status(401).body("Invalid username or password");
//        }
//
//        String token = tokenProvider.createToken(existingUser.getEmail());
//        return ResponseEntity.ok("Bearer " + token);
//    }
}
