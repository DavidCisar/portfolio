package io.dcisar.backend.technology.language;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @GetMapping
    public List<Language> getLanguages() {
        return languageService.getLanguages();
    }

    @GetMapping("/{languageId}")
    public Language getLanguage(@PathVariable Long languageId) {
        return languageService.getLanguage(languageId);
    }

}
