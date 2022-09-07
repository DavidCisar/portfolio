package io.dcisar.backend.project;

import io.dcisar.backend.framework.Framework;
import io.dcisar.backend.language.Language;
import io.dcisar.backend.topic.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
