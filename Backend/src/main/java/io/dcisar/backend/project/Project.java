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

    public Project(
            String name,
            String description,
            String projectContext) {
        this.name = name;
        this.description = description;
        this.projectContext = projectContext;
    }
}
