package io.dcisar.backend.technology.language;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.dcisar.backend.project.Project;
import io.dcisar.backend.technology.framework.Framework;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Language")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;
    private String version;

    @JsonIgnore
    @OneToMany
    private List<Framework> frameworks;

    @JsonIgnore
    @ManyToMany(mappedBy = "languagesInProject")
    private List<Project> projects = new ArrayList<>();

    public Language() {}

    public Language(String name, String description, String version) {
        this.name = name;
        this.description = description;
        this.version = version;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Framework> getFrameworks() {
        return frameworks;
    }

    public void setFrameworks(List<Framework> frameworks) {
        this.frameworks = frameworks;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public void removeFrameworkFromLanguage(Framework frameworkToBeDeleted) {
        frameworks.remove(frameworkToBeDeleted);
    }
}
