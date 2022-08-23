package io.dcisar.backend.project;

import io.dcisar.backend.technology.framework.FrameworkDTO;
import io.dcisar.backend.technology.language.LanguageDTO;
import io.dcisar.backend.technology.topic.Topic;

import java.util.ArrayList;
import java.util.List;

public class ProjectDTO {

    public long id;
    public String name;
    public String description;
    public String projectContext;

    public List<LanguageDTO> languagesInProject = new ArrayList<>();
    public List<FrameworkDTO> frameworksInProject = new ArrayList<>();
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
