package io.dcisar.backend.project;

import io.dcisar.backend.technology.framework.Framework;
import io.dcisar.backend.technology.framework.FrameworkRepository;
import io.dcisar.backend.technology.language.Language;
import io.dcisar.backend.technology.language.LanguageRepository;
import io.dcisar.backend.technology.topic.Topic;
import io.dcisar.backend.technology.topic.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final FrameworkRepository frameworkRepository;
    private final TopicRepository topicRepository;
    private final LanguageRepository languageRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            FrameworkRepository frameworkRepository,
            TopicRepository topicRepository,
            LanguageRepository languageRepository)
    {
        this.projectRepository = projectRepository;
        this.frameworkRepository = frameworkRepository;
        this.topicRepository = topicRepository;
        this.languageRepository = languageRepository;
    }

    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow();
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public boolean save(Project project) {
        if (projectRepository.findByName(project.getName()).isPresent()) {
            return false;
        }
        projectRepository.save(project);
        return true;
    }

    public boolean addTopic(Long topicId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Topic topic = topicRepository.findById(topicId).orElseThrow();
        if (!project.getTopicsInProject().contains(topic)) {
            project.addTopicToProject(topic);
            projectRepository.save(project);
            return true;
        }
        return false;
    }

    public boolean removeTopic(Long topicId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Topic topic = topicRepository.findById(topicId).orElseThrow();
        if (project.getTopicsInProject().contains(topic)) {
            project.removeTopicFromProject(topic);
            projectRepository.save(project);
            return true;
        }
        return false;
    }

    public boolean addFramework(Long frameworkId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Framework framework = frameworkRepository.findById(frameworkId).orElseThrow();
        if (!project.getFrameworksInProject().contains(framework)) {
            project.addFrameworkToProject(framework);
            projectRepository.save(project);
            return true;
        }
        return false;
    }

    public boolean removeFramework(Long frameworkId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Framework framework = frameworkRepository.findById(frameworkId).orElseThrow();
        if (project.getFrameworksInProject().contains(framework)) {
            project.removeFrameworkFromProject(framework);
            projectRepository.save(project);
            return true;
        }
        return false;
    }

    public boolean addLanguage(Long languageId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Language language = languageRepository.findById(languageId).orElseThrow();
        if (!project.getLanguagesInProject().contains(language)) {
            project.addLanguageToProject(language);
            projectRepository.save(project);
            return true;
        }
        return false;
    }

    public boolean removeLanguage(Long languageId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Language language = languageRepository.findById(languageId).orElseThrow();
        if (project.getLanguagesInProject().contains(language)) {
            project.removeLanguageFromProject(language);
            projectRepository.save(project);
            return true;
        }
        return false;
    }
}
