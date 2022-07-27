package io.dcisar.backend.admin;

import io.dcisar.backend.project.Project;
import io.dcisar.backend.project.ProjectService;
import io.dcisar.backend.technology.framework.Framework;
import io.dcisar.backend.technology.framework.FrameworkService;
import io.dcisar.backend.technology.language.LanguageService;
import io.dcisar.backend.technology.topic.Topic;
import io.dcisar.backend.technology.topic.TopicService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
public class AdminController {

    private final ProjectService projectService;
    private final FrameworkService frameworkService;
    private final LanguageService languageService;
    private final TopicService topicService;

    public AdminController(ProjectService projectService, FrameworkService frameworkService, LanguageService languageService, TopicService topicService) {
        this.projectService = projectService;
        this.frameworkService = frameworkService;
        this.languageService = languageService;
        this.topicService = topicService;
    }

    // Framework Management
    @PostMapping("createFramework")
    public String createFramework(@RequestBody Framework framework) {
        if (frameworkService.createFramework(new Framework(framework.getName(), framework.getDescription(), framework.getVersion()))) {
            return String.format("Added framework %s to database", framework.getName());
        }
        return "Already in database";
    }

    @DeleteMapping("removeFramework")
    public String removeFramework(@RequestBody Framework framework) {
        if (frameworkService.removeFramework(framework)) {
            return String.format("Removed framework %s to database", framework.getName());
        }
        return "Not in database";
    }

    // Topic Management
    @PostMapping("createTopic")
    public String createTopic(@RequestBody Topic topic) {
        if (topicService.createTopic(new Topic(topic.getName()))) {
            return String.format("Added topic %s to database", topic.getName());
        }
        return "Already in database";
    }

    @DeleteMapping("removeTopic")
    public String removeTopic(@RequestBody Topic topic) {
        if (topicService.removeTopic(topic)) {
            return String.format("Removed topic %s to database", topic.getName());
        }
        return "Not in database";
    }


    // Project Management
    @PostMapping("createProject")
    public String createProject(@RequestBody Project project) {
        if (projectService.save(new Project(project.getName(), project.getDescription(), project.getProjectContext()))) {
            return "Saved Project!";
        }
        return "Project already created!";
    }

    @PostMapping("addTopic")
    public String addTopic(@RequestParam Long projectId, @RequestParam Long topicId) {
        if (projectService.addTopic(topicId, projectId)) {
            return String.format("Added topic to project #%d", projectId);
        }
        return "Already added to project";
    }


}
