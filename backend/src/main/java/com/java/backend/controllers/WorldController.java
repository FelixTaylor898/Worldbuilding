package com.java.backend.controllers;

import com.java.backend.domain.AppUserService;
import com.java.backend.domain.WorldService;
import com.java.backend.model.AppUser;
import com.java.backend.model.World;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/world")
public class WorldController {

    private final WorldService worldService;
    private final AppUserService userService;

    @Autowired
    public WorldController(WorldService worldService, AppUserService userService) {
        this.worldService = worldService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Iterable<World>> getAllWorlds(@RequestHeader("Authorization") String authHeader) {
        AppUser user = userService.findUserByHeader(authHeader);
        try {
            Iterable<World> worlds = worldService.findByUser(user);
            return ResponseEntity.ok(worlds);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{worldId}")
    public ResponseEntity<World> getWorldById(@RequestHeader("Authorization") String authHeader,
                                              @PathVariable Long worldId) {
        Iterable<World> worlds = getAllWorlds(authHeader).getBody();

        // Find the world that matches the given worldId
        Optional<World> world = StreamSupport.stream(worlds.spliterator(), false)
                .filter(w -> w.getWorldId().equals(worldId))
                .findFirst();

        return world.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<World> addWorld(@RequestBody World world, @RequestHeader("Authorization") String authHeader) {
        try {
            AppUser user = userService.findUserByHeader(authHeader);
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