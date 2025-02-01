package com.java.backend.controllers;

import com.java.backend.model.AppUser;
import com.java.backend.security.*;
import com.java.backend.domain.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AppUserController {

    private final AppUserService userService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final JwtConverter converter;
    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService userService, AuthenticationManager authenticationManager, JwtConverter converter, AppUserService appUserService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.converter = converter;
        this.appUserService = appUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AppUser user) {
        try {
            appUserService.registerUser(user);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Registration failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}