package com.java.backend.controllers;

import com.java.backend.domain.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
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
}