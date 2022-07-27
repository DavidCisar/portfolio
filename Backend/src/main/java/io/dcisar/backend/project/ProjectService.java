package io.dcisar.backend.project;

import io.dcisar.backend.technology.topic.Topic;
import io.dcisar.backend.technology.topic.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TopicRepository topicRepository;

    public ProjectService(ProjectRepository projectRepository, TopicRepository topicRepository) {
        this.projectRepository = projectRepository;
        this.topicRepository = topicRepository;
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
}
