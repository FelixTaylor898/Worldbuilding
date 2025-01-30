package com.java.backend.data;

import com.java.backend.model.AppUser;
import com.java.backend.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    List<AppUser> findByRole(Role role);

}
