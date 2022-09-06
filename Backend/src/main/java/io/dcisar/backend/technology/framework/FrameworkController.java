package io.dcisar.backend.technology.framework;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/frameworks")
@RequiredArgsConstructor
public class FrameworkController {

    private final FrameworkService frameworkService;

    @GetMapping
    public List<FrameworkDTO> getFrameworks() {
        return frameworkService.getFrameworks();
    }

    @GetMapping("/{frameworkId}")
    public FrameworkDTO getFramework(@PathVariable Long frameworkId) {
        return frameworkService.getFramework(frameworkId);
    }

}
