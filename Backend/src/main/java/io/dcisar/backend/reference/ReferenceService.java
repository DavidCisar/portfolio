package io.dcisar.backend.reference;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReferenceService {

    private final ReferenceRepository referenceRepository;

    public boolean createReference(ReferenceDTO referenceDTO) {
        if (!referenceRepository.findByName(referenceDTO.name.toLowerCase()).isPresent()) {
            Reference referenceToBeCreated = mapReferenceDTOToReference(referenceDTO);
            referenceRepository.save(referenceToBeCreated);
            return true;
        }
        return false;
    }

    public boolean acceptReference(Long id) {
        Optional<Reference> reference = referenceRepository.findById(id);
        if (reference.isPresent()) {
            Reference referenceToBeAccepted = reference.get();
            referenceToBeAccepted.setAccepted(true);
            referenceRepository.save(referenceToBeAccepted);
            return true;
        }
        return false;
    }

    public boolean declineReference(Long id) {
        Optional<Reference> reference = referenceRepository.findById(id);
        if (reference.isPresent()) {
            Reference referenceToBeDeclined = reference.get();
            referenceToBeDeclined.setAccepted(false);
            referenceRepository.save(referenceToBeDeclined);
            return true;
        }
        return false;
    }

    public List<ReferenceDTO> getReferences() {
        List<Reference> references = referenceRepository.findAll();
        return references.stream()
                .map(reference -> mapReferenceToDTO(reference))
                .collect(Collectors.toList());

    }

    private ReferenceDTO mapReferenceToDTO(Reference reference) {
        return ReferenceDTO.builder()
                .id(reference.getId())
                .name(reference.getName().toLowerCase())
                .message(reference.getMessage())
                .link(reference.getLink())
                .isAccepted(reference.isAccepted())
                .createdAt(reference.getCreatedAt().toString().substring(8,10) + "." + reference.getCreatedAt().toString().substring(5,7) + "." + reference.getCreatedAt().toString().substring(2,4))
                .build();
    }

    private Reference mapReferenceDTOToReference(ReferenceDTO referenceDTO) {
        return Reference.builder()
                .name(referenceDTO.name)
                .message(referenceDTO.message)
                .link(referenceDTO.link)
                .isAccepted(false)
                .createdAt(new Date(System.currentTimeMillis()))
                .build();

    }

}
