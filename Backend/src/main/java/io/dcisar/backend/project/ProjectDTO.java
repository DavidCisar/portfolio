package io.dcisar.backend.project;

import io.dcisar.backend.technology.framework.Framework;
import io.dcisar.backend.technology.language.Language;
import io.dcisar.backend.technology.topic.Topic;

import java.util.ArrayList;
import java.util.List;

public class ProjectDTO {

    public long id;
    public String name;
    public String description;
    public String projectContext;

    public List<Language> languagesInProject = new ArrayList<>();
    public List<Framework> frameworksInProject = new ArrayList<>();
    public List<Topic> topicsInProject = new ArrayList<>();

    public ProjectDTO() {}

    public ProjectDTO(
            String name,
            String description,
            String projectContext) {
        this.name = name;
        this.description = description;
        this.projectContext = projectContext;
    }

}
