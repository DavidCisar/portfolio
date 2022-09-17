package io.dcisar.backend.user;

import static io.dcisar.backend.user.Authority.ADMIN_AUTHORITIES;
import static io.dcisar.backend.user.Authority.USER_AUTHORITIES;

public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES);

    private String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}
