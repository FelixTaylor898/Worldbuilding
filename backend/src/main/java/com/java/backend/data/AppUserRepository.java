package com.java.backend.data;

import com.java.backend.model.AppUser;
import com.java.backend.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    List<AppUser> findByRole(Role role);
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByUsername(String username);
}
