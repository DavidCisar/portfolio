package io.dcisar.backend.technology.language;

import io.dcisar.backend.project.Project;
import io.dcisar.backend.technology.Technology;
import io.dcisar.backend.technology.framework.Framework;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Language")
public class Language extends Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany
    private List<Framework> frameworks;

    @ManyToMany(mappedBy = "languagesInProject")
    private List<Project> projects = new ArrayList<>();

    public Language(String name, String description, String version) {
        super(name, description, version);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
