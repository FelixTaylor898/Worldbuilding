package com.java.backend.controllers;

import com.java.backend.model.AppUser;
import com.java.backend.model.enums.Role;
import com.java.backend.security.JwtConverter;
import com.java.backend.domain.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class AppUserController {
    private final PasswordEncoder passwordEncoder;

    private final AppUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtConverter converter;

    @Autowired
    public AppUserController(PasswordEncoder passwordEncoder, AppUserService userService, AuthenticationManager authenticationManager, JwtConverter converter) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.converter = converter;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AppUser user) {
        if (userService.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Email already in use", HttpStatus.BAD_REQUEST);
        }
        if (userService.existsByUsername(user.getUsername())) {
            return new ResponseEntity<>("Username already in use", HttpStatus.BAD_REQUEST);
        }
        try {
            // Register the user
            userService.registerUser(user);

            // Authenticate the user manually with the password encoder
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()  // Make sure this is the plain password, not the hashed one
                    )
            );

            // Set the authentication context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return new ResponseEntity<>("User registered and logged in", HttpStatus.CREATED);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Bad credentials: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("Registration failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody AppUser user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), passwordEncoder.encode(user.getPassword()))
            );
            String token = converter.getTokenFromUser(new AppUser(authentication.getPrincipal()));
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return new ResponseEntity<>("Login failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<AppUser> getCurrentUser(Principal principal) {
        try {
            AppUser user = userService.findByUsername(principal.getName());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody AppUser updatedUser, Principal principal) {
        try {
            if (!principal.getName().equals(updatedUser.getUsername())) {
                return new ResponseEntity<>("You can only update your own profile", HttpStatus.FORBIDDEN);
            }
            userService.updateUser(principal.getName(), updatedUser);
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return new ResponseEntity<>("Update failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(Principal principal) {
        try {
            userService.deleteUser(principal.getName());
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return new ResponseEntity<>("Deletion failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/upgrade/{username}")
    public ResponseEntity<String> upgradeToAdmin(@PathVariable String username, Principal principal) {
        try {
            AppUser requestingUser = userService.findByUsername(principal.getName());
            if (requestingUser == null || !requestingUser.getRole().equals(Role.ROLE_ADMIN)) {
                return new ResponseEntity<>("You must be an admin to perform this action", HttpStatus.FORBIDDEN);
            }
            userService.upgradeToAdmin(username);
            return ResponseEntity.ok("User upgraded to admin successfully");
        } catch (Exception e) {
            return new ResponseEntity<>("Upgrade failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<Iterable<AppUser>> getAllUsers() {
        try {
            Iterable<AppUser> users = userService.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AppUser> getUserById(@PathVariable Long userId) {
        try {
            AppUser user = userService.findById(userId);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}