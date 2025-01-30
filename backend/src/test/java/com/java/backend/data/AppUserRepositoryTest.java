package com.java.backend.data;

import com.java.backend.model.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
