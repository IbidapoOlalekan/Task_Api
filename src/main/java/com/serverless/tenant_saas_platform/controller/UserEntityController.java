package com.serverless.tenant_saas_platform.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserEntityController {

    @GetMapping("/profile")
    public String getUserProfile(Authentication authentication) {
        return "Hello " + authentication.getName() + "! You are authenticated." ;
    }
}
