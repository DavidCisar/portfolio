package io.dcisar.backend.technology.framework;

import io.dcisar.backend.project.Project;
import io.dcisar.backend.project.ProjectRepository;
import io.dcisar.backend.technology.language.Language;
import io.dcisar.backend.technology.language.LanguageRepository;
import io.dcisar.backend.technology.language.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FrameworkService {

    private final FrameworkRepository frameworkRepository;
    private final ProjectRepository projectRepository;
    private final LanguageRepository languageRepository;
    private final LanguageService languageService;

    public boolean createFramework(FrameworkDTO frameworkDTO) {
        if (frameworkRepository.findByName(frameworkDTO.name).isPresent()) {
            return false;
        }
        Framework frameworkToBeCreated = mapFrameworkDTOToFramework(frameworkDTO);
        frameworkRepository.save(frameworkToBeCreated);
        return true;
    }

    public boolean updateFramework(FrameworkDTO frameworkDTO) {
        if (!frameworkRepository.findByName(frameworkDTO.name).isPresent()) {
            return false;
        }

        if(!languageService.createLanguage(frameworkDTO.languageDTO)) {
            languageService.updateLanguage(frameworkDTO.languageDTO);
        }

        Language language = languageRepository.findByName(frameworkDTO.languageDTO.name).orElseThrow();
        Framework frameworkToBeUpdated = frameworkRepository.findByName(frameworkDTO.name).orElseThrow();

        if (frameworkDTO.description != null) {
            frameworkToBeUpdated.setDescription(frameworkDTO.description);
        }
        if (frameworkDTO.version != null) {
            frameworkToBeUpdated.setVersion(frameworkDTO.version);
        }
        if (frameworkDTO.documentation != null) {
            frameworkToBeUpdated.setDocumentation(frameworkDTO.documentation);
        }
        if (frameworkDTO.languageDTO != null) {
            frameworkToBeUpdated.setLanguage(language);
        }

        frameworkRepository.save(frameworkToBeUpdated);
        return true;
    }

    public boolean deleteFramework(Long id) {
        if (frameworkRepository.findById(id).isEmpty()) {
            return false;
        }
        Framework frameworkToBeDeleted = frameworkRepository.findById(id).orElseThrow();
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

    public List<FrameworkDTO> getFrameworks() {
        List<Framework> frameworks = frameworkRepository.findAll();
        List<FrameworkDTO> request = new ArrayList<FrameworkDTO>();
        for (Framework framework : frameworks) {
            FrameworkDTO frameworkDTO = mapFrameworkToFrameworkDTO(framework);
            request.add(frameworkDTO);
        }
        return request;
    }

    public FrameworkDTO getFramework(Long frameworkId) {
        return mapFrameworkToFrameworkDTO(frameworkRepository.findById(frameworkId).orElseThrow());
    }

    public Framework mapFrameworkDTOToFramework(FrameworkDTO frameworkDTO) {
        Framework framework = Framework.builder()
                .name(frameworkDTO.name)
                .description(frameworkDTO.description)
                .version(frameworkDTO.version)
                .documentation(frameworkDTO.documentation)
                .build();

        if (!languageService.createLanguage(frameworkDTO.languageDTO)) {
            languageService.updateLanguage(frameworkDTO.languageDTO);
        }
        framework.setLanguage(languageRepository.findByName(frameworkDTO.languageDTO.name).orElseThrow());
        System.out.println("framework = " + framework);
        return framework;
    }

    public FrameworkDTO mapFrameworkToFrameworkDTO(Framework framework) {
        return FrameworkDTO.builder()
                .id(framework.getId())
                .name(framework.getName())
                .description(framework.getDescription())
                .version(framework.getVersion())
                .documentation(framework.getDocumentation())
                .languageDTO(languageService.mapLanguageToLanguageDTO(framework.getLanguage()))
                .build();
    }
}
