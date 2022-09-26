package io.dcisar.backend.reference;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/references")
@RequiredArgsConstructor
public class ReferenceController {

    private final ReferenceService referenceService;

    @GetMapping
    public List<ReferenceDTO> getReferences() {
        return referenceService.getReferences();
    }

    @PostMapping("/createReference")
    public ResponseEntity<String> createReference(@RequestBody ReferenceDTO referenceDTO) {
        if (referenceService.createReference(referenceDTO)) {
            return new ResponseEntity<>(
                    "Added reference to database",
                    HttpStatus.CREATED
            );
        }
        return new ResponseEntity<>("Already submitted a reference", HttpStatus.BAD_REQUEST);
    }
}
