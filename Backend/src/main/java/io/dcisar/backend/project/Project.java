package io.dcisar.backend.project;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Project {

    @Id
    private long id;
    private String name;
    private String description;
    private String projectContext;

    public Project(
            String name,
            String description,
            String projectContext) {
        this.name = name;
        this.description = description;
        this.projectContext = projectContext;
    }
}
