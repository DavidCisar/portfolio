package io.dcisar.backend.technology.language;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDTO {

    public long id;
    public String name;
    public String description;
    public String version;
    public String documentation;

    @Override
    public String toString() {
        return "LanguageDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", version='" + version + '\'' +
                ", documentation='" + documentation + '\'' +
                '}';
    }
}
