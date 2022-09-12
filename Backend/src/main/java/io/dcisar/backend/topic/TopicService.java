package io.dcisar.backend.topic;

import io.dcisar.backend.project.Project;
import io.dcisar.backend.project.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final ProjectRepository projectRepository;

    public TopicService(TopicRepository topicRepository, ProjectRepository projectRepository) {
        this.topicRepository = topicRepository;
        this.projectRepository = projectRepository;
    }

    public boolean createTopic(TopicDTO topicDTO) {
        if (topicRepository.findByName(topicDTO.name).isPresent()) {
            return false;
        }
        Topic topicToBeCreated = mapDTOToTopic(topicDTO);
        topicRepository.save(topicToBeCreated);
        return true;
    }

    public boolean updateTopic(TopicDTO topicDTO) {
        if (!topicRepository.findById(topicDTO.id).isPresent() || topicDTO.name.equals("")) {
            return false;
        }
        Topic topicToBeUpdated = topicRepository.findById(topicDTO.id).orElseThrow();
        topicToBeUpdated.setName(topicDTO.name);
        topicRepository.save(topicToBeUpdated);
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

    public List<TopicDTO> getTopics() {
        List<Topic> topics = topicRepository.findAll();
        return topics.stream()
                .map(Topic -> mapTopicToTopicDTO(Topic))
                .collect(Collectors.toList());
    }

    public TopicDTO getTopic(Long topicId) {
        return mapTopicToTopicDTO(topicRepository.findById(topicId).orElseThrow());
    }

    private Topic mapDTOToTopic(TopicDTO topicDTO) {
        return Topic.builder()
                .name(topicDTO.name)
                .build();
    }

    public TopicDTO mapTopicToTopicDTO(Topic topic) {
        return TopicDTO.builder()
                .name(topic.getName())
                .build();
    }
}
