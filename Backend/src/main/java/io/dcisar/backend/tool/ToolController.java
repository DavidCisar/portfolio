package io.dcisar.backend.tool;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tools")
@RequiredArgsConstructor
public class ToolController {

    private final ToolService toolService;
    private final ToolRepository toolRepository;

    @GetMapping
    public List<ToolDTO> getTools() {
        return toolService.getTools();
    }

}
