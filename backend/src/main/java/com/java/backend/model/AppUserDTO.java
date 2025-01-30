package com.java.backend.model;

import com.java.backend.model.enums.Role;

import java.time.LocalDateTime;

public class AppUserDTO {
    private Long userId;
    private String username;
    private String email;
    private Role role;
    private LocalDateTime createdAt;

    public AppUserDTO(String username, Long userId, String email, LocalDateTime createdAt, Role role) {
        this.username = username;
        this.userId = userId;
        this.email = email;
        this.createdAt = createdAt;
        this.role = role;
    }

    public AppUserDTO(AppUser user) {
        this.username = user.getUsername();
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.role = user.getRole();
    }

    public AppUserDTO() {}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
