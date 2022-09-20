package io.dcisar.backend.user;

import lombok.Builder;

@Builder
public class UserDTO {
    public Long id;
    public String firstName;
    public String lastName;
    public String username;
    public String email;
}
