package io.dcisar.backend.technology.language;

import io.dcisar.backend.project.Project;
import io.dcisar.backend.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final ProjectRepository projectRepository;

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

        if (languageDTO.documentation != null) {
            languageToBeUpdated.setDocumentation(languageDTO.documentation);
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
        Language language = Language.builder()
                .name(languageDTO.name)
                .description(languageDTO.description)
                .version(languageDTO.version)
                .documentation(languageDTO.documentation)
                .build();
        return language;
    }

    public LanguageDTO mapLanguageToLanguageDTO(Language language) {
        return LanguageDTO.builder()
                .id(language.getId())
                .name(language.getName())
                .description(language.getDescription())
                .version(language.getVersion())
                .documentation(language.getDocumentation())
                .build();
    }
}
