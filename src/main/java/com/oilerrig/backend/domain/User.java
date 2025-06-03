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
