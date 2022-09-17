package io.dcisar.backend.user;

public class Authority {
    public static final String[] USER_AUTHORITIES = {"rating:create", "rating:update", "rating:delete"};
    public static final String[] ADMIN_AUTHORITIES = {"rating:delete", "admin:all"};
}
