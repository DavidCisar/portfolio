package io.dcisar.backend.database;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseDTO {

    public Long id;
    public String name;
    public String description;

}
