package io.dcisar.backend.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    public boolean createRating(RatingDTO ratingDTO) {
        if (!ratingRepository.findByName(ratingDTO.name.toLowerCase()).isPresent()) {
            Rating ratingToBeCreated = mapRatingDTOToRating(ratingDTO);
            ratingRepository.save(ratingToBeCreated);
            return true;
        }
        return false;
    }

    public boolean acceptRating(Long id) {
        Optional<Rating> rating = ratingRepository.findById(id);
        if (rating.isPresent()) {
            Rating ratingToBeAccepted = rating.get();
            ratingToBeAccepted.setAccepted(true);
            ratingRepository.save(ratingToBeAccepted);
        }
        return false;
    }

    public boolean declineRating(Long id) {
        Optional<Rating> rating = ratingRepository.findById(id);
        if (rating.isPresent()) {
            Rating ratingToBeDeclined = rating.get();
            ratingToBeDeclined.setAccepted(false);
            ratingRepository.save(ratingToBeDeclined);
        }
        return false;
    }

    public List<RatingDTO> getRatings() {
        List<Rating> ratings = ratingRepository.findAll();
        return ratings.stream()
                .map(rating -> mapRatingToDTO(rating))
                .collect(Collectors.toList());

    }

    private RatingDTO mapRatingToDTO(Rating rating) {
        return RatingDTO.builder()
                .id(rating.getId())
                .name(rating.getName().toLowerCase())
                .message(rating.getMessage())
                .link(rating.getLink())
                .isAccepted(false)
                .build();
    }

    private Rating mapRatingDTOToRating(RatingDTO ratingDTO) {
        return Rating.builder()
                .id(ratingDTO.id)
                .name(ratingDTO.name)
                .message(ratingDTO.message)
                .link(ratingDTO.link)
                .isAccepted(ratingDTO.isAccepted)
                .build();

    }

}
