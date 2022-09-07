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
    public List<Topic> getTopics() {
        return topicService.getTopics();
    }

    @GetMapping("/{topicId}")
    public Topic getTopic(@PathVariable Long topicId) {
        return topicService.getTopic(topicId);
    }

}
