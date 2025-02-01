package com.java.backend.data;

import com.java.backend.model.AppUser;
import com.java.backend.model.World;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WorldRepository extends JpaRepository<World, Long> {
    List<World> findByUser(AppUser user);
}
