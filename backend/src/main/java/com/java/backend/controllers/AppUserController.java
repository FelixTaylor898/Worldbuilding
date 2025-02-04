package com.java.backend.controllers;

import com.java.backend.model.AppUser;
import com.java.backend.model.enums.Role;
import com.java.backend.security.JwtConverter;
import com.java.backend.domain.AppUserService;
import com.java.backend.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/user")
public class AppUserController {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AppUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtConverter converter;

    @Autowired
    public AppUserController(PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AppUserService userService, AuthenticationManager authenticationManager, JwtConverter converter) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.converter = converter;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AppUser user) {
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username already in use");
        }

        try {
            // Encode password before saving
            String pass = user.getPassword();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreatedAt(LocalDateTime.now());
            userService.registerUser(user);

            // Manually authenticate since we saved an encoded password
            UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, pass, userDetails.getAuthorities());

            authenticationManager.authenticate(authToken); // Authenticate
            SecurityContextHolder.getContext().setAuthentication(authToken);


            return ResponseEntity.status(HttpStatus.CREATED).body("User registered");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad credentials: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody AppUser user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            String token = converter.getTokenFromUser(new AppUser(authentication.getPrincipal()));
            String token2 = jwtTokenProvider.generateToken(user.getUsername());
            return ResponseEntity.ok(token2);
        } catch (Exception e) {
            return new ResponseEntity<>("Login failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@RequestBody AppUser updatedUser,
                                             @RequestHeader("Authorization") String authHeader,
                                             @PathVariable Long id) {
        try {
            AppUser authenticatedUser = userService.findUserByHeader(authHeader);
            boolean isAdmin = authenticatedUser.getRole().equals(Role.ROLE_ADMIN);
            boolean isSelfUpdate = authenticatedUser.getUsername().equals(updatedUser.getUsername());
            if (!isAdmin && !isSelfUpdate) {
                return new ResponseEntity<>("You can only update your own profile", HttpStatus.FORBIDDEN);
            }
            updatedUser.setRole(authenticatedUser.getRole());
            userService.updateUser(id, updatedUser);
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return new ResponseEntity<>("Update failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String authHeader, @PathVariable String username) {
        try {
            AppUser authenticatedUser = userService.findUserByHeader(authHeader);
            if (authenticatedUser.getRole() == Role.ROLE_ADMIN || authenticatedUser.getUsername().equals(username)) {
                userService.deleteUser(username);
                return ResponseEntity.ok("User deleted successfully");
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Deletion failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/upgrade/{username}")
    public ResponseEntity<String> upgradeToAdmin(@PathVariable String username, @RequestHeader("Authorization") String authHeader) {
        try {
            AppUser requestingUser = userService.findUserByHeader(authHeader);
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
    public ResponseEntity<Iterable<AppUser>> getAllUsers(@RequestHeader("Authorization") String authHeader) {
        try {
            AppUser user = userService.findUserByHeader(authHeader);
            if (user.getRole() != Role.ROLE_ADMIN) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            Iterable<AppUser> users = userService.findAll();


            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<AppUser> getByUsername(@RequestHeader("Authorization") String authHeader, @PathVariable String username) {
        try {
            AppUser user = userService.findUserByHeader(authHeader);
            if (user.getRole() != Role.ROLE_ADMIN) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            AppUser returnedUser = userService.findByUsername(username);
            return ResponseEntity.ok(returnedUser);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<AppUser> getMe(@RequestHeader("Authorization") String authHeader) {
        try {
            AppUser user = userService.findUserByHeader(authHeader);
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