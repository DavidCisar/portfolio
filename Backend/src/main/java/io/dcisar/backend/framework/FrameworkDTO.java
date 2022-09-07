package io.dcisar.backend.framework;

import io.dcisar.backend.project.Project;
import io.dcisar.backend.language.LanguageDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrameworkDTO {

    public long id;
    public String name;
    public String description;
    public String version;
    public String documentation;

    public LanguageDTO languageDTO;
    public List<Project> projects = new ArrayList<>();

    @Override
    public String toString() {
        return "FrameworkDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", version='" + version + '\'' +
                ", documentation='" + documentation + '\'' +
                ", languageDTO=" + languageDTO +
                ", projects=" + projects +
                '}';
    }
}
