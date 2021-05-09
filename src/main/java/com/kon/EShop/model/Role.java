package com.kon.EShop.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    GUEST,
    USER,
    ADMIN,
    MANAGER;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}