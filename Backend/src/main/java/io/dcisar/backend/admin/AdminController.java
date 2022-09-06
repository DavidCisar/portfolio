package io.dcisar.backend.admin;

import io.dcisar.backend.project.ProjectDTO;
import io.dcisar.backend.project.ProjectService;
import io.dcisar.backend.technology.framework.FrameworkDTO;
import io.dcisar.backend.technology.framework.FrameworkService;
import io.dcisar.backend.technology.language.LanguageDTO;
import io.dcisar.backend.technology.language.LanguageService;
import io.dcisar.backend.technology.topic.Topic;
import io.dcisar.backend.technology.topic.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProjectService projectService;
    private final FrameworkService frameworkService;
    private final LanguageService languageService;
    private final TopicService topicService;


    // Language Management
    @PostMapping("/createLanguage")
    public ResponseEntity<String> createLanguage(@RequestBody LanguageDTO languageDTO) {
        if (languageService.createLanguage(languageDTO)) {
            return new ResponseEntity<>(
                    String.format("Added language %s to database", languageDTO.name),
                    HttpStatus.CREATED
            );
        }
        return new ResponseEntity<>("Already in database", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/updateLanguage")
    public ResponseEntity<String> updateLanguage(@RequestBody LanguageDTO languageDTO) {
        if (languageService.updateLanguage(languageDTO)) {
            return new ResponseEntity<>(
                    String.format("Updated language %s", languageDTO.name),
                    HttpStatus.ACCEPTED
            );
        }
        return new ResponseEntity<>("Language not found", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/deleteLanguage/{id}")
    public ResponseEntity<String> deleteLanguage(@PathVariable Long id) {
        if (languageService.deleteLanguage(id)) {
            return new ResponseEntity<>(
                    String.format("Removed language with id %d from database", id),
                    HttpStatus.ACCEPTED
            );
        }
        return new ResponseEntity<>("Not in database", HttpStatus.BAD_REQUEST);
    }


    // Framework Management
    @PostMapping("/createFramework")
    public ResponseEntity<String> createFramework(@RequestBody FrameworkDTO frameworkDTO) {
        if (frameworkService.createFramework(frameworkDTO)) {
            return new ResponseEntity<>(
                    String.format("Added framework %s to database", frameworkDTO.name),
                    HttpStatus.CREATED
            );
        }
        return new ResponseEntity<>("Already in database", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/updateFramework")
    public ResponseEntity<String> updateFramework(@RequestBody FrameworkDTO frameworkDTO) {
        if (frameworkService.updateFramework(frameworkDTO)) {
            return new ResponseEntity<>(
                    String.format("Updated framework %s", frameworkDTO.name),
                    HttpStatus.ACCEPTED
            );
        }
        return new ResponseEntity<>("Framework not found", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/deleteFramework/{id}")
    public ResponseEntity<String> deleteFramework(@PathVariable Long id) {
        if (frameworkService.deleteFramework(id)) {
            return new ResponseEntity<>(
                    String.format("Deleted framework with id %d from database", id),
                    HttpStatus.ACCEPTED
            );
        }
        return new ResponseEntity<>("Not in database", HttpStatus.BAD_REQUEST);
    }


    // Topic Management
    @PostMapping("/createTopic")
    public ResponseEntity<String> createTopic(@RequestBody Topic topic) {
        Topic topicToBeCreated = Topic.builder().name(topic.getName()).build();
        if (topicService.createTopic(topicToBeCreated)) {
            return new ResponseEntity<>(
                    String.format("Added topic %s with id %d to database", topicToBeCreated.getName(), topicToBeCreated.getId()),
                    HttpStatus.CREATED
            );
        }
        return new ResponseEntity<>("Already in database", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/deleteTopic/{id}")
    public ResponseEntity<String> deleteTopic(@PathVariable Long id) {
        if (topicService.deleteTopic(id)) {
            return new ResponseEntity<>(
                    String.format("Removed topic with id %d from database", id),
                    HttpStatus.ACCEPTED
            );
        }
        return new ResponseEntity<>("Not in database", HttpStatus.BAD_REQUEST);
    }


    // Project Management
    @PostMapping("/createProject")
    public ResponseEntity<String> createProject(@RequestBody ProjectDTO projectDTO) {
        if (projectService.createProject(projectDTO)) {
            return new ResponseEntity<>("Saved Project!",
                    HttpStatus.CREATED
            );
        }
        return new ResponseEntity<>("Project already created!", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/updateProject")
    public ResponseEntity<String> updateProject(@RequestBody ProjectDTO projectDTO) {
        System.out.println("projectDTO = " + projectDTO);
        if (projectService.updateProject(projectDTO)) {
            return new ResponseEntity<>(
                    String.format("Updated project %s", projectDTO.name),
                    HttpStatus.ACCEPTED
            );
        }
        return new ResponseEntity<>("Project not found", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/deleteProject/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        if (projectService.deleteById(id)) {
            return new ResponseEntity<>(
                    String.format("Deleted project with id %d", id),
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>("Project not found", HttpStatus.BAD_REQUEST);
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

}
