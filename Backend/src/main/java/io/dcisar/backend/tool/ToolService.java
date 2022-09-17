package io.dcisar.backend.tool;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ToolService {

    private final ToolRepository toolRepository;

    public boolean createTool(ToolDTO toolDTO) {
        if (toolRepository.findByName(toolDTO.name).isPresent()) {
            return false;
        }
        Tool toolToBeCreated = mapToolDTOToTool(toolDTO);
        toolRepository.save(toolToBeCreated);
        return true;
    }

    public boolean updateTool(ToolDTO toolDTO) {
        if (!toolRepository.findById(toolDTO.id).isPresent()) {
            return false;
        }
        Tool toolToBeUpdated = toolRepository.findById(toolDTO.id).get();
        toolToBeUpdated.setName(toolDTO.name);
        toolToBeUpdated.setDescription(toolDTO.description);
        toolRepository.save(toolToBeUpdated);
        return true;
    }

    public boolean deleteTool(Long id) {
        if (!toolRepository.findById(id).isPresent()) {
            return false;
        }
        Tool toolToBeDeleted = toolRepository.findById(id).get();
        toolRepository.delete(toolToBeDeleted);
        return true;
    }

    public List<ToolDTO> getTools() {
        List<Tool> tools = toolRepository.findAll();
        return tools.stream()
                .map(tool -> mapToolToDTO(tool))
                .collect(Collectors.toList());
    }

    public Tool mapToolDTOToTool(ToolDTO toolDTO) {
        return Tool.builder()
                .name(toolDTO.name)
                .description(toolDTO.description)
                .build();
    }

    public ToolDTO mapToolToDTO(Tool tool) {
        return ToolDTO.builder()
                .id(tool.getId())
                .name(tool.getName())
                .description(tool.getDescription())
                .build();
    }
}
