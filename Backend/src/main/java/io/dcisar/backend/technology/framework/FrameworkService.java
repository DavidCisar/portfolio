package io.dcisar.backend.technology.framework;

import io.dcisar.backend.technology.language.Language;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrameworkService {

    private final FrameworkRepository frameworkRepository;

    public FrameworkService(FrameworkRepository frameworkRepository) {
        this.frameworkRepository = frameworkRepository;
    }

    public boolean createFramework(Framework framework) {
        return true;
    }

    public boolean removeFramework(Framework framework) {
        return true;
    }

    public List<Framework> getFrameworks() {
        return frameworkRepository.findAll();
    }

    public Framework getFramework(Long frameworkId) {
        return frameworkRepository.findById(frameworkId).orElseThrow();
    }
}
