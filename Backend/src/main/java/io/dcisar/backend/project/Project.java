package io.dcisar.backend.project;

import io.dcisar.backend.technology.framework.Framework;
import io.dcisar.backend.technology.language.Language;
import io.dcisar.backend.technology.topic.Topic;

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
    private String website;

    @ManyToMany
    @JoinTable(
            name = "languagesInProject",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<Language> languagesInProject = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "frameworksInProject",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "framework_id")
    )
    private List<Framework> frameworksInProject = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "topicsInProject",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private List<Topic> topicsInProject = new ArrayList<>();

    public Project() {}

    public Project(
            String name,
            String description,
            String projectContext,
            String website
    ) {
        this.name = name;
        this.description = description;
        this.projectContext = projectContext;
        this.website = website;
    }

    public boolean addLanguageToProject(Language language) {
        if (!languagesInProject.contains(language)) {
            languagesInProject.add(language);
            return true;
        }
        return false;
    }

    public boolean removeLanguageFromProject(Language language) {
        return languagesInProject.remove(language);
    }

    public boolean addFrameworkToProject(Framework framework) {
        if (!frameworksInProject.contains(framework)) {
            frameworksInProject.add(framework);
            return true;
        }
        return false;
    }

    public boolean removeFrameworkFromProject(Framework framework) {
        return frameworksInProject.remove(framework);
    }

    public boolean addTopicToProject(Topic topic) {
        if (!topicsInProject.contains(topic)) {
            topicsInProject.add(topic);
            return true;
        }
        return false;
    }

    public boolean removeTopicFromProject(Topic topic) {
        return topicsInProject.remove(topic);
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<Language> getLanguagesInProject() {
        return languagesInProject;
    }

    public void setLanguagesInProject(List<Language> languagesInProject) {
        this.languagesInProject = languagesInProject;
    }

    public List<Framework> getFrameworksInProject() {
        return frameworksInProject;
    }

    public void setFrameworksInProject(List<Framework> frameworksInProject) {
        this.frameworksInProject = frameworksInProject;
    }

    public List<Topic> getTopicsInProject() {
        return topicsInProject;
    }

    public void setTopicsInProject(List<Topic> topicsInProject) {
        this.topicsInProject = topicsInProject;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", projectContext='" + projectContext + '\'' +
                ", languagesInProject=" + languagesInProject +
                ", frameworksInProject=" + frameworksInProject +
                ", topicsInProject=" + topicsInProject.size() +
                '}';
    }
}
