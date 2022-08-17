package io.dcisar.backend.admin;

import io.dcisar.backend.project.Project;
import io.dcisar.backend.project.ProjectService;
import io.dcisar.backend.technology.framework.Framework;
import io.dcisar.backend.technology.framework.FrameworkService;
import io.dcisar.backend.technology.language.Language;
import io.dcisar.backend.technology.language.LanguageService;
import io.dcisar.backend.technology.topic.Topic;
import io.dcisar.backend.technology.topic.TopicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> createLanguage(@RequestBody Language language) {
        Language languageToBeCreated = new Language(language.getName(), language.getDescription(), language.getVersion());
        if (languageService.createLanguage(languageToBeCreated)) {
            return new ResponseEntity<>(
                    String.format("Added language %s with id %d to database", languageToBeCreated.getName(), languageToBeCreated.getId()),
                    HttpStatus.CREATED
            );
        }
        return new ResponseEntity<>("Already in database", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/removeLanguage")
    public ResponseEntity<String> removeLanguage(@RequestBody Language language) {
        if (languageService.removeLanguage(language)) {
            return new ResponseEntity<>(
                    String.format("Removed language %s from database", language.getName()),
                    HttpStatus.ACCEPTED
            );
        }
        return new ResponseEntity<>("Not in database", HttpStatus.BAD_REQUEST);
    }


    // Framework Management
    @PostMapping("/createFramework")
    public ResponseEntity<String> createFramework(@RequestBody Framework framework) {
        try {
            Language languageToBeCreated = new Language(
            framework.getLanguage().getName(),
            framework.getLanguage().getDescription(),
            framework.getLanguage().getVersion());

            if (languageService.createLanguage(languageToBeCreated)) {
                Framework frameworkToBeCreated = new Framework(framework.getName(), framework.getDescription(), framework.getVersion());
                frameworkToBeCreated.setLanguage(languageToBeCreated);
                if (frameworkService.createFramework(frameworkToBeCreated)) {
                    return new ResponseEntity<>(
                            String.format(
                                    "Added framework %s with id %d to database and created new language!",
                                    frameworkToBeCreated.getName(), frameworkToBeCreated.getId()),
                            HttpStatus.CREATED
                    );
                }
            }

            Language language = languageService.findByName(framework.getLanguage().getName());
            Framework frameworkToBeCreated = new Framework(framework.getName(), framework.getDescription(), framework.getVersion());
            frameworkToBeCreated.setLanguage(language);

            if (frameworkService.createFramework(frameworkToBeCreated)) {
                return new ResponseEntity<>(
                        String.format("Added framework %s with id %d to database", frameworkToBeCreated.getName(), frameworkToBeCreated.getId()),
                        HttpStatus.CREATED
                );
            }

            return new ResponseEntity<>("Already in database", HttpStatus.BAD_REQUEST);

        } catch (NullPointerException e){
            return new ResponseEntity<>(
                    "Framework needs to contain a language!",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @DeleteMapping("/removeFramework")
    public ResponseEntity<String> removeFramework(@RequestBody Framework framework) {
        if (frameworkService.removeFramework(framework)) {
            return new ResponseEntity<>(
                    String.format("Removed framework %s from database", framework.getName()),
                    HttpStatus.ACCEPTED
            );
        }
        return new ResponseEntity<>("Not in database", HttpStatus.BAD_REQUEST);
    }


    // Topic Management
    @PostMapping("/createTopic")
    public ResponseEntity<String> createTopic(@RequestBody Topic topic) {
        Topic topicToBeCreated = new Topic(topic.getName());
        if (topicService.createTopic(topicToBeCreated)) {
            return new ResponseEntity<>(
                    String.format("Added topic %s with id %d to database", topicToBeCreated.getName(), topicToBeCreated.getId()),
                    HttpStatus.CREATED
            );
        }
        return new ResponseEntity<>("Already in database", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/removeTopic")
    public ResponseEntity<String> removeTopic(@RequestBody Topic topic) {
        if (topicService.removeTopic(topic)) {
            return new ResponseEntity<>(
                    String.format("Removed topic %s from database", topic.getName()),
                    HttpStatus.ACCEPTED
            );
        }
        return new ResponseEntity<>("Not in database", HttpStatus.BAD_REQUEST);
    }


    // Project Management
    @PostMapping(
            path = "/createProject",
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> createProject(@RequestBody Project project) {
        if (projectService.createProject(new Project(project.getName(), project.getDescription(), project.getProjectContext()))) {
            return new ResponseEntity<>("Saved Project!",
                    HttpStatus.CREATED
            );
        }
        return new ResponseEntity<>("Project already created!", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/addTopicToProject")
    public ResponseEntity<String> addTopicToProject(@RequestParam Long projectId, @RequestParam Long topicId) {
        if (projectService.addTopic(topicId, projectId)) {
            return new ResponseEntity<>(
                    String.format("Successfully added topic %d to project #%d", topicId, projectId),
                    HttpStatus.ACCEPTED
            );
        }
        return new ResponseEntity<>("Already added to project", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/removeTopicFromProject")
    public ResponseEntity<String> removeTopicFromProject(@RequestParam Long projectId, @RequestParam Long topicId) {
        if (projectService.removeTopic(topicId, projectId)) {
            return new ResponseEntity<>(
                    String.format("Successfully removed topic with id %d from project #%d", topicId, projectId),
                    HttpStatus.ACCEPTED
            );
        }
        return new ResponseEntity<>("Topic not found within project!", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/addFrameworkToProject")
    public ResponseEntity<String> addFrameworkToProject(@RequestParam Long projectId, @RequestParam Long frameworkId) {
        if (projectService.addFramework(frameworkId, projectId)) {
            return new ResponseEntity<>(
                    String.format("Successfully added framework %d to project #%d", frameworkId, projectId),
                    HttpStatus.ACCEPTED
            );
        }
        return new ResponseEntity<>("Already added to project", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/removeFrameworkFromProject")
    public ResponseEntity<String> removeFrameworkFromProject(@RequestParam Long projectId, @RequestParam Long frameworkId) {
        if (projectService.removeFramework(frameworkId, projectId)) {
            return new ResponseEntity<>(
                    String.format("Successfully removed framework with id %d from project #%d", frameworkId, projectId),
                    HttpStatus.ACCEPTED
            );
        }
        return new ResponseEntity<>("Framework not found within project!", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/addLanguageToProject")
    public ResponseEntity<String> addLanguageToProject(@RequestParam Long projectId, @RequestParam Long languageId) {
        if (projectService.addLanguage(languageId, projectId)) {
            return new ResponseEntity<>(
                    String.format("Successfully added language %d to project #%d", languageId, projectId),
                    HttpStatus.ACCEPTED
            );
        }
        return new ResponseEntity<>("Already added to project", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/removeLanguageFromProject")
    public ResponseEntity<String> removeLanguageFromProject(@RequestParam Long projectId, @RequestParam Long languageId) {
        if (projectService.removeLanguage(languageId, projectId)) {
            return new ResponseEntity<>(
                    String.format("Successfully removed language with id %d from project #%d", languageId, projectId),
                    HttpStatus.ACCEPTED
            );
        }
        return new ResponseEntity<>("Language not found within project!", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/updateProject")
    public ResponseEntity<String> updateProject(@RequestBody Project project) {
        return new ResponseEntity<>("ToDo!", HttpStatus.BAD_REQUEST);
    }


}
