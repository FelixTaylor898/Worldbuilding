package com.java.backend.domain;

import com.java.backend.data.AppUserRepository;
import com.java.backend.model.AppUser;
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
class AppUserServiceTest {

    @Mock
    private AppUserRepository repository;

    @InjectMocks
    private AppUserService service;

    private AppUser user;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setUserId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");
    }

    @Test
    void testAddUser() {
        when(repository.save(user)).thenReturn(user);
        AppUser result = service.addUser(user);
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        verify(repository, times(1)).save(user);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(repository).deleteById(user.getUserId());
        service.deleteUser(user.getUserId());
        verify(repository, times(1)).deleteById(user.getUserId());
    }

    @Test
    void testFindAll() {
        List<AppUser> users = Arrays.asList(user);
        when(repository.findAll()).thenReturn(users);
        List<AppUser> result = service.findAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(repository.findById(user.getUserId())).thenReturn(Optional.of(user));
        AppUser result = service.findById(user.getUserId());
        assertNotNull(result);
        assertEquals(user.getUserId(), result.getUserId());
        verify(repository, times(1)).findById(user.getUserId());
    }

    @Test
    void testFindById_NotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        AppUser result = service.findById(2L);
        assertNull(result);
        verify(repository, times(1)).findById(2L);
    }

    @Test
    void testFindByUsername() {
        when(repository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        AppUser result = service.findByUsername(user.getUsername());
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        verify(repository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    void testFindByUsername_NotFound() {
        when(repository.findByUsername("unknown")).thenReturn(Optional.empty());
        AppUser result = service.findByUsername("unknown");
        assertNull(result);
        verify(repository, times(1)).findByUsername("unknown");
    }

    @Test
    void testFindByEmail() {
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        AppUser result = service.findByEmail(user.getEmail());
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        verify(repository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testFindByEmail_NotFound() {
        when(repository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
        AppUser result = service.findByEmail("unknown@example.com");
        assertNull(result);
        verify(repository, times(1)).findByEmail("unknown@example.com");
    }
}