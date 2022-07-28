package io.dcisar.backend.admin;

import io.dcisar.backend.project.Project;
import io.dcisar.backend.project.ProjectService;
import io.dcisar.backend.technology.framework.Framework;
import io.dcisar.backend.technology.framework.FrameworkService;
import io.dcisar.backend.technology.language.Language;
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


    // Language Management
    @PostMapping("/createLanguage")
    public String createLanguage(@RequestBody Language language) {
        Language languageToBeCreated = new Language(language.getName(), language.getDescription(), language.getVersion());
        if (languageService.createLanguage(languageToBeCreated)) {
            return String.format("Added language %s with id %d to database", languageToBeCreated.getName(), languageToBeCreated.getId());
        }
        return "Already in database";
    }

    @DeleteMapping("/removeLanguage")
    public String removeLanguage(@RequestBody Language language) {
        if (languageService.removeLanguage(language)) {
            return String.format("Removed language %s from database", language.getName());
        }
        return "Not in database";
    }


    // Framework Management
    @PostMapping("/createFramework")
    public String createFramework(@RequestBody Framework framework) {
        Language languageToBeCreated = new Language(
                framework.getLanguage().getName(),
                framework.getLanguage().getDescription(),
                framework.getLanguage().getVersion());

        if (languageService.createLanguage(languageToBeCreated)) {
            Framework frameworkToBeCreated = new Framework(framework.getName(), framework.getDescription(), framework.getVersion());
            frameworkToBeCreated.setLanguage(languageToBeCreated);
            if (frameworkService.createFramework(frameworkToBeCreated)) {
                return String.format("Added framework %s with id %d to database and created new language!", frameworkToBeCreated.getName(), frameworkToBeCreated.getId());
            }
        }
        Language language = languageService.findByName(framework.getLanguage().getName());
        Framework frameworkToBeCreated = new Framework(framework.getName(), framework.getDescription(), framework.getVersion());
        frameworkToBeCreated.setLanguage(language);
        if (frameworkService.createFramework(frameworkToBeCreated)) {
            return String.format("Added framework %s with id %d to database", frameworkToBeCreated.getName(), frameworkToBeCreated.getId());
        }
        return "Already in database";
    }

    @DeleteMapping("/removeFramework")
    public String removeFramework(@RequestBody Framework framework) {
        if (frameworkService.removeFramework(framework)) {
            return String.format("Removed framework %s from database", framework.getName());
        }
        return "Not in database";
    }


    // Topic Management
    @PostMapping("/createTopic")
    public String createTopic(@RequestBody Topic topic) {
        Topic topicToBeCreated = new Topic(topic.getName());
        if (topicService.createTopic(topicToBeCreated)) {
            return String.format("Added topic %s with id %d to database", topicToBeCreated.getName(), topicToBeCreated.getId());
        }
        return "Already in database";
    }

    @DeleteMapping("/removeTopic")
    public String removeTopic(@RequestBody Topic topic) {
        if (topicService.removeTopic(topic)) {
            return String.format("Removed topic %s from database", topic.getName());
        }
        return "Not in database";
    }


    // Project Management
    @PostMapping("/createProject")
    public String createProject(@RequestBody Project project) {
        if (projectService.save(new Project(project.getName(), project.getDescription(), project.getProjectContext()))) {
            return "Saved Project!";
        }
        return "Project already created!";
    }

    @PostMapping("/addTopicToProject")
    public String addTopicToProject(@RequestParam Long projectId, @RequestParam Long topicId) {
        if (projectService.addTopic(topicId, projectId)) {
            return String.format("Successfully added topic %d to project #%d", topicId, projectId);
        }
        return "Already added to project";
    }

    @PutMapping("/removeTopicFromProject")
    public String removeTopicFromProject(@RequestParam Long projectId, @RequestParam Long topicId) {
        if (projectService.removeTopic(topicId, projectId)) {
            return String.format("Successfully removed topic with id %d from project #%d", topicId, projectId);
        }
        return "Topic not found within project!";
    }

    @PostMapping("/addFrameworkToProject")
    public String addFrameworkToProject(@RequestParam Long projectId, @RequestParam Long frameworkId) {
        if (projectService.addFramework(frameworkId, projectId)) {
            return String.format("Successfully added framework %d to project #%d", frameworkId, projectId);
        }
        return "Already added to project";
    }

    @PutMapping("/removeFrameworkFromProject")
    public String removeFrameworkFromProject(@RequestParam Long projectId, @RequestParam Long frameworkId) {
        if (projectService.removeFramework(frameworkId, projectId)) {
            return String.format("Successfully removed framework with id %d from project #%d", frameworkId, projectId);
        }
        return "Framework not found within project!";
    }

    @PostMapping("/addLanguageToProject")
    public String addLanguageToProject(@RequestParam Long projectId, @RequestParam Long languageId) {
        if (projectService.addLanguage(languageId, projectId)) {
            return String.format("Successfully added language %d to project #%d", languageId, projectId);
        }
        return "Already added to project";
    }

    @PutMapping("/removeLanguageFromProject")
    public String removeLanguageFromProject(@RequestParam Long projectId, @RequestParam Long languageId) {
        if (projectService.removeLanguage(languageId, projectId)) {
            return String.format("Successfully removed language with id %d from project #%d", languageId, projectId);
        }
        return "Language not found within project!";
    }

    @PutMapping("/updateProject")
    public String updateProject(@RequestBody Project project) {
        return "ToDo!";
    }


}
