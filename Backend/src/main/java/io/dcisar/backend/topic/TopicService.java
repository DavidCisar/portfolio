package io.dcisar.backend.topic;

import io.dcisar.backend.project.Project;
import io.dcisar.backend.project.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final ProjectRepository projectRepository;

    public TopicService(TopicRepository topicRepository, ProjectRepository projectRepository) {
        this.topicRepository = topicRepository;
        this.projectRepository = projectRepository;
    }

    public boolean createTopic(Topic topic) {
        if (topicRepository.findByName(topic.getName()).isPresent()) {
            return false;
        }
        topicRepository.save(topic);
        return true;
    }

    public boolean deleteTopic(Long id) {
        if (!topicRepository.findById(id).isPresent()) {
            return false;
        }
        Topic topicToBeDeleted = topicRepository.findById(id).orElseThrow();
        List<Project> projects = projectRepository.findAll();
        for (Project project : projects) {
            if (project.getTopicsInProject().contains(topicToBeDeleted)) {
                project.removeTopicFromProject(topicToBeDeleted);
                projectRepository.save(project);
            }
        }
        topicRepository.delete(topicToBeDeleted);
        return true;
    }

    public List<Topic> getTopics() {
        return topicRepository.findAll();
    }

    public Topic getTopic(Long topicId) {
        return topicRepository.findById(topicId).orElseThrow();
    }
}
