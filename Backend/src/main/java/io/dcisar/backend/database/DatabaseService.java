package io.dcisar.backend.database;

import io.dcisar.backend.tool.Tool;
import io.dcisar.backend.tool.ToolDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DatabaseService {

    private final DatabaseRepository databaseRepository;

    public List<DatabaseDTO> getDatabases() {
        List<Database> databases = databaseRepository.findAll();
        return databases.stream()
                .map(this::mapDatabaseToDTO)
                .collect(Collectors.toList());
    }

    public boolean createDatabase(DatabaseDTO databaseDTO) {
        if (databaseRepository.findByName(databaseDTO.name).isPresent()) {
            return false;
        }
        Database databaseToBeCreated = mapDatabaseDTOToDatabase(databaseDTO);
        databaseRepository.save(databaseToBeCreated);
        return true;
    }

    public boolean updateDatabase(DatabaseDTO databaseDTO) {
        if (!databaseRepository.findById(databaseDTO.id).isPresent()) {
            return false;
        }
        Database databaseToBeUpdated = databaseRepository.findById(databaseDTO.id).get();
        databaseToBeUpdated.setName(databaseDTO.name);
        databaseToBeUpdated.setDescription(databaseDTO.description);
        databaseRepository.save(databaseToBeUpdated);
        return true;
    }

    public boolean deleteDatabase(Long id) {
        if (!databaseRepository.findById(id).isPresent()) {
            return false;
        }
        Database toolToBeDeleted = databaseRepository.findById(id).get();
        databaseRepository.delete(toolToBeDeleted);
        return true;
    }

    public DatabaseDTO mapDatabaseToDTO(Database database) {
        return DatabaseDTO.builder()
                .id(database.getId())
                .name(database.getName())
                .description(database.getDescription())
                .build();
    }

    public Database mapDatabaseDTOToDatabase(DatabaseDTO databaseDTO) {
        return Database.builder()
                .name(databaseDTO.name)
                .description(databaseDTO.description)
                .build();
    }
}
