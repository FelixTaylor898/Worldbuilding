package com.java.backend.domain;

import com.java.backend.data.AppUserRepository;
import com.java.backend.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
