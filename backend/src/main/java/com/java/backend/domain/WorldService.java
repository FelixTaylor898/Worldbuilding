package com.java.backend.domain;

import com.java.backend.data.WorldRepository;
import com.java.backend.model.AppUser;
import com.java.backend.model.World;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WorldService {

    @Autowired
    private final WorldRepository repository;

    @Autowired
    public WorldService(WorldRepository repository) {
        this.repository = repository;
    }

    public World addWorld(World world) {
        world.setCreatedAt(LocalDateTime.now());
        return repository.save(world);
    }

    public World updateWorld(Long worldId, World updatedWorld) {
        Optional<World> op = repository.findById(worldId);
        if (op.isPresent()) {
            World existingWorld = op.get();
            if (!updatedWorld.getDescription().isEmpty()) existingWorld.setDescription(updatedWorld.getDescription());
            if (!updatedWorld.getName().isEmpty()) existingWorld.setName(updatedWorld.getName());
            return repository.save(existingWorld);
        }
        return null;
    }

    public void deleteWorld(Long worldId) {
        repository.deleteById(worldId);
    }

    public List<World> findAll() {
        return repository.findAll();
    }

    public World findById(Long id) {
        Optional<World> op = repository.findById(id);
        return op.orElse(null);
    }

    public List<World> findByUser(AppUser user) {
        return repository.findByUser(user);
    }
}
