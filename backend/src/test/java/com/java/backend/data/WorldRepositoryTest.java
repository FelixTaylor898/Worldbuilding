package com.java.backend.data;

import com.java.backend.model.AppUser;
import com.java.backend.model.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.config.location=classpath:application-test.properties")
@ActiveProfiles("test")
public class WorldRepositoryTest {
    @Autowired
    private WorldRepository worldRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    KnownGoodState state;
    
    @BeforeEach
    public void setUp() {
        state.reset();
    }

    @Test
    public void testFindAll() {
        List<World> list = worldRepository.findAll();
        assertEquals(2, list.size());
    }

    @Test
    public void testFindByUsers() {
        AppUser user = new AppUser();
        user.setUserId(1L);
        List<World> list = worldRepository.findByUser(user);
        assertEquals(1, list.size());
        user.setUserId(2L);
        list = worldRepository.findByUser(user);
        assertEquals(1, list.size());
    }

    @Test
    public void testFindByInvalidUser() {
        AppUser user = new AppUser();
        user.setUserId(3L);
        List<World> list = worldRepository.findByUser(user);
        assertEquals(0, list.size());
    }

    @Test
    public void testFindById() {
        Optional<World> op = worldRepository.findById(1L);
        assertTrue(op.isPresent());
        World world = op.get();
        assertNotNull(world);
        assertEquals(1, world.getWorldId());
        assertEquals(1, world.getUser().getUserId());
        assertEquals("Earth", world.getName());
        assertEquals("A blue planet with diverse ecosystems and climates.", world.getDescription());
    }

    @Test
    public void testFindByIdInvalid() {
        Optional<World> op = worldRepository.findById(5L);
        assertTrue(op.isEmpty());
    }
}