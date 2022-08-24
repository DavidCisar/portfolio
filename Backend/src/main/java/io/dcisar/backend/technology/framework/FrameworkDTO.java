package io.dcisar.backend.technology.framework;

import io.dcisar.backend.project.Project;
import io.dcisar.backend.technology.language.LanguageDTO;

import java.util.ArrayList;
import java.util.List;

public class FrameworkDTO {

    public long id;
    public String name;
    public String description;
    public String version;

    public LanguageDTO languageDTO;
    public List<Project> projects = new ArrayList<>();

    public FrameworkDTO() {}

    public FrameworkDTO(String name, String description, String version) {
        this.name = name;
        this.description = description;
        this.version = version;
    }
}
