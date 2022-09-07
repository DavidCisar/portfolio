package io.dcisar.backend.project;

import io.dcisar.backend.framework.FrameworkDTO;
import io.dcisar.backend.language.LanguageDTO;
import io.dcisar.backend.topic.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    public long id;
    public String name;
    public String description;
    public String projectContext;
    public String website;

    public List<LanguageDTO> languagesInProject = new ArrayList<>();
    public List<FrameworkDTO> frameworksInProject = new ArrayList<>();
    public List<Topic> topicsInProject = new ArrayList<>();

    @Override
    public String toString() {
        return "ProjectDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", projectContext='" + projectContext + '\'' +
                ", website='" + website + '\'' +
                ", languagesInProject=" + languagesInProject +
                ", frameworksInProject=" + frameworksInProject +
                ", topicsInProject=" + topicsInProject +
                '}';
    }

    public void addLanguageDTOToProjectDTO(LanguageDTO languageDTO) {
        this.languagesInProject.add(languageDTO);
    }

    public void addFrameworkDTOToProjectDTO(FrameworkDTO frameworkDTO) {
        this.frameworksInProject.add(frameworkDTO);
    }

    public void addTopicToProjectDTO(Topic topic) {
        this.topicsInProject.add(topic);
    }
}
