package io.dcisar.backend.project;

import io.dcisar.backend.technology.framework.Framework;
import io.dcisar.backend.technology.framework.FrameworkRepository;
import io.dcisar.backend.technology.framework.FrameworkService;
import io.dcisar.backend.technology.language.Language;
import io.dcisar.backend.technology.language.LanguageRepository;
import io.dcisar.backend.technology.language.LanguageService;
import io.dcisar.backend.technology.topic.Topic;
import io.dcisar.backend.technology.topic.TopicRepository;
import io.dcisar.backend.technology.topic.TopicService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final FrameworkRepository frameworkRepository;
    private final TopicRepository topicRepository;
    private final LanguageRepository languageRepository;
    private final FrameworkService frameworkService;
    private final LanguageService languageService;
    private final TopicService topicService;

    public ProjectService(
            ProjectRepository projectRepository,
            FrameworkRepository frameworkRepository,
            TopicRepository topicRepository,
            LanguageRepository languageRepository,
            FrameworkService frameworkService,
            LanguageService languageService,
            TopicService topicService)
    {
        this.projectRepository = projectRepository;
        this.frameworkRepository = frameworkRepository;
        this.topicRepository = topicRepository;
        this.languageRepository = languageRepository;
        this.frameworkService = frameworkService;
        this.languageService = languageService;
        this.topicService = topicService;
    }

    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow();
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public boolean createProject(Project project) {
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

    public Project mapProjectDTOToProject(ProjectDTO projectDTO) {
        Project project = new Project(projectDTO.name, projectDTO.description, projectDTO.projectContext);

        for (Language language : projectDTO.languagesInProject) {
            languageService.createLanguage(language);
            project.addLanguageToProject(languageRepository.findByName(language.getName()).orElseThrow());
        }

        for (Framework framework : projectDTO.frameworksInProject) {
            frameworkService.createFramework(framework);
            project.addFrameworkToProject(frameworkRepository.findByName(framework.getName()).orElseThrow());
        }

        for (Topic topic : projectDTO.topicsInProject) {
            topicService.createTopic(topic);
            project.addTopicToProject(topicRepository.findByName(topic.getName()).orElseThrow());
        }

        return project;
    }
}
