package com.java.backend.controllers;

import com.java.backend.domain.WorldService;
import com.java.backend.model.AppUser;
import com.java.backend.model.World;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/world")
public class WorldController {

    private final WorldService worldService;

    @Autowired
    public WorldController(WorldService worldService) {
        this.worldService = worldService;
    }

    @GetMapping
    public ResponseEntity<Iterable<World>> getAllUsers() {
        try {
            Iterable<World> worlds = worldService.findAll();
            return ResponseEntity.ok(worlds);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
