package io.dcisar.backend.technology.language;

import io.dcisar.backend.project.Project;
import io.dcisar.backend.technology.framework.Framework;

import java.util.ArrayList;
import java.util.List;

public class LanguageDTO {

    public long id;
    public String name;
    public String description;
    public String version;
    public String documentation;

    public List<Framework> frameworks;
    public List<Project> projects = new ArrayList<>();

    public LanguageDTO() {}

    public LanguageDTO(
            String name,
            String description,
            String version,
            String documentation
    ) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.documentation = documentation;
    }

}
