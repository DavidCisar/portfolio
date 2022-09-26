package io.dcisar.backend.reference;

import lombok.Builder;

@Builder
public class ReferenceDTO {

    public Long id;
    public String name;
    public String message;
    public String link;
    public String createdAt;
    public boolean isAccepted;
}
