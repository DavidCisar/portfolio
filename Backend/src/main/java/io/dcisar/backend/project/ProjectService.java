package io.dcisar.backend.project;

import io.dcisar.backend.framework.Framework;
import io.dcisar.backend.framework.FrameworkDTO;
import io.dcisar.backend.framework.FrameworkRepository;
import io.dcisar.backend.framework.FrameworkService;
import io.dcisar.backend.language.Language;
import io.dcisar.backend.language.LanguageDTO;
import io.dcisar.backend.language.LanguageRepository;
import io.dcisar.backend.language.LanguageService;
import io.dcisar.backend.topic.Topic;
import io.dcisar.backend.topic.TopicRepository;
import io.dcisar.backend.topic.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final FrameworkRepository frameworkRepository;
    private final TopicRepository topicRepository;
    private final LanguageRepository languageRepository;
    private final FrameworkService frameworkService;
    private final LanguageService languageService;
    private final TopicService topicService;

    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow();
    }

    public List<ProjectDTO> getProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectDTO> projectDTOs = new ArrayList<>();

        for (Project project : projects) {
            ProjectDTO projectDTO = mapProjectToProjectDTO(project);
            projectDTOs.add(projectDTO);
        }

        return projectDTOs;
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
        if (projectDTO.website != null) {
            projectToBeUpdated.setWebsite(projectDTO.website);
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
        Project project = Project.builder()
                .name(projectDTO.name)
                .description(projectDTO.description)
                .projectContext(projectDTO.projectContext)
                .website(projectDTO.website)
                .languagesInProject(new ArrayList<Language>())
                .frameworksInProject(new ArrayList<Framework>())
                .topicsInProject(new ArrayList<Topic>())
                .build();

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

    public ProjectDTO mapProjectToProjectDTO(Project project) {
        ProjectDTO projectDTO = ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .projectContext(project.getProjectContext())
                .website(project.getWebsite())
                .languagesInProject(new ArrayList<LanguageDTO>())
                .frameworksInProject(new ArrayList<FrameworkDTO>())
                .topicsInProject(new ArrayList<Topic>())
                .build();

        for (Language language : project.getLanguagesInProject()) {
            LanguageDTO languageDTO = languageService.mapLanguageToLanguageDTO(language);
            projectDTO.addLanguageDTOToProjectDTO(languageDTO);
        }

        for (Framework framework : project.getFrameworksInProject()) {
            FrameworkDTO frameworkDTO = frameworkService.mapFrameworkToFrameworkDTO(framework);
            projectDTO.addFrameworkDTOToProjectDTO(frameworkDTO);
        }

        for (Topic topic : projectDTO.topicsInProject) {
            projectDTO.addTopicToProjectDTO(topic);
        }

        return projectDTO;
    }

    public boolean deleteById(Long id) {
        if (projectRepository.findById(id).isPresent()) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
