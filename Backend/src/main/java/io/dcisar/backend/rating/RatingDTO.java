package io.dcisar.backend.rating;

import io.dcisar.backend.user.UserDTO;
import lombok.Builder;

@Builder
public class RatingDTO {

    public Long id;
    public int rating;
    public String message;
    public UserDTO userDTO;
    
}
