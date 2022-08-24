package io.dcisar.backend.technology.language;

import io.dcisar.backend.project.Project;
import io.dcisar.backend.project.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final ProjectRepository projectRepository;

    public LanguageService(LanguageRepository languageRepository, ProjectRepository projectRepository) {
        this.languageRepository = languageRepository;
        this.projectRepository = projectRepository;
    }

    public List<Language> getLanguages() {
        return languageRepository.findAll();
    }

    public Language getLanguage(Long languageId) {
        return languageRepository.findById(languageId).orElseThrow();
    }

    public boolean createLanguage(LanguageDTO languageDTO) {
        if (languageRepository.findByName(languageDTO.name).isPresent()) {
            return false;
        }
        Language languageToBeCreated = mapLanguageDTOToLanguage(languageDTO);
        languageRepository.save(languageToBeCreated);
        return true;
    }

    public boolean updateLanguage(LanguageDTO languageDTO) {
        if (!languageRepository.findByName(languageDTO.name).isPresent()) {
            return false;
        }
        Language languageToBeUpdated = languageRepository.findByName(languageDTO.name).orElseThrow();
        if (languageDTO.description != null) {
            languageToBeUpdated.setDescription(languageDTO.description);
        }

        if (languageDTO.version != null) {
            languageToBeUpdated.setVersion(languageDTO.version);
        }

        languageRepository.save(languageToBeUpdated);
        return true;
    }

    public boolean removeLanguage(LanguageDTO languageDTO) {
        if (languageRepository.findByName(languageDTO.name).isEmpty()) {
            return false;
        }
        Language languageToBeDeleted = languageRepository.findByName(languageDTO.name).orElseThrow();
        List<Project> projects = projectRepository.findAll();
        for (Project project : projects) {
            if (project.getLanguagesInProject().contains(languageToBeDeleted)) {
                project.removeLanguageFromProject(languageToBeDeleted);
                projectRepository.save(project);
            }
        }
        languageRepository.delete(languageToBeDeleted);
        return true;
    }

    public Language findByName(String name) {
        return languageRepository.findByName(name).orElseThrow();
    }

    public Language mapLanguageDTOToLanguage(LanguageDTO languageDTO) {
        Language language = new Language(languageDTO.name, languageDTO.description, languageDTO.version);
        return language;
    }
}
