package io.dcisar.backend.technology.topic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.dcisar.backend.project.Project;

import javax.persistence.*;
import java.util.List;

@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "topicsInProject")
    private List<Project> projects;

    public Topic() {}

    public Topic(String name) {
        this.name = name;
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

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public void addProjectToTopic(Project project) {
        if (!projects.contains(project)) {
            System.out.println("Project gets added to topic");
            projects.add(project);
        }
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", projects=" + projects +
                '}';
    }
}
