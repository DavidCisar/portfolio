package io.dcisar.backend.technology.language;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.dcisar.backend.project.Project;
import io.dcisar.backend.technology.framework.Framework;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class LanguageDTO {

    public long id;
    public String name;
    public String description;
    public String version;

    public List<Framework> frameworks;
    public List<Project> projects = new ArrayList<>();

    public LanguageDTO() {}

    public LanguageDTO(String name, String description, String version) {
        this.name = name;
        this.description = description;
        this.version = version;
    }

}
