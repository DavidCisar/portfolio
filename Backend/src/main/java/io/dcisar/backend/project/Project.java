package io.dcisar.backend.project;

import io.dcisar.backend.technology.Technology;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {

    @Id
    private long id;
    private String name;
    private String description;
    private String projectContext;

    @ManyToMany
    @JoinTable(
            name = "technologiesInProject",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    private List<Technology> technologiesInProject = new ArrayList<Technology>();

    public Project() {}

    public Project(
            String name,
            String description,
            String projectContext) {
        this.name = name;
        this.description = description;
        this.projectContext = projectContext;
    }

    public void addTechnologyToProject(Technology technology) {
        technologiesInProject.add(technology);
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

    public List<Technology> getTechnologiesInProject() {
        return technologiesInProject;
    }

    public void setTechnologiesInProject(List<Technology> technologiesInProject) {
        this.technologiesInProject = technologiesInProject;
    }
}
