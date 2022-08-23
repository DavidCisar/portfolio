package io.dcisar.backend.technology.framework;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.dcisar.backend.project.Project;
import io.dcisar.backend.technology.language.Language;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

public class FrameworkDTO {

    public long id;
    public String name;
    public String description;
    public String version;

    public Language language;
    public List<Project> projects = new ArrayList<>();

    public FrameworkDTO() {}

    public FrameworkDTO(String name, String description, String version) {
        this.name = name;
        this.description = description;
        this.version = version;
    }
}
