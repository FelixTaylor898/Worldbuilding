package com.java.backend.data;

import com.java.backend.model.AppUser;
import com.java.backend.model.World;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorldRepository extends JpaRepository<World, Long> {
    List<World> findByUser(AppUser user);
}
