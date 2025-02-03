package com.java.backend.controllers;

import com.java.backend.domain.AppUserService;
import com.java.backend.domain.WorldService;
import com.java.backend.model.AppUser;
import com.java.backend.model.World;
import com.java.backend.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/world")
public class WorldController {

    private final JwtTokenProvider jwtTokenProvider;
    private final WorldService worldService;
    private final AppUserService userService;

    @Autowired
    public WorldController(JwtTokenProvider jwtTokenProvider, WorldService worldService, AppUserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.worldService = worldService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Iterable<World>> getAllWorlds() {
        try {
            Iterable<World> worlds = worldService.findAll();
            return ResponseEntity.ok(worlds);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping
    public ResponseEntity<World> addWorld(@RequestBody World world, @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from Authorization header
            String token = authHeader.replace("Bearer ", "");

            // Retrieve username from token
            String username = jwtTokenProvider.getUsernameFromToken(token);

            // Fetch user from the database
            AppUser user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Set the user in the world object
            world.setUser(user);

            // Save the world
            World savedWorld = worldService.addWorld(world);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedWorld);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}