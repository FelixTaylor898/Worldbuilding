package com.java.backend.controllers;

import com.java.backend.model.AppUser;
import com.java.backend.model.AppUserDTO;
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

import java.security.Principal;
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
            userService.registerUser(user);

            // Manually authenticate since we saved an encoded password
            UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, pass, userDetails.getAuthorities());

            authenticationManager.authenticate(authToken); // Authenticate
            SecurityContextHolder.getContext().setAuthentication(authToken);

            return ResponseEntity.status(HttpStatus.CREATED).body("User registered and logged in");
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

    @GetMapping("/me")
    public ResponseEntity<AppUserDTO> getCurrentUser(Principal principal) {
        try {
            AppUser user = userService.findByUsername(principal.getName());
            return ResponseEntity.ok(new AppUserDTO(user));
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
    public ResponseEntity<Iterable<AppUserDTO>> getAllUsers() {
        try {
            Iterable<AppUser> users = userService.findAll();

            // Convert AppUser to AppUserDTO
            List<AppUserDTO> userDTOs = StreamSupport.stream(users.spliterator(), false)
                    .map(AppUserDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(userDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{userId}")
    public ResponseEntity<AppUserDTO> getUserById(@PathVariable Long userId) {
        try {
            AppUser user = userService.findById(userId);
            if (user != null) {
                return ResponseEntity.ok(new AppUserDTO(user));
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}