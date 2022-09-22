package io.dcisar.backend.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping
    public List<RatingDTO> getRatings() {
        return ratingService.getRatings();
    }

    @PostMapping("/createRating")
    public ResponseEntity<String> createRating(@RequestBody RatingDTO ratingDTO) {
        if (ratingService.createRating(ratingDTO)) {
            return new ResponseEntity<>(
                    "Added rating to database",
                    HttpStatus.CREATED
            );
        }
        return new ResponseEntity<>("Already submitted a rating", HttpStatus.BAD_REQUEST);
    }
}
