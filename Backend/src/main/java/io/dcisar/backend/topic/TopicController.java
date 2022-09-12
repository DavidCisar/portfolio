package io.dcisar.backend.topic;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public List<TopicDTO> getTopics() {
        return topicService.getTopics();
    }

    @GetMapping("/{id}")
    public TopicDTO getTopic(@PathVariable Long id) {
        return topicService.getTopic(id);
    }

}
