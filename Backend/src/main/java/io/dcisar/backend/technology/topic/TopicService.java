package io.dcisar.backend.technology.topic;

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

    public boolean removeTopic(Topic topic) {
        if (!topicRepository.findByName(topic.getName()).isPresent()) {
            return false;
        }
        Topic topicToBeDeleted = topicRepository.findByName(topic.getName()).orElseThrow();
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