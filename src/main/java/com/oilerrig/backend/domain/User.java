package com.oilerrig.backend.domain;

import java.time.OffsetDateTime;

public class User {
    private String email;
    private String name;
    private OffsetDateTime createdAt;
    private UserRole role;

    public enum UserRole {
        GUEST, CLIENT, ADMIN
    }

    // business logic and helpers
    public boolean isGuest() {
        return this.role == UserRole.GUEST;
    }
    public boolean isClient() {
        return this.role == UserRole.CLIENT;
    }
    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }

    public boolean hasEmail() {
        return this.email != null;
    }
    public boolean hasName() {
        return this.name != null;
    }

    public boolean isValid() {
        return this.hasEmail() && this.hasName() && this.createdAt != null && this.role != null;
    }

    // getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

}
