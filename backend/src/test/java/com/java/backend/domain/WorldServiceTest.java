package com.java.backend.domain;

import com.java.backend.data.WorldRepository;
import com.java.backend.model.AppUser;
import com.java.backend.model.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorldServiceTest {

    @Mock
    private WorldRepository repository;

    @InjectMocks
    private WorldService worldService;

    private World world;

    @BeforeEach
    void setUp() {
        AppUser user = new AppUser();
        user.setUserId(1L);
        world = new World();
        world.setUser(user);
        world.setWorldId(1L);
        world.setName("Fantasy Land");
        world.setDescription("A magical realm");
    }

    @Test
    void testAddWorld() {
        when(repository.save(world)).thenReturn(world);
        World savedWorld = worldService.addWorld(world);
        assertNotNull(savedWorld);
        assertEquals("Fantasy Land", savedWorld.getName());
        verify(repository, times(1)).save(world);
    }

    @Test
    void testUpdateWorld() {
        World updatedWorld = new World();
        updatedWorld.setName("Sci-Fi Land");
        updatedWorld.setDescription("A futuristic realm");

        when(repository.findById(1L)).thenReturn(Optional.of(world));
        when(repository.save(any(World.class))).thenReturn(updatedWorld);

        World result = worldService.updateWorld(1L, updatedWorld);
        assertNotNull(result);
        assertEquals("Sci-Fi Land", result.getName());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(World.class));
    }

    @Test
    void testUpdateWorld_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        World result = worldService.updateWorld(1L, world);
        assertNull(result);
    }

    @Test
    void testDeleteWorld() {
        doNothing().when(repository).deleteById(1L);
        worldService.deleteWorld(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAll() {
        List<World> worlds = Arrays.asList(world, new World());
        when(repository.findAll()).thenReturn(worlds);
        List<World> result = worldService.findAll();
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(repository.findById(1L)).thenReturn(Optional.of(world));
        World foundWorld = worldService.findById(1L);
        assertNotNull(foundWorld);
        assertEquals(1L, foundWorld.getWorldId());
    }

    @Test
    void testFindById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        World result = worldService.findById(1L);
        assertNull(result);
    }

    @Test
    void testFindByUser() {
        AppUser user = new AppUser();
        List<World> userWorlds = Arrays.asList(world);
        when(repository.findByUser(user)).thenReturn(userWorlds);
        List<World> result = worldService.findByUser(user);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByUser(user);
    }
}