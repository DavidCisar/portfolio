package io.dcisar.backend.project;

import io.dcisar.backend.technology.Language;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;
    private String projectContext;

    @ManyToMany
    @JoinTable(
            name = "languagesInProject",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<Language> languagesInProject = new ArrayList<>();

    public Project() {}

    public Project(
            String name,
            String description,
            String projectContext) {
        this.name = name;
        this.description = description;
        this.projectContext = projectContext;
    }

    public void addLanguageToProject(Language language) {
        languagesInProject.add(language);
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

    public String getProjectContext() {
        return projectContext;
    }

    public void setProjectContext(String projectContext) {
        this.projectContext = projectContext;
    }

    public List<Language> getLanguagesInProject() {
        return languagesInProject;
    }

    public void setLanguagesInProject(List<Language> languagesInProject) {
        this.languagesInProject = languagesInProject;
    }
}
