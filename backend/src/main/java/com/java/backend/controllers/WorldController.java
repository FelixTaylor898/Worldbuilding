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

    @DeleteMapping("/{worldId}")
    public ResponseEntity<String> deleteWorld(@RequestHeader("Authorization") String authHeader, @PathVariable Long worldId) {
        try {
            World world = worldService.findById(worldId);
            AppUser authenticatedUser = userService.findUserByHeader(authHeader);
            if (authenticatedUser.getUsername().equals(world.getUser().getUsername())) {
                worldService.deleteWorld(worldId);
                return ResponseEntity.ok("World deleted successfully");
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Deletion failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<World>> getAllWorlds(@RequestHeader("Authorization") String authHeader) {
        AppUser user = userService.findUserByHeader(authHeader);
        try {
            Iterable<World> worlds = worldService.findByUser(user);
            return ResponseEntity.ok(worlds);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{worldId}")
    public ResponseEntity<World> getWorldById(@RequestHeader("Authorization") String authHeader,
                                              @PathVariable Long worldId) {
        Iterable<World> worlds = getAllWorlds(authHeader).getBody();
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
            world.setUser(user);
            World savedWorld = worldService.addWorld(world);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedWorld);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateWorld(@RequestBody World world,
                                             @RequestHeader("Authorization") String authHeader,
                                             @PathVariable Long id) {
        try {
            World oldWorld = worldService.findById(id);
            AppUser authenticatedUser = userService.findUserByHeader(authHeader);
            if (!authenticatedUser.getUsername().equals(oldWorld.getUser().getUsername())) {
                return new ResponseEntity<>("You can only update your own world", HttpStatus.FORBIDDEN);
            }
            worldService.updateWorld(id, world);
            return ResponseEntity.ok("World updated successfully");
        } catch (Exception e) {
            return new ResponseEntity<>("Update failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}