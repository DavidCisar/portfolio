package io.dcisar.backend.technology.framework;

import io.dcisar.backend.project.Project;
import io.dcisar.backend.technology.Technology;
import io.dcisar.backend.technology.language.Language;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Framework extends Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToMany(mappedBy = "frameworksInProject")
    private List<Project> projects = new ArrayList<>();

    public Framework(String name, String description, String version) {
        super(name, description, version);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
