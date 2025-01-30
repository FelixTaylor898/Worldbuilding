package com.java.backend.domain;

import com.java.backend.data.AppUserRepository;
import com.java.backend.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    @Autowired
    private final AppUserRepository repository;

    @Autowired
    public AppUserService(AppUserRepository repository) {
        this.repository = repository;
    }

    public AppUser addUser(AppUser user) {
        return repository.save(user);
    }

    public void deleteUser(Long userId) {
        repository.deleteById(userId);
    }

    public List<AppUser> findAll() {
        return repository.findAll();
    }

    public AppUser findById(Long id) {
        Optional<AppUser> op = repository.findById(id);
        return op.orElse(null);
    }

    public AppUser findByUsername(String username) {
        Optional<AppUser> op = repository.findByUsername(username);
        return op.orElse(null);
    }

    public AppUser findByEmail(String email) {
        Optional<AppUser> op = repository.findByEmail(email);
        return op.orElse(null);
    }
}