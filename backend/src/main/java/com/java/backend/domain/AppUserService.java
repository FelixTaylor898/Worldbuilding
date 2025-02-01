package com.java.backend.domain;

import com.java.backend.data.AppUserRepository;
import com.java.backend.model.AppUser;
import com.java.backend.model.AppUserDTO;
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

    public List<AppUserDTO> findAll() {
        return repository.findAll().stream()
                .map(AppUserDTO::new) // Convert each AppUser to AppUserDTO
                .collect(Collectors.toList());
    }

    public AppUserDTO findById(Long id) {
        Optional<AppUser> op = repository.findById(id);
        return op.map(AppUserDTO::new).orElse(null); // Return AppUserDTO if found
    }

    public AppUserDTO findByUsername(String username) {
        Optional<AppUser> op = repository.findByUsername(username);
        return op.map(AppUserDTO::new).orElse(null); // Return AppUserDTO if found
    }

    public AppUserDTO findByEmail(String email) {
        Optional<AppUser> op = repository.findByEmail(email);
        return op.map(AppUserDTO::new).orElse(null); // Return AppUserDTO if found
    }

    public AppUserDTO registerUser(AppUser user) {
        // Encode the password before saving it
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPasswordHash(encodedPassword);

        // Save the user to the database and return AppUserDTO
        return new AppUserDTO(repository.save(user));
    }
}