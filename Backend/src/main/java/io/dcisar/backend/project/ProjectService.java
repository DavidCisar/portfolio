package io.dcisar.backend.project;

import io.dcisar.backend.technology.framework.Framework;
import io.dcisar.backend.technology.framework.FrameworkDTO;
import io.dcisar.backend.technology.framework.FrameworkRepository;
import io.dcisar.backend.technology.framework.FrameworkService;
import io.dcisar.backend.technology.language.Language;
import io.dcisar.backend.technology.language.LanguageDTO;
import io.dcisar.backend.technology.language.LanguageRepository;
import io.dcisar.backend.technology.language.LanguageService;
import io.dcisar.backend.technology.topic.Topic;
import io.dcisar.backend.technology.topic.TopicRepository;
import io.dcisar.backend.technology.topic.TopicService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public boolean createProject(ProjectDTO projectDTO) {
        if (projectRepository.findByName(projectDTO.name).isPresent()) {
            return false;
        }

        Project projectToBeCreated = mapProjectDTOToProject(projectDTO);
        projectRepository.save(projectToBeCreated);
        return true;
    }

    public boolean updateProject(ProjectDTO projectDTO) {
        if (!projectRepository.findById(projectDTO.id).isPresent()) {
            return false;
        }

        List<Language> languagesInProject = new ArrayList<>();
        for (LanguageDTO languageDTO : projectDTO.languagesInProject) {
            if (!languageService.createLanguage(languageDTO)) {
                languageService.updateLanguage(languageDTO);
            }
            Language language = languageRepository.findByName(languageDTO.name).orElseThrow();
            languagesInProject.add(language);
        }

        List<Framework> frameworksInProject = new ArrayList<>();
        for (FrameworkDTO frameworkDTO : projectDTO.frameworksInProject) {
            if (!frameworkService.createFramework(frameworkDTO)) {
                frameworkService.updateFramework(frameworkDTO);
            }
            Framework framework = frameworkRepository.findByName(frameworkDTO.name).orElseThrow();
            frameworksInProject.add(framework);
        }

        List<Topic> topicsInProject = new ArrayList<>();
        for (Topic topicDTO : projectDTO.topicsInProject) {
            Topic topic = topicRepository.findByName(topicDTO.getName()).orElseThrow();
            topicsInProject.add(topic);
        }

        Project projectToBeUpdated = projectRepository.findById(projectDTO.id).orElseThrow();
        if (projectDTO.name != null) {
            projectToBeUpdated.setName(projectDTO.name);
        }
        if (projectDTO.description != null) {
            projectToBeUpdated.setDescription(projectDTO.description);
        }
        if (projectDTO.projectContext != null) {
            projectToBeUpdated.setProjectContext(projectDTO.projectContext);
        }
        projectToBeUpdated.setLanguagesInProject(languagesInProject);
        projectToBeUpdated.setFrameworksInProject(frameworksInProject);
        projectToBeUpdated.setTopicsInProject(topicsInProject);

        projectRepository.save(projectToBeUpdated);
        return true;
    }



    public boolean addTopic(Long topicId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Topic topic = topicRepository.findById(topicId).orElseThrow();

        Boolean success = project.addTopicToProject(topic);
        projectRepository.save(project);

        return success;
    }

    public boolean removeTopic(Long topicId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Topic topic = topicRepository.findById(topicId).orElseThrow();

        Boolean success = project.removeTopicFromProject(topic);
        projectRepository.save(project);

        return success;
    }

    public boolean addFramework(Long frameworkId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Framework framework = frameworkRepository.findById(frameworkId).orElseThrow();

        Boolean success = project.addFrameworkToProject(framework);
        projectRepository.save(project);

        return success;
    }

    public boolean removeFramework(Long frameworkId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Framework framework = frameworkRepository.findById(frameworkId).orElseThrow();

        Boolean success = project.removeFrameworkFromProject(framework);
        projectRepository.save(project);

        return success;
    }

    public boolean addLanguage(Long languageId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Language language = languageRepository.findById(languageId).orElseThrow();

        Boolean success = project.addLanguageToProject(language);
        projectRepository.save(project);

        return success;
    }

    public boolean removeLanguage(Long languageId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Language language = languageRepository.findById(languageId).orElseThrow();

        Boolean success = project.removeLanguageFromProject(language);
        projectRepository.save(project);

        return success;
    }

    public Project mapProjectDTOToProject(ProjectDTO projectDTO) {
        Project project = new Project(projectDTO.name, projectDTO.description, projectDTO.projectContext, projectDTO.website);

        for (LanguageDTO languageDTO : projectDTO.languagesInProject) {
            if (!languageService.createLanguage(languageDTO)) {
                languageService.updateLanguage(languageDTO);
            }
            project.addLanguageToProject(languageRepository.findByName(languageDTO.name).orElseThrow());
        }

        for (FrameworkDTO frameworkDTO : projectDTO.frameworksInProject) {
            if (!frameworkService.createFramework(frameworkDTO)) {
                frameworkService.updateFramework(frameworkDTO);
            }
            project.addFrameworkToProject(frameworkRepository.findByName(frameworkDTO.name).orElseThrow());
        }

        for (Topic topic : projectDTO.topicsInProject) {
            topicService.createTopic(topic);
            project.addTopicToProject(topicRepository.findByName(topic.getName()).orElseThrow());
        }

        return project;
    }
}
