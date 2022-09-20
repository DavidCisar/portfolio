package io.dcisar.backend.rating;

import io.dcisar.backend.user.User;
import io.dcisar.backend.user.UserDTO;
import io.dcisar.backend.user.UserDetailsService;
import io.dcisar.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    public boolean createRating(RatingDTO ratingDTO) {
        Optional<User> user = userRepository.findById(ratingDTO.userDTO.id);
        if (user.isPresent()) {
            if (!ratingRepository.findByUser(user.get()).isPresent()) {
                Rating ratingToBeCreated = mapRatingDTOToRating(ratingDTO);
                ratingRepository.save(ratingToBeCreated);
                return true;
            }
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
        UserDTO userDTO = userDetailsService.mapUserToDTO(rating.getUser());
        return RatingDTO.builder()
                .id(rating.getId())
                .rating(rating.getRating())
                .message(rating.getMessage())
                .userDTO(userDTO)
                .build();
    }

    private Rating mapRatingDTOToRating(RatingDTO ratingDTO) {
        User user = userRepository.findByUsername(ratingDTO.userDTO.username).get();
        return Rating.builder()
                .id(ratingDTO.id)
                .rating(ratingDTO.rating)
                .message(ratingDTO.message)
                .user(user)
                .build();

    }

}
