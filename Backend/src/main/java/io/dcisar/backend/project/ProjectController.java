package io.dcisar.backend.project;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<Project> getProjects() {
        return projectService.getProjects();
    }

    @PostMapping("createProject")
    public String createProject(@RequestBody Project project) {
        projectService.save(new Project(project.getName(), project.getDescription(), project.getProjectContext()));
        return "Saved Project!";
    }
}
