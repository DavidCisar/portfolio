package io.dcisar.backend.technology.language;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {

    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public List<Language> getLanguages() {
        return languageRepository.findAll();
    }

    public Language getLanguage(Long languageId) {
        return languageRepository.findById(languageId).orElseThrow();
    }
}
