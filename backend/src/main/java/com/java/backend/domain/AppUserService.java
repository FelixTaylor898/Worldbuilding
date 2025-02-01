package com.java.backend.domain;

import com.java.backend.data.AppUserRepository;
import com.java.backend.model.AppUser;
import com.java.backend.model.AppUserDTO;
import com.java.backend.model.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    private final AppUserRepository repository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserService(AppUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
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
        // Encode the password before saving it
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
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
}