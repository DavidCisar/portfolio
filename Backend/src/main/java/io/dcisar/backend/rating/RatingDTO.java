package io.dcisar.backend.rating;

import io.dcisar.backend.user.UserDTO;
import lombok.Builder;

@Builder
public class RatingDTO {

    public Long id;
    public String name;
    public String message;
    public String link;
    public boolean isAccepted;
}
