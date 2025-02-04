package com.java.backend.domain;

import com.java.backend.data.AppUserRepository;
import com.java.backend.model.AppUser;
import com.java.backend.model.enums.Role;
import com.java.backend.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserService implements UserDetailsService {
    private final JwtTokenProvider jwtTokenProvider;

    private final AppUserRepository repository;

    @Autowired
    public AppUserService(JwtTokenProvider jwtTokenProvider, AppUserRepository repository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.repository = repository;
    }

    public void deleteUser(Long userId) {
        repository.deleteById(userId);
    }

    public void deleteUser(String username) {
        repository.deleteByUsername(username);
    }


    public List<AppUser> findAll() {
        return repository.findAll();
    }

    public AppUser findById(Long id) {
        Optional<AppUser> op = repository.findById(id);
        return op.orElse(null); // Return AppUserDTO if found
    }

    public AppUser findByUsername(String username) {
        Optional<AppUser> op = repository.findByUsername(username);
        return op.orElse(null); // Return AppUserDTO if found
    }

    public AppUser findByEmail(String email) {
        Optional<AppUser> op = repository.findByEmail(email);
        return op.orElse(null); // Return AppUser if found
    }

    public AppUser registerUser(AppUser user) {
        user.setRole(Role.ROLE_USER);
        return repository.save(user);
    }
    public void updateUser(String name, AppUser updatedUser) {
        AppUser existingUser = findByUsername(name);
        if (existingUser != null) {
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            repository.save(existingUser);
        }
    }

    public void upgradeToAdmin(String username) {
        AppUser user = findByUsername(username);
        if (user != null) {
            user.setRole(Role.ROLE_ADMIN);
            repository.save(user);
        }
    }

    public boolean existsByUsername(String username) {
        return findByUsername(username) != null;
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")) // Adjust roles as needed
        );
    }

    public AppUser findUserByHeader(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtTokenProvider.getUsernameFromToken(token);
        return findByUsername(username);
    }
}