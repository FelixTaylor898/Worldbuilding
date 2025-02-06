package com.java.backend.data;

import com.java.backend.model.AppUser;
import com.java.backend.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.config.location=classpath:application-test.properties")
@ActiveProfiles("test")
public class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    KnownGoodState state;

    @BeforeEach
    public void setUp() {
        state.reset();
    }

    @Test
    public void findAllUsersTest() {
        List<AppUser> list = appUserRepository.findAll();
        assertEquals(2, list.size());
    }

    @Test
    public void findByUserIdTest() {
        Optional<AppUser> op = appUserRepository.findById(1L);
        assertTrue(op.isPresent());
        AppUser user = op.get();
        assertEquals("john_doe", user.getUsername());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals(Role.ROLE_ADMIN, user.getRole());
    }

    @Test
    public void findByUserIdInvalidTest() {
        Optional<AppUser> op = appUserRepository.findById(5L);
        assertTrue(op.isEmpty());
    }

    @Test
    public void findByRoleTest() {
        List<AppUser> users = appUserRepository.findByRole(Role.ROLE_ADMIN);
        assertEquals(1, users.size());
    }


    @Test
    public void findByUserEmailTest() {
        Optional<AppUser> op = appUserRepository.findByEmail("john.doe@example.com");
        assertTrue(op.isPresent());
        AppUser user = op.get();
        //     (1, 'john_doe', 'john.doe@example.com', 'hashed_password_123', 'admin', NOW()),
        assertEquals("john_doe", user.getUsername());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals(Role.ROLE_ADMIN, user.getRole());
    }

    @Test
    public void findByUserEmailInvalidTest() {
        Optional<AppUser> op = appUserRepository.findByEmail("blah@blah.com");
        assertTrue(op.isEmpty());
    }

    @Test
    public void findByUsernameTest() {
        Optional<AppUser> op = appUserRepository.findByUsername("john_doe");
        assertTrue(op.isPresent());
        AppUser user = op.get();
        assertEquals("john_doe", user.getUsername());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals(Role.ROLE_ADMIN, user.getRole());
    }

    @Test
    public void findByUsernameInvalidTest() {
        Optional<AppUser> op = appUserRepository.findByEmail("john_doe");
        assertTrue(op.isEmpty());
    }
}
