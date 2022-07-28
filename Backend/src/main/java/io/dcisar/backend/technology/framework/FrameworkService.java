package io.dcisar.backend.technology.framework;

import io.dcisar.backend.project.Project;
import io.dcisar.backend.project.ProjectRepository;
import io.dcisar.backend.technology.language.Language;
import io.dcisar.backend.technology.language.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrameworkService {

    private final FrameworkRepository frameworkRepository;
    private final ProjectRepository projectRepository;
    private final LanguageRepository languageRepository;

    public FrameworkService(FrameworkRepository frameworkRepository, ProjectRepository projectRepository, LanguageRepository languageRepository) {
        this.frameworkRepository = frameworkRepository;
        this.projectRepository = projectRepository;
        this.languageRepository = languageRepository;
    }

    public boolean createFramework(Framework framework) {
        if (frameworkRepository.findByName(framework.getName()).isPresent()) {
            return false;
        }
        frameworkRepository.save(framework);
        return true;
    }

    public boolean removeFramework(Framework framework) {
        if (frameworkRepository.findByName(framework.getName()).isEmpty()) {
            return false;
        }
        Framework frameworkToBeDeleted = frameworkRepository.findByName(framework.getName()).orElseThrow();
        List<Project> projects = projectRepository.findAll();
        List<Language> languages = languageRepository.findAll();
        for (Project project : projects) {
            if (project.getFrameworksInProject().contains(frameworkToBeDeleted)) {
                project.removeFrameworkFromProject(frameworkToBeDeleted);
                projectRepository.save(project);
            }
        }
        for (Language language : languages) {
            if (language.getFrameworks().contains(frameworkToBeDeleted)) {
                language.removeFrameworkFromLanguage(frameworkToBeDeleted);
                languageRepository.save(language);}
        }
        frameworkRepository.delete(frameworkToBeDeleted);
        return true;
    }

    public List<Framework> getFrameworks() {
        return frameworkRepository.findAll();
    }

    public Framework getFramework(Long frameworkId) {
        return frameworkRepository.findById(frameworkId).orElseThrow();
    }
}
